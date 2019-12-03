package com.company.Clases;

public class Usuarios {
    private String correoElectronico;
    private String contraseña;
    private double saldo;

    public Usuarios(){}

    public Usuarios(String correoElectronico,String contraseña,double saldo){
        this.correoElectronico = correoElectronico;
        this.contraseña = contraseña;
        this.saldo = saldo;

    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
}
