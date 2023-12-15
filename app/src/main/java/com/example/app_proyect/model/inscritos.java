package com.example.app_proyect.model;

public class inscritos {
    private String  id;
    private String idAlumno;
    private String idGrupo;

    @Override
    public String toString() {
        return id;
    }

    public inscritos() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(String idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }
}
