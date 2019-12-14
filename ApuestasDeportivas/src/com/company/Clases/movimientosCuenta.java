package com.company.Clases;

public class MovimientosCuenta {

    private String correoUsuario;
    private int numeroApuestasRealizadas;
    private int numeroApuestasGanadas;

    public MovimientosCuenta(){}

    public MovimientosCuenta(String correoUsuario, int numeroApuestasRealizadas, int numeroApuestasGanadas){
        this.correoUsuario = correoUsuario;
        this.numeroApuestasRealizadas = numeroApuestasRealizadas;
        this.numeroApuestasGanadas = numeroApuestasGanadas;

    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public int getNumeroApuestasGanadas() {
        return numeroApuestasGanadas;
    }

    public int getNumeroApuestasRealizadas() {
        return numeroApuestasRealizadas;
    }
}
