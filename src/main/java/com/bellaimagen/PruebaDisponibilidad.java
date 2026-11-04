package com.bellaimagen;

import com.bellaimagen.dao.CitaDAO;
import java.time.LocalDateTime;

public class PruebaDisponibilidad {

    public static void main(String[] args) {

        CitaDAO dao = new CitaDAO();

        try {
            LocalDateTime inicio = LocalDateTime.of(
                    2026, 11, 10, 10, 0
            );

            LocalDateTime fin = LocalDateTime.of(
                    2026, 11, 10, 12, 30
            );

            // Especialista con ID 1
            boolean ocupado = dao.hayConflicto(
                    1, inicio, fin
            );

            System.out.println("PRUEBA DE DISPONIBILIDAD");
            System.out.println("--------------------------------");

            if (ocupado) {
                System.out.println(
                        "El especialista tiene una cita en ese horario."
                );
            } else {
                System.out.println(
                        "El especialista está disponible."
                );
            }

            System.out.println("--------------------------------");
            System.out.println("Consulta completada correctamente.");

        } catch (Exception e) {
            System.out.println("Error al consultar disponibilidad:");
            e.printStackTrace();
        }
    }
}