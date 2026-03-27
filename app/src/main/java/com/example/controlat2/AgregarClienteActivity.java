package com.example.controlat2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class AgregarClienteActivity extends AppCompatActivity {

    private EditText etNombreCliente, etTelefonoCliente, etCorreoCliente, etNotasCliente;
    private Button btnGuardarCliente;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cliente);

        etNombreCliente = findViewById(R.id.etNombreCliente);
        etTelefonoCliente = findViewById(R.id.etTelefonoCliente);
        etCorreoCliente = findViewById(R.id.etCorreoCliente);
        etNotasCliente = findViewById(R.id.etNotasCliente);
        btnGuardarCliente = findViewById(R.id.btnGuardarCliente);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "controlat-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        btnGuardarCliente.setOnClickListener(v -> {
            String nombre = etNombreCliente.getText().toString().trim();
            String telefono = etTelefonoCliente.getText().toString().trim();
            String correo = etCorreoCliente.getText().toString().trim();
            String notas = etNotasCliente.getText().toString().trim();

            if (nombre.isEmpty() || telefono.isEmpty()) {
                Toast.makeText(this, "Nombre y teléfono son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            Cliente cliente = new Cliente(nombre, telefono, correo, notas);
            db.clienteDao().insertar(cliente);

            Toast.makeText(this, "Cliente guardado", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}