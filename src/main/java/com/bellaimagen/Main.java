
package com.bellaimagen;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        Label mensaje = new Label("¡Bienvenido a Bella Imagen!");

        mensaje.setStyle(
            "-fx-font-size: 26px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #9B6685;"
        );

        StackPane panel = new StackPane(mensaje);
        panel.setStyle("-fx-background-color: #FFF5F8;");

        Scene escena = new Scene(panel, 900, 550);

        stage.setTitle("Bella Imagen - Sistema de Gestión");
        stage.setScene(escena);
        stage.setMinWidth(700);
        stage.setMinHeight(450);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
