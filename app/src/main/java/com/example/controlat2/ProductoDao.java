package com.example.controlat2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ProductoDao {
    @Insert
    void insertar (Producto producto);
    @Query ("SELECT * FROM productos")
    List <Producto> obtenerTodos();
    @Update
    void actualizar(Producto producto);
    @Delete
    void eliminar(Producto producto);
}
