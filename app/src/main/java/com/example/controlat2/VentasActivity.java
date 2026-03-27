package com.example.controlat2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
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
    private Button btnAgregarVenta, btnEliminarVenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ventas);

        recyclerVentas = findViewById(R.id.recyclerVentas);
        btnAgregarVenta = findViewById(R.id.btnAgregarVenta);
        btnEliminarVenta = findViewById(R.id.btnEliminarVenta);

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (listaVentas != null && db != null && adapter != null) {
            listaVentas.clear();
            listaVentas.addAll(db.ventaDao().obtenerTodas());
            adapter.notifyDataSetChanged();
        }
    }
}