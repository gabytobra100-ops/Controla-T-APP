package com.example.controlat2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PedidoDao {

    @Insert
    void insertar(Pedido pedido);

    @Update
    void actualizar(Pedido pedido);

    @Delete
    void eliminar(Pedido pedido);

    @Query("SELECT * FROM pedidos ORDER BY id DESC")
    List<Pedido> obtenerTodos();
}