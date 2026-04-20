package com.example.controlat2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.List;

public class PersonalActivity extends AppCompatActivity {

    private Button btnRegistrarVenta, btnVerProductos, btnVerPedidos;
    private TextView txtResumenProductos, txtResumenVentas, txtResumenPedidos;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        btnRegistrarVenta = findViewById(R.id.btnRegistrarVenta);
        btnVerProductos = findViewById(R.id.btnVerProductos);
        btnVerPedidos = findViewById(R.id.btnVerPedidos);

        txtResumenProductos = findViewById(R.id.txtResumenProductos);
        txtResumenVentas = findViewById(R.id.txtResumenVentas);
        txtResumenPedidos = findViewById(R.id.txtResumenPedidos);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "controlat-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        cargarResumen();

        btnRegistrarVenta.setOnClickListener(v -> {
            Intent intent = new Intent(PersonalActivity.this, AgregarVentaActivity.class);
            startActivity(intent);
        });

        btnVerProductos.setOnClickListener(v -> {
            Intent intent = new Intent(PersonalActivity.this, ProductosEmpleadoActivity.class);
            startActivity(intent);
        });

        btnVerPedidos.setOnClickListener(v -> {
            Intent intent = new Intent(PersonalActivity.this, PedidosActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarResumen();
    }

    private void cargarResumen() {
        List<Producto> productos = db.productoDao().obtenerTodos();
        List<Venta> ventas = db.ventaDao().obtenerTodas();
        List<Pedido> pedidos = db.pedidoDao().obtenerTodos();

        int pendientes = 0;
        for (Pedido pedido : pedidos) {
            if (pedido.getEstado() != null && pedido.getEstado().equalsIgnoreCase("Pendiente")) {
                pendientes++;
            }
        }

        txtResumenProductos.setText(String.valueOf(productos.size()));
        txtResumenVentas.setText(String.valueOf(ventas.size()));
        txtResumenPedidos.setText(String.valueOf(pendientes));
    }
}