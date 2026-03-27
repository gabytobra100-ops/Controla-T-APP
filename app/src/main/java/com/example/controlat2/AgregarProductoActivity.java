package com.example.controlat2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class AgregarProductoActivity extends AppCompatActivity {

    private EditText etNombre, etDescripcion, etPrecio, etStock;
    private Button btnGuardar;
    private AppDatabase db;

    private boolean modoEditar = false;
    private int idProducto = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etPrecio = findViewById(R.id.etPrecio);
        etStock = findViewById(R.id.etStock);
        btnGuardar = findViewById(R.id.btnGuardar);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "controlat-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        if (getIntent().getBooleanExtra("modoEditar", false)) {
            modoEditar = true;
            idProducto = getIntent().getIntExtra("id", -1);

            String nombre = getIntent().getStringExtra("nombre");
            String descripcion = getIntent().getStringExtra("descripcion");
            double precio = getIntent().getDoubleExtra("precio", 0);
            int stock = getIntent().getIntExtra("stock", 0);

            etNombre.setText(nombre);
            etDescripcion.setText(descripcion);
            etPrecio.setText(String.valueOf(precio));
            etStock.setText(String.valueOf(stock));

            btnGuardar.setText("Actualizar Producto");
        }

        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();
            String precioTexto = etPrecio.getText().toString().trim();
            String stockTexto = etStock.getText().toString().trim();

            if (nombre.isEmpty() || descripcion.isEmpty() || precioTexto.isEmpty() || stockTexto.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            double precio = Double.parseDouble(precioTexto);
            int stock = Integer.parseInt(stockTexto);

            if (modoEditar) {
                Producto productoActualizado = new Producto(nombre, descripcion, precio, stock);
                productoActualizado.setId(idProducto);

                db.productoDao().actualizar(productoActualizado);
                Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show();
            } else {
                Producto productoNuevo = new Producto(nombre, descripcion, precio, stock);
                db.productoDao().insertar(productoNuevo);
                Toast.makeText(this, "Producto guardado", Toast.LENGTH_SHORT).show();
            }

            finish();
        });
    }
}