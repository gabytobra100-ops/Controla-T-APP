package com.example.controlat2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VentaDao {

    @Insert
    void insertar(Venta venta);

    @Update
    void actualizar(Venta venta);

    @Delete
    void eliminar(Venta venta);

    @Query("SELECT * FROM ventas ORDER BY id DESC")
    List<Venta> obtenerTodas();

    @Query("SELECT COUNT(*) FROM ventas")
    int contarVentas();

    @Query("SELECT COALESCE(SUM(total), 0) FROM ventas")
    double obtenerIngresosTotales();

    @Query("SELECT * FROM ventas WHERE clienteId = :clienteId ORDER BY id DESC")
    List<Venta> obtenerVentasPorCliente(int clienteId);
}