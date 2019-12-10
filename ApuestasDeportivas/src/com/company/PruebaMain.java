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
public class PruebaMain {
    public static void main(String[] args) {
        boolean admin = true;
        int opcionMenu = 0;
        int opcionSubMenu = 0;
        int opcionCuenta = 0;
        double cantidad = 0;
        String correo = "";
        String usuario = "";
        String contrasenha = "";
        Scanner teclado = new Scanner(System.in);
        Utilidades utd = new Utilidades();
        JDBC jdbc = new JDBC();
        boolean execute = false;
        Connection conexion = null;

        //Variables para mostrar los datos de la consulta
        ArrayList<Apuesta> apuestasRealizadas = new ArrayList<Apuesta>();
        ArrayList<Movimiento> movimientosCuenta= new ArrayList<Movimiento>();
        ArrayList<Apuesta> apuestasGanadas = new ArrayList<Apuesta>();
        ArrayList<Partido> partidos = new ArrayList<>();
        int filas = 0;

        Partido partido = new Partido();

            //Login
            System.out.println("Introduce el usuario");
            usuario = teclado.next();
            System.out.println("Introduce la contrasenha");
            contrasenha = teclado.next();
            if (usuario.equalsIgnoreCase("admin") && contrasenha.equalsIgnoreCase("admin")) {
                admin = false;
            }

            if (admin) {
                opcionMenu = utd.leerYValidarOpcionMenuLoginConUsuario();
            } else {
                opcionMenu = utd.leerYValidarOpcionMenuLoginConAdministrador();
            }


            //Login
            while (opcionMenu != 0) {
                switch (opcionMenu) {
                    case 1:
                        //Menu Usuario
                        System.out.println("Menu Usuario");
                        opcionSubMenu = utd.leerYValidarOpcionMenuUsuario();
                        while(opcionSubMenu != 0) {
                            switch (opcionSubMenu) {
                                    case 1:
                                        System.out.println("Realizar Apuesta");
                                        break;

                                    case 2:
                                        System.out.println("Ver partidos disponibles");
                                        break;

                                    case 3:
                                        System.out.println("Comprobar Apuesta Anterior");
                                        break;

                                    case 4:
                                        System.out.println("Cuenta");
                                        opcionCuenta = utd.leerYValidarOpcionSubMenuCuenta();

                                    while(opcionCuenta != 0) {
                                        switch (opcionCuenta) {
                                            case 1: //Ingresar Dinero
                                                //System.out.println("Ingresar Dinero");
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
                                                //System.out.println("Retirar Dinero");
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
                                                //System.out.println("Movimientos de la cuenta");
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
                                        opcionCuenta = utd.leerYValidarOpcionSubMenuCuenta();
                                    }
                                    break;
                            }
                            opcionSubMenu = utd.leerYValidarOpcionMenuUsuario();
                        }
                        break;

                    case 2:
                        //Menu Administrador
                        System.out.println("Menu Administrador");
                        opcionSubMenu = utd.leerYValidarOpcionMenuAdministrador();
                        while(opcionSubMenu != 0) {
                            switch (opcionSubMenu) {
                                case 1:
                                    //System.out.println("Crear Partido");
                                    //CrearPartido
                                    partido = utd.crearPartido();
                                    conexion = jdbc.crearConexion(usuario, contrasenha);
                                    filas = jdbc.crearPartidoBBDD(conexion,partido);
                                    if(filas > 0){
                                        System.out.println("Se ha creado el partido correctamente");
                                    }
                                    break;

                                case 2:
                                    System.out.println("Abrir Partido");
                                    break;

                                case 3:
                                    System.out.println("Cerrar Partido");
                                    break;

                                case 4:
                                    System.out.println("Apuestas del partido");
                                    break;

                                case 5:
                                    System.out.println("Pagar las apuestas ganadas del partido");
                                    break;
                            }
                            opcionSubMenu = utd.leerYValidarOpcionMenuAdministrador();
                        }
                        break;
                }
                if (admin) {
                    opcionMenu = utd.leerYValidarOpcionMenuLoginConUsuario();
                }
                else {
                    opcionMenu = utd.leerYValidarOpcionMenuLoginConAdministrador();
                }
            }
    }
}
