package com.example.controlat2;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private TextView btnPeriodoDiario, btnPeriodoSemanal, btnPeriodoMensual;
    private TextView txtTituloPeriodo;
    private String periodoSeleccionado = "diario";
    private SharedPreferences preferences;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private AppDatabase db;

    private TextView txtPorcentajeMeta, txtEstadoMeta;
    private View frameCirculoIngresos;

    private TextView txtDashboardIngresos, txtDashboardClientes, txtDashboardVentas,
            txtDashboardProductos, txtDashboardSinStock;

    private TextView txtRanking1, txtRanking2, txtRanking3, txtRanking4, txtRanking5;
    private TextView btnRankingClientes, btnRankingFragancias, btnRankingDias;
    private String tipoRankingSeleccionado = "clientes";
    private TextView txtAlerta1, txtAlerta2, txtAlerta3;

    private View barVentas, barClientes, barProductos;
    private TextView txtGraficaVentasValor, txtGraficaClientesValor, txtGraficaProductosValor;

    private CardView cardAlertas;
    private View layoutAlertas;

    private List<String> listaAlertasCompletas = new ArrayList<>();

    private CardView cardRankingComercial;
    private View layoutRankingComercial;
    private CardView cardClientesDashboard, cardVentasDashboard, cardProductosDashboard, cardSinStockDashboard;
    private CardView cardIngresosDashboard;
    private View layoutIngresosDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // BOTONES/TABS DE ARRIBA
        btnPeriodoDiario = findViewById(R.id.btnPeriodoDiario);
        btnPeriodoSemanal = findViewById(R.id.btnPeriodoSemanal);
        btnPeriodoMensual = findViewById(R.id.btnPeriodoMensual);
        txtTituloPeriodo = findViewById(R.id.txtTituloPeriodo);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbarAdmin);

        txtDashboardIngresos = findViewById(R.id.txtDashboardIngresos);
        txtPorcentajeMeta = findViewById(R.id.txtPorcentajeMeta);
        txtEstadoMeta = findViewById(R.id.txtEstadoMeta);
        frameCirculoIngresos = findViewById(R.id.frameCirculoIngresos);

        txtDashboardClientes = findViewById(R.id.txtDashboardClientes);
        txtDashboardVentas = findViewById(R.id.txtDashboardVentas);
        txtDashboardProductos = findViewById(R.id.txtDashboardProductos);
        txtDashboardSinStock = findViewById(R.id.txtDashboardSinStock);

        cardClientesDashboard = findViewById(R.id.cardClientesDashboard);
        cardVentasDashboard = findViewById(R.id.cardVentasDashboard);
        cardProductosDashboard = findViewById(R.id.cardProductosDashboard);
        cardSinStockDashboard = findViewById(R.id.cardSinStockDashboard);

        txtRanking1 = findViewById(R.id.txtRanking1);
        txtRanking2 = findViewById(R.id.txtRanking2);
        txtRanking3 = findViewById(R.id.txtRanking3);
        txtRanking4 = findViewById(R.id.txtRanking4);
        txtRanking5 = findViewById(R.id.txtRanking5);

        btnRankingClientes = findViewById(R.id.btnRankingClientes);
        btnRankingFragancias = findViewById(R.id.btnRankingFragancias);
        btnRankingDias = findViewById(R.id.btnRankingDias);

        txtAlerta1 = findViewById(R.id.txtAlerta1);
        txtAlerta2 = findViewById(R.id.txtAlerta2);
        txtAlerta3 = findViewById(R.id.txtAlerta3);

        barVentas = findViewById(R.id.barVentas);
        barClientes = findViewById(R.id.barClientes);
        barProductos = findViewById(R.id.barProductos);

        txtGraficaVentasValor = findViewById(R.id.txtGraficaVentasValor);
        txtGraficaClientesValor = findViewById(R.id.txtGraficaClientesValor);
        txtGraficaProductosValor = findViewById(R.id.txtGraficaProductosValor);

        cardRankingComercial = findViewById(R.id.cardRankingComercial);
        layoutRankingComercial = findViewById(R.id.layoutRankingComercial);

        cardAlertas = findViewById(R.id.cardAlertas);
        layoutAlertas = findViewById(R.id.layoutAlertas);

        cardIngresosDashboard = findViewById(R.id.cardIngresosDashboard);
        layoutIngresosDashboard = findViewById(R.id.layoutIngresosDashboard);

        preferences = getSharedPreferences("meta_ingresos", MODE_PRIVATE);

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

        // SOLO EL CÍRCULO ABRE LA VENTANA DE METAS
        frameCirculoIngresos.setOnClickListener(v -> mostrarDialogoMetas());

        // TABS DE PERIODO
        btnPeriodoDiario.setOnClickListener(v -> {
            periodoSeleccionado = "diario";
            actualizarTabsPeriodo();
            cargarDashboard();
        });

        btnPeriodoSemanal.setOnClickListener(v -> {
            periodoSeleccionado = "semanal";
            actualizarTabsPeriodo();
            cargarDashboard();
        });

        btnPeriodoMensual.setOnClickListener(v -> {
            periodoSeleccionado = "mensual";
            actualizarTabsPeriodo();
            cargarDashboard();
        });

        actualizarTabsPeriodo();
        cargarDashboard();

        cardAlertas.setOnClickListener(v -> mostrarDialogoAlertas());
        layoutAlertas.setOnClickListener(v -> mostrarDialogoAlertas());

        cardClientesDashboard.setOnClickListener(v ->
                startActivity(new Intent(AdminActivity.this, ClientesActivity.class)));

        cardVentasDashboard.setOnClickListener(v ->
                startActivity(new Intent(AdminActivity.this, VentasActivity.class)));

        cardProductosDashboard.setOnClickListener(v ->
                startActivity(new Intent(AdminActivity.this, ProductosActivity.class)));

        cardSinStockDashboard.setOnClickListener(v ->
                startActivity(new Intent(AdminActivity.this, ProductosActivity.class)));

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
                startActivity(new Intent(AdminActivity.this, PedidosActivity.class));
            } else if (id == R.id.nav_reportes) {
                startActivity(new Intent(AdminActivity.this, ReportesActivity.class));
            } else if (id == R.id.nav_cerrar_sesion) {
                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        });

        btnRankingClientes.setOnClickListener(v -> {
            tipoRankingSeleccionado = "clientes";
            actualizarTabsRanking();
            cargarDashboard();
        });

        btnRankingFragancias.setOnClickListener(v -> {
            tipoRankingSeleccionado = "fragancias";
            actualizarTabsRanking();
            cargarDashboard();
        });

        btnRankingDias.setOnClickListener(v -> {
            tipoRankingSeleccionado = "dias";
            actualizarTabsRanking();
            cargarDashboard();
        });

        actualizarTabsRanking();

    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDashboard();
    }

    private void actualizarTabsRanking() {
        btnRankingClientes.setBackgroundResource(
                tipoRankingSeleccionado.equals("clientes") ? R.drawable.bg_periodo_activo : R.drawable.bg_periodo_inactivo);
        btnRankingFragancias.setBackgroundResource(
                tipoRankingSeleccionado.equals("fragancias") ? R.drawable.bg_periodo_activo : R.drawable.bg_periodo_inactivo);
        btnRankingDias.setBackgroundResource(
                tipoRankingSeleccionado.equals("dias") ? R.drawable.bg_periodo_activo : R.drawable.bg_periodo_inactivo);

        btnRankingClientes.setTextColor(tipoRankingSeleccionado.equals("clientes") ? 0xFFFFFFFF : 0xFFB0BEC5);
        btnRankingFragancias.setTextColor(tipoRankingSeleccionado.equals("fragancias") ? 0xFFFFFFFF : 0xFFB0BEC5);
        btnRankingDias.setTextColor(tipoRankingSeleccionado.equals("dias") ? 0xFFFFFFFF : 0xFFB0BEC5);
    }

    private void cargarDashboard() {
        List<Producto> productos = db.productoDao().obtenerTodos();
        List<Venta> ventas = db.ventaDao().obtenerTodas();
        List<Cliente> clientes = db.clienteDao().obtenerTodos();
        List<Pedido> pedidos = db.pedidoDao().obtenerTodos();

        int totalProductos = productos.size();
        int totalVentas = ventas.size();
        int totalClientes = clientes.size();
        int sinStock = 0;
        int stockBajo = 0;
        double ingresos = calcularIngresosPorPeriodo(ventas, periodoSeleccionado);

        for (Producto producto : productos) {
            if (producto.getStock() == 0) {
                sinStock++;
            }
            if (producto.getStock() > 0 && producto.getStock() <= 2) {
                stockBajo++;
            }
        }

        if (txtDashboardProductos != null) {
            txtDashboardProductos.setText(String.valueOf(totalProductos));
        }
        if (txtDashboardVentas != null) {
            txtDashboardVentas.setText(String.valueOf(totalVentas));
        }
        if (txtDashboardClientes != null) {
            txtDashboardClientes.setText(String.valueOf(totalClientes));
        }
        if (txtDashboardSinStock != null) {
            txtDashboardSinStock.setText(String.valueOf(sinStock));
        }
        if (txtDashboardIngresos != null) {
            txtDashboardIngresos.setText("$" + String.format("%.2f", ingresos));
        }

        float meta = 0;
        String nombrePeriodo = "";

        if (periodoSeleccionado.equals("diario")) {
            meta = preferences.getFloat("meta_diaria", 0);
            nombrePeriodo = "diaria";
            txtTituloPeriodo.setText("Balance diario");
        } else if (periodoSeleccionado.equals("semanal")) {
            meta = preferences.getFloat("meta_semanal", 0);
            nombrePeriodo = "semanal";
            txtTituloPeriodo.setText("Balance semanal");
        } else {
            meta = preferences.getFloat("meta_mensual", 0);
            nombrePeriodo = "mensual";
            txtTituloPeriodo.setText("Balance mensual");
        }

        if (meta > 0) {
            int porcentaje = Math.round((float) (ingresos * 100) / meta);

            if (txtPorcentajeMeta != null) {
                txtPorcentajeMeta.setText(porcentaje + "% de la meta " + nombrePeriodo);
            }

            if (porcentaje < 40) {
                if (txtEstadoMeta != null) txtEstadoMeta.setText("Nivel bajo");
                if (frameCirculoIngresos != null) {
                    frameCirculoIngresos.setBackgroundResource(R.drawable.bg_circle_rojo);
                }
            } else if (porcentaje < 80) {
                if (txtEstadoMeta != null) txtEstadoMeta.setText("En progreso");
                if (frameCirculoIngresos != null) {
                    frameCirculoIngresos.setBackgroundResource(R.drawable.bg_circle_amarillo);
                }
            } else {
                if (txtEstadoMeta != null) txtEstadoMeta.setText("Meta cumplida");
                if (frameCirculoIngresos != null) {
                    frameCirculoIngresos.setBackgroundResource(R.drawable.bg_circle_dashboard);
                }
            }
        } else {
            if (txtPorcentajeMeta != null) {
                txtPorcentajeMeta.setText("0% de la meta " + nombrePeriodo);
            }
            if (txtEstadoMeta != null) {
                txtEstadoMeta.setText("Sin meta definida");
            }
            if (frameCirculoIngresos != null) {
                frameCirculoIngresos.setBackgroundResource(R.drawable.bg_circle_dashboard);
            }
        }

        float metaDiaria = preferences.getFloat("meta_diaria", 0);
        float metaSemanal = preferences.getFloat("meta_semanal", 0);
        float metaMensual = preferences.getFloat("meta_mensual", 0);

        double ingresosDiarios = calcularIngresosPorPeriodo(ventas, "diario");
        double ingresosSemanales = calcularIngresosPorPeriodo(ventas, "semanal");
        double ingresosMensuales = calcularIngresosPorPeriodo(ventas, "mensual");

        TextView txtPorcentajeDiario = findViewById(R.id.txtPorcentajeDiario);
        TextView txtPorcentajeSemanal = findViewById(R.id.txtPorcentajeSemanal);
        TextView txtPorcentajeMensual = findViewById(R.id.txtPorcentajeMensual);

        int porcentajeDiario = metaDiaria > 0 ? Math.round((float) (ingresosDiarios * 100) / metaDiaria) : 0;
        int porcentajeSemanal = metaSemanal > 0 ? Math.round((float) (ingresosSemanales * 100) / metaSemanal) : 0;
        int porcentajeMensual = metaMensual > 0 ? Math.round((float) (ingresosMensuales * 100) / metaMensual) : 0;

        txtPorcentajeDiario.setText(porcentajeDiario + "%");
        txtPorcentajeSemanal.setText(porcentajeSemanal + "%");
        txtPorcentajeMensual.setText(porcentajeMensual + "%");

        if (txtGraficaVentasValor != null) {
            txtGraficaVentasValor.setText(String.valueOf(totalVentas));
        }
        if (txtGraficaClientesValor != null) {
            txtGraficaClientesValor.setText(String.valueOf(totalClientes));
        }
        if (txtGraficaProductosValor != null) {
            txtGraficaProductosValor.setText(String.valueOf(totalProductos));
        }

        int ventasPeriodo = calcularVentasPorPeriodo(ventas, periodoSeleccionado);
        int clientesPeriodo = calcularClientesPorPeriodo(ventas, periodoSeleccionado);
        int productosPeriodo = calcularProductosPorPeriodo(ventas, periodoSeleccionado);

        if (barVentas != null && barClientes != null && barProductos != null) {
            actualizarGraficaBarras(ventasPeriodo, clientesPeriodo, productosPeriodo);
        }

        cargarRankingSegunFiltro(ventas);
        cargarAlertas(productos, ventas, pedidos, sinStock, stockBajo);
    }

    private int calcularVentasPorPeriodo(List<Venta> ventas, String periodo) {
        int contador = 0;

        for (Venta venta : ventas) {
            if (ventaPerteneceAlPeriodo(venta.getFecha(), periodo)) {
                contador++;
            }
        }

        return contador;
    }

    private int calcularClientesPorPeriodo(List<Venta> ventas, String periodo) {
        Map<String, Boolean> clientesUnicos = new HashMap<>();

        for (Venta venta : ventas) {
            if (ventaPerteneceAlPeriodo(venta.getFecha(), periodo)) {
                String nombreCliente = venta.getNombreCliente();

                if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
                    nombreCliente = "Público general";
                }

                clientesUnicos.put(nombreCliente, true);
            }
        }

        return clientesUnicos.size();
    }

    private int calcularProductosPorPeriodo(List<Venta> ventas, String periodo) {
        int total = 0;

        for (Venta venta : ventas) {
            if (ventaPerteneceAlPeriodo(venta.getFecha(), periodo)) {
                total += venta.getCantidad();
            }
        }

        return total;
    }

    private boolean ventaPerteneceAlPeriodo(String fechaTexto, String periodo) {
        if (fechaTexto == null || fechaTexto.trim().isEmpty()) {
            return false;
        }

        try {
            String[] partes = fechaTexto.split("/");
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int anio = Integer.parseInt(partes[2]);

            java.util.Calendar fechaVenta = java.util.Calendar.getInstance();
            fechaVenta.set(java.util.Calendar.DAY_OF_MONTH, dia);
            fechaVenta.set(java.util.Calendar.MONTH, mes - 1);
            fechaVenta.set(java.util.Calendar.YEAR, anio);
            fechaVenta.set(java.util.Calendar.HOUR_OF_DAY, 0);
            fechaVenta.set(java.util.Calendar.MINUTE, 0);
            fechaVenta.set(java.util.Calendar.SECOND, 0);
            fechaVenta.set(java.util.Calendar.MILLISECOND, 0);

            java.util.Calendar hoy = java.util.Calendar.getInstance();

            if (periodo.equals("diario")) {
                return esMismoDia(fechaVenta, hoy);
            } else if (periodo.equals("semanal")) {
                return esMismaSemana(fechaVenta, hoy);
            } else {
                return esMismoMes(fechaVenta, hoy);
            }

        } catch (Exception e) {
            return false;
        }
    }
    private boolean esMismoDia(java.util.Calendar fechaVenta, java.util.Calendar hoy) {
        return fechaVenta.get(java.util.Calendar.DAY_OF_MONTH) == hoy.get(java.util.Calendar.DAY_OF_MONTH)
                && fechaVenta.get(java.util.Calendar.MONTH) == hoy.get(java.util.Calendar.MONTH)
                && fechaVenta.get(java.util.Calendar.YEAR) == hoy.get(java.util.Calendar.YEAR);
    }
    private boolean esMismaSemana(java.util.Calendar fechaVenta, java.util.Calendar hoy) {
        return fechaVenta.get(java.util.Calendar.WEEK_OF_YEAR) == hoy.get(java.util.Calendar.WEEK_OF_YEAR)
                && fechaVenta.get(java.util.Calendar.YEAR) == hoy.get(java.util.Calendar.YEAR);
    }
    private boolean esMismoMes(java.util.Calendar fechaVenta, java.util.Calendar hoy) {
        return fechaVenta.get(java.util.Calendar.MONTH) == hoy.get(java.util.Calendar.MONTH)
                && fechaVenta.get(java.util.Calendar.YEAR) == hoy.get(java.util.Calendar.YEAR);
    }

    private boolean esMismoDia(long fecha1, long fecha2) {
        return android.text.format.DateUtils.isToday(fecha1);
    }

    private boolean esMismaSemana(long fecha1, long fecha2) {
        long diff = Math.abs(fecha1 - fecha2);
        return diff <= (7L * 24 * 60 * 60 * 1000);
    }

    private boolean esMismoMes(long fecha1, long fecha2) {
        java.util.Calendar c1 = java.util.Calendar.getInstance();
        java.util.Calendar c2 = java.util.Calendar.getInstance();

        c1.setTimeInMillis(fecha1);
        c2.setTimeInMillis(fecha2);

        return c1.get(java.util.Calendar.MONTH) == c2.get(java.util.Calendar.MONTH)
                && c1.get(java.util.Calendar.YEAR) == c2.get(java.util.Calendar.YEAR);
    }

    private void cargarRankingSegunFiltro(List<Venta> ventas) {
        List<String> ranking;

        if (tipoRankingSeleccionado.equals("clientes")) {
            ranking = generarRankingClientes(ventas);
        } else if (tipoRankingSeleccionado.equals("fragancias")) {
            ranking = generarRankingProductos(ventas);
        } else {
            ranking = generarRankingDias(ventas);
        }

        txtRanking1.setText(obtenerLineaRanking(ranking, 0));
        txtRanking2.setText(obtenerLineaRanking(ranking, 1));
        txtRanking3.setText(obtenerLineaRanking(ranking, 2));
        txtRanking4.setText(obtenerLineaRanking(ranking, 3));
        txtRanking5.setText(obtenerLineaRanking(ranking, 4));
    }

    private String obtenerLineaRanking(List<String> ranking, int indice) {
        if (indice < ranking.size()) {
            return (indice + 1) + ". " + ranking.get(indice);
        }
        return (indice + 1) + ". Sin datos";
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
        if (barra == null) return;

        ViewGroup.LayoutParams params = barra.getLayoutParams();
        if (params == null) return;

        params.height = dpToPx(Math.max(alturaDp, 10));
        barra.setLayoutParams(params);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void cargarPreviewRankingClientes(List<Venta> ventas) {
        List<String> ranking = generarRankingClientes(ventas);

        txtRanking1.setText(obtenerLineaRanking(ranking, 0));
        txtRanking2.setText(obtenerLineaRanking(ranking, 1));
        txtRanking3.setText(obtenerLineaRanking(ranking, 2));
        txtRanking4.setText(obtenerLineaRanking(ranking, 3));
        txtRanking5.setText(obtenerLineaRanking(ranking, 4));
    }


    private List<String> generarRankingClientes(List<Venta> ventas) {
        Map<String, Integer> conteo = new HashMap<>();

        for (Venta venta : ventas) {
            String nombre = venta.getNombreCliente();
            if (nombre == null || nombre.trim().isEmpty()) {
                nombre = "Público general";
            }
            conteo.put(nombre, conteo.getOrDefault(nombre, 0) + 1);
        }

        return ordenarRanking(conteo, " compras");
    }

    private List<String> generarRankingProductos(List<Venta> ventas) {
        Map<String, Integer> conteo = new HashMap<>();

        for (Venta venta : ventas) {
            String producto = venta.getNombreProducto();
            if (producto == null || producto.trim().isEmpty()) continue;
            conteo.put(producto, conteo.getOrDefault(producto, 0) + venta.getCantidad());
        }

        return ordenarRanking(conteo, " ventas");
    }

    private List<String> generarRankingDias(List<Venta> ventas) {
        Map<String, Integer> conteo = new HashMap<>();

        for (Venta venta : ventas) {
            String fecha = venta.getFecha();
            if (fecha == null || fecha.trim().isEmpty()) continue;
            conteo.put(fecha, conteo.getOrDefault(fecha, 0) + 1);
        }

        return ordenarRanking(conteo, " ventas");
    }

    private List<String> ordenarRanking(Map<String, Integer> conteo, String sufijo) {
        List<Map.Entry<String, Integer>> lista = new ArrayList<>(conteo.entrySet());
        lista.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        List<String> resultado = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : lista) {
            resultado.add(entry.getKey() + " (" + entry.getValue() + sufijo + ")");
        }
        return resultado;
    }

    private void cargarAlertas(List<Producto> productos, List<Venta> ventas, List<Pedido> pedidos, int sinStock, int stockBajo) {
        listaAlertasCompletas.clear();

        if (sinStock > 0) {
            listaAlertasCompletas.add("Hay " + sinStock + " producto(s) sin stock");
        }

        if (stockBajo > 0) {
            listaAlertasCompletas.add("Hay " + stockBajo + " producto(s) con stock bajo");
        }

        for (Pedido pedido : pedidos) {
            if (pedido.getEstado().equalsIgnoreCase("Pendiente")) {
                listaAlertasCompletas.add("Pedido pendiente: " + pedido.getNombreCliente() +
                        " quiere " + pedido.getCantidad() + " " + pedido.getNombreProducto());
            }
        }

        if (ventas.isEmpty()) {
            listaAlertasCompletas.add("Aún no hay ventas registradas");
        } else {
            listaAlertasCompletas.add("Se han registrado " + ventas.size() + " venta(s)");
        }

        if (listaAlertasCompletas.isEmpty()) {
            listaAlertasCompletas.add("Sin alertas");
        }

        txtAlerta1.setText("• " + obtenerAlerta(0));
        txtAlerta2.setText("• " + obtenerAlerta(1));
        txtAlerta3.setText("• " + obtenerAlerta(2));
    }

    private String obtenerAlerta(int indice) {
        if (indice < listaAlertasCompletas.size()) {
            return listaAlertasCompletas.get(indice);
        }
        return "Sin alertas";
    }

    private void mostrarDialogoAlertas() {
        StringBuilder builder = new StringBuilder();

        for (String alerta : listaAlertasCompletas) {
            builder.append("• ").append(alerta).append("\n\n");
        }

        new AlertDialog.Builder(this)
                .setTitle("Notificaciones del negocio")
                .setMessage(builder.toString().trim())
                .setPositiveButton("Cerrar", null)
                .show();
    }

    private void mostrarDialogoMetas() {
        double ingresosActuales = 0;
        List<Venta> ventas = db.ventaDao().obtenerTodas();

        for (Venta venta : ventas) {
            ingresosActuales += venta.getTotal();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Configurar metas de ingresos");

        androidx.appcompat.widget.LinearLayoutCompat layout =
                new androidx.appcompat.widget.LinearLayoutCompat(this);
        layout.setOrientation(androidx.appcompat.widget.LinearLayoutCompat.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        TextView txtIngresosActuales = new TextView(this);
        txtIngresosActuales.setText("Ingresos actuales: $" + String.format("%.2f", ingresosActuales));
        txtIngresosActuales.setTextSize(18f);
        txtIngresosActuales.setPadding(0, 0, 0, 30);

        TextView lblMetaDiaria = new TextView(this);
        lblMetaDiaria.setText("Meta diaria");
        lblMetaDiaria.setTextSize(15f);
        lblMetaDiaria.setTextColor(0xFF000000);

        EditText etMetaDiaria = new EditText(this);
        etMetaDiaria.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etMetaDiaria.setText(String.valueOf(preferences.getFloat("meta_diaria", 0)));
        etMetaDiaria.setHint("Ingresa meta diaria");

        TextView lblMetaSemanal = new TextView(this);
        lblMetaSemanal.setText("Meta semanal");
        lblMetaSemanal.setTextSize(15f);
        lblMetaSemanal.setTextColor(0xFF000000);
        lblMetaSemanal.setPadding(0, 20, 0, 0);

        EditText etMetaSemanal = new EditText(this);
        etMetaSemanal.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etMetaSemanal.setText(String.valueOf(preferences.getFloat("meta_semanal", 0)));
        etMetaSemanal.setHint("Ingresa meta semanal");

        TextView lblMetaMensual = new TextView(this);
        lblMetaMensual.setText("Meta mensual");
        lblMetaMensual.setTextSize(15f);
        lblMetaMensual.setTextColor(0xFF000000);
        lblMetaMensual.setPadding(0, 20, 0, 0);

        EditText etMetaMensual = new EditText(this);
        etMetaMensual.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etMetaMensual.setText(String.valueOf(preferences.getFloat("meta_mensual", 0)));
        etMetaMensual.setHint("Ingresa meta mensual");

        layout.addView(txtIngresosActuales);

        layout.addView(lblMetaDiaria);
        layout.addView(etMetaDiaria);

        layout.addView(lblMetaSemanal);
        layout.addView(etMetaSemanal);

        layout.addView(lblMetaMensual);
        layout.addView(etMetaMensual);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            float metaDiaria = parseFloatSeguro(etMetaDiaria.getText().toString());
            float metaSemanal = parseFloatSeguro(etMetaSemanal.getText().toString());
            float metaMensual = parseFloatSeguro(etMetaMensual.getText().toString());

            SharedPreferences.Editor editor = preferences.edit();
            editor.putFloat("meta_diaria", metaDiaria);
            editor.putFloat("meta_semanal", metaSemanal);
            editor.putFloat("meta_mensual", metaMensual);
            editor.apply();

            cargarDashboard();
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
    private float parseFloatSeguro(String valor) {
        try {
            return Float.parseFloat(valor);
        } catch (Exception e) {
            return 0;
        }
    }
    private void actualizarTabsPeriodo() {
        btnPeriodoDiario.setBackgroundResource(
                periodoSeleccionado.equals("diario") ? R.drawable.bg_periodo_activo : R.drawable.bg_periodo_inactivo);
        btnPeriodoSemanal.setBackgroundResource(
                periodoSeleccionado.equals("semanal") ? R.drawable.bg_periodo_activo : R.drawable.bg_periodo_inactivo);
        btnPeriodoMensual.setBackgroundResource(
                periodoSeleccionado.equals("mensual") ? R.drawable.bg_periodo_activo : R.drawable.bg_periodo_inactivo);

        btnPeriodoDiario.setTextColor(periodoSeleccionado.equals("diario") ? 0xFFFFFFFF : 0xFFB0BEC5);
        btnPeriodoSemanal.setTextColor(periodoSeleccionado.equals("semanal") ? 0xFFFFFFFF : 0xFFB0BEC5);
        btnPeriodoMensual.setTextColor(periodoSeleccionado.equals("mensual") ? 0xFFFFFFFF : 0xFFB0BEC5);
    }
    private double calcularIngresosPorPeriodo(List<Venta> ventas, String periodo) {
        double total = 0;

        java.util.Calendar hoy = java.util.Calendar.getInstance();
        int diaActual = hoy.get(java.util.Calendar.DAY_OF_MONTH);
        int mesActual = hoy.get(java.util.Calendar.MONTH) + 1;
        int anioActual = hoy.get(java.util.Calendar.YEAR);
        int semanaActual = hoy.get(java.util.Calendar.WEEK_OF_YEAR);

        for (Venta venta : ventas) {
            String fecha = venta.getFecha();
            if (fecha == null || !fecha.contains("/")) continue;

            try {
                String[] partes = fecha.split("/");
                int dia = Integer.parseInt(partes[0]);
                int mes = Integer.parseInt(partes[1]);
                int anio = Integer.parseInt(partes[2]);

                java.util.Calendar fechaVenta = java.util.Calendar.getInstance();
                fechaVenta.set(anio, mes - 1, dia);

                if (periodo.equals("diario")) {
                    if (dia == diaActual && mes == mesActual && anio == anioActual) {
                        total += venta.getTotal();
                    }
                } else if (periodo.equals("semanal")) {
                    int semanaVenta = fechaVenta.get(java.util.Calendar.WEEK_OF_YEAR);
                    if (semanaVenta == semanaActual && anio == anioActual) {
                        total += venta.getTotal();
                    }
                } else if (periodo.equals("mensual")) {
                    if (mes == mesActual && anio == anioActual) {
                        total += venta.getTotal();
                    }
                }
            } catch (Exception e) {
                // Ignorar fechas mal formateadas
            }
        }

        return total;
    }

}