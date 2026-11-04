package com.bellaimagen.dao;

import com.bellaimagen.conexion.Conexion;
import com.bellaimagen.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDAO {

    public Usuario autenticar(String nombreUsuario, String contrasena)
            throws SQLException {

        String sql = "SELECT id_usuario, nombre, usuario, "
                + "contrasena_hash, rol, estado "
                + "FROM USUARIO WHERE usuario = ? LIMIT 1";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    String hash = rs.getString("contrasena_hash");
                    String estado = rs.getString("estado");

                    if (!"Activo".equalsIgnoreCase(estado)) {
                        return null;
                    }

                    if (hash == null || !BCrypt.checkpw(contrasena, hash)) {
                        return null;
                    }

                    Usuario usuario = new Usuario();

                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setUsuario(rs.getString("usuario"));
                    usuario.setRol(rs.getString("rol"));
                    usuario.setEstado(estado);

                    return usuario;
                }
            }
        }

        return null;
    }

    public boolean registrar(Usuario usuario) throws SQLException {

        if (usuario.getNombre() == null || usuario.getNombre().isBlank()
                || usuario.getUsuario() == null || usuario.getUsuario().isBlank()
                || usuario.getContrasena() == null
                || usuario.getContrasena().length() < 8) {
            throw new IllegalArgumentException(
                    "Completa los datos y utiliza una contraseña de al menos 8 caracteres."
            );
        }

        if (!"ADMINISTRADOR".equals(usuario.getRol())
                && !"RECEPCIONISTA".equals(usuario.getRol())) {
            throw new IllegalArgumentException("El rol no es válido.");
        }

        String hash = BCrypt.hashpw(
                usuario.getContrasena(),
                BCrypt.gensalt(12)
        );

        String sql = "INSERT INTO USUARIO "
                + "(nombre, usuario, contrasena_hash, rol, estado) "
                + "VALUES (?, ?, ?, ?, 'Activo')";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre().trim());
            ps.setString(2, usuario.getUsuario().trim());
            ps.setString(3, hash);
            ps.setString(4, usuario.getRol());

            return ps.executeUpdate() > 0;
        }
    }
}