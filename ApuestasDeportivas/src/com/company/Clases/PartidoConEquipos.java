package com.company.Clases;

import java.util.UUID;

public class PartidoConEquipos {
    private UUID id;
    private String competicion;
    private String equipoLocal;
    private String equipoVisitante;

    public PartidoConEquipos() {

    }

    public PartidoConEquipos(UUID id, String competicion, String equipoLocal, String equipoVisitante) {
        this.id = id;
        this.competicion = competicion;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCompeticion() {
        return competicion;
    }

    public void setCompeticion(String competicion) {
        this.competicion = competicion;
    }

    public String getEquipoLocal() {
        return equipoLocal;
    }

    public void setEquipoLocal(String equipoLocal) {
        this.equipoLocal = equipoLocal;
    }

    public String getEquipoVisitante() {
        return equipoVisitante;
    }

    public void setEquipoVisitante(String equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }
}
