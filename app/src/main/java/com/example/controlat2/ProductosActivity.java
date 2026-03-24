package com.example.controlat2;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ProductosActivity extends AppCompatActivity {

    private RecyclerView recyclerProductos;
    private List<Producto> listaProductos;
    private ProductoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_productos);

        recyclerProductos = findViewById(R.id.recyclerProductos);
        recyclerProductos.setLayoutManager(new LinearLayoutManager(this));

        listaProductos = new ArrayList<>();

        // Productos de prueba
        listaProductos.add(new Producto("212 VIP Men Black", "Fragancia 30 ml", 100.0, 4, 1));
        listaProductos.add(new Producto("Acqua De Gio Men", "Fragancia 30 ml", 100.0, 2, 2));
        listaProductos.add(new Producto("Blue De Chanel Men", "Fragancia 30 ml", 100.0, 5, 3));
        listaProductos.add(new Producto("Blue Seduction Men", "Fragancia 30 ml", 100.0, 0,4));
        listaProductos.add(new Producto("Boss Orange Men", "Fragancia 30 ml", 100.0, 4, 5));
        listaProductos.add(new Producto("Ck one", "Fragancia 30 ml", 100.0, 2, 6));
        listaProductos.add(new Producto("Dulce Gabana", "Fragancia 30 ml", 100.0, 5, 7));
        listaProductos.add(new Producto("Eros Men", "Fragancia 30 ml", 100.0, 0,8));
        listaProductos.add(new Producto("Imagination", "Fragancia 30 ml", 100.0, 4, 9));
        listaProductos.add(new Producto("Invictus Aqua", "Fragancia 30 ml", 100.0, 2, 10));
        listaProductos.add(new Producto("Invictus Intense Men", "Fragancia 30 ml", 100.0, 5, 11));
        listaProductos.add(new Producto("Invictus Legend Men", "Fragancia 30 ml", 100.0, 0,12));
        listaProductos.add(new Producto("Invictus men", "Fragancia 30 ml", 100.0, 4, 13));
        listaProductos.add(new Producto("Invictus Onix", "Fragancia 30 ml", 100.0, 2, 14));
        listaProductos.add(new Producto("Invictus Victory", "Fragancia 30 ml", 100.0, 5, 15));
        listaProductos.add(new Producto("Lacoste Red Men", "Fragancia 30 ml", 100.0, 0,16));

        adapter = new ProductoAdapter(listaProductos);
        recyclerProductos.setAdapter(adapter);
    }
}