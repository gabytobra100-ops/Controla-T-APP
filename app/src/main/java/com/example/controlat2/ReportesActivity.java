package com.example.controlat2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportesActivity extends AppCompatActivity {

    private TextView txtTotalProductos, txtTotalVentas, txtIngresosTotales, txtProductosSinStock;
    private TextView txtClienteFrecuente, txtProductoMasVendido;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        txtTotalProductos = findViewById(R.id.txtTotalProductos);
        txtTotalVentas = findViewById(R.id.txtTotalVentas);
        txtIngresosTotales = findViewById(R.id.txtIngresosTotales);
        txtProductosSinStock = findViewById(R.id.txtProductosSinStock);
        txtClienteFrecuente = findViewById(R.id.txtClienteFrecuente);
        txtProductoMasVendido = findViewById(R.id.txtProductoMasVendido);

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
        List<Producto> productos = db.productoDao().obtenerTodos();
        List<Venta> ventas = db.ventaDao().obtenerTodas();

        int totalProductos = productos.size();
        int totalVentas = ventas.size();

        double ingresos = 0;
        int sinStock = 0;

        Map<String, Integer> comprasPorCliente = new HashMap<>();
        Map<String, Integer> ventasPorProducto = new HashMap<>();

        for (Venta venta : ventas) {
            ingresos += venta.getTotal();

            String nombreCliente = venta.getNombreCliente();
            if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
                nombreCliente = "Público general";
            }

            comprasPorCliente.put(
                    nombreCliente,
                    comprasPorCliente.getOrDefault(nombreCliente, 0) + 1
            );

            String nombreProducto = venta.getNombreProducto();
            ventasPorProducto.put(
                    nombreProducto,
                    ventasPorProducto.getOrDefault(nombreProducto, 0) + venta.getCantidad()
            );
        }

        for (Producto producto : productos) {
            if (producto.getStock() == 0) {
                sinStock++;
            }
        }

        String clienteFrecuente = "-";
        int maxCompras = 0;

        for (Map.Entry<String, Integer> entry : comprasPorCliente.entrySet()) {
            if (entry.getValue() > maxCompras) {
                maxCompras = entry.getValue();
                clienteFrecuente = entry.getKey() + " (" + entry.getValue() + " compras)";
            }
        }

        String productoMasVendido = "-";
        int maxVentas = 0;

        for (Map.Entry<String, Integer> entry : ventasPorProducto.entrySet()) {
            if (entry.getValue() > maxVentas) {
                maxVentas = entry.getValue();
                productoMasVendido = entry.getKey() + " (" + entry.getValue() + " unidades)";
            }
        }

        txtTotalProductos.setText(String.valueOf(totalProductos));
        txtTotalVentas.setText(String.valueOf(totalVentas));
        txtIngresosTotales.setText("$" + String.format("%.2f", ingresos));
        txtProductosSinStock.setText(String.valueOf(sinStock));

        txtClienteFrecuente.setText(clienteFrecuente);
        txtProductoMasVendido.setText(productoMasVendido);
    }
}