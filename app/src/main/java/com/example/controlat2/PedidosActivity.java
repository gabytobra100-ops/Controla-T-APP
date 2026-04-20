package com.example.controlat2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class PedidosActivity extends AppCompatActivity {

    private RecyclerView recyclerPedidos;
    private PedidoAdapter adapter;
    private List<Pedido> listaPedidos;
    private AppDatabase db;

    private Button btnAgregarPedido, btnEliminarPedido, btnModificarPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        recyclerPedidos = findViewById(R.id.recyclerPedidos);
        btnAgregarPedido = findViewById(R.id.btnAgregarPedido);
        btnEliminarPedido = findViewById(R.id.btnEliminarPedido);
        btnModificarPedido = findViewById(R.id.btnModificarPedido);

        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "controlat-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        listaPedidos = db.pedidoDao().obtenerTodos();
        adapter = new PedidoAdapter(listaPedidos);
        recyclerPedidos.setAdapter(adapter);

        btnAgregarPedido.setOnClickListener(v -> {
            Intent intent = new Intent(PedidosActivity.this, AgregarPedidoActivity.class);
            startActivity(intent);
        });

        btnEliminarPedido.setOnClickListener(v -> {
            Pedido pedidoSeleccionado = adapter.getPedidoSeleccionado();

            if (pedidoSeleccionado != null) {
                db.pedidoDao().eliminar(pedidoSeleccionado);
                listaPedidos.clear();
                listaPedidos.addAll(db.pedidoDao().obtenerTodos());
                adapter.limpiarSeleccion();
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(
                        PedidosActivity.this,
                        "Selecciona un pedido primero",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        btnModificarPedido.setOnClickListener(v -> {
            Pedido pedidoSeleccionado = adapter.getPedidoSeleccionado();

            if (pedidoSeleccionado != null) {
                Intent intent = new Intent(PedidosActivity.this, AgregarPedidoActivity.class);
                intent.putExtra("modoEditar", true);
                intent.putExtra("idPedido", pedidoSeleccionado.getId());

                intent.putExtra("nombreCliente", pedidoSeleccionado.getNombreCliente());
                intent.putExtra("nombreProducto", pedidoSeleccionado.getNombreProducto());
                intent.putExtra("cantidad", pedidoSeleccionado.getCantidad());
                intent.putExtra("estado", pedidoSeleccionado.getEstado());

                startActivity(intent);
            } else {
                Toast.makeText(
                        PedidosActivity.this,
                        "Selecciona un pedido primero",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (db != null && adapter != null) {
            listaPedidos.clear();
            listaPedidos.addAll(db.pedidoDao().obtenerTodos());
            adapter.limpiarSeleccion();
            adapter.notifyDataSetChanged();
        }
    }
}