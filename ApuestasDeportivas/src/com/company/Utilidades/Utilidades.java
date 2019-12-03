package com.company.Utilidades;

import com.company.Clases.Partido;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.UUID;

public class Utilidades {

    public double leerYValidarCantidad(){
        double cantidad = 0.0;
        Scanner teclado = new Scanner(System.in);

        do{
            System.out.println("Introduce una cantidad: ");
            cantidad = teclado.nextInt();
        }while(cantidad <= 0);

        return cantidad;
    }

    //Cuando se loguee para despues del login
    public int leerYValidarOpcionMenuLogin(){
        int opcion = 0;
        Scanner teclado = new Scanner(System.in);
        do{
            System.out.println("Escoge una de las siguientes opciones: ");
            System.out.println("0. Para salir del menu ");
            System.out.println("1 Menu Usuario");
            System.out.println("2 Menu Administrador");

            opcion = teclado.nextInt();
        }while (opcion < 0 || opcion > 2);

        return opcion;
    }

    //Con esto seria menu usuario
    public int leerYValidarOpcionMenuUsuario(){
        int opcionMenu = 0;
        Scanner teclado = new Scanner(System.in);
        do{
            System.out.println("Escoge una de las siguientes opciones: ");
            System.out.println("0 Para salir del menu ");
            System.out.println("1 Realizar Apuesta ");
            System.out.println("2 Ver Partidos Disponibles ");
            System.out.println("3 Comprobar Apuesta Anterior ");
            System.out.println("4 Crear un partido");

            opcionMenu = teclado.nextInt();
        }while (opcionMenu < 0 || opcionMenu > 4);

        return opcionMenu;
    }

    //Esto seria para el menu de cuenta
    public int leerYValidarOpcionSubMenuCuenta(){
        int opcionSubMenu = 0;
        Scanner teclado = new Scanner(System.in);
        do{
            System.out.println("Escoge una de las siguientes opciones: ");
            System.out.println("0 Para salir del submenu ");
            System.out.println("1 Realizar Dinero ");
            System.out.println("2 Retirar Dinero ");
            System.out.println("3 Movimientos de la cuenta ");

            opcionSubMenu = teclado.nextInt();
        }while (opcionSubMenu < 0 || opcionSubMenu > 3);

        return opcionSubMenu;
    }

    //Esto seria para el menu de administrador
    public int leerYValidarOpcionMenuAdministrador(){
        int opcionSubMenu = 0;
        Scanner teclado = new Scanner(System.in);
        do{
            System.out.println("Escoge una de las siguientes opciones: ");
            System.out.println("0 Para salir del submenu ");
            System.out.println("1 Crear Partido ");
            System.out.println("2 Abrir Partido ");
            System.out.println("3 Cerrar Partido ");
            System.out.println("4 Apuestas del Partido ");
            System.out.println("5 Pagar las apuestas ganadas del partido ");

            opcionSubMenu = teclado.nextInt();
        }while (opcionSubMenu < 0 || opcionSubMenu > 5);

        return opcionSubMenu;
    }

    public int leerYValidarGolesLocales(){
        Scanner teclado = new Scanner(System.in);
        int golesLocales = 0;

        do{
            System.out.println("Introduce los goles del equipo local:");
            golesLocales = teclado.nextInt();
        }while(golesLocales < 0);

        return golesLocales;
    }

    public int leerYValidarGolesVisitantes(){
        Scanner teclado = new Scanner(System.in);
        int golesVisitantes = 0;

        do{
            System.out.println("Introduce los goles del equipo visitante:");
            golesVisitantes = teclado.nextInt();
        }while(golesVisitantes < 0);

        return golesVisitantes;
    }

    public static boolean esBisiesto(int anio){

        boolean dev = false;

        if(anio % 4 == 0 && anio % 100 != 0 || anio % 400 ==0){
            dev = true;

        }
        else
            dev = false;

        return dev;


    }

    public static boolean existe(int anio, int mes, int dia, int hora, int minutos, int segundos){

        boolean dev = false;

        if(anio >= 1582 && (mes >= 1 && mes <= 12) && dia >=1 && (hora >= 0 && hora < 23) && (minutos > 0 && minutos < 60) && (segundos > 0 && segundos < 60)){
            switch(mes){
                case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                    if(dia <= 31){
                        dev = true;
                    }
                    break;

                case 2:
                    if(esBisiesto(anio)){
                        if(dia <= 29){
                             dev = true;
                        }
                    }else {
                        if (dia <= 28) {
                            dev = true;
                        }
                    }
                break;
                default:
                    if(dia <= 30){
                        dev = true;
                    }
                    break;
            }

        }
        return dev;

    }

    public LocalDateTime fechaInicio(){
        Scanner teclado = new Scanner(System.in);
        LocalDateTime fechaInicio = null;
        int anio = 0;
        int mes = 0;
        int dia = 0;
        int hora = 0;
        int minutos = 0;
        int segundos = 0;

        do{
            System.out.println("Introduce un año:");
            anio = teclado.nextInt();
            System.out.println("Introduce un mes:");
            mes = teclado.nextInt();
            System.out.println("Introduce un dia:");
            dia = teclado.nextInt();
            System.out.println("Introduce un hora:");
            hora = teclado.nextInt();
            System.out.println("Introduce un minutos:");
            minutos = teclado.nextInt();
            System.out.println("Introduce un segundos:");
            segundos = teclado.nextInt();

            fechaInicio = LocalDateTime.of(anio,mes,dia,hora,minutos,segundos);
        }while(!existe(anio,mes,dia,hora,minutos,segundos));

        return fechaInicio;
    }

    public LocalDateTime fechaFin(){
        Scanner teclado = new Scanner(System.in);
        LocalDateTime fechaFin = null;
        int anio = 0;
        int mes = 0;
        int dia = 0;
        int hora = 0;
        int minutos = 0;
        int segundos = 0;

        do{
            System.out.println("Introduce un año:");
            anio = teclado.nextInt();
            System.out.println("Introduce un mes:");
            mes = teclado.nextInt();
            System.out.println("Introduce un dia:");
            dia = teclado.nextInt();
            System.out.println("Introduce un hora:");
            hora = teclado.nextInt();
            System.out.println("Introduce un minutos:");
            minutos = teclado.nextInt();
            System.out.println("Introduce un segundos:");
            segundos = teclado.nextInt();

            fechaFin = LocalDateTime.of(anio, mes, dia, hora, minutos, segundos);
        }while(!existe(anio,mes,dia,hora,minutos,segundos));

        return fechaFin;
    }

    public boolean estaAbierto(){
        Scanner teclado = new Scanner(System.in);
        boolean abierto = false;
        char opcion = ' ';

        do{
            System.out.println("¿Esta el partido abierto? [S] o [N]");
            opcion = Character.toUpperCase(teclado.next().charAt(0));
        }while(opcion != 'S' && opcion != 'N');

        if(opcion == 'S'){
            abierto = true;
        }
        return abierto;
    }

    public double limiteTipo1(){
        Scanner teclado = new Scanner(System.in);
        double limite = 0.0;
        do{
            System.out.println("Introduce el limite del tipo 1:");
            limite = teclado.nextDouble();
        }while(limite <= 0);

        return limite;
    }

    public double limiteTipo2(){
        Scanner teclado = new Scanner(System.in);
        double limite = 0.0;
        do{
            System.out.println("Introduce el limite del tipo 2:");
            limite = teclado.nextDouble();
        }while(limite <= 0);

        return limite;
    }
    public double limiteTipo3(){
        Scanner teclado = new Scanner(System.in);
        double limite = 0.0;
        do{
            System.out.println("Introduce el limite del tipo 3:");
            limite = teclado.nextDouble();
        }while(limite <= 0);

        return limite;
    }

    public Partido crearPartido(){
        Scanner teclado = new Scanner(System.in);
        int golesLocales = 0;
        int golesVisitantes = 0;
        String competicion = "";
        LocalDateTime fechaInicio = null;
        LocalDateTime fechaFin = null;
        boolean estaAbierto = false;
        double limiteTipo1 = 0.0;
        double limiteTipo2 = 0.0;
        double limiteTipo3 = 0.0;
        Partido partido = new Partido();

        golesLocales = leerYValidarGolesLocales();
        golesVisitantes = leerYValidarGolesVisitantes();
        System.out.println("Introduce una competicion");
        competicion = teclado.nextLine();
        fechaInicio = fechaInicio();
        fechaFin = fechaFin();
        estaAbierto = estaAbierto();
        limiteTipo1 = limiteTipo1();
        limiteTipo2 = limiteTipo2();
        limiteTipo3 = limiteTipo3();

        partido = new Partido(competicion,golesLocales,golesVisitantes,fechaInicio,fechaFin,estaAbierto,limiteTipo1,limiteTipo2,limiteTipo3);

        return partido;
    }

    /*
    Interfaz
    Nombre: Login
    Comentario: Este metodo re encarga de loguear al administrador, si es admin nos devuelve true sino false
    Cabecera: public boolean Login(String usuario, String contrasenha)
    Precondiciones: No hay
    Entrada: String usuario //El usuario
             String contrasenha //La contraseña
    Salida: boolean correcto //Si el usuario es adminisitrador devolvera un true, si es erroneo devuleve false
    E/S: No hay
    Postcondiciones: Asociado al nombre, si la variable correcto es true decimos que el usuario es administrador y false sino lo es
     */

    public boolean Login(String usuario, String contrasenha) {
        boolean correcto =  false;
        if(usuario.equalsIgnoreCase("admin") && contrasenha.equalsIgnoreCase("admin")) {
            correcto = true;
        }

        return correcto;
    }



}
