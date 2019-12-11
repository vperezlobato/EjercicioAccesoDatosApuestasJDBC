package com.company;

import com.company.ClaseAbstracta.Apuesta;
import com.company.Clases.Movimiento;
import com.company.Clases.Partido;
import com.company.Clases.PartidoConEquipos;
import com.company.Clases.movimientosCuenta;
import com.company.Gestionadoras.JDBC;
import com.company.Utilidades.Utilidades;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

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
        boolean execute = false;
        UUID eleccion = null;
        Connection conexion = null;
        Scanner teclado = new Scanner(System.in);
        Utilidades utd = new Utilidades();
        JDBC jdbc = new JDBC();
        Character unoxdos = ' ';
        Character tipo = ' ';
        UUID idPartido;

        //Variables para mostrar los datos de la consulta
        ArrayList<Apuesta> apuestasRealizadas = new ArrayList<Apuesta>();
        ArrayList<Movimiento> movimientosCuenta= new ArrayList<Movimiento>();
        ArrayList<Apuesta> apuestasGanadas = new ArrayList<Apuesta>();
        ArrayList<Partido> partidos = new ArrayList<>();
        ArrayList<PartidoConEquipos> partidosFull = new ArrayList<>();
        movimientosCuenta movimientos = new movimientosCuenta();
        ArrayList<Partido> partidosDisponibles = new ArrayList<>();
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
                                        conexion = jdbc.crearConexion(usuario, contrasenha);
                                        cantidad = utd.leerYValidarCantidad();
                                        partidosFull = jdbc.partidos(conexion);
                                        eleccion = utd.mostrarPartidos(partidosFull);
                                        unoxdos = utd.leerYValidarUnoXDos(); //falta en utilidades el metodo que valide esto
                                        System.out.println("Introduce un correo: ");
                                        correo = teclado.next();
                                        execute = jdbc.realizarApuesta(conexion, cantidad, eleccion, correo, unoxdos);
                                        if(execute){
                                            System.out.println("Se ha relizar la apuesta correctamente");
                                        }
                                        break;

                                    case 2://ver partidos disponibles
                                        conexion = jdbc.crearConexion(usuario, contrasenha);
                                        partidosDisponibles = jdbc.partidosAbiertos(conexion);

                                        for(int i = 0; i< partidosDisponibles.size(); i++){
                                            System.out.println(partidosDisponibles.get(i).getId() + " �" + " -> " + partidosDisponibles.get(i).getCompeticion()
                                                    + " -> " + partidosDisponibles.get(i).getGolLocal() + " -> " + partidosDisponibles.get(i).getGolVisitante()
                                                    + " -> " + partidosDisponibles.get(i).getFechaInicio() + " -> " + partidosDisponibles.get(i).getFechaFin()
                                                    + " -> " + partidosDisponibles.get(i).getEstaAbierto() + " -> " + partidosDisponibles.get(i).getLimiteAlcanzadoTipo1()
                                                    + " -> " + partidosDisponibles.get(i).getLimiteAlcanzadoTipo2() + " -> " + partidosDisponibles.get(i).getLimiteAlcanzadoTipo3());

                                        }
                                        break;

                                    case 3:
                                        //Menu Apuestas y que el usuario elija
                                        conexion = jdbc.crearConexion(usuario, contrasenha);
                                        tipo = utd.leerYValidarTipo();
                                        eleccion = utd.mostrarApuestas(jdbc.obtenerApuestas(conexion));
                                        switch (tipo) {
                                            case 1:
                                                jdbc.apuestaCompleta(conexion, eleccion, tipo);
                                            break;

                                            case 2:
                                                jdbc.apuestaCompleta(conexion, eleccion, tipo);
                                                break;

                                            case 3:
                                                jdbc.apuestaCompleta(conexion, eleccion, tipo);
                                                break;

                                        }
                                        //System.out.println("Comprobar Apuesta Anterior");
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
                                                movimientos = jdbc.movimientosCuenta(conexion,correo);

                                                System.out.println(movimientos.getCorreoUsuario() + " �" + " -> " + movimientos.getNumeroApuestasGanadas()
                                                        + " -> " + movimientos.getNumeroApuestasRealizadas());
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

                                case 2://abrirPartido
                                    conexion = jdbc.crearConexion(usuario, contrasenha);
                                    //partidos = jdbc.partido(conexion);
                                    //eleccion = utd.mostrarPartidos(partidos);
                                    //execute = jdbc.abrirPartido(eleccion);
                                    if(execute){
                                        System.out.println("Se ha abierto el partido correctamente");
                                    }
                                break;

                                case 3://cerrarPartido
                                    conexion = jdbc.crearConexion(usuario, contrasenha);
                                    //partidos = jdbc.partido(conexion);
                                    //eleccion = utd.mostrarPartidos(partidos);
                                    //execute = jdbc.cerrarPartido(eleccion);
                                    if(execute){
                                        System.out.println("Se ha cerrado el partido correctamente");
                                    }
                                break;

                                case 4: //consultar apuestas del partido
                                    conexion = jdbc.crearConexion(usuario, contrasenha);
                                    //partidos = jdbc.partido(conexion);
                                    //eleccion = utd.mostrarPartidos(partidos);
                                    //convertir la eleccion en String que es lo que recibe por parametros consultarPartido
                                     //arrayList = jdbc.consultarPartido(conexion,eleccion);
                                     //mostrar datos
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
