
package com.bellaimagen.conexion;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    public static Connection conectar() throws SQLException {

        String host = dotenv.get("DB_HOST");
        String port = dotenv.get("DB_PORT");
        String database = dotenv.get("DB_NAME");
        String user = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");

        if (host == null || port == null || database == null
                || user == null || password == null) {
            throw new SQLException(
                    "Faltan datos de conexión en el archivo .env"
            );
        }

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database
                + "?useSSL=true&requireSSL=true&serverTimezone=UTC";

        return DriverManager.getConnection(url, user, password);
    }
}
