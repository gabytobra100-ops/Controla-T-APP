package com.example.controlat2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PersonalActivity extends AppCompatActivity {

    private Button btnRegistrarVenta, btnVerProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        btnRegistrarVenta = findViewById(R.id.btnRegistrarVenta);
        btnVerProductos = findViewById(R.id.btnVerProductos);

        // Ir directo a registrar venta
        btnRegistrarVenta.setOnClickListener(v -> {
            Intent intent = new Intent(PersonalActivity.this, AgregarVentaActivity.class);
            startActivity(intent);
        });

        // Ver productos (solo consulta)
        btnVerProductos.setOnClickListener(v -> {
            Intent intent = new Intent(PersonalActivity.this, ProductosEmpleadoActivity.class);
            startActivity(intent);
        });
    }
}