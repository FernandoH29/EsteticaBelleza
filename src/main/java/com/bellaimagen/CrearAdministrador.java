package com.bellaimagen;

import com.bellaimagen.conexion.Conexion;
import com.bellaimagen.dao.UsuarioDAO;
import com.bellaimagen.modelo.Usuario;
import java.io.Console;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Scanner;

public class CrearAdministrador {

    public static void main(String[] args) {

        Console consola = System.console();
        Scanner scanner = new Scanner(System.in);

        try {
            int administradores = 0;

            try (Connection conexion = Conexion.conectar();
                 PreparedStatement ps = conexion.prepareStatement(
                         "SELECT COUNT(*) FROM USUARIO WHERE rol = 'ADMINISTRADOR'");
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    administradores = rs.getInt(1);
                }
            }

            if (administradores > 0) {
                System.out.println("Ya existe una cuenta de administrador.");
                return;
            }

            System.out.println("==================================");
            System.out.println("  CONFIGURAR ADMINISTRADOR INICIAL");
            System.out.println("==================================");

            String nombre;
            String usuario;

            if (consola != null) {
                nombre = consola.readLine("Nombre completo: ");
                usuario = consola.readLine("Nombre de usuario: ");
            } else {
                System.out.println("AVISO: la contraseña se mostrará al escribirla.");
                System.out.print("Nombre completo: ");
                nombre = scanner.nextLine();

                System.out.print("Nombre de usuario: ");
                usuario = scanner.nextLine();
            }

            char[] password;
            char[] confirmacion;

            if (consola != null) {
                password = consola.readPassword("Contraseña (mínimo 8 caracteres): ");
                confirmacion = consola.readPassword("Confirma la contraseña: ");
            } else {
                System.out.print("Contraseña (mínimo 8 caracteres): ");
                password = scanner.nextLine().toCharArray();

                System.out.print("Confirma la contraseña: ");
                confirmacion = scanner.nextLine().toCharArray();
            }

            if (password == null || confirmacion == null
                    || !Arrays.equals(password, confirmacion)) {
                System.out.println("Las contraseñas no coinciden.");
                return;
            }

            if (password.length < 8) {
                System.out.println("La contraseña debe tener al menos 8 caracteres.");
                return;
            }

            Usuario nuevo = new Usuario();
            nuevo.setNombre(nombre);
            nuevo.setUsuario(usuario);
            nuevo.setContrasena(new String(password));
            nuevo.setRol("ADMINISTRADOR");

            Arrays.fill(password, '\0');
            Arrays.fill(confirmacion, '\0');

            UsuarioDAO dao = new UsuarioDAO();

            if (dao.registrar(nuevo)) {
                System.out.println();
                System.out.println("¡ADMINISTRADOR CREADO CORRECTAMENTE!");
                System.out.println("Ya puedes utilizar esta cuenta para iniciar sesión.");
            }

        } catch (Exception e) {
            System.out.println("No se pudo crear el administrador.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }
}