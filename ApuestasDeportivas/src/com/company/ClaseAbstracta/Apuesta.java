package com.company.ClaseAbstracta;

import java.util.UUID;

//Era abstracto
public class Apuesta {

    private UUID id;
    private Double cantidad;
    private Double cuota;
    private UUID idPartido;
    private String correoUsuario;
    private Character tipo;

    public Apuesta() {
        this.id = new UUID(0,0);
        this.cantidad = 0.0;
        this.cuota = 0.0;
        this.idPartido = new UUID(0,0);;
        this.correoUsuario = "";
        this.tipo = ' ';
    }

    public Apuesta(UUID id, Double cantidad, Double cuota, UUID idPartido, String correoUsuario, Character tipo) {
        this.id = id;
        this.cantidad = cantidad;
        this.cuota = cuota;
        this.idPartido = idPartido;
        this.correoUsuario = correoUsuario;
        this.tipo = tipo;
    }

    public Apuesta(UUID id, Double cantidad,Character tipo){
        this.id = id;
        this.cantidad = cantidad;
        this.tipo = tipo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getCuota() {
        return cuota;
    }

    public void setCuota(Double cuota) {
        this.cuota = cuota;
    }

    public UUID getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(UUID idPartido) {
        this.idPartido = idPartido;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public Character getTipo() {
        return tipo;
    }

    public void setTipo(Character tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Apuesta{" +
                "id=" + id +
                ", cantidad=" + cantidad +
                ", cuota=" + cuota +
                ", idPartido=" + idPartido +
                ", correoUsuario='" + correoUsuario + '\'' +
                ", tipo=" + tipo +
                '}';
    }
}

