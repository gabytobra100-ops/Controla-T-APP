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
    private boolean modoEditar = false;
    private int idCliente = -1;

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

        // --- MODO EDITAR ---
        if (getIntent().getBooleanExtra("modoEditar", false)){
            modoEditar = true;
            idCliente = getIntent().getIntExtra("id", -1);

            String nombre = getIntent().getStringExtra("nombre");
            String telefono = getIntent().getStringExtra("telefono");
            String correo = getIntent().getStringExtra("correo");
            String notas = getIntent().getStringExtra("notas");

            etNombreCliente.setText(nombre);
            etTelefonoCliente.setText(telefono);
            etCorreoCliente.setText(correo);
            etNotasCliente.setText(notas);

            btnGuardarCliente.setText("Actualizar cliente");
        }

        btnGuardarCliente.setOnClickListener(v -> {
            String nombre = etNombreCliente.getText().toString().trim();
            String telefono = etTelefonoCliente.getText().toString().trim();
            String correo = etCorreoCliente.getText().toString().trim();
            String notas = etNotasCliente.getText().toString().trim();

            if (nombre.isEmpty() || telefono.isEmpty()) {
                Toast.makeText(this, "Nombre y teléfono son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }
            if (modoEditar){
                Cliente clienteActualizado = new Cliente(nombre, telefono, correo, notas);
                clienteActualizado.setId(idCliente);

                db.clienteDao().actualizar(clienteActualizado);
                Toast.makeText(this, "Cliente actualizado", Toast.LENGTH_SHORT).show();
            } else {
                Cliente clienteNuevo = new Cliente(nombre, telefono, correo, notas);
                db.clienteDao().insertar(clienteNuevo);
                Toast.makeText(this, "Cliente guardado", Toast.LENGTH_SHORT).show();
            }
            finish();
        });
    }
}