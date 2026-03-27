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

public class ClientesActivity extends AppCompatActivity {

    private RecyclerView recyclerClientes;
    private List<Cliente> listaClientes;
    private ClienteAdapter adapter;
    private AppDatabase db;
    private Button btnAgregarCliente, btnEliminarCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clientes);

        btnAgregarCliente = findViewById(R.id.btnAgregarCliente);
        btnEliminarCliente = findViewById(R.id.btnEliminarCliente);
        recyclerClientes = findViewById(R.id.recyclerClientes);

        recyclerClientes.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "controlat-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        listaClientes = db.clienteDao().obtenerTodos();

        adapter = new ClienteAdapter(listaClientes);
        recyclerClientes.setAdapter(adapter);

        btnAgregarCliente.setOnClickListener(v -> {
            Intent intent = new Intent(ClientesActivity.this, AgregarClienteActivity.class);
            startActivity(intent);
        });

        btnEliminarCliente.setOnClickListener(v -> {
            Cliente clienteSeleccionado = adapter.getClienteSeleccionado();

            if (clienteSeleccionado != null) {
                db.clienteDao().eliminar(clienteSeleccionado);

                listaClientes.clear();
                listaClientes.addAll(db.clienteDao().obtenerTodos());

                adapter.limpiarSeleccion();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (listaClientes != null && db != null && adapter != null) {
            listaClientes.clear();
            listaClientes.addAll(db.clienteDao().obtenerTodos());
            adapter.notifyDataSetChanged();
        }
    }
}