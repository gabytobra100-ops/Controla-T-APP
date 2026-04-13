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

    @Query("SELECT * FROM ventas")
    List<Venta> obtenerTodas();
    @Query("SELECT COUNT(*)FROM ventas")
    int contarVentas();
    @Query("SELECT SUM (total) FROM ventas")
    double obtenerIngresosTotales();

    @Query("SELECT * FROM ventas WHERE clienteId = :clienteId")
    List<Venta> obtenerVentasPorCliente(int clienteId);

    @Update
    void actualizar(Venta venta);

    @Delete
    void eliminar(Venta venta);
}