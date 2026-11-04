package com.bellaimagen.dao;

import com.bellaimagen.conexion.Conexion;
import com.bellaimagen.modelo.Cita;
import com.bellaimagen.modelo.Servicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {

    public int calcularDuracionTotal(List<Servicio> servicios) {

        int duracionTotal = 0;

        for (Servicio servicio : servicios) {
            duracionTotal += servicio.getDuracionEstimada();
            duracionTotal += servicio.getTiempoBuffer();
        }

        return duracionTotal;
    }

    public LocalDateTime calcularFechaHoraFin(
            LocalDateTime inicio,
            List<Servicio> servicios) {

        int minutos = calcularDuracionTotal(servicios);
        return inicio.plusMinutes(minutos);
    }

    public boolean hayConflicto(
            int idEspecialista,
            LocalDateTime inicio,
            LocalDateTime fin) throws SQLException {

        String sql = "SELECT id_cita FROM CITA "
                + "WHERE id_especialista = ? "
                + "AND estado IN ('Programada', 'En Atención') "
                + "AND fecha_hora_inicio < ? "
                + "AND fecha_hora_fin > ? "
                + "LIMIT 1";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEspecialista);
            ps.setTimestamp(2, Timestamp.valueOf(fin));
            ps.setTimestamp(3, Timestamp.valueOf(inicio));

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int registrar(Cita cita) throws SQLException {

        if (cita.getServicios() == null
                || cita.getServicios().isEmpty()) {
            throw new SQLException(
                    "Debes seleccionar al menos un servicio."
            );
        }

        if (cita.getFechaHoraInicio() == null) {
            throw new SQLException(
                    "Debes seleccionar la fecha y hora de la cita."
            );
        }

        LocalDateTime inicio = cita.getFechaHoraInicio();

        LocalDateTime fin = calcularFechaHoraFin(
                inicio,
                cita.getServicios()
        );

        cita.setFechaHoraFin(fin);
        cita.setEstado("Programada");

        String sqlCita = "INSERT INTO CITA "
                + "(fecha_hora_inicio, fecha_hora_fin, estado, "
                + "id_cliente, id_especialista) "
                + "VALUES (?, ?, ?, ?, ?)";

        String sqlDetalle = "INSERT INTO DETALLE_CITA "
                + "(id_cita, id_servicio, precio_aplicado) "
                + "VALUES (?, ?, ?)";

        try (Connection con = Conexion.conectar()) {

            con.setAutoCommit(false);

            try {

                String sqlConflicto = "SELECT id_cita FROM CITA "
                        + "WHERE id_especialista = ? "
                        + "AND estado IN ('Programada', 'En Atención') "
                        + "AND fecha_hora_inicio < ? "
                        + "AND fecha_hora_fin > ? "
                        + "LIMIT 1 FOR UPDATE";

                try (PreparedStatement ps = con.prepareStatement(
                        sqlConflicto)) {

                    ps.setInt(1, cita.getIdEspecialista());
                    ps.setTimestamp(2, Timestamp.valueOf(fin));
                    ps.setTimestamp(3, Timestamp.valueOf(inicio));

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            throw new SQLException(
                                    "CONFLICTO: El especialista ya tiene "
                                    + "una cita en ese horario."
                            );
                        }
                    }
                }

                int idGenerado;

                try (PreparedStatement ps = con.prepareStatement(
                        sqlCita, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setTimestamp(1, Timestamp.valueOf(inicio));
                    ps.setTimestamp(2, Timestamp.valueOf(fin));
                    ps.setString(3, "Programada");
                    ps.setInt(4, cita.getIdCliente());
                    ps.setInt(5, cita.getIdEspecialista());

                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {

                        if (!rs.next()) {
                            throw new SQLException(
                                    "No se pudo obtener el ID de la cita."
                            );
                        }

                        idGenerado = rs.getInt(1);
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(
                        sqlDetalle)) {

                    for (Servicio servicio : cita.getServicios()) {

                        ps.setInt(1, idGenerado);
                        ps.setInt(2, servicio.getIdServicio());
                        ps.setDouble(3, servicio.getPrecio());

                        ps.addBatch();
                    }

                    ps.executeBatch();
                }

                con.commit();

                cita.setIdCita(idGenerado);

                return idGenerado;

            } catch (SQLException ex) {

                con.rollback();
                throw ex;

            } finally {

                con.setAutoCommit(true);
            }
        }
    }

    // Consultar todas las citas con sus datos relacionados
    public List<Cita> listarTodas() throws SQLException {

        List<Cita> citas = new ArrayList<>();

        String sql = "SELECT "
                + "c.id_cita, "
                + "c.fecha_hora_inicio, "
                + "c.fecha_hora_fin, "
                + "c.estado, "
                + "c.id_cliente, "
                + "c.id_especialista, "
                + "cl.nombre AS nombre_cliente, "
                + "e.nombre AS nombre_especialista, "
                + "GROUP_CONCAT(s.nombre ORDER BY s.nombre "
                + "SEPARATOR ', ') AS nombres_servicios "
                + "FROM CITA c "
                + "INNER JOIN CLIENTE cl "
                + "ON c.id_cliente = cl.id_cliente "
                + "INNER JOIN ESPECIALISTA e "
                + "ON c.id_especialista = e.id_especialista "
                + "LEFT JOIN DETALLE_CITA dc "
                + "ON c.id_cita = dc.id_cita "
                + "LEFT JOIN SERVICIO s "
                + "ON dc.id_servicio = s.id_servicio "
                + "GROUP BY c.id_cita, c.fecha_hora_inicio, "
                + "c.fecha_hora_fin, c.estado, c.id_cliente, "
                + "c.id_especialista, cl.nombre, e.nombre "
                + "ORDER BY c.fecha_hora_inicio DESC";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Cita cita = new Cita();

                cita.setIdCita(rs.getInt("id_cita"));

                cita.setFechaHoraInicio(
                        rs.getTimestamp("fecha_hora_inicio")
                                .toLocalDateTime()
                );

                cita.setFechaHoraFin(
                        rs.getTimestamp("fecha_hora_fin")
                                .toLocalDateTime()
                );

                cita.setEstado(rs.getString("estado"));
                cita.setIdCliente(rs.getInt("id_cliente"));
                cita.setIdEspecialista(
                        rs.getInt("id_especialista")
                );

                cita.setNombreCliente(
                        rs.getString("nombre_cliente")
                );

                cita.setNombreEspecialista(
                        rs.getString("nombre_especialista")
                );

                cita.setNombresServicios(
                        rs.getString("nombres_servicios")
                );

                citas.add(cita);
            }
        }

        return citas;
    }

    // Actualizar el estado de una cita
    public boolean actualizarEstado(
            int idCita,
            String nuevoEstado) throws SQLException {

        if (!"Programada".equals(nuevoEstado)
                && !"En Atención".equals(nuevoEstado)
                && !"Concluida".equals(nuevoEstado)) {

            throw new IllegalArgumentException(
                    "El estado seleccionado no es válido."
            );
        }

        String sql = "UPDATE CITA SET estado = ? "
                + "WHERE id_cita = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idCita);

            return ps.executeUpdate() > 0;
        }
    }
}