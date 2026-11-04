package com.bellaimagen;

import com.bellaimagen.dao.ServicioDAO;
import com.bellaimagen.modelo.Servicio;

public class PruebaServicios {

    public static void main(String[] args) {

        try {
            ServicioDAO dao = new ServicioDAO();

            System.out.println("SERVICIOS BELLA IMAGEN");
            System.out.println("--------------------------------");

            for (Servicio s : dao.listarTodos()) {
                System.out.println(
                    "ID: " + s.getIdServicio()
                    + " | Nombre: " + s.getNombre()
                    + " | Duración: " + s.getDuracionEstimada() + " min"
                    + " | Buffer: " + s.getTiempoBuffer() + " min"
                    + " | Precio: $" + s.getPrecio()
                );
            }

            System.out.println("--------------------------------");
            System.out.println("Total de servicios: "
                    + dao.listarTodos().size());

        } catch (Exception e) {
            System.out.println("Error al consultar servicios:");
            e.printStackTrace();
        }
    }
}