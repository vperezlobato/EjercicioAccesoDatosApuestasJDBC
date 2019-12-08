package com.company;

import com.company.ClaseAbstracta.Apuesta;
import com.company.Clases.Movimiento;
import com.company.Clases.Partido;
import com.company.Gestionadoras.JDBC;
import com.company.Utilidades.Utilidades;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

/*
Analisis
    Entrada: - cantidad;
             - correo;
    Salida: - Menus
            - Interactividad con el usuario
            - Datos relacionados con partidos
            - Datos relacionados con apuestas
    Requisitos:

PG Level 0
Inicio
    Login
    MenuUsuario
    MenuAdministrador
Fin

PG MenuUsuario Level 1
Inicio
    Repetir
        RealizarApuesta
        VerPartidosDisponibles
        ComprobarApuestaAnterior
        Cuenta
    Mientras no sea salir
Fin

PG MenuAdministrador Level 1
Inicio
    Repetir
        CrearPartido
        AbrirPartido
        CerrarPartido
        ApuestasPartido
        PagarApuestas
    Mientras no sea salir
Fin

PG Cuenta
Inicio
    Repetir
        IngresarDinero
        RetirarDinero
        MovimientosCuenta
    Mientras no sea salir
Fin

 */

public class Main {

    public static void main(String[] args) {

        //variables para las validaciones
        double cantidad = 0;
        String correo = "";
        int opcionMenu = 0;
        int opcionSubMenu = 0;
        String usuario = "";
        String contrasenha = "";

        Scanner teclado = new Scanner(System.in);
        Connection conexion = null;

        Utilidades utd = new Utilidades();
        JDBC jdbc = new JDBC();

        //Variable para saber si se ha insertado correctamente un dato de la BBDD
        boolean execute = false;

        //Variables para mostrar los datos de la consulta
        ArrayList<Apuesta> apuestasRealizadas = new ArrayList<Apuesta>();
        ArrayList<Movimiento> movimientosCuenta= new ArrayList<Movimiento>();
        ArrayList<Apuesta> apuestasGanadas = new ArrayList<Apuesta>();
        ArrayList<Partido> partidos = new ArrayList<>();
        int filas = 0;

        Partido partido = new Partido();

        //Login
        do {
            System.out.println("Introduce el usuario");
            usuario = teclado.next();
            System.out.println("Introduce la contrasenha");
            contrasenha = teclado.next();
            jdbc.crearConexion(usuario, contrasenha);
        }
        while(conexion == null);

        if(utd.Login(usuario, contrasenha)) {
            //Meter menu administrador y usuario
            opcionMenu = utd.leerYValidarOpcionMenuLoginConUsuario();
            switch (opcionMenu) {
                case 1:
                    //Menu usuario
                    opcionSubMenu = utd.leerYValidarOpcionMenuUsuario();
                    switch (opcionSubMenu) {
                        case 1:
                            //Apuestas realizadas (Metodo Rafa)

                        break;

                        case 2:
                            //VerPartidosDisponibles
                            conexion = jdbc.crearConexion(usuario, contrasenha);
                            partidos = jdbc.partidosAbiertos(conexion);
                            //partidos.stream().forEach(a -> a.getCompeticion() + " " + );
                        break;

                        case 3:
                            //ComprobarApuestaAnterior (Metodo Rafa)
                            conexion = jdbc.crearConexion(usuario, contrasenha);
                            break;

                        case 4:
                            //Cuenta
                            do {
                                opcionSubMenu = utd.leerYValidarOpcionSubMenuCuenta();

                                switch (opcionSubMenu) {
                                    case 1: //Ingresar Dinero
                                        cantidad = utd.leerYValidarCantidad();
                                        System.out.println("Introduce un correo: ");
                                        correo = teclado.next();

                                        conexion = jdbc.crearConexion(usuario, contrasenha);
                                        execute = jdbc.ingresarDinero(conexion, cantidad, correo);

                                        if(execute){
                                            System.out.println("Se ha ingresado correctamente");
                                        }
                                        break;

                                    case 2:
                                        //RetirarDinero
                                        cantidad = utd.leerYValidarCantidad();
                                        System.out.println("Introduce un correo: ");
                                        correo = teclado.next();

                                        conexion = jdbc.crearConexion(usuario, contrasenha);
                                        execute = jdbc.retirarDinero(conexion, cantidad, correo);

                                        if(execute){
                                            System.out.println("Se ha retirado correctamente");
                                        }
                                        break;

                                    case 3:
                                        //MovimientosCuenta
                                        System.out.println("Introduce un correo: ");
                                        correo = teclado.next();

                                        conexion = jdbc.crearConexion(usuario, contrasenha);
                                        movimientosCuenta = jdbc.movimientosCuenta(conexion,correo);

                                        for(int i = 0; i < movimientosCuenta.size(); i++) {
                                            System.out.println(movimientosCuenta.get(i).getId() + " �" + " -> " + movimientosCuenta.get(i).getCantidad()
                                                    + " -> " + movimientosCuenta.get(i).getCorreoUsuario() + " -> " + movimientosCuenta.get(i).getTipo());
                                        }
                                        break;

                                }
                                opcionSubMenu = utd.leerYValidarOpcionMenuAdministrador();
                            }
                            while (opcionSubMenu == 0);

                            break;

                    }

                break;

                case 2:
                    //Menu administrador
                    do {
                        opcionSubMenu = utd.leerYValidarOpcionMenuAdministrador();

                        switch (opcionSubMenu) {
                            case 1:
                                //CrearPartido
                                partido = utd.crearPartido();
                                conexion = jdbc.crearConexion(usuario, contrasenha);
                                filas = jdbc.crearPartidoBBDD(conexion,partido);
                                if(filas > 0){
                                    System.out.println("Se ha creado el partido correctamente");
                                }
                                break;

                            case 2:
                                //AbrirPartido
                                break;

                            case 3:
                                //CerrarPartido
                                break;

                            case 4:
                                //ApuestasPartido
                                break;

                            case 5:
                                //PagarApuestas
                                break;
                        }
                        opcionSubMenu = utd.leerYValidarOpcionMenuAdministrador();
                    }
                    while (opcionSubMenu == 0);

                    break;
            }
        }
        else {
            //Meter solo menu usuario
        }
        /*
        opcionMenu = utd.leerYValidarOpcionMenuUsuario();
        while (opcionMenu != 0) {
            //Hay que rediseñar con el nuevo pseudocodigo
            switch (opcionMenu){
                case 1: //Ingresar Dinero
                    cantidad = utd.leerYValidarCantidad();
                    System.out.println("Introduce un correo: ");
                    correo = teclado.next();

                    conexion = jdbc.crearConexion();
                    execute = jdbc.ingresarDinero(conexion, cantidad, correo);

                    if(execute){
                        System.out.println("Se ha ingresado correctamente");
                    }
                break;
*/
        /*
                case 2: //Retirar Dinero
                    cantidad = utd.leerYValidarCantidad();
                    System.out.println("Introduce un correo: ");
                    correo = teclado.next();

                    conexion = jdbc.crearConexion();
                    execute = jdbc.retirarDinero(conexion, cantidad, correo);

                    if(execute){
                        System.out.println("Se ha retirado correctamente");
                    }
                break;

                case 3: // Movimientos de cuenta
                    opcionSubMenu = utd.leerYValidarOpcionSubMenuCuenta();
                    while(opcionSubMenu != 0){
                        switch (opcionSubMenu){
                            case 1:
                                System.out.println("Introduce un correo: ");
                                correo = teclado.next();

                                conexion = jdbc.crearConexion();
                                movimientosCuenta = jdbc.movimientosCuenta(conexion,correo);

                                for(int i = 0; i < movimientosCuenta.size(); i++) {
                                    System.out.println(movimientosCuenta.get(i).getId() + " �" + " -> " + movimientosCuenta.get(i).getCantidad()
                                            + " -> " + movimientosCuenta.get(i).getCorreoUsuario() + " -> " + movimientosCuenta.get(i).getTipo());
                                }
                            break;*/
        /*
                            case 2:
                                System.out.println("Introduce un correo: ");
                                correo = teclado.next();

                                conexion = jdbc.crearConexion();
                                apuestasRealizadas = jdbc.apuestasRealizadas(conexion,correo);

                                for(int i = 0; i < apuestasRealizadas.size(); i++) {
                                    System.out.println(apuestasRealizadas.get(i).getId() + " �" + " -> " + apuestasRealizadas.get(i).getCantidad()
                                            + " -> " + apuestasRealizadas.get(i).getTipo());
                                }

                            break;


                            case 3:
                                System.out.println("Introduce un correo: ");
                                correo = teclado.next();

                                conexion = jdbc.crearConexion();
                                apuestasGanadas = jdbc.apuestasGanadas(conexion,correo);
                                for(int i = 0; i < apuestasGanadas.size(); i++) {
                                    System.out.println(apuestasGanadas.get(i).getId() + " �" + " -> " + apuestasGanadas.get(i).getCantidad()
                                            + " -> " + apuestasGanadas.get(i).getCuota() + " -> " + apuestasGanadas.get(i).getIdPartido()
                                            + " -> " + apuestasGanadas.get(i).getCorreoUsuario() + " -> " + apuestasGanadas.get(i).getTipo());
                                }
                            break;
                        }
                        opcionSubMenu = utd.leerYValidarOpcionSubMenuCuenta();
                    }
                break;

/*
                case 4:
                    partido = utd.crearPartido();
                    conexion = jdbc.crearConexion();
                    filas = jdbc.crearPartidoBBDD(conexion,partido);
                    if(filas > 0){
                        System.out.println("Se ha creado el partido correctamente");
                    }
                break;
            }
            opcionMenu = utd.leerYValidarOpcionMenuUsuario();
        }*/
    }
}
