package com.example.controlat2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "productos")
public class Producto {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private int imagenResId;

    public Producto(String nombre, String descripcion, double precio, int stock, int imagenResId) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.imagenResId = imagenResId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public int getImagenResId() {
        return imagenResId;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setImagenResId(int imagenResId) {
        this.imagenResId = imagenResId;
    }

    @Override
    public String toString() {
        return nombre;
    }
}