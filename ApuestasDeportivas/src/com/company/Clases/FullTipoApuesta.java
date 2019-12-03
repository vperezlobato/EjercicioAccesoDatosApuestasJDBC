package com.company.Clases;

public class FullTipoApuesta {
    private Character tipo;
    private Integer golLocal;
    private Integer golVisitante;
    private Character localOVisitante;
    private Integer goles;
    private Character unoxDos;
    private Integer totalApostado;

    public FullTipoApuesta(){}

    public FullTipoApuesta(Character tipo,Integer golLocal,Integer golVisitante, Character localOVisitante,Integer goles,Character unoxDos,Integer totalApostado){
        this.tipo = tipo;
        this.golLocal = golLocal;
        this.golVisitante = golVisitante;
        this.localOVisitante = localOVisitante;
        this.goles = goles;
        this.unoxDos = unoxDos;
        this.totalApostado = totalApostado;
    }

    public Character getLocalOVisitante() {
        return localOVisitante;
    }

    public Character getTipo() {
        return tipo;
    }

    public Integer getGoles() {
        return goles;
    }

    public Integer getGolLocal() {
        return golLocal;
    }

    public Integer getGolVisitante() {
        return golVisitante;
    }

    public Character getUnoxDos() {
        return unoxDos;
    }

    public Integer getTotalApostado() {
        return totalApostado;
    }
}
