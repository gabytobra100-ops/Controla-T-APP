package com.example.controlat2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class VentasActivity extends AppCompatActivity {

    private RecyclerView recyclerVentas;
    private VentaAdapter adapter;
    private List<Venta> listaVentas;
    private AppDatabase db;

    private Button btnAgregarVenta, btnEliminarVenta, btnModificarVenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);

        recyclerVentas = findViewById(R.id.recyclerVentas);
        btnAgregarVenta = findViewById(R.id.btnAgregarVenta);
        btnEliminarVenta = findViewById(R.id.btnEliminarVenta);
        btnModificarVenta = findViewById(R.id.btnModificarVenta);

        recyclerVentas.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "controlat-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        listaVentas = db.ventaDao().obtenerTodas();
        adapter = new VentaAdapter(listaVentas);
        recyclerVentas.setAdapter(adapter);

        btnAgregarVenta.setOnClickListener(v -> {
            Intent intent = new Intent(VentasActivity.this, AgregarVentaActivity.class);
            startActivity(intent);
        });

        btnEliminarVenta.setOnClickListener(v -> {
            Venta ventaSeleccionada = adapter.getVentaSeleccionada();

            if (ventaSeleccionada != null) {
                db.ventaDao().eliminar(ventaSeleccionada);
                listaVentas.clear();
                listaVentas.addAll(db.ventaDao().obtenerTodas());
                adapter.limpiarSeleccion();
                adapter.notifyDataSetChanged();
            }
        });

        btnModificarVenta.setOnClickListener(v -> {
            Venta ventaSeleccionada = adapter.getVentaSeleccionada();

            if (ventaSeleccionada != null) {
                Intent intent = new Intent(VentasActivity.this, AgregarVentaActivity.class);
                intent.putExtra("modoEditar", true);
                intent.putExtra("idVenta", ventaSeleccionada.getId());
                intent.putExtra("clienteId", ventaSeleccionada.getClienteId());
                intent.putExtra("nombreCliente", ventaSeleccionada.getNombreCliente());
                intent.putExtra("nombreProducto", ventaSeleccionada.getNombreProducto());
                intent.putExtra("cantidad", ventaSeleccionada.getCantidad());
                intent.putExtra("total", ventaSeleccionada.getTotal());
                intent.putExtra("fecha", ventaSeleccionada.getFecha());
                startActivity(intent);
            } else {
                android.widget.Toast.makeText(
                        VentasActivity.this,
                        "Selecciona una venta primero",
                        android.widget.Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (db != null && adapter != null) {
            listaVentas.clear();
            listaVentas.addAll(db.ventaDao().obtenerTodas());
            adapter.limpiarSeleccion();
            adapter.notifyDataSetChanged();
        }
    }
}