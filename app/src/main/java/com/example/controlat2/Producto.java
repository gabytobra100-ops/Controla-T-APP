package com.example.controlat2;

public class Producto {
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private int id;

    public Producto (String nombre, String descripcion, double precio, int stock, int id){
        this.nombre=nombre;
        this.descripcion=descripcion;
        this.precio= precio;
        this.stock= stock;
        this.id= id;
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

    public int getId() {
        return id;
    }
}
