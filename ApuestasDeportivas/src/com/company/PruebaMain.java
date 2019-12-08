package com.company;

import com.company.Utilidades.Utilidades;

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
        String usuario = "";
        String contrasenha = "";
        Scanner teclado = new Scanner(System.in);
        Utilidades utd = new Utilidades();

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
                                            case 1:
                                                System.out.println("Ingresar Dinero");
                                                break;

                                            case 2:
                                                System.out.println("Retirar Dinero");
                                                break;

                                            case 3:
                                                System.out.println("Movimientos de la cuenta");
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
                                    System.out.println("Crear Partido");
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
                            break;
                        }
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
