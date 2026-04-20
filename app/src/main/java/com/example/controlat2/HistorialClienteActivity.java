package com.example.controlat2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class HistorialClienteActivity extends AppCompatActivity {

    private Toolbar toolbarHistorial;
    private TextView txtTituloHistorial;
    private RecyclerView recyclerHistorial;

    private AppDatabase db;
    private VentaHistorialAdapter adapter;
    private List<Venta> listaVentasCliente;

    private int clienteId;
    private String nombreCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_cliente);

        toolbarHistorial = findViewById(R.id.toolbarHistorial);
        txtTituloHistorial = findViewById(R.id.txtTituloHistorial);
        recyclerHistorial = findViewById(R.id.recyclerHistorial);

        setSupportActionBar(toolbarHistorial);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        clienteId = getIntent().getIntExtra("clienteId", -1);
        nombreCliente = getIntent().getStringExtra("nombreCliente");

        if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
            nombreCliente = "Cliente";
        }

        txtTituloHistorial.setText("Historial de " + nombreCliente);

        recyclerHistorial.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "controlat-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        listaVentasCliente = new ArrayList<>();

        List<Venta> todasLasVentas = db.ventaDao().obtenerTodas();
        for (Venta venta : todasLasVentas) {
            if (venta.getClienteId() == clienteId) {
                listaVentasCliente.add(venta);
            }
        }

        adapter = new VentaHistorialAdapter(listaVentasCliente);
        recyclerHistorial.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}