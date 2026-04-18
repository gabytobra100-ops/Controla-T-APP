package com.example.controlat2;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Producto.class, Venta.class, Cliente.class, Pedido.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PedidoDao pedidoDao();
    public abstract ProductoDao productoDao();
    public abstract VentaDao ventaDao();
    public abstract ClienteDao clienteDao();
}