package com.example.controlat2;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Producto.class, Venta.class, Cliente.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductoDao productoDao();
    public abstract VentaDao ventaDao();
    public abstract ClienteDao clienteDao();
}