package com.example.controlat2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private AppDatabase db;

    private TextView txtDashboardIngresos, txtDashboardClientes, txtDashboardVentas,
            txtDashboardProductos, txtDashboardSinStock;

    private TextView txtTopCliente1, txtTopCliente2, txtTopCliente3;
    private TextView txtAlerta1, txtAlerta2, txtAlerta3;

    private View barVentas, barClientes, barProductos;
    private TextView txtGraficaVentasValor, txtGraficaClientesValor, txtGraficaProductosValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbarAdmin);

        txtDashboardIngresos = findViewById(R.id.txtDashboardIngresos);
        txtDashboardClientes = findViewById(R.id.txtDashboardClientes);
        txtDashboardVentas = findViewById(R.id.txtDashboardVentas);
        txtDashboardProductos = findViewById(R.id.txtDashboardProductos);
        txtDashboardSinStock = findViewById(R.id.txtDashboardSinStock);

        txtTopCliente1 = findViewById(R.id.txtTopCliente1);
        txtTopCliente2 = findViewById(R.id.txtTopCliente2);
        txtTopCliente3 = findViewById(R.id.txtTopCliente3);

        txtAlerta1 = findViewById(R.id.txtAlerta1);
        txtAlerta2 = findViewById(R.id.txtAlerta2);
        txtAlerta3 = findViewById(R.id.txtAlerta3);

        barVentas = findViewById(R.id.barVentas);
        barClientes = findViewById(R.id.barClientes);
        barProductos = findViewById(R.id.barProductos);

        txtGraficaVentasValor = findViewById(R.id.txtGraficaVentasValor);
        txtGraficaClientesValor = findViewById(R.id.txtGraficaClientesValor);
        txtGraficaProductosValor = findViewById(R.id.txtGraficaProductosValor);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "controlat-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                0,
                0
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        cargarDashboard();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_dashboard) {
                drawerLayout.closeDrawers();
                return true;
            } else if (id == R.id.nav_clientes) {
                startActivity(new Intent(AdminActivity.this, ClientesActivity.class));
            } else if (id == R.id.nav_productos) {
                startActivity(new Intent(AdminActivity.this, ProductosActivity.class));
            } else if (id == R.id.nav_ventas) {
                startActivity(new Intent(AdminActivity.this, VentasActivity.class));
            } else if (id == R.id.nav_pedidos) {
                Toast.makeText(this, "Módulo de pedidos próximamente", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_reportes) {
                startActivity(new Intent(AdminActivity.this, ReportesActivity.class));
            } else if (id == R.id.nav_cerrar_sesion) {
                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDashboard();
    }

    private void cargarDashboard() {
        List<Producto> productos = db.productoDao().obtenerTodos();
        List<Venta> ventas = db.ventaDao().obtenerTodas();
        List<Cliente> clientes = db.clienteDao().obtenerTodos();

        int totalProductos = productos.size();
        int totalVentas = ventas.size();
        int totalClientes = clientes.size();
        int sinStock = 0;
        int stockBajo = 0;
        double ingresos = 0;

        for (Producto producto : productos) {
            if (producto.getStock() == 0) {
                sinStock++;
            }
            if (producto.getStock() > 0 && producto.getStock() <= 2) {
                stockBajo++;
            }
        }

        for (Venta venta : ventas) {
            ingresos += venta.getTotal();
        }

        txtDashboardProductos.setText(String.valueOf(totalProductos));
        txtDashboardVentas.setText(String.valueOf(totalVentas));
        txtDashboardClientes.setText(String.valueOf(totalClientes));
        txtDashboardSinStock.setText(String.valueOf(sinStock));
        txtDashboardIngresos.setText("$" + String.format("%.2f", ingresos));

        int maxValorResumen = Math.max(totalVentas, Math.max(totalClientes, totalProductos));
        if (maxValorResumen == 0) {
            maxValorResumen = 1;
        }

        txtGraficaVentasValor.setText(String.valueOf(totalVentas));
        txtGraficaClientesValor.setText(String.valueOf(totalClientes));
        txtGraficaProductosValor.setText(String.valueOf(totalProductos));

        actualizarGraficaBarras(totalVentas, totalClientes, totalProductos);

        cargarTopClientes(ventas);
        cargarAlertas(productos, ventas, sinStock, stockBajo);
    }

    private void actualizarGraficaBarras(int ventas, int clientes, int productos) {
        int maxValor = Math.max(ventas, Math.max(clientes, productos));
        if (maxValor == 0) {
            maxValor = 1;
        }

        int alturaMaxDp = 120;

        ajustarAlturaBarra(barVentas, (ventas * alturaMaxDp) / maxValor);
        ajustarAlturaBarra(barClientes, (clientes * alturaMaxDp) / maxValor);
        ajustarAlturaBarra(barProductos, (productos * alturaMaxDp) / maxValor);
    }

    private void ajustarAlturaBarra(View barra, int alturaDp) {
        ViewGroup.LayoutParams params = barra.getLayoutParams();
        params.height = dpToPx(Math.max(alturaDp, 10));
        barra.setLayoutParams(params);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void cargarTopClientes(List<Venta> ventas) {
        Map<String, Integer> comprasPorCliente = new HashMap<>();

        for (Venta venta : ventas) {
            String nombreCliente = venta.getNombreCliente();

            if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
                nombreCliente = "Público general";
            }

            comprasPorCliente.put(
                    nombreCliente,
                    comprasPorCliente.getOrDefault(nombreCliente, 0) + 1
            );
        }

        List<Map.Entry<String, Integer>> listaOrdenada = new ArrayList<>(comprasPorCliente.entrySet());
        listaOrdenada.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        txtTopCliente1.setText("1. Sin datos");
        txtTopCliente2.setText("2. Sin datos");
        txtTopCliente3.setText("3. Sin datos");

        if (listaOrdenada.size() > 0) {
            txtTopCliente1.setText("1. " + listaOrdenada.get(0).getKey() + " (" + listaOrdenada.get(0).getValue() + " compras)");
        }
        if (listaOrdenada.size() > 1) {
            txtTopCliente2.setText("2. " + listaOrdenada.get(1).getKey() + " (" + listaOrdenada.get(1).getValue() + " compras)");
        }
        if (listaOrdenada.size() > 2) {
            txtTopCliente3.setText("3. " + listaOrdenada.get(2).getKey() + " (" + listaOrdenada.get(2).getValue() + " compras)");
        }
    }

    private void cargarAlertas(List<Producto> productos, List<Venta> ventas, int sinStock, int stockBajo) {
        txtAlerta1.setText("• Sin alertas");
        txtAlerta2.setText("• Sin alertas");
        txtAlerta3.setText("• Sin alertas");

        if (sinStock > 0) {
            txtAlerta1.setText("• Hay " + sinStock + " producto(s) sin stock");
        } else {
            txtAlerta1.setText("• No hay productos agotados");
        }

        if (stockBajo > 0) {
            txtAlerta2.setText("• Hay " + stockBajo + " producto(s) con stock bajo");
        } else {
            txtAlerta2.setText("• El stock general está estable");
        }

        if (ventas.isEmpty()) {
            txtAlerta3.setText("• Aún no hay ventas registradas");
        } else {
            txtAlerta3.setText("• Se han registrado " + ventas.size() + " venta(s)");
        }
    }
}