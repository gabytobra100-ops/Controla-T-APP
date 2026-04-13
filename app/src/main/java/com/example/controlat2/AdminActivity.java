package com.example.controlat2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    private LinearLayout btnVentas, btnProductos, btnClientes, btnReportes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnVentas = findViewById(R.id.btnVentas);
        btnProductos = findViewById(R.id.btnProductos);
        btnClientes = findViewById(R.id.btnClientes);
        btnReportes = findViewById(R.id.btnReportes);

        btnVentas.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, VentasActivity.class);
            startActivity(intent);
        });

        btnProductos.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ProductosActivity.class);
            startActivity(intent);
        });

        btnClientes.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ClientesActivity.class);
            startActivity(intent);
        });

        btnReportes.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ReportesActivity.class);
            startActivity(intent);
        });
    }
}