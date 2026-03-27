package com.example.controlat2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class ReportesActivity extends AppCompatActivity {

    private TextView txtTotalProductos, txtTotalVentas, txtIngresosTotales, txtProductosSinStock;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reportes);

        txtTotalProductos = findViewById(R.id.txtTotalProductos);
        txtTotalVentas = findViewById(R.id.txtTotalVentas);
        txtIngresosTotales = findViewById(R.id.txtIngresosTotales);
        txtProductosSinStock = findViewById(R.id.txtProductosSinStock);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "controlat-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        cargarReportes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarReportes();
    }

    private void cargarReportes() {
        int totalProductos = db.productoDao().contarProductos();
        int totalVentas = db.ventaDao().contarVentas();
        int productosSinStock = db.productoDao().contarProductosSinStock();

        Double ingresos = db.ventaDao().obtenerIngresosTotales();
        if (ingresos == null) {
            ingresos = 0.0;
        }

        txtTotalProductos.setText("Total de productos: " + totalProductos);
        txtTotalVentas.setText("Total de ventas: " + totalVentas);
        txtIngresosTotales.setText("Ingresos totales: $" + ingresos);
        txtProductosSinStock.setText("Productos sin stock: " + productosSinStock);
    }
}