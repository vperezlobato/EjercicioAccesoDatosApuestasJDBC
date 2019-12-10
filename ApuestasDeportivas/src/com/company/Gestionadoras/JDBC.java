package com.company.Gestionadoras;

import com.company.ClaseAbstracta.Apuesta;
import com.company.Clases.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class JDBC {

    /*
     Nombre: crearConexion
     Comentario: Con este metodo se crea la conexion con la base de datos
     Cabecera: public Connection crearConexion(String usuario,String contrasena)
     Precondiciones: No hay
     Entradas:
        -String usuario
        -String contrasena
     Salidas: Connection conexion
     Postcondiciones: Devuelve la conexion asociada al nombre
     */

    public Connection crearConexion(String usuario,String contrasena){
        String sourceURL = "jdbc:sqlserver://localhost;DatabaseName=CasaDeApuestas";
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(sourceURL, usuario, contrasena);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conexion;
    }


    /*
     Nombre: ingresarDinero
     Comentario: Con este metodo se ingresara en la base de datos la cantidad de dinero indicada, en el usuario con ese correo.
     Cabecera: public boolean ingresarDinero(Connection conexion,double cantidad,String correo)
     Precondiciones:
        -Cantidad debe ser mayor que 0
     Entradas:
        -Connection conexion
        -double cantidad
        -String correo
     Salidas: boolean execute
     Postcondiciones: Devuelve si se ha ingresado true, false en caso contrario asociado al nombre.
     */
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


    /*
     Nombre: retirarDinero
     Comentario: Con este metodo se retirara en la base de datos la cantidad de dinero indicada, en el usuario con ese correo.
     Cabecera: public boolean retirarDinero(Connection conexion,double cantidad,String correo)
     Precondiciones:
        -Cantidad debe ser mayor que 0
     Entradas:
        -Connection conexion
        -double cantidad
        -String correo
     Salidas: boolean execute
     Postcondiciones: Devuelve si se ha ingresado true, false en caso contrario asociado al nombre.
     */
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

    /*
     Nombre: movimientosCuenta
     Comentario: Con este metodo se visualizaran las apuestas ganadas y las apuestas realizadas de un usuario en concreto.
     Cabecera: public movimientosCuenta movimientosCuenta(Connection conexion, String correo)
     Precondiciones: --
     Entradas:
        -Connection conexion
        -String correo
     Salidas: movimientosCuenta movimientos
     Postcondiciones: Devuelve asociado al nombre, un movimientosCuenta con el correo del usuario, la apuestas realizadas, y las apuestas ganadas.
     */
    public movimientosCuenta movimientosCuenta(Connection conexion, String correo){
        PreparedStatement sentencia = null;
        String miOrden = "";
        ResultSet datos = null;
        movimientosCuenta movimientoCuenta = new movimientosCuenta();

        try {
            miOrden = " Select M.correoUsuario,P.[Numero de apuestas],D.[Numero de apuestas ganadas] from Movimientos AS M " +
                    " INNER JOIN " +
                    " (Select COUNT(ID) as [Numero de apuestas],CorreoUsuario From Apuestas Where correoUsuario = ? group by CorreoUsuario) AS P " +
                    " ON M.correoUsuario = P.CorreoUsuario " +
                    " INNER JOIN " +
                    " (Select COUNT(A.ID) as [Numero de apuestas ganadas],A.CorreoUsuario From Apuestas AS A " +
                    "                    LEFT JOIN ApuestasTipo1 AS AT1 ON A.ID = AT1.IDApuesta " +
                    "                    INNER JOIN Partidos AS P ON P.ID = A.IDPartido " +
                    "                    LEFT JOIN ApuestasTipo2 AS AT2 ON AT2.IDApuesta = A.ID " +
                    "                    LEFT JOIN ApuestasTipo3 AS AT3 ON AT3.IDApuesta = A.ID " +
                    "                    WHERE ((AT1.GolesLocal = P.golesLocal AND AT1.GolesVisitante = P.golesVisitante) " +
                    "                    OR (AT2.LocalOVisitante = 'L' AND AT2.Goles = P.golesLocal ) " +
                    "                    OR (AT3.[1X2] = '1' AND P.golesLocal > P.golesVisitante) OR (AT3.[1X2] = '2' AND P.golesLocal < P.golesVisitante) OR (AT3.[1X2] = 'X' AND P.golesLocal = P.golesVisitante)) " +
                    "                    AND A.CorreoUsuario = ?" +
                    " group by A.CorreoUsuario) AS D " +
                    " ON D.CorreoUsuario = M.correoUsuario " +
                    " group by M.correoUsuario,P.[Numero de apuestas],D.[Numero de apuestas ganadas] ";
            sentencia = conexion.prepareStatement(miOrden);
            sentencia.setString(1, correo);
            sentencia.setString(2, correo);
            datos = sentencia.executeQuery();

            movimientoCuenta = new movimientosCuenta(datos.getString("correoUsuario"), datos.getInt("Numero de apuestas"), datos.getInt("Numero de apuestas ganadas"));

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return movimientoCuenta;
    }

    /*
    Nombre: movimientosCuenta
    Comentario: Con este metodo se crea un partido en la base de datos.
    Cabecera: public int crearPartidoBBDD(Connection conexion, Partido partido)
    Precondiciones: --
    Entradas:
            -Connection conexion
            -Partido partido
    Salidas: int filas
    Postcondiciones: Devuelve asociado al nombre, el numero de filas afectadas.
    */
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

    /*
    Nombre: consultarPartido
    Comentario: Con este metodo se consultan las diferentes apuestas de un partido filtrando por tipo y resultado
    Cabecera: public ArrayList<FullTipoApuesta> consultarPartido(Connection conexion,String idPartido)
    Precondiciones: --
    Entradas:
            -Connection conexion
            -String idPartido
    Salidas: ArrayList<FullTipoApuesta> lista
    Postcondiciones: Devuelve asociado al nombre, una lista FullTipoApuesta con su informacion.
    */
    public ArrayList<FullTipoApuesta> consultarPartido(Connection conexion,String idPartido){
        CallableStatement cstmt = null;
        boolean execute = false;
        ResultSet datos;
        FullTipoApuesta fullTipoApuestas;
        ArrayList<FullTipoApuesta> lista = new ArrayList<FullTipoApuesta>();
        try {
            cstmt = conexion.prepareCall("{ call consultarPartido(?)}");
            cstmt.setString(1, idPartido);
            datos = cstmt.executeQuery();

            while(datos.next()) {
                fullTipoApuestas = new FullTipoApuesta(datos.getString("Tipo").charAt(0), datos.getInt("GolesLocal"),datos.getInt("GolesVisitante") ,datos.getString("LocalOVisitante").charAt(0),datos.getInt("Goles"),datos.getString("unoxDos").charAt(0),datos.getInt("TotalApostado"));
                lista.add(fullTipoApuestas);
            }
        }catch (SQLException e) {
            System.out.println("No se pudo consultar el partido");
        }finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lista;
    }

    /*
    Nombre: RealizarApuestas (Para Tipo1)
    Comentario: Con este metodo se realizan las apuestas del tipo 1 y nos devuelve un boolean si se ha realizado
    correctamente o no
    Cabecera: public boolean realizarApuesta(Connection conexion, Double cantidad, UUID idPartido, String correo, Integer golLocal, Integer golVisitante)
    Precondiciones: El partido debe estar introducido en la BBDD con anterioridad
                    El correo debe existir en la BBDD con anterioridad
    Entrada: Connection conn //Es la conexion con la BBDD
             Double cantidad //La cantidad apostada
             UUID idPartido //El partido donde se quiere realizar la apuesta
             String correo //El correo del usuario de la apuesta
             Integer golLocal //Los goles introducidos al equipo local
             Integer golVisitante //Los goles introducidos al equipo visitante
    Salida: boolean resultado //Si es true es que se ha realizado la apuesta y si es false no se ha realizado
    E/S: No hay
    Postcondiciones: Asociado al nombre, el boolean realizado si es true es que se ha realizado la apuesta y si es false no se ha realizado
     */
    public boolean realizarApuesta(Connection conexion, Double cantidad, UUID idPartido, String correo, Integer golLocal, Integer golVisitante) {
        //String query = "EXECUTE RealizarApuestaTipo1(?,?,?,?,?,?)";
        String query = "{CALL RealizarApuestaTipo1(?,?,?,?,?,?)}";
        double cuota = 0.0;
        boolean resultado = false;
        //ResultSet rs = null;
        cuota = calcularCuota(conexion,'1', idPartido, golLocal, golVisitante);
        if(cuota > 1.5) {
            try {
                CallableStatement cs = conexion.prepareCall(query);
                cs.setDouble(1, cantidad);
                cs.setDouble(2, cuota);
                cs.setObject(3, idPartido);
                cs.setString(4, correo);
                cs.setInt(5, golLocal);
                cs.setInt(6, golVisitante);
                cs.executeQuery();

                resultado = true;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return resultado;
    }

    /*
    Nombre: RealizarApuestas (Para Tipo2)
    Comentario: Con este metodo se realizan las apuestas del tipo 2 y nos devuelve un boolean si se ha realizado
    correctamente o no
    Cabecera: public boolean realizarApuesta(Connection conexion, Double cantidad, UUID idPartido, String correo,
    Character localOVisitante, Integer goles)
    Precondiciones: El partido debe estar introducido en la BBDD con anterioridad
                    El correo debe existir en la BBDD con anterioridad
    Entrada: Connection conn //Es la conexion con la BBDD
             Double cantidad //La cantidad apostada
             UUID idPartido //El partido donde se quiere realizar la apuesta
             String correo //El correo del usuario de la apuesta
             Character localOVisitante //Un caracter que decide cual es el equipo al que se desea apostar
             Integer goles //Los goles introducidos al equipo
    Salida: boolean resultado //Si es true es que se ha realizado la apuesta y si es false no se ha realizado
    E/S: No hay
    Postcondiciones: Asociado al nombre, el boolean realizado si es true es que se ha realizado la apuesta y si es false no se ha realizado
 */
    public boolean realizarApuesta(Connection conexion, Double cantidad, UUID idPartido, String correo, Character localOVisitante, Integer goles) {
        String query = "EXECUTE RealizarApuestaTipo2(?,?,?,?,?,?)";
        double cuota = 0.0;
        boolean resultado = false;
        cuota = calcularCuota(conexion,'2', idPartido, localOVisitante, goles);
        if(cuota > 1.5) {
            try {
                CallableStatement cs = conexion.prepareCall(query);
                cs.setDouble(1, cantidad);
                cs.setDouble(2, cuota);
                cs.setObject(3, idPartido);
                cs.setString(4, correo);
                cs.setString(5, String.valueOf(localOVisitante));
                cs.setInt(6, goles);

                cs.execute();

                resultado = true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return resultado;
    }

    /*
    Nombre: RealizarApuestas (Para Tipo3)
    Comentario: Con este metodo se realizan las apuestas del tipo 3 y nos devuelve un boolean si se ha realizado
    correctamente o no
    Cabecera: public boolean realizarApuesta(Connection conexion, Double cantidad, UUID idPartido, String correo,
    Character unoxdos)
    Precondiciones: El partido debe estar introducido en la BBDD con anterioridad
                    El correo debe existir en la BBDD con anterioridad
    Entrada: Connection conn //Es la conexion con la BBDD
             Double cantidad //La cantidad apostada
             UUID idPartido //El partido donde se quiere realizar la apuesta
             String correo //El correo del usuario de la apuesta
             Character unoxdos //Un caracter que decide cual es el equipo ganador del encuentro
    Salida: boolean resultado //Si es true es que se ha realizado la apuesta y si es false no se ha realizado
    E/S: No hay
    Postcondiciones: Asociado al nombre, el boolean realizado si es true es que se ha realizado la apuesta y si es false no se ha realizado
*/
    public boolean realizarApuesta(Connection conexion, Double cantidad, UUID idPartido, String correo, Character unoxdos) {
        String query = "EXECUTE RealizarApuestaTipo3(?,?,?,?,?)";
        double cuota = 0.0;
        boolean resultado = false;
        cuota = calcularCuota(conexion,'3', idPartido, unoxdos);
        if(cuota > 1.5) {
            try {
                CallableStatement cs = conexion.prepareCall(query);
                cs.setDouble(1, cantidad);
                cs.setDouble(2, cuota);
                cs.setObject(3, idPartido);
                cs.setString(4, correo);
                cs.setString(5, String.valueOf(unoxdos));
                resultado = cs.execute();

                resultado = true;


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return resultado;
    }

    /*
    Interfaz
    Nombre: calcularCuota (Tipo 1)
    Comentario: Con este metodo se calculara la cuota para apuestas del tipo 1
    Cabecera: public double calcularCuota(Connection conn, char tipo , UUID idPartido, Integer golLocal, Integer golVisitante)
    Precondiciones: El partido debe estar introducido en la BBDD con anterioridad
    Entrada: Connection conn //Es la conexion con la BBDD
             char tipo //Es el tipo de la apuesta a introducir
             UUID idPartido //El partido donde de la apuesta que se desea introducir
             Integer golLocal //Los goles introducidos al equipo local
             Integer golVisitante //Los goles introducidos al equipo visitante
    Salida: double resultado //Es la cuota final de la apuesta
    E/S: No hay
    Postcondiciones: Asociado al nombre, la cuota final de la apuesta
     */
    public double calcularCuota(Connection conn, char tipo , UUID idPartido, Integer golLocal, Integer golVisitante){
        double resultado = 0.0;
        double cantidad = 0.0;
        double t = 0.0;
        ResultSet rs = null;
        String query = "SELECT SUM (cantidad) AS cantidad FROM Apuestas WHERE Tipo = ? AND IDPartido = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, String.valueOf(tipo));
            ps.setObject(2, idPartido);
            rs = ps.executeQuery();

            if(rs.next()) {
                cantidad = rs.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(cantidad <= 40) {
            resultado = 4.0;
        }
        else {
            //Query apuestas igual a la apostada
            query = "SELECT SUM(A.cantidad) AS cantidad FROM Apuestas AS A " +
                    "INNER JOIN ApuestasTipo1 AS AP ON A.ID = AP.IDApuesta " +
                    "WHERE AP.GolesLocal = ? AND AP.GolesVisitante = ? AND A.IDPartido = ?";
            try {
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, golLocal);
                ps.setInt(2, golVisitante);
                ps.setObject(3, idPartido);
                rs = ps.executeQuery();

                if(rs.next()) {
                    t = rs.getDouble("cantidad");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            resultado = ((cantidad/t) - 1) * 0.8;
        }
        return resultado;
    }

    /*
    Interfaz
    Nombre: calcularCuota (Tipo 2)
    Comentario: Con este metodo se calculara la cuota para apuestas del tipo 2
    Cabecera: public double calcularCuota(Connection conn, char tipo , UUID idPartido, Character localOVisitante, Integer goles)
    Precondiciones: El partido debe estar introducido en la BBDD con anterioridad
    Entrada: Connection conn //Es la conexion con la BBDD
             char tipo //Es el tipo de la apuesta a introducir
             UUID idPartido //El partido donde de la apuesta que se desea introducir
             Character localOVisitante //Un caracter que decide cual es el equipo al que se desea apostar
             Integer goles //Los goles introducidos al equipo
    Salida: double resultado //Es la cuota final de la apuesta
    E/S: No hay
    Postcondiciones: Asociado al nombre, la cuota final de la apuesta
    */
    public double calcularCuota(Connection conn, char tipo , UUID idPartido, char localOVisitante, Integer goles){
        double resultado = 0.0;
        double cantidad = 0.0;
        double t = 0.0;
        ResultSet rs = null;
        String query = "SELECT SUM (cantidad) AS cantidad FROM Apuestas WHERE Tipo = ? AND IDPartido = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, String.valueOf(tipo));
            ps.setObject(2, idPartido);
            rs = ps.executeQuery();

            if(rs.next()) {
                cantidad = rs.getDouble("cantidad");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(cantidad <= 40) {
            resultado = 3.0;
        }
        else {
            //Query apuestas igual a la apostada
            query = "SELECT SUM(A.cantidad) AS cantidad FROM Apuestas AS A " +
                    "INNER JOIN ApuestasTipo2 AS AP2 ON A.ID = AP2.IDApuesta " +
                    "WHERE AP2.LocalOVisitante = ? AND AP2.Goles = ? AND A.IDPartido = ?";
            try {
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, String.valueOf(localOVisitante));
                ps.setInt(2, goles);
                ps.setObject(3, idPartido);
                rs = ps.executeQuery();

                if(rs.next()) {
                    t = rs.getDouble("cantidad");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            resultado = ((cantidad/t) - 1) * 0.8;
        }
        return resultado;
    }

    /*
   Interfaz
   Nombre: calcularCuota (Tipo 3)
   Comentario: Con este metodo se calculara la cuota para apuestas del tipo 3
   Cabecera: public double calcularCuota(Connection conn, char tipo , UUID idPartido, Character unoxdos)
   Precondiciones: El partido debe estar introducido en la BBDD con anterioridad
   Entrada: Connection conn //Es la conexion con la BBDD
            char tipo //Es el tipo de la apuesta a introducir
            UUID idPartido //El partido donde de la apuesta que se desea introducir
            Character unoxdos //Un caracter que decide cual es el equipo ganador
            Integer goles //Los goles introducidos al equipo
   Salida: double resultado //Es la cuota final de la apuesta
   E/S: No hay
   Postcondiciones: Asociado al nombre, la cuota final de la apuesta
   */
    public double calcularCuota(Connection conn, char tipo , UUID idPartido, char unoxDos){
        double resultado = 0.0;
        double cantidad = 0.0;
        double t = 0.0;
        ResultSet rs = null;
        String query = "SELECT SUM (cantidad) AS cantidad FROM Apuestas WHERE Tipo = ? AND IDPartido = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, String.valueOf(tipo));
            ps.setObject(2, idPartido);
            rs = ps.executeQuery();

            if(rs.next()) {
                cantidad = rs.getDouble("cantidad");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(cantidad <= 40) {
            resultado = 3.0;
        }
        else {
            //Query apuestas igual a la apostada
            query = "SELECT SUM(A.cantidad) AS Total FROM Apuestas AS A " +
                    "INNER JOIN ApuestasTipo3 AS AP3 ON A.ID = AP3.IDApuesta " +
                    "WHERE AP3.[1X2] = ?  AND A.IDPartido = ?";
            try {
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, String.valueOf(unoxDos));
                ps.setObject(2, idPartido);
                rs = ps.executeQuery();

                if(rs.next()) {
                    t = rs.getDouble("cantidad");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            resultado = ((cantidad/t) - 1) * 0.8;
        }
        return resultado;
    }

    /*
    Interfaz
    Nombre: partidosAbiertos
    Comentario: Con este metodo se mostrarian los partidos que estan abiertos en este momento
    Cabecera: public ArrayList<Partido> partidosAbiertos(Connection conexion)
    Precondiciones: No hay
    Entrada: Connection conexion //El objeto asociado a la conexion
    Salida: ArrayList<Partido> partidos //Una lista con los partidos que estan abiertos
    E/S: No hay
    Postcondiciones: Asociado al nombre, una lista con los partidos que estan abiertos
     */
    public ArrayList<Partido> partidosAbiertos(Connection conexion) {
        String query = "SELECT * FROM Partidos WHERE EstaAbierto = 1";
        Partido partido = new Partido();
        ArrayList<Partido> partidos = new ArrayList<>();
        ResultSet rs = null;
        try {
            Statement s = conexion.createStatement();
            rs = s.executeQuery(query);

            while(rs.next()) {
                partido = new Partido(rs.getString("competicion"), rs.getInt("golesLocal"), rs.getInt("golesVisitante"), (Timestamp) rs.getObject("fechaInicio"), (Timestamp)rs.getObject("fechaFin"));
                partidos.add(partido);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return partidos;
    }


    /*
    Interfaz
    Nombre: apuestaCompleta
    Comentario: Con este metodo se muestra la informacion completa de la apuesta
    Cabecera: public Apuesta apuestaCompleta(Connection conn, UUID idApuesta, Character tipo)
    Precondiciones: La apuesta debe estar creada con anterioridad en la BBDD
    Entrada: Connection conexion //El objeto asociado a la conexion
             UUID idApuesta //Es el id de la apuesta que se quiere mostrar su informacion
             Character tipo //Es el tipo de apuesta realizada
    Salida: Apuesta ap //Es la apuesta con toda su informacion
    E/S: No hay
    Postcondiciones: Asociado al nombre, la apuesta con su informacion
    **** NOTA: Apuesta es abstracta, el objeto que se devolvera sera ApuestaTipo1 o ApuestaTipo2 o ApuestaTipo3 ****
     */

    public Apuesta apuestaCompleta(Connection conn, UUID idApuesta, Character tipo) {
        Apuesta ap;
        if(tipo == '1') {
            ap = apuestaCompletaTipo1(conn, idApuesta);
        }
        else if(tipo == '2') {
            ap = apuestaCompletaTipo2(conn, idApuesta);
        }
        else {
            ap = apuestaCompletaTipo3(conn, idApuesta);
        }
        return ap;
    }

    /*
    Nombre: apuestaCompletaTipo1
    Comentario: Con este metodo se muestra la informacion completa de la apuesta de tipo 1
    Cabecera: public ApuestaTipo1 apuestaCompletaTipo1(Connection conn, UUID idApuesta)
    Precondiciones: La apuesta debe estar creada con anterioridad en la BBDD
    Entrada: Connection conexion //El objeto asociado a la conexion
             UUID idApuesta //Es el id de la apuesta que se quiere mostrar su informacion
    Salida: ApuestaTipo1 apuesta //Es la apuesta de tipo 1 con toda su informacion
    E/S: No hay
    Postcondiciones: Asociado al nombre, la apuesta de tipo 1 con su informacion
     */
    public ApuestaTipo1 apuestaCompletaTipo1(Connection conn, UUID idApuesta) {
        ApuestaTipo1 apuesta = new ApuestaTipo1();
        ResultSet rs = null;
        String query = "SELECT A.CorreoUsuario, A.cantidad, A.cuota, IDPartido, A.Tipo, AT.GolesLocal, AT.GolesVisitante FROM Apuestas AS A INNER JOIN ApuestasTipo1 AS AT ON A.ID AND AT.IDApuestas WHERE AT.IDApuesta = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setObject(1, idApuesta);
            rs = ps.executeQuery();

            if(rs.next()) {
                apuesta = new ApuestaTipo1(new UUID(0,0).fromString(rs.getString("ID")), rs.getDouble("cantidad"), rs.getDouble("cuota"), new UUID(0,0).fromString(rs.getString("IDPartido")), rs.getString("CorreoUsuario"), rs.getString("Tipo").charAt(0), rs.getInt("GolesLocal"), rs.getInt("GolesVisitante"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return apuesta;
    }

    /*
    Nombre: apuestaCompletaTipo2
    Comentario: Con este metodo se muestra la informacion completa de la apuesta de tipo 2
    Cabecera: public ApuestaTipo2 apuestaCompletaTipo2(Connection conn, UUID idApuesta)
    Precondiciones: La apuesta debe estar creada con anterioridad en la BBDD
    Entrada: Connection conexion //El objeto asociado a la conexion
             UUID idApuesta //Es el id de la apuesta que se quiere mostrar su informacion
    Salida: ApuestaTipo2 apuesta //Es la apuesta de tipo 2 con toda su informacion
    E/S: No hay
    Postcondiciones: Asociado al nombre, la apuesta de tipo 2 con su informacion
     */
    public ApuestaTipo2 apuestaCompletaTipo2(Connection conn, UUID idApuesta) {
        ApuestaTipo2 apuesta = new ApuestaTipo2();
        ResultSet rs = null;
        String query = "SELECT A.CorreoUsuario, A.cantidad, A.cuota, IDPartido, A.Tipo, AT.LocalOVisitante, AT.Goles FROM Apuestas AS A INNER JOIN ApuestasTipo2 AS AT ON A.ID AND AT.IDApuestas WHERE AT.IDApuesta = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setObject(1, idApuesta);
            rs = ps.executeQuery();

            if(rs.next()) {
                apuesta = new ApuestaTipo2(new UUID(0,0).fromString(rs.getString("ID")), rs.getDouble("cantidad"), rs.getDouble("cuota"), new UUID(0,0).fromString(rs.getString("IDPartido")), rs.getString("CorreoUsuario"), rs.getString("Tipo").charAt(0), rs.getString("LocalOVisitante").charAt(0), rs.getInt("Goles"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return apuesta;
    }

    /*
    Nombre: apuestaCompletaTipo3
    Comentario: Con este metodo se muestra la informacion completa de la apuesta de tipo 3
    Cabecera: public ApuestaTipo3 apuestaCompletaTipo3(Connection conn, UUID idApuesta)
    Precondiciones: La apuesta debe estar creada con anterioridad en la BBDD
    Entrada: Connection conexion //El objeto asociado a la conexion
             UUID idApuesta //Es el id de la apuesta que se quiere mostrar su informacion
    Salida: ApuestaTipo3 apuesta //Es la apuesta de tipo 3 con toda su informacion
    E/S: No hay
    Postcondiciones: Asociado al nombre, la apuesta de tipo 3 con su informacion
     */
    public ApuestaTipo3 apuestaCompletaTipo3(Connection conn, UUID idApuesta) {
        ApuestaTipo3 apuesta = new ApuestaTipo3();
        ResultSet rs = null;
        String query = "SELECT A.CorreoUsuario, A.cantidad, A.cuota, IDPartido, A.Tipo, AT.[1X2] FROM Apuestas AS A INNER JOIN ApuestasTipo3 AS AT ON A.ID AND AT.IDApuestas WHERE AT.IDApuesta = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setObject(1, idApuesta);
            rs = ps.executeQuery();

            if(rs.next()) {
                apuesta = new ApuestaTipo3(new UUID(0,0).fromString(rs.getString("ID")), rs.getDouble("cantidad"), rs.getDouble("cuota"), new UUID(0,0).fromString(rs.getString("IDPartido")), rs.getString("CorreoUsuario"), rs.getString("Tipo").charAt(0), rs.getString("[1X2]").charAt(0));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return apuesta;
    }

    /*
    Nombre: abrirPartido
    Comentario: Con este metodo se abre un partido pasado por parametro
    Cabecera: public boolean abrirPartido(Connection conn, UUID idPartido)
    Precondiciones: El partido debe estar creado con anterioridad en la BBDD
    Entrada: Connection conexion //El objeto asociado a la conexion
             UUID idPartido //Es el id del partido que se va a abrir para poder realizar apuestas
    Salida: boolean realizado //Un booleano para saber si se ha realizado correctamente
    E/S: No hay
    Postcondiciones: Asociado al nombre, si es true, se ha podido abrir el partido y si es false no se ha podido
     */
    public boolean abrirPartido(Connection conn, UUID idPartido) {
        //ResultSet rs = null;
        boolean realizado = false;
        int filas = 0;
        String query = "UPDATE Partidos SET EstaAbierto = 1 WHERE ID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setObject(1, idPartido);
            filas = ps.executeUpdate();
            if(filas != 0) {
                realizado = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return realizado;
    }

    /*
    Nombre: cerrarPartido
    Comentario: Con este metodo se cierra un partido pasado por parametro
    Cabecera: public boolean cerrarPartido(Connection conn, UUID idPartido)
    Precondiciones: El partido debe estar creado con anterioridad en la BBDD
    Entrada: Connection conexion //El objeto asociado a la conexion
             UUID idPartido //Es el id del partido que se va a cerrar para no permitir apuestas
    Salida: boolean realizado //Un booleano para saber si se ha realizado correctamente
    E/S: No hay
    Postcondiciones: Asociado al nombre, si es true, se ha podido cerrar el partido y si es false no se ha podido
     */
    public boolean cerrarPartido(Connection conn, UUID idPartido) {
        ///ResultSet rs = null;
        boolean realizado = false;
        int filas = 0;
        String query = "UPDATE Partidos SET EstaAbierto = 0 WHERE ID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setObject(1, idPartido);
            filas = ps.executeUpdate();
            if(filas != 0) {
                realizado = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return realizado;
    }

    /*
    Nombre: pagarApuestasGanadas
    Comentario: Con este metodo se pagan aquellas apuestas que han sido ganadas de un partido pasado por parametro
    Cabecera: public boolean pagarApuestasGanadas (Connection conn, UUID idPartido)
    Precondiciones: El partido debe estar creado con anterioridad en la BBDD
    Entrada: Connection conexion //El objeto asociado a la conexion
             UUID idPartido //Es el id del partido que se van a revisar los ganadores y pagar aquellas ganadoras
    Salida: boolean realizado //Un booleano para saber si se ha realizado correctamente
    E/S: No hay
    Postcondiciones: Asociado al nombre, si es true, se pagan las apuestas y si es false no se ha podido
     */
    public boolean pagarApuestasGanadas (Connection conn, UUID idPartido) {
        boolean realizado = false;
        String query = "{CALL AñadirGanancias(?)}";
        try {
            CallableStatement cs = conn.prepareCall(query);
            cs.setObject(1, idPartido);
            realizado = cs.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return realizado;
    }
}
