package com.example.controlat2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.List;
import java.util.Calendar;

public class AgregarPedidoActivity extends AppCompatActivity {

    private Spinner spClientePedido, spProductoPedido;
    private EditText etCantidadPedido, etFechaEntregaPedido;
    private Button btnGuardarPedido;
    private AppDatabase db;

    private List<Cliente> listaClientes;
    private List<Producto> listaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_pedido);

        spClientePedido = findViewById(R.id.spClientePedido);
        spProductoPedido = findViewById(R.id.spProductoPedido);
        etCantidadPedido = findViewById(R.id.etCantidadPedido);
        etFechaEntregaPedido = findViewById(R.id.etFechaEntregaPedido);
        btnGuardarPedido = findViewById(R.id.btnGuardarPedido);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "controlat-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        cargarClientes();
        cargarProductos();
        configurarCalendario();

        btnGuardarPedido.setOnClickListener(v -> guardarPedido());
    }

    private void cargarClientes() {
        listaClientes = db.clienteDao().obtenerTodos();

        ArrayAdapter<Cliente> adapterClientes = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaClientes
        );
        adapterClientes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClientePedido.setAdapter(adapterClientes);
    }

    private void cargarProductos() {
        listaProductos = db.productoDao().obtenerTodos();

        ArrayAdapter<Producto> adapterProductos = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaProductos
        );
        adapterProductos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProductoPedido.setAdapter(adapterProductos);
    }

    private void configurarCalendario() {
        etFechaEntregaPedido.setFocusable(false);
        etFechaEntregaPedido.setClickable(true);

        etFechaEntregaPedido.setOnClickListener(v -> {
            Calendar calendario = Calendar.getInstance();

            int anio = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AgregarPedidoActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        String diaFormateado = String.format("%02d", dayOfMonth);
                        String mesFormateado = String.format("%02d", month + 1);
                        String fechaSeleccionada = diaFormateado + "/" + mesFormateado + "/" + year;
                        etFechaEntregaPedido.setText(fechaSeleccionada);
                    },
                    anio, mes, dia
            );

            datePickerDialog.show();
        });
    }

    private void guardarPedido() {
        if (listaClientes == null || listaClientes.isEmpty()) {
            Toast.makeText(this, "No hay clientes registrados", Toast.LENGTH_SHORT).show();
            return;
        }

        if (listaProductos == null || listaProductos.isEmpty()) {
            Toast.makeText(this, "No hay productos registrados", Toast.LENGTH_SHORT).show();
            return;
        }

        Cliente clienteSeleccionado = (Cliente) spClientePedido.getSelectedItem();
        Producto productoSeleccionado = (Producto) spProductoPedido.getSelectedItem();

        String cantidadTexto = etCantidadPedido.getText().toString().trim();
        String fechaEntrega = etFechaEntregaPedido.getText().toString().trim();

        if (clienteSeleccionado == null || productoSeleccionado == null ||
                TextUtils.isEmpty(cantidadTexto) || TextUtils.isEmpty(fechaEntrega)) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadTexto);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cantidad <= 0) {
            Toast.makeText(this, "La cantidad debe ser mayor a 0", Toast.LENGTH_SHORT).show();
            return;
        }

        Pedido pedido = new Pedido(
                clienteSeleccionado.getNombre(),
                productoSeleccionado.getNombre(),
                cantidad,
                fechaEntrega,
                "Pendiente"
        );

        db.pedidoDao().insertar(pedido);

        Toast.makeText(this, "Pedido guardado", Toast.LENGTH_SHORT).show();
        finish();
    }
}