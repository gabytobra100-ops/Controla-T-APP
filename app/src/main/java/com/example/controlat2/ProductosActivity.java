package com.example.controlat2;

import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_productos);

        recyclerProductos = findViewById(R.id.recyclerProductos);
        recyclerProductos.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "controlat-db")
                .allowMainThreadQueries()
                .build();

        listaProductos = db.productoDao().obtenerTodos();

        if (listaProductos.isEmpty()) {
            db.productoDao().insertar(new Producto("212 VIP Men Black", "Fragancia 60 ml", 100.0, 4));
            db.productoDao().insertar(new Producto("Invictus", "Fragancia 100 ml", 150.0, 2));
            db.productoDao().insertar(new Producto("Sauvage", "Fragancia 60 ml", 180.0,3));
            db.productoDao().insertar(new Producto("Acqua De Gio Men", "Fragancia 60 ml", 100.0, 0));

            listaProductos = db.productoDao().obtenerTodos();
        }

        adapter = new ProductoAdapter(listaProductos);
        recyclerProductos.setAdapter(adapter);
    }
}