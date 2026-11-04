package com.bellaimagen;

import com.bellaimagen.dao.EspecialistaDAO;
import com.bellaimagen.modelo.Especialista;

import java.util.List;

public class PruebaEspecialistas {

    public static void main(String[] args) {

        try {
            EspecialistaDAO dao = new EspecialistaDAO();

            List<Especialista> especialistas = dao.listarTodos();

            System.out.println("================================");
            System.out.println("    ESPECIALISTAS BELLA IMAGEN");
            System.out.println("================================");

            for (Especialista especialista : especialistas) {

                System.out.println(
                    "ID: " + especialista.getIdEspecialista()
                    + " | Nombre: " + especialista.getNombre()
                    + " | Especialidad: " + especialista.getEspecialidad()
                    + " | Estado: " + especialista.getEstado()
                );
            }

            System.out.println("--------------------------------");
            System.out.println(
                "Total de especialistas: " + especialistas.size()
            );

        } catch (Exception e) {
            System.out.println("Error al consultar especialistas:");
            e.printStackTrace();
        }
    }
}