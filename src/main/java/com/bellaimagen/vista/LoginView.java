package com.bellaimagen.vista;

import com.bellaimagen.PanelAdministrativo;
import com.bellaimagen.dao.UsuarioDAO;
import com.bellaimagen.modelo.Usuario;
import java.sql.SQLException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginView {

    private static final String VINO = "#581825";
    private static final String ROSA = "#A85D83";

    public static void mostrar() {

        Stage ventana = new Stage();
        ventana.setTitle("Bella Imagen - Acceso del personal");
        ventana.initModality(Modality.APPLICATION_MODAL);

        Label logo = new Label("BELLA IMAGEN");
        logo.setFont(Font.font("Georgia", FontWeight.BOLD, 27));
        logo.setTextFill(Color.web(VINO));

        Label subtitulo = new Label("ACCESO DEL PERSONAL");
        subtitulo.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        subtitulo.setTextFill(Color.web(ROSA));

        Label texto = new Label("Ingresa tus credenciales para continuar");
        texto.setFont(Font.font("Arial", 13));
        texto.setTextFill(Color.web("#746674"));

        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Nombre de usuario");
        txtUsuario.setMaxWidth(300);

        PasswordField txtContrasena = new PasswordField();
        txtContrasena.setPromptText("Contraseña");
        txtContrasena.setMaxWidth(300);

        Label mensaje = new Label();
        mensaje.setTextFill(Color.web("#B42318"));
        mensaje.setWrapText(true);

        Button ingresar = new Button("INICIAR SESIÓN");
        ingresar.setMaxWidth(300);
        ingresar.setPadding(new Insets(13));
        ingresar.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        ingresar.setTextFill(Color.WHITE);
        ingresar.setStyle(
                "-fx-background-color: " + VINO + ";" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );

        ingresar.setOnAction(e -> {

            String usuario = txtUsuario.getText().trim();
            String contrasena = txtContrasena.getText();

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                mensaje.setText("Ingresa tu usuario y contraseña.");
                return;
            }

            ingresar.setDisable(true);
            mensaje.setText("Verificando acceso...");

            try {
                UsuarioDAO dao = new UsuarioDAO();
                Usuario autenticado = dao.autenticar(usuario, contrasena);

                if (autenticado == null) {
                    mensaje.setText("Usuario o contraseña incorrectos, o cuenta inactiva.");
                    txtContrasena.clear();
                    txtContrasena.requestFocus();
                    return;
                }

                if (!"ADMINISTRADOR".equals(autenticado.getRol())) {
                    mensaje.setText("Tu cuenta no tiene acceso al panel administrativo.");
                    txtContrasena.clear();
                    return;
                }

                mensaje.setText("");
                ventana.close();
                PanelAdministrativo.mostrar();

            } catch (SQLException ex) {
                mensaje.setText("No se pudo conectar con la base de datos.");
                System.err.println("Error de inicio de sesión: " + ex.getMessage());
            } finally {
                ingresar.setDisable(false);
            }
        });

        VBox contenido = new VBox(
                16,
                logo,
                subtitulo,
                texto,
                txtUsuario,
                txtContrasena,
                mensaje,
                ingresar
        );

        contenido.setAlignment(Pos.CENTER);
        contenido.setPadding(new Insets(35));
        contenido.setStyle(
                "-fx-background-color: #FAF8F5;" +
                "-fx-background-radius: 15;"
        );

        Scene escena = new Scene(contenido, 420, 440);

        ventana.setScene(escena);
        ventana.setResizable(false);
        ventana.showAndWait();
    }
}