package com.bellaimagen.dao;

import com.bellaimagen.conexion.Conexion;
import com.bellaimagen.modelo.Servicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicioDAO {

    public void insertar(Servicio servicio) throws SQLException {

        String sql = "INSERT INTO SERVICIO "
                + "(nombre, duracion_estimada, tiempo_buffer, precio) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, servicio.getNombre());
            ps.setInt(2, servicio.getDuracionEstimada());
            ps.setInt(3, servicio.getTiempoBuffer());
            ps.setDouble(4, servicio.getPrecio());

            ps.executeUpdate();
        }
    }

    public List<Servicio> listarTodos() throws SQLException {

        List<Servicio> servicios = new ArrayList<>();

        String sql = "SELECT id_servicio, nombre, duracion_estimada, "
                + "tiempo_buffer, precio FROM SERVICIO ORDER BY id_servicio";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Servicio servicio = new Servicio(
                    rs.getInt("id_servicio"),
                    rs.getString("nombre"),
                    rs.getInt("duracion_estimada"),
                    rs.getInt("tiempo_buffer"),
                    rs.getDouble("precio")
                );

                servicios.add(servicio);
            }
        }

        return servicios;
    }

    public void actualizar(Servicio servicio) throws SQLException {

        String sql = "UPDATE SERVICIO SET nombre = ?, "
                + "duracion_estimada = ?, tiempo_buffer = ?, precio = ? "
                + "WHERE id_servicio = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, servicio.getNombre());
            ps.setInt(2, servicio.getDuracionEstimada());
            ps.setInt(3, servicio.getTiempoBuffer());
            ps.setDouble(4, servicio.getPrecio());
            ps.setInt(5, servicio.getIdServicio());

            ps.executeUpdate();
        }
    }

    public void eliminar(int idServicio) throws SQLException {

        String sql = "DELETE FROM SERVICIO WHERE id_servicio = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idServicio);
            ps.executeUpdate();
        }
    }
}