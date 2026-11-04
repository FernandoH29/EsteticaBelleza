package com.bellaimagen;

import com.bellaimagen.dao.ClienteDAO;
import com.bellaimagen.modelo.Cliente;
import java.util.List;

public class PruebaClientes {

    public static void main(String[] args) {

        try {
            ClienteDAO dao = new ClienteDAO();

            List<Cliente> clientes = dao.listarTodos();

            System.out.println("================================");
            System.out.println("       CLIENTES BELLA IMAGEN");
            System.out.println("================================");

            for (Cliente cliente : clientes) {
                System.out.println(
                    "ID: " + cliente.getIdCliente()
                    + " | Nombre: " + cliente.getNombre()
                    + " | Telefono: " + cliente.getTelefono()
                    + " | Email: " + cliente.getEmail()
                );
            }

            System.out.println("--------------------------------");
            System.out.println("Total de clientes: " + clientes.size());

        } catch (Exception e) {
            System.out.println("Error al consultar clientes:");
            e.printStackTrace();
        }
    }
}