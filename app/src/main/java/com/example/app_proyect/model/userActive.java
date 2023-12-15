package com.example.app_proyect.model;

public class userActive {
    private String nombre;
    private String id;

    public userActive() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Usuario: " + nombre;
    }
}
