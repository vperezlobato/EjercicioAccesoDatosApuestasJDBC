package com.company.Clases;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.GregorianCalendar;
import java.util.UUID;

public class Partido {
    private UUID id;
    private String competicion;
    private int golLocal;
    private int golVisitante;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private boolean estaAbierto;
    private double limiteAlcanzadoTipo1;
    private double limiteAlcanzadoTipo2;
    private double limiteAlcanzadoTipo3;

    public Partido() {

    }



    public Partido(String competicion, int golLocal, int golVisitante, Timestamp fechaInicio, Timestamp fechaFin,boolean estaAbierto,double limiteAlcanzadoTipo1,double limiteAlcanzadoTipo2,double limiteAlcanzadoTipo3) {
        this.competicion = competicion;
        this.golLocal = golLocal;
        this.golVisitante = golVisitante;
        this.fechaInicio = fechaInicio.toLocalDateTime();
        this.fechaFin = fechaFin.toLocalDateTime();
        this.estaAbierto = estaAbierto;
        this.limiteAlcanzadoTipo1 = limiteAlcanzadoTipo1;
        this.limiteAlcanzadoTipo2 = limiteAlcanzadoTipo2;
        this.limiteAlcanzadoTipo3 = limiteAlcanzadoTipo3;
    }

    public Partido(String competicion, int golLocal, int golVisitante, LocalDateTime fechaInicio, LocalDateTime fechaFin,boolean estaAbierto,double limiteAlcanzadoTipo1,double limiteAlcanzadoTipo2,double limiteAlcanzadoTipo3) {
        this.competicion = competicion;
        this.golLocal = golLocal;
        this.golVisitante = golVisitante;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estaAbierto = estaAbierto;
        this.limiteAlcanzadoTipo1 = limiteAlcanzadoTipo1;
        this.limiteAlcanzadoTipo2 = limiteAlcanzadoTipo2;
        this.limiteAlcanzadoTipo3 = limiteAlcanzadoTipo3;
    }

    public Partido(String competicion, int golesLocal, int golesVisitante, Timestamp fechaInicio, Timestamp fechaFin) {
        this.competicion = competicion;
        this.golLocal = golesLocal;
        this.golVisitante = golesVisitante;
        this.fechaInicio = fechaInicio.toLocalDateTime();
        this.fechaFin = fechaFin.toLocalDateTime();
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

    public int getGolLocal() {
        return golLocal;
    }

    public void setGolLocal(int golLocal) {
        this.golLocal = golLocal;
    }

    public int getGolVisitante() {
        return golVisitante;
    }

    public void setGolVisitante(int golVisitante) {
        this.golVisitante = golVisitante;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getLimiteAlcanzadoTipo1() {
        return limiteAlcanzadoTipo1;
    }

    public double getLimiteAlcanzadoTipo2() {
        return limiteAlcanzadoTipo2;
    }

    public double getLimiteAlcanzadoTipo3() {
        return limiteAlcanzadoTipo3;
    }

    public boolean getEstaAbierto() {
        return estaAbierto;
    }

    public void setEstaAbierto(boolean estaAbierto) {
        this.estaAbierto = estaAbierto;
    }

    @Override
    public String toString() {
        return competicion + golLocal + golVisitante + fechaInicio + fechaFin;
    }
}
