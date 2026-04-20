package com.example.controlat2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.List;
import java.util.Calendar;

public class AgregarVentaActivity extends AppCompatActivity {

    private Spinner spProductos;
    private EditText etCantidadVenta, etFechaVenta;
    private TextView txtTotalCalculado;
    private Button btnGuardarVenta;
    private AppDatabase db;

    private List<Producto> listaProductosConStock;
    private Producto productoSeleccionado;

    private boolean modoEditar = false;
    private int idVenta = -1;
    private int clienteId = 0;
    private String nombreCliente = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_venta);

        spProductos = findViewById(R.id.spProductos);
        etCantidadVenta = findViewById(R.id.etCantidadVenta);
        etFechaVenta = findViewById(R.id.etFechaVenta);
        txtTotalCalculado = findViewById(R.id.txtTotalCalculado);
        btnGuardarVenta = findViewById(R.id.btnGuardarVenta);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "controlat-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        listaProductosConStock = db.productoDao().obtenerProductosConStock();

        ArrayAdapter<Producto> adapterSpinner = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaProductosConStock
        );
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProductos.setAdapter(adapterSpinner);

        if (!listaProductosConStock.isEmpty()) {
            productoSeleccionado = listaProductosConStock.get(0);
        }

        spProductos.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                productoSeleccionado = listaProductosConStock.get(position);
                calcularTotal();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        etCantidadVenta.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                calcularTotal();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        configurarCalendario();

        // MODO EDITAR
        if (getIntent().getBooleanExtra("modoEditar", false)) {
            modoEditar = true;
            idVenta = getIntent().getIntExtra("idVenta", -1);
            clienteId = getIntent().getIntExtra("clienteId", 0);
            nombreCliente = getIntent().getStringExtra("nombreCliente");
            String nombreProducto = getIntent().getStringExtra("nombreProducto");
            int cantidad = getIntent().getIntExtra("cantidad", 1);
            String fecha = getIntent().getStringExtra("fecha");

            etCantidadVenta.setText(String.valueOf(cantidad));
            etFechaVenta.setText(fecha);
            btnGuardarVenta.setText("Actualizar venta");

            if (nombreProducto != null) {
                for (int i = 0; i < listaProductosConStock.size(); i++) {
                    if (listaProductosConStock.get(i).getNombre().equals(nombreProducto)) {
                        spProductos.setSelection(i);
                        productoSeleccionado = listaProductosConStock.get(i);
                        break;
                    }
                }
            }

            calcularTotal();
        }

        btnGuardarVenta.setOnClickListener(v -> guardarVenta());
    }

    private void configurarCalendario() {
        etFechaVenta.setFocusable(false);
        etFechaVenta.setClickable(true);

        etFechaVenta.setOnClickListener(v -> {
            Calendar calendario = Calendar.getInstance();

            int anio = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AgregarVentaActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        String diaFormateado = String.format("%02d", dayOfMonth);
                        String mesFormateado = String.format("%02d", month + 1);
                        String fechaSeleccionada = diaFormateado + "/" + mesFormateado + "/" + year;
                        etFechaVenta.setText(fechaSeleccionada);
                    },
                    anio, mes, dia
            );

            datePickerDialog.show();
        });
    }

    private void guardarVenta() {
        if (productoSeleccionado == null) {
            Toast.makeText(this, "No hay productos con stock", Toast.LENGTH_SHORT).show();
            return;
        }

        String cantidadTexto = etCantidadVenta.getText().toString().trim();
        String fecha = etFechaVenta.getText().toString().trim();

        if (cantidadTexto.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(cantidadTexto);

        if (cantidad <= 0) {
            Toast.makeText(this, "La cantidad debe ser mayor a 0", Toast.LENGTH_SHORT).show();
            return;
        }

        double total = productoSeleccionado.getPrecio() * cantidad;

        if (modoEditar) {
            Venta ventaActualizada = new Venta(
                    clienteId,
                    nombreCliente,
                    productoSeleccionado.getNombre(),
                    cantidad,
                    total,
                    fecha
            );
            ventaActualizada.setId(idVenta);

            db.ventaDao().actualizar(ventaActualizada);
            Toast.makeText(this, "Venta actualizada", Toast.LENGTH_SHORT).show();
        } else {
            Venta nuevaVenta = new Venta(
                    clienteId,
                    nombreCliente,
                    productoSeleccionado.getNombre(),
                    cantidad,
                    total,
                    fecha
            );
            db.ventaDao().insertar(nuevaVenta);
            Toast.makeText(this, "Venta guardada", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void calcularTotal() {
        if (productoSeleccionado == null) {
            txtTotalCalculado.setText("Total: $0.0");
            return;
        }

        String cantidadTexto = etCantidadVenta.getText().toString().trim();

        if (cantidadTexto.isEmpty()) {
            txtTotalCalculado.setText("Total: $0.0");
            return;
        }

        int cantidad = Integer.parseInt(cantidadTexto);
        double total = productoSeleccionado.getPrecio() * cantidad;
        txtTotalCalculado.setText("Total: $" + total);
    }
}