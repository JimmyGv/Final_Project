package com.example.app_proyect.model;

public class aceptados {
    private String id;
    private  String Usuario;
    private String correo;
    private String userType;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        String st;
        if (estatus.equals("I")){
            st = "Inactivo";
        }else{
            st = "Activo";
        }
        return Usuario + " "+st;
    }

    public aceptados() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    private String estatus;
    private String contrasena;
}
