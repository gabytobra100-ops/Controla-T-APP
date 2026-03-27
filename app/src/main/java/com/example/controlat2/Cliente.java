package com.example.controlat2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "clientes")
public class Cliente {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nombre;
    private String telefono;
    private String correo;
    private String notas;

    public Cliente(String nombre, String telefono, String correo, String notas) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.notas = notas;
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

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String getNotas() {
        return notas;
    }

    @Override
    public String toString() {
        return nombre + " - " + telefono;
    }
}