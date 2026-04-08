package com.example.controlat2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ClienteDao {

    @Update
    void actualizar(Cliente cliente);
    @Insert
    void insertar(Cliente cliente);

    @Query("SELECT * FROM clientes")
    List<Cliente> obtenerTodos();

    @Delete
    void eliminar(Cliente cliente);
}