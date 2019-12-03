package com.company.Gestionadoras;

import com.company.ClaseAbstracta.Apuesta;
import com.company.Clases.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

public class JDBC {

    public Connection crearConexion(String usuario,String constrasena){
        String sourceURL = "jdbc:sqlserver://localhost;DatabaseName=CasaDeApuestas";
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(sourceURL, usuario, constrasena);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conexion;
    }

    public boolean ingresarDinero(Connection conexion,double cantidad,String correo){
        CallableStatement cstmt = null;
        boolean execute = false;
        try {
            cstmt = conexion.prepareCall("{ call IngresarDinero(?,?)}");
            cstmt.setDouble(1, cantidad);
            cstmt.setString(2, correo);
            execute = cstmt.execute();
        }catch (SQLException e) {
            System.out.println("El ingreso no se ha podido realizar");
        }finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return execute;
    }

    public boolean retirarDinero(Connection conexion,double cantidad,String correo){
        CallableStatement cstmt = null;
        boolean execute = false;
        try {
            cstmt = conexion.prepareCall("{ call RetirarDinero(?,?)}");
            cstmt.setDouble(1, cantidad);
            cstmt.setString(2, correo);
            execute = cstmt.execute();
        }catch (SQLException e) {
            System.out.println("El ingreso no se ha podido realizar");
        }finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return execute;
    }


    public ArrayList<Movimiento> movimientosCuenta(Connection conexion, String correo){
        PreparedStatement sentencia = null;
        String miOrden = "";
        ResultSet datos = null;
        Movimiento movimiento = new Movimiento();
        ArrayList<Movimiento> movimientos= new ArrayList<Movimiento>();

        try {
            miOrden = "Select * From Movimientos Where correoUsuario = ?";
            sentencia = conexion.prepareStatement(miOrden);
            sentencia.setString(1, correo);
            datos = sentencia.executeQuery();

            while(datos.next()) {
                movimiento = new Movimiento(new UUID(0,0).fromString( datos.getString("ID")), datos.getString("correoUsuario"), datos.getDouble("cantidad"), datos.getString("tipo"));
                movimientos.add(movimiento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return movimientos;
    }

    public ArrayList<Apuesta> apuestasRealizadas(Connection conexion, String correo){
        PreparedStatement sentencia = null;
        String miOrden = "";
        ResultSet datos = null;
        Apuesta apuesta = null;
        ArrayList<Apuesta> apuestas = new ArrayList<Apuesta>();

        try{
            miOrden = "Select ID,cantidad,Tipo From Apuestas Where correoUsuario = ?";
            sentencia = conexion.prepareStatement(miOrden);
            sentencia.setString(1,correo);
            datos = sentencia.executeQuery();

            while(datos.next()){
                if(datos.getString("Tipo").equals("1")){
                    apuesta = new ApuestaTipo1(new UUID(0,0).fromString(datos.getString("ID")),datos.getDouble("cantidad"),
                            datos.getString("Tipo").charAt(0));
                }else
                    if(datos.getString("Tipo").equals("2")){
                        apuesta = new ApuestaTipo2(new UUID(0,0).fromString(datos.getString("ID")),datos.getDouble("cantidad"),
                                datos.getString("Tipo").charAt(0));
                    }else
                        if(datos.getString("Tipo").equals("3")){
                            apuesta = new ApuestaTipo3(new UUID(0,0).fromString(datos.getString("ID")),datos.getDouble("cantidad"),
                                    datos.getString("Tipo").charAt(0));
                        }
                apuestas.add(apuesta);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return apuestas;
    }

    public ArrayList<Apuesta> apuestasGanadas(Connection conexion, String correo){
        PreparedStatement sentencia = null;
        String miOrden = "";
        ResultSet datos = null;
        ArrayList<Apuesta> apuestas = new ArrayList<Apuesta>();
        Apuesta apuesta = null;

        try{
            miOrden = "Select A.ID,A.cantidad,A.cuota,A.IDPartido,A.CorreoUsuario,A.Tipo From Apuestas AS A " +
                    "LEFT JOIN ApuestasTipo1 AS AT1 ON A.ID = AT1.IDApuesta " +
                    "INNER JOIN Partidos AS P ON P.ID = A.IDPartido " +
                    "LEFT JOIN ApuestasTipo2 AS AT2 ON AT2.IDApuesta = A.ID  " +
                    "LEFT JOIN ApuestasTipo3 AS AT3 ON AT3.IDApuesta = A.ID " +
                    "WHERE (AT1.GolesLocal = P.golesLocal AND AT1.GolesVisitante = P.golesVisitante) " +
                    "OR (AT2.LocalOVisitante = 'L' AND AT2.Goles = P.golesLocal )   " +
                    "OR (AT3.[1X2] = '1' AND P.golesLocal > P.golesVisitante) OR (AT3.[1X2] = '2' AND P.golesLocal < P.golesVisitante) OR (AT3.[1X2] = 'X' AND P.golesLocal = P.golesVisitante) " +
                    "AND correoUsuario = ?";
            sentencia = conexion.prepareStatement(miOrden);
            sentencia.setString(1,correo);
            datos = sentencia.executeQuery();

            while(datos.next()){
                if(datos.getString("Tipo").equals("1")){
                    apuesta = new ApuestaTipo1(new UUID(0,0).fromString(datos.getString("ID")),datos.getDouble("cantidad"),
                            datos.getDouble("cuota"),new UUID(0,0).fromString(datos.getString("IDPartido")),
                            datos.getString("CorreoUsuario"),datos.getString("Tipo").charAt(0));
                }else
                if(datos.getString("Tipo").equals("2")){
                    apuesta = new ApuestaTipo2(new UUID(0,0).fromString(datos.getString("ID")),datos.getDouble("cantidad"),
                            datos.getDouble("cuota"),new UUID(0,0).fromString(datos.getString("IDPartido")),
                            datos.getString("CorreoUsuario"),datos.getString("Tipo").charAt(0));
                }else
                if(datos.getString("Tipo").equals("3")){
                    apuesta = new ApuestaTipo3(new UUID(0,0).fromString(datos.getString("ID")),datos.getDouble("cantidad"),
                            datos.getDouble("cuota"),new UUID(0,0).fromString(datos.getString("IDPartido")),
                            datos.getString("CorreoUsuario"),datos.getString("Tipo").charAt(0));
                }
                apuestas.add(apuesta);

            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return apuestas;
    }

    public int crearPartidoBBDD(Connection conexion, Partido partido){
        int filas = 0;
        PreparedStatement sentencia = null;
        String  miOrden = "";

        try{
            miOrden = "INSERT INTO Partidos (competicion,golesLocal,golesVisitante,fechaInicio,fechaFin,EstaAbierto,limiteAlcanzadoTipo1,limiteAlcanzadoTipo2,limiteAlcanzadoTipo3) values (?,?,?,?,?,?,?,?,?)";
            sentencia = conexion.prepareStatement(miOrden);
            sentencia.setString(1,partido.getCompeticion());
            sentencia.setDouble(2,partido.getGolLocal());
            sentencia.setDouble(3,partido.getGolVisitante());
            sentencia.setTimestamp(4,Timestamp.valueOf(partido.getFechaInicio()));
            sentencia.setTimestamp(5,Timestamp.valueOf(partido.getFechaFin()));
            sentencia.setBoolean(6,partido.getEstaAbierto());
            sentencia.setDouble(7,partido.getLimiteAlcanzadoTipo1());
            sentencia.setDouble(8,partido.getLimiteAlcanzadoTipo1());
            sentencia.setDouble(9,partido.getLimiteAlcanzadoTipo1());
            filas = sentencia.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();;
        }

        return filas;
    }

    public boolean consultarPartido(Connection conexion,String idPartido){
        CallableStatement cstmt = null;
        boolean execute = false;
        try {
            cstmt = conexion.prepareCall("{ call consultarPartido(?)}");
            cstmt.setString(1, idPartido);
            execute = cstmt.execute();
        }catch (SQLException e) {
            System.out.println("No se pudo consultar el partido");
        }finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return execute;
    }
}
