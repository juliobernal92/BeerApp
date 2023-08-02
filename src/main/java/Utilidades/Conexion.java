package Utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    Connection con = null;
    String base = "BeerApp"; //Nombre de la base de datos
    String url = "jdbc:postgresql://localhost:5432/" + base; //Direccion, puerto y nombre de la Base de Datos
    String user = "postgres"; //Usuario de Acceso a base de datos
    String password = "123"; //Password del usuario

    public Connection getConexion() {
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url,user,password);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e);
        }
        return con;
    }
    
}

