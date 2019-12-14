package com.company;

import com.company.ClaseAbstracta.Apuesta;
import com.company.Clases.*;
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
        int filas = 0;
        int opcionMenu = 0;
        int opcionSubMenu = 0;
        int opcionCuenta = 0;
        int gol = 0;
        int golLocal = 0;
        int golVisitante = 0;
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
        Character unodos = ' ';
        Character tipo = ' ';
        UUID idPartido;
        String eleccionCadena = "";

        //Objetos de clases necesarios
        ApuestaTipo1 apuestaTipo1 = new ApuestaTipo1();
        ApuestaTipo2 apuestaTipo2 = new ApuestaTipo2();
        ApuestaTipo3 apuestaTipo3 = new ApuestaTipo3();
        MovimientosCuenta movimiento = new MovimientosCuenta();
        Partido partido = new Partido();

        //Variables para mostrar los datos de la consulta
        ArrayList<Apuesta> apuestasRealizadas = new ArrayList<Apuesta>();
        ArrayList<Movimiento> movimientosCuenta= new ArrayList<Movimiento>();
        ArrayList<Apuesta> apuestasGanadas = new ArrayList<Apuesta>();
        ArrayList<Partido> partidos = new ArrayList<Partido>();
        ArrayList<PartidoConEquipos> partidosFull = new ArrayList<PartidoConEquipos>();
        ArrayList<FullTipoApuesta> fullTipoApuesta = new ArrayList<FullTipoApuesta>();
        ArrayList<PartidoConEquipos> partidosDisponibles = new ArrayList<PartidoConEquipos>();
        ArrayList<MovimientosCuenta> movimientos = new ArrayList<>();




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
                                        tipo = utd.leerYValidarTipo();
                                        switch(tipo) {
                                            case '1':
                                                System.out.println("Elige los goles al equipo local");
                                                golLocal = utd.golMayoroIgualque0();
                                                System.out.println("Elige los goles al equipo visitante");
                                                golVisitante = utd.golMayoroIgualque0();
                                                System.out.println("Introduce un correo: ");
                                                correo = teclado.next();
                                                execute = jdbc.realizarApuesta(conexion, cantidad, eleccion, correo, golLocal, golVisitante);
                                                if(execute){
                                                    System.out.println("Se ha relizar la apuesta correctamente");
                                                }
                                                else {
                                                    System.out.println("Ha habido un error");
                                                }
                                                break;

                                            case '2':
                                                unodos = utd.leerYValidarUnoDos();
                                                System.out.println("Elige los goles");
                                                gol = utd.golMayoroIgualque0();
                                                System.out.println("Introduce un correo: ");
                                                correo = teclado.next();
                                                execute = jdbc.realizarApuesta(conexion, cantidad, eleccion, correo, unodos, gol);
                                                if(execute){
                                                    System.out.println("Se ha relizar la apuesta correctamente");
                                                } else {
                                                    System.out.println("Ha habido un error");
                                                }
                                                break;

                                            case '3':
                                                unoxdos = utd.leerYValidarUnoXDos();
                                                System.out.println("Introduce un correo: ");
                                                correo = teclado.next();
                                                execute = jdbc.realizarApuesta(conexion, cantidad, eleccion, correo, unoxdos);
                                                if(execute){
                                                    System.out.println("Se ha relizar la apuesta correctamente");
                                                } else {
                                                    System.out.println("Ha habido un error");
                                                }
                                                break;
                                        }

                                        break;

                                    case 2://ver partidos disponibles
                                        conexion = jdbc.crearConexion(usuario, contrasenha);
                                        partidosDisponibles = jdbc.partidosAbiertos(conexion);

                                        for(int i = 0; i< partidosDisponibles.size(); i++){
                                            System.out.println(partidosDisponibles.get(i).getId() + " �" + " -> " + partidosDisponibles.get(i).getCompeticion()
                                                    + " -> " + partidosDisponibles.get(i).getEquipoLocal() + " -> " + partidosDisponibles.get(i).getEquipoVisitante());

                                        }
                                        break;

                                    case 3:
                                        //Menu Apuestas y que el usuario elija
                                        conexion = jdbc.crearConexion(usuario, contrasenha);
                                        eleccion = utd.mostrarApuestas(jdbc.obtenerApuestas(conexion));
                                        tipo = utd.leerYValidarTipo();
                                        switch (tipo) {
                                            case '1':
                                                apuestaTipo1 = (ApuestaTipo1) jdbc.apuestaCompleta(conexion, eleccion, tipo);
                                                if(apuestaTipo1.getCorreoUsuario().equalsIgnoreCase("")) {
                                                    System.out.println("Esta apuesta no es de el tipo correspondiente");
                                                }
                                                else {
                                                    System.out.println(apuestaTipo1.toString());
                                                }


                                            break;

                                            case '2':
                                                apuestaTipo2 = (ApuestaTipo2) jdbc.apuestaCompleta(conexion, eleccion, tipo);
                                                if(apuestaTipo2.getCorreoUsuario().equalsIgnoreCase("")) {
                                                    System.out.println("Esta apuesta no es de el tipo correspondiente");
                                                }
                                                else {
                                                    System.out.println(apuestaTipo2.toString());
                                                }
                                                break;

                                            case '3':
                                                apuestaTipo3 = (ApuestaTipo3) jdbc.apuestaCompleta(conexion, eleccion, tipo);
                                                if(apuestaTipo3.getCorreoUsuario().equalsIgnoreCase("")) {
                                                    System.out.println("Esta apuesta no es de el tipo correspondiente");
                                                }
                                                else {
                                                    System.out.println(apuestaTipo3.toString());
                                                }
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

                                                for(int i = 0; i < movimientos.size(); i++) {
                                                    System.out.println(movimientos.get(i).getCorreoUsuario() + " �" + " -> " + movimientos.get(i).getNumeroApuestasGanadas()
                                                            + " -> " + movimientos.get(i).getNumeroApuestasRealizadas());
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

                                case 2://abrirPartido
                                    conexion = jdbc.crearConexion(usuario, contrasenha);
                                    partidosFull = jdbc.partidos(conexion);
                                    eleccion = utd.mostrarPartidos(partidosFull);
                                    execute = jdbc.abrirPartido(conexion,eleccion);
                                    if(execute){
                                        System.out.println("Se ha abierto el partido correctamente");
                                    }
                                break;

                                case 3://cerrarPartido
                                    conexion = jdbc.crearConexion(usuario, contrasenha);
                                    partidosFull = jdbc.partidos(conexion);
                                    eleccion = utd.mostrarPartidos(partidosFull);
                                    execute = jdbc.cerrarPartido(conexion,eleccion);
                                    if(execute){
                                        System.out.println("Se ha cerrado el partido correctamente");
                                    }
                                break;

                                case 4: //consultar apuestas del partido
                                    conexion = jdbc.crearConexion(usuario, contrasenha);
                                    partidosFull = jdbc.partidos(conexion);
                                    eleccion = utd.mostrarPartidos(partidosFull);
                                    fullTipoApuesta = jdbc.consultarPartido(conexion,eleccion);
                                    for(int i = 0; i < fullTipoApuesta.size(); i++){
                                        System.out.println(fullTipoApuesta.get(i).getTipo() + " �" + " -> " + fullTipoApuesta.get(i).getGolLocal()
                                                + " -> " + fullTipoApuesta.get(i).getGolVisitante() + " -> " + fullTipoApuesta.get(i).getGoles()
                                                + " -> " + fullTipoApuesta.get(i).getLocalOVisitante() + " -> " + fullTipoApuesta.get(i).getUnoxDos()
                                                + " -> " + fullTipoApuesta.get(i).getTotalApostado());
                                    }
                                break;

                                case 5:
                                    //System.out.println("Pagar las apuestas ganadas del partido");
                                    conexion = jdbc.crearConexion(usuario, contrasenha);
                                    partidosFull = jdbc.partidos(conexion);
                                    eleccion = utd.mostrarPartidos(partidosFull);
                                    execute = jdbc.pagarApuestasGanadas(conexion, eleccion);
                                    if(execute){
                                        System.out.println("Se han pagado aquellas apuestas que se han ganado del partido seleccionado");
                                    }
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
