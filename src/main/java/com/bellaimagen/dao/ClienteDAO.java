package com.bellaimagen.dao;

import com.bellaimagen.conexion.Conexion;
import com.bellaimagen.modelo.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void insertar(Cliente cliente) throws SQLException {

        String sql = "INSERT INTO CLIENTE (nombre, telefono, email) VALUES (?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getTelefono());
            ps.setString(3, cliente.getEmail());

            ps.executeUpdate();
        }
    }

    public Cliente insertarYObtener(Cliente cliente) throws SQLException {

        String sql = "INSERT INTO CLIENTE (nombre, telefono, email) VALUES (?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getTelefono());
            ps.setString(3, cliente.getEmail());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setIdCliente(rs.getInt(1));
                    return cliente;
                }
            }
        }

        throw new SQLException("No se pudo obtener el ID del cliente registrado.");
    }

    public Cliente buscarPorTelefonoOCorreo(
            String telefono, String email) throws SQLException {

        String sql = "SELECT id_cliente, nombre, telefono, email "
                + "FROM CLIENTE "
                + "WHERE telefono = ? OR LOWER(email) = LOWER(?) "
                + "ORDER BY id_cliente";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, telefono);
            ps.setString(2, email);

            try (ResultSet rs = ps.executeQuery()) {

                Cliente encontrado = null;

                while (rs.next()) {

                    Cliente actual = new Cliente(
                            rs.getInt("id_cliente"),
                            rs.getString("nombre"),
                            rs.getString("telefono"),
                            rs.getString("email")
                    );

                    if (encontrado == null) {
                        encontrado = actual;
                    } else if (encontrado.getIdCliente()
                            != actual.getIdCliente()) {

                        throw new SQLException(
                                "El teléfono y el correo electrónico "
                                + "pertenecen a clientes diferentes. "
                                + "Verifica los datos antes de continuar."
                        );
                    }
                }

                return encontrado;
            }
        }
    }

    public List<Cliente> listarTodos() throws SQLException {

        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT id_cliente, nombre, telefono, email "
                + "FROM CLIENTE ORDER BY id_cliente";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Cliente cliente = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );

                clientes.add(cliente);
            }
        }

        return clientes;
    }

    public void actualizar(Cliente cliente) throws SQLException {

        String sql = "UPDATE CLIENTE SET nombre = ?, telefono = ?, email = ? "
                + "WHERE id_cliente = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getTelefono());
            ps.setString(3, cliente.getEmail());
            ps.setInt(4, cliente.getIdCliente());

            ps.executeUpdate();
        }
    }

    public void eliminar(int idCliente) throws SQLException {

        String sql = "DELETE FROM CLIENTE WHERE id_cliente = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            ps.executeUpdate();
        }
    }
}