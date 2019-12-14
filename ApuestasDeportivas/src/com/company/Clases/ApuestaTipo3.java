package com.company.Clases;

import com.company.ClaseAbstracta.Apuesta;

import java.util.UUID;

public class ApuestaTipo3 extends Apuesta {
    Character unoxDos;

    public ApuestaTipo3() {
        unoxDos = ' ';
    }

    public ApuestaTipo3(UUID id, Double cantidad, Double cuota, UUID idPartido, String correoUsuario, Character tipo, Character unoxDos) {
        super(id, cantidad, cuota, idPartido, correoUsuario, tipo);
        this.unoxDos = unoxDos;
    }

    public ApuestaTipo3(UUID id, Double cantidad, Character tipo) {
        super(id, cantidad, tipo);
    }

    public ApuestaTipo3(UUID id, Double cantidad, Double cuota, UUID idPartido, String correoUsuario, Character tipo) {
        super(id, cantidad, cuota, idPartido, correoUsuario, tipo);
    }

    public Character getUnoxDos() {
        return unoxDos;
    }

    public void setUnoxDos(Character unoxDos) {
        this.unoxDos = unoxDos;
    }

    @Override
    public String toString() {
        return "ApuestaTipo3{" +
                "unoxDos=" + unoxDos +
                "} " + super.toString();
    }
}
