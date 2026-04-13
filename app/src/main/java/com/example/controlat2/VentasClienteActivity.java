package com.example.controlat2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class VentasClienteActivity extends AppCompatActivity {

    private RecyclerView recyclerVentas;
    private TextView txtTituloVentasCliente;
    private VentaAdapter adapter;
    private AppDatabase db;
    private List<Venta> listaVentas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas_cliente);

        recyclerVentas = findViewById(R.id.recyclerVentasCliente);
        txtTituloVentasCliente = findViewById(R.id.txtTituloVentasCliente);

        recyclerVentas.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "controlat-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        int clienteId = getIntent().getIntExtra("clienteId", -1);
        String nombreCliente = getIntent().getStringExtra("nombreCliente");

        if (nombreCliente != null && !nombreCliente.isEmpty()) {
            txtTituloVentasCliente.setText("Historial de " + nombreCliente);
        }

        listaVentas = db.ventaDao().obtenerVentasPorCliente(clienteId);

        adapter = new VentaAdapter(listaVentas);
        recyclerVentas.setAdapter(adapter);
    }
}