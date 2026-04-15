package com.example.controlat2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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
    private Button btnAgregarPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        recyclerPedidos = findViewById(R.id.recyclerPedidos);
        btnAgregarPedido = findViewById(R.id.btnAgregarPedido);

        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "controlat-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        cargarPedidos();

        btnAgregarPedido.setOnClickListener(v -> {
            startActivity(new Intent(this, AgregarPedidoActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarPedidos();
    }

    private void cargarPedidos() {
        listaPedidos = db.pedidoDao().obtenerTodos();
        adapter = new PedidoAdapter(listaPedidos, db);
        recyclerPedidos.setAdapter(adapter);
    }
}