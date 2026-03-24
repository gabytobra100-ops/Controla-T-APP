package com.example.controlat2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName ="productos")
public class Producto {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;

    public Producto (String nombre, String descripcion, double precio, int stock){
        this.nombre=nombre;
        this.descripcion=descripcion;
        this.precio= precio;
        this.stock= stock;

    }
    public String getNombre(){
        return nombre;
    }
    public String getDescripcion(){
        return descripcion;
    }
    public double getPrecio (){
        return precio;
    }
    public int getStock() {
        return stock;
    }

    public int getId(){return id;}
    public void setId(int id) {
        this.id = id;
    }
}
