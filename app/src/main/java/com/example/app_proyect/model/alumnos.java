package com.example.app_proyect.model;

public class alumnos {
    private String id;
    private String promedio;
    private String grado;
    private String padre;
    private String madre;
    private String noTel1;
    private String noTel2;
    private String idUsuario;
    private String curp;

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getPromedio() {
        return promedio;
    }

    public void setPromedio(String promedio) {
        this.promedio = promedio;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getPadre() {
        return padre;
    }

    public void setPadre(String padre) {
        this.padre = padre;
    }

    public String getMadre() {
        return madre;
    }

    public void setMadre(String madre) {
        this.madre = madre;
    }

    public String getNoTel1() {
        return noTel1;
    }

    public void setNoTel1(String noTel1) {
        this.noTel1 = noTel1;
    }

    public String getNoTel2() {
        return noTel2;
    }

    public void setNoTel2(String noTel2) {
        this.noTel2 = noTel2;
    }

    public alumnos() {
    }

    @Override
    public String toString() {
        return promedio + '\''+ grado;
    }
}
