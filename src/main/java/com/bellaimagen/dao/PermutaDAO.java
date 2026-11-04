package com.bellaimagen.dao;

import com.bellaimagen.conexion.Conexion;
import com.bellaimagen.modelo.Permuta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PermutaDAO {

    public void insertar(Permuta permuta) throws SQLException {

        if (permuta.getFecha() == null) {
            throw new SQLException("Debes seleccionar una fecha.");
        }

        if (permuta.getIdEspecialistaOrigen()
                == permuta.getIdEspecialistaDestino()) {
            throw new SQLException(
                    "Los especialistas deben ser diferentes.");
        }

        String sql = """
                INSERT INTO PERMUTA_TURNO
                (fecha, id_especialista_origen,
                 id_especialista_destino, estado)
                VALUES (?, ?, ?, 'Pendiente')
                """;

        try (Connection conexion = Conexion.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(permuta.getFecha()));
            ps.setInt(2, permuta.getIdEspecialistaOrigen());
            ps.setInt(3, permuta.getIdEspecialistaDestino());

            ps.executeUpdate();
        }
    }

    public List<Permuta> listarTodas() throws SQLException {

        List<Permuta> lista = new ArrayList<>();

        String sql = """
                SELECT
                    p.id_permuta,
                    p.fecha,
                    p.id_especialista_origen,
                    p.id_especialista_destino,
                    p.estado,
                    eo.nombre AS nombre_origen,
                    ed.nombre AS nombre_destino
                FROM PERMUTA_TURNO p
                INNER JOIN ESPECIALISTA eo
                    ON p.id_especialista_origen = eo.id_especialista
                INNER JOIN ESPECIALISTA ed
                    ON p.id_especialista_destino = ed.id_especialista
                ORDER BY p.fecha DESC, p.id_permuta DESC
                """;

        try (Connection conexion = Conexion.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Permuta permuta = new Permuta();

                permuta.setIdPermuta(rs.getInt("id_permuta"));
                permuta.setFecha(rs.getDate("fecha").toLocalDate());
                permuta.setIdEspecialistaOrigen(
                        rs.getInt("id_especialista_origen"));
                permuta.setIdEspecialistaDestino(
                        rs.getInt("id_especialista_destino"));
                permuta.setEstado(rs.getString("estado"));
                permuta.setNombreOrigen(rs.getString("nombre_origen"));
                permuta.setNombreDestino(rs.getString("nombre_destino"));

                lista.add(permuta);
            }
        }

        return lista;
    }

    public void actualizarEstado(int idPermuta, String nuevoEstado)
            throws SQLException {

        if (!nuevoEstado.equals("Pendiente")
                && !nuevoEstado.equals("Aprobada")
                && !nuevoEstado.equals("Rechazada")) {
            throw new SQLException("Estado de permuta no válido.");
        }

        String sql = """
                UPDATE PERMUTA_TURNO
                SET estado = ?
                WHERE id_permuta = ?
                """;

        try (Connection conexion = Conexion.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPermuta);

            ps.executeUpdate();
        }
    }
}
