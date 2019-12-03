package com.company.Conductores;

import com.company.Clases.Movimiento;
import com.company.Gestionadoras.JDBC;
import com.company.Utilidades.Utilidades;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class ConductoresJDBC {
    public static void main(String[] args){
        Utilidades utd = new Utilidades();
        JDBC jdbc = new JDBC();
        int filas = 0;
        Connection conexion = null;
        GregorianCalendar fechaInicio =  new GregorianCalendar(2019,5,12,21,30,25);
        GregorianCalendar fechaFin =  new GregorianCalendar(2019,6,12,21,30,25);
        conexion = jdbc.crearConexion("victor", "victor");
        ArrayList<Movimiento> movimientosCuenta = new ArrayList<Movimiento>();

        movimientosCuenta = jdbc.movimientosCuenta(conexion,"test@test.test");

        for(int i = 0; i < movimientosCuenta.size(); i++) {
            System.out.println(movimientosCuenta.get(i).getId() + " �" + " -> " + movimientosCuenta.get(i).getCantidad()
                    + " -> " + movimientosCuenta.get(i).getCorreoUsuario() + " -> " + movimientosCuenta.get(i).getTipo());
        }
    }
}
