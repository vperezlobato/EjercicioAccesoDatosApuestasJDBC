package com.company.Clases;

import java.util.UUID;

public class Movimiento {
    private UUID id;
    private String correoUsuario;
    private double cantidad;
    private String tipo;

    public Movimiento(){}

    public Movimiento(UUID id, String correoUsuario,double cantidad, String tipo){
        this.id = id;
        this.correoUsuario = correoUsuario;
        this.cantidad = cantidad;
        this.tipo = tipo;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public UUID getId() {
        return id;
    }
}
