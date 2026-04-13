package com.example.controlat2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ventas")
public class Venta {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int clienteId;
    private String nombreCliente;
    private String nombreProducto;
    private int cantidad;
    private double total;
    private String fecha;

    public Venta(int clienteId, String nombreCliente, String nombreProducto, int cantidad, double total, String fecha) {
        this.clienteId = clienteId;
        this.nombreCliente = nombreCliente;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.total = total;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getTotal() {
        return total;
    }

    public String getFecha() {
        return fecha;
    }
}