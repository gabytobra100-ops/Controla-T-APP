package com.example.controlat2;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.DatePickerDialog;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.List;

public class AgregarPedidoActivity extends AppCompatActivity {

    private Spinner spClientesPedido, spProductosPedido, spEstadoPedido;
    private EditText etCantidadPedido, etFechaEntregaPedido;
    private Button btnGuardarPedido;

    private AppDatabase db;
    private List<Cliente> listaClientes;
    private List<Producto> listaProductos;

    private boolean modoEditar = false;
    private int idPedido = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_pedido);

        spClientesPedido = findViewById(R.id.spClientesPedido);
        spProductosPedido = findViewById(R.id.spProductosPedido);
        spEstadoPedido = findViewById(R.id.spEstadoPedido);
        etCantidadPedido = findViewById(R.id.etCantidadPedido);
        etFechaEntregaPedido = findViewById(R.id.etFechaEntregaPedido);
        btnGuardarPedido = findViewById(R.id.btnGuardarPedido);

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

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "controlat-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        listaClientes = db.clienteDao().obtenerTodos();
        listaProductos = db.productoDao().obtenerTodos();

        ArrayAdapter<Cliente> adapterClientes = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaClientes
        );
        adapterClientes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClientesPedido.setAdapter(adapterClientes);

        ArrayAdapter<Producto> adapterProductos = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaProductos
        );
        adapterProductos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProductosPedido.setAdapter(adapterProductos);

        String[] estados = {"Pendiente", "Entregado", "Cancelado"};
        ArrayAdapter<String> adapterEstados = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                estados
        );
        adapterEstados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEstadoPedido.setAdapter(adapterEstados);

        if (getIntent().getBooleanExtra("modoEditar", false)) {
            modoEditar = true;
            idPedido = getIntent().getIntExtra("idPedido", -1);

            String nombreCliente = getIntent().getStringExtra("nombreCliente");
            String nombreProducto = getIntent().getStringExtra("nombreProducto");
            int cantidad = getIntent().getIntExtra("cantidad", 1);
            String estado = getIntent().getStringExtra("estado");
            String fechaEntrega = getIntent().getStringExtra("fechaEntrega");

            etCantidadPedido.setText(String.valueOf(cantidad));
            etFechaEntregaPedido.setText(fechaEntrega);
            btnGuardarPedido.setText("Actualizar pedido");

            if (nombreCliente != null) {
                for (int i = 0; i < listaClientes.size(); i++) {
                    if (listaClientes.get(i).getNombre().equals(nombreCliente)) {
                        spClientesPedido.setSelection(i);
                        break;
                    }
                }
            }

            if (nombreProducto != null) {
                for (int i = 0; i < listaProductos.size(); i++) {
                    if (listaProductos.get(i).getNombre().equals(nombreProducto)) {
                        spProductosPedido.setSelection(i);
                        break;
                    }
                }
            }

            if (estado != null) {
                for (int i = 0; i < estados.length; i++) {
                    if (estados[i].equals(estado)) {
                        spEstadoPedido.setSelection(i);
                        break;
                    }
                }
            }
        }

        btnGuardarPedido.setOnClickListener(v -> guardarPedido());
    }

    private void guardarPedido() {
        if (listaClientes.isEmpty() || listaProductos.isEmpty()) {
            Toast.makeText(this, "Debe haber clientes y productos registrados", Toast.LENGTH_SHORT).show();
            return;
        }

        Cliente clienteSeleccionado = (Cliente) spClientesPedido.getSelectedItem();
        Producto productoSeleccionado = (Producto) spProductosPedido.getSelectedItem();
        String estadoSeleccionado = spEstadoPedido.getSelectedItem().toString();

        String cantidadTexto = etCantidadPedido.getText().toString().trim();
        String fechaEntrega = etFechaEntregaPedido.getText().toString().trim();

        if (cantidadTexto.isEmpty() || fechaEntrega.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(cantidadTexto);

        Pedido pedido = new Pedido(
                clienteSeleccionado.getNombre(),
                productoSeleccionado.getNombre(),
                cantidad,
                fechaEntrega,
                estadoSeleccionado
        );

        if (modoEditar) {
            pedido.setId(idPedido);
            db.pedidoDao().actualizar(pedido);
            Toast.makeText(this, "Pedido actualizado", Toast.LENGTH_SHORT).show();
        } else {
            db.pedidoDao().insertar(pedido);
            Toast.makeText(this, "Pedido guardado", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}