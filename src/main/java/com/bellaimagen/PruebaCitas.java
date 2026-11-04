package com.bellaimagen;

import com.bellaimagen.dao.CitaDAO;
import com.bellaimagen.modelo.Servicio;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class PruebaCitas {

    public static void main(String[] args) {

        CitaDAO dao = new CitaDAO();

        Servicio manicure = new Servicio(
                1, "Manicure", 60, 15, 150.00
        );

        Servicio pedicure = new Servicio(
                2, "Pedicure", 60, 15, 180.00
        );

        List<Servicio> servicios = Arrays.asList(
                manicure, pedicure
        );

        LocalDateTime inicio = LocalDateTime.of(
                2026, 11, 10, 10, 0
        );

        int duracion = dao.calcularDuracionTotal(servicios);

        LocalDateTime fin = dao.calcularFechaHoraFin(
                inicio, servicios
        );

        System.out.println("PRUEBA DE CÁLCULO DE CITAS");
        System.out.println("--------------------------------");
        System.out.println("Servicios seleccionados: "
                + servicios.size());
        System.out.println("Duración total: " + duracion + " minutos");
        System.out.println("Inicio: " + inicio);
        System.out.println("Fin: " + fin);
        System.out.println("--------------------------------");

        if (duracion == 150) {
            System.out.println("PRUEBA EXITOSA");
        } else {
            System.out.println("ERROR EN EL CÁLCULO");
        }
    }
}