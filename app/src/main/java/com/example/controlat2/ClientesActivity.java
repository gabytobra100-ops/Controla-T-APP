package com.example.controlat2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class ClientesActivity extends AppCompatActivity {

    private RecyclerView recyclerClientes;
    private List<Cliente> listaClientes;
    private List<Cliente> listaClientesCompleta;
    private ClienteAdapter adapter;
    private AppDatabase db;
    private Button btnAgregarCliente, btnEliminarCliente, btnModificarCliente;
    private EditText etBuscarCliente;
    private Button btnVerHistorial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clientes);

        btnAgregarCliente = findViewById(R.id.btnAgregarCliente);
        btnEliminarCliente = findViewById(R.id.btnEliminarCliente);
        btnModificarCliente = findViewById(R.id.btnModificarCliente);
        recyclerClientes = findViewById(R.id.recyclerClientes);
        etBuscarCliente = findViewById(R.id.etBuscarCliente);
        btnVerHistorial = findViewById(R.id.btnVerHistorial);

        recyclerClientes.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "controlat-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        adapter = new ClienteAdapter(listaClientes);
        recyclerClientes.setAdapter(adapter);
        listaClientes = db.clienteDao().obtenerTodos();
        listaClientesCompleta = new ArrayList<>(listaClientes);

        etBuscarCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarClientes(s.toString());
            }
        });

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
        btnModificarCliente.setOnClickListener(v -> {
            Cliente clienteSeleccionado = adapter.getClienteSeleccionado();

            if (clienteSeleccionado != null) {
                Intent intent = new Intent(ClientesActivity.this, AgregarClienteActivity.class);
                intent.putExtra("modoEditar", true);
                intent.putExtra("id", clienteSeleccionado.getId());
                intent.putExtra("nombre", clienteSeleccionado.getNombre());
                intent.putExtra("telefono", clienteSeleccionado.getTelefono());
                intent.putExtra("correo", clienteSeleccionado.getCorreo());
                intent.putExtra("notas", clienteSeleccionado.getNotas());
                startActivity(intent);
            }
        });

        btnVerHistorial.setOnClickListener(v -> {
            Cliente clienteSeleccionado = adapter.getClienteSeleccionado();

            if (clienteSeleccionado != null) {
                Intent intent = new Intent(ClientesActivity.this, VentasClienteActivity.class);
                intent.putExtra("nombreCliente", clienteSeleccionado.getNombre());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (db != null && adapter != null) {
            listaClientes = db.clienteDao().obtenerTodos();
            listaClientesCompleta = new ArrayList<>(listaClientes);

            adapter.actualizarLista(listaClientes);
        }
    }
    private void filtrarClientes (String texto){
        List<Cliente> listaFiltrada = new ArrayList<>();
        for (Cliente cliente: listaClientesCompleta){
            if (cliente.getNombre().toLowerCase().contains(texto.toLowerCase()) ||
                    cliente .getTelefono().contains(texto)){
                listaFiltrada.add(cliente);
            }
        }
        adapter.actualizarLista(listaFiltrada);
    }
}