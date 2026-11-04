package com.bellaimagen.dao;

import com.bellaimagen.conexion.Conexion;
import com.bellaimagen.modelo.Especialista;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EspecialistaDAO {

    public void insertar(Especialista especialista) throws SQLException {

        String sql = "INSERT INTO ESPECIALISTA (nombre, especialidad, estado) VALUES (?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, especialista.getNombre());
            ps.setString(2, especialista.getEspecialidad());
            ps.setString(3, especialista.getEstado());

            ps.executeUpdate();
        }
    }

    public List<Especialista> listarTodos() throws SQLException {

        List<Especialista> especialistas = new ArrayList<>();

        String sql = "SELECT id_especialista, nombre, especialidad, estado FROM ESPECIALISTA ORDER BY id_especialista";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Especialista especialista = new Especialista(
                    rs.getInt("id_especialista"),
                    rs.getString("nombre"),
                    rs.getString("especialidad"),
                    rs.getString("estado")
                );

                especialistas.add(especialista);
            }
        }

        return especialistas;
    }

    public void actualizar(Especialista especialista) throws SQLException {

        String sql = "UPDATE ESPECIALISTA SET nombre = ?, especialidad = ?, estado = ? WHERE id_especialista = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, especialista.getNombre());
            ps.setString(2, especialista.getEspecialidad());
            ps.setString(3, especialista.getEstado());
            ps.setInt(4, especialista.getIdEspecialista());

            ps.executeUpdate();
        }
    }

    public void cambiarEstado(int idEspecialista, String nuevoEstado)
            throws SQLException {

        String sql = "UPDATE ESPECIALISTA SET estado = ? WHERE id_especialista = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idEspecialista);

            ps.executeUpdate();
        }
    }
}