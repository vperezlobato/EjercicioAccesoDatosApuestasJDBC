package com.company.Clases;

import com.company.ClaseAbstracta.Apuesta;

import java.util.UUID;

public class ApuestaTipo2 extends Apuesta {
    private Character localOVisitante;
    private Integer goles;

    public ApuestaTipo2() {
        localOVisitante = ' ';
        goles = 0;

    }

    public ApuestaTipo2(UUID id, Double cantidad, Character tipo) {
        super(id, cantidad, tipo);
    }

    public ApuestaTipo2(UUID id, Double cantidad, Double cuota, UUID idPartido, String correoUsuario, Character tipo) {
        super(id, cantidad, cuota, idPartido, correoUsuario, tipo);
    }

    public ApuestaTipo2(UUID id, Double cantidad, Double cuota, UUID idPartido, String correoUsuario, Character tipo, Character localOVisitante, Integer goles) {
        super(id, cantidad, cuota, idPartido, correoUsuario, tipo);
        this.localOVisitante = localOVisitante;
        this.goles = goles;
    }

    public Character getLocalOVisitante() {
        return localOVisitante;
    }

    public void setLocalOVisitante(Character localOVisitante) {
        this.localOVisitante = localOVisitante;
    }

    public Integer getGoles() {
        return goles;
    }

    public void setGoles(Integer goles) {
        this.goles = goles;
    }

    @Override
    public String toString() {
        return "ApuestaTipo2{" +
                "localOVisitante=" + localOVisitante +
                ", goles=" + goles +
                "} " + super.toString();
    }
}
