package com.example.controlat2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class ProductosEmpleadoActivity extends AppCompatActivity {

    private RecyclerView recyclerProductosEmpleado;
    private List<Producto> listaProductos;
    private ProductoAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_empleado);

        recyclerProductosEmpleado = findViewById(R.id.recyclerProductosEmpleado);
        recyclerProductosEmpleado.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "controlat-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        listaProductos = db.productoDao().obtenerTodos();

        adapter = new ProductoAdapter(listaProductos, db);
        recyclerProductosEmpleado.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (listaProductos != null && db != null && adapter != null) {
            listaProductos.clear();
            listaProductos.addAll(db.productoDao().obtenerTodos());
            adapter.notifyDataSetChanged();
        }
    }
}