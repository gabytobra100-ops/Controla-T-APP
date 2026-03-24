package com.example.controlat2;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Producto.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    public abstract ProductoDao productoDao();
}
