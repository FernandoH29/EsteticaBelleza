
package com.bellaimagen;

import com.bellaimagen.conexion.Conexion;
import java.sql.Connection;
import java.sql.SQLException;

public class PruebaConexion {

    public static void main(String[] args) {

        System.out.println("Conectando con Bella Imagen...");

        try (Connection conexion = Conexion.conectar()) {

            System.out.println("¡CONEXIÓN EXITOSA!");
            System.out.println("Base de datos: "
                    + conexion.getCatalog());
            System.out.println("Java está conectado correctamente con Railway.");

        } catch (SQLException e) {

            System.out.println("ERROR AL CONECTAR CON RAILWAY.");
            System.out.println("Mensaje: " + e.getMessage());

        }
    }
}
