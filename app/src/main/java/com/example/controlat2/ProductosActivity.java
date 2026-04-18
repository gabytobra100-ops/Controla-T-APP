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

public class ProductosActivity extends AppCompatActivity {

    private RecyclerView recyclerProductos;
    private List<Producto> listaProductos;
    private ProductoAdapter adapter;
    private AppDatabase db;
    private Button btnAgregar, btnEliminar, btnModificar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_productos);

        btnAgregar = findViewById(R.id.btnAgregar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnModificar = findViewById(R.id.btnModificar);
        recyclerProductos = findViewById(R.id.recyclerProductos);

        recyclerProductos.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "controlat-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        listaProductos = db.productoDao().obtenerTodos();

        if (listaProductos.isEmpty()) {
            db.productoDao().insertar(new Producto("212 VIP Men Black", "Fragancia 60 ml", 100.0, 4, R.drawable.fragancias));
            db.productoDao().insertar(new Producto("Invictus", "Fragancia 100 ml", 150.0, 2, R.drawable.fragancias));
            db.productoDao().insertar(new Producto("Sauvage", "Fragancia 60 ml", 180.0, 3, R.drawable.fragancias));
            db.productoDao().insertar(new Producto("Acqua De Gio Men", "Fragancia 60 ml", 100.0, 0, R.drawable.fragancias));

            listaProductos = db.productoDao().obtenerTodos();
        }

        adapter = new ProductoAdapter(listaProductos, db);
        recyclerProductos.setAdapter(adapter);

        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(ProductosActivity.this, AgregarProductoActivity.class);
            startActivity(intent);
        });

        btnEliminar.setOnClickListener(v -> {
            Producto productoSeleccionado = adapter.getProductoSeleccionado();
            if (productoSeleccionado != null) {
                db.productoDao().eliminar(productoSeleccionado);
                listaProductos.clear();
                listaProductos.addAll(db.productoDao().obtenerTodos());
                adapter.limpiarSeleccion();
                adapter.notifyDataSetChanged();
            }
        });

        btnModificar.setOnClickListener(v -> {
            Producto productoSeleccionado = adapter.getProductoSeleccionado();

            if (productoSeleccionado != null) {
                Intent intent = new Intent(ProductosActivity.this, AgregarProductoActivity.class);
                intent.putExtra("modoEditar", true);
                intent.putExtra("id", productoSeleccionado.getId());
                intent.putExtra("nombre", productoSeleccionado.getNombre());
                intent.putExtra("descripcion", productoSeleccionado.getDescripcion());
                intent.putExtra("precio", productoSeleccionado.getPrecio());
                intent.putExtra("stock", productoSeleccionado.getStock());
                intent.putExtra("imagenResId", productoSeleccionado.getImagenResId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (listaProductos != null && db != null && adapter != null) {
            listaProductos.clear();
            listaProductos.addAll(db.productoDao().obtenerTodos());
            adapter.notifyDataSetChanged();
            adapter.limpiarSeleccion();
        }
    }
}