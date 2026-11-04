
package com.bellaimagen;

import com.bellaimagen.vista.ClientesView;
import com.bellaimagen.vista.CitasView;
import com.bellaimagen.vista.EspecialistasView;
import com.bellaimagen.vista.ServiciosView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class PanelAdministrativo extends Application {

    public static void mostrar() {
        new PanelAdministrativo().start(new Stage());
    }

    private BorderPane panelPrincipal;
    private VBox menuLateral;

    private final String ROSA = "#A85D83";
    private final String ROSA_CLARO = "#F9EDF3";
    private final String DORADO = "#C6A15B";
    private final String FONDO = "#FAF8FA";

    @Override
    public void start(Stage stage) {

        panelPrincipal = new BorderPane();
        panelPrincipal.setStyle("-fx-background-color: " + FONDO + ";");

        crearMenuLateral();
        crearEncabezado();
        mostrarInicio();

        Scene escena = new Scene(panelPrincipal, 1200, 750);

        stage.setTitle("Bella Imagen - Sistema de Gestion");
        stage.setScene(escena);
        stage.setMinWidth(950);
        stage.setMinHeight(650);
        stage.show();
    }

    private void crearMenuLateral() {

        menuLateral = new VBox(12);
        menuLateral.setPadding(new Insets(30, 18, 25, 18));
        menuLateral.setPrefWidth(235);
        menuLateral.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-border-color: #F0E4EB;" +
                "-fx-border-width: 0 1 0 0;"
        );

        Label logo = new Label("BELLA IMAGEN");
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 21));
        logo.setTextFill(Color.web(ROSA));

        Label subtitulo = new Label("SISTEMA DE GESTION");
        subtitulo.setFont(Font.font("Arial", 10));
        subtitulo.setTextFill(Color.web("#A99AA3"));

        VBox marca = new VBox(5, logo, subtitulo);
        marca.setPadding(new Insets(5, 0, 30, 5));

        Label menuTitulo = new Label("MENU PRINCIPAL");
        menuTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        menuTitulo.setTextFill(Color.web("#A99AA3"));
        menuTitulo.setPadding(new Insets(0, 0, 5, 8));

        Button inicio = crearBotonMenu("Inicio");
        Button clientes = crearBotonMenu("Clientes");
        Button especialistas = crearBotonMenu("Especialistas");
        Button servicios = crearBotonMenu("Servicios");
        Button citas = crearBotonMenu("Citas");
        Button permutas = crearBotonMenu("Permutas de turnos");

        inicio.setStyle(estiloBotonActivo());

        inicio.setOnAction(e -> {
            mostrarInicio();
            activarBoton(inicio);
        });

        clientes.setOnAction(e -> {
            ClientesView.mostrar();
            activarBoton(clientes);
        });

        especialistas.setOnAction(e -> {
            EspecialistasView.mostrar();
            activarBoton(especialistas);
        });

        servicios.setOnAction(e -> {
            ServiciosView.mostrar();
            activarBoton(servicios);
        });

        citas.setOnAction(e -> {
            activarBoton(citas);

            javafx.scene.control.ChoiceDialog<String> dialogo =
                    new javafx.scene.control.ChoiceDialog<>(
                            "Agendar cita",
                            "Agendar cita",
                            "Ver agenda"
                    );

            dialogo.setTitle("Bella Imagen - Citas");
            dialogo.setHeaderText("AdministraciÃ³n de citas");
            dialogo.setContentText("Â¿QuÃ© deseas hacer?");

            dialogo.showAndWait().ifPresent(opcion -> {
                if (opcion.equals("Agendar cita")) {
                    CitasView.mostrar();
                } else if (opcion.equals("Ver agenda")) {
                    com.bellaimagen.vista.AgendaView.mostrar();
                }
            });
        });

        permutas.setOnAction(e -> {
            com.bellaimagen.vista.PermutasView.mostrar();
            activarBoton(permutas);
        });

        Region espacio = new Region();
        VBox.setVgrow(espacio, Priority.ALWAYS);

        Label version = new Label("Bella Imagen | Version 1.0");
        version.setFont(Font.font("Arial", 10));
        version.setTextFill(Color.web("#A99AA3"));

        menuLateral.getChildren().addAll(
                marca,
                menuTitulo,
                inicio,
                clientes,
                especialistas,
                servicios,
                citas,
                permutas,
                espacio,
                version
        );

        panelPrincipal.setLeft(menuLateral);
    }

    private Button crearBotonMenu(String texto) {

        Button boton = new Button(texto);
        boton.setMaxWidth(Double.MAX_VALUE);
        boton.setAlignment(Pos.CENTER_LEFT);
        boton.setPadding(new Insets(13, 12, 13, 15));
        boton.setFont(Font.font("Arial", 13));
        boton.setStyle(estiloBotonNormal());

        boton.setOnMouseEntered(e -> {
            if (!boton.getStyle().equals(estiloBotonActivo())) {
                boton.setStyle(
                        "-fx-background-color: #F9EDF3;" +
                        "-fx-text-fill: " + ROSA + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;" +
                        "-fx-alignment: CENTER_LEFT;"
                );
            }
        });

        boton.setOnMouseExited(e -> {
            if (!boton.getStyle().equals(estiloBotonActivo())) {
                boton.setStyle(estiloBotonNormal());
            }
        });

        return boton;
    }

    private String estiloBotonNormal() {
        return "-fx-background-color: transparent;" +
                "-fx-text-fill: #665965;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-alignment: CENTER_LEFT;";
    }

    private String estiloBotonActivo() {
        return "-fx-background-color: " + ROSA_CLARO + ";" +
                "-fx-text-fill: " + ROSA + ";" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-alignment: CENTER_LEFT;";
    }

    private void activarBoton(Button seleccionado) {

        for (var nodo : menuLateral.getChildren()) {
            if (nodo instanceof Button boton) {
                boton.setStyle(estiloBotonNormal());
            }
        }

        seleccionado.setStyle(estiloBotonActivo());
    }

    private void crearEncabezado() {

        Label titulo = new Label("Panel de administracion");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web("#493B46"));

        Label usuario = new Label("Administracion");
        usuario.setFont(Font.font("Arial", 12));
        usuario.setTextFill(Color.web(ROSA));

        HBox encabezado = new HBox(titulo, usuario);
        encabezado.setAlignment(Pos.CENTER_LEFT);
        encabezado.setSpacing(20);
        encabezado.setPadding(new Insets(22, 30, 22, 30));
        encabezado.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #F0E4EB;" +
                "-fx-border-width: 0 0 1 0;"
        );

        HBox.setHgrow(titulo, Priority.ALWAYS);

        panelPrincipal.setTop(encabezado);
    }

    private void mostrarInicio() {

        VBox contenido = new VBox(25);
        contenido.setPadding(new Insets(35));
        contenido.setStyle("-fx-background-color: " + FONDO + ";");

        Label bienvenida = new Label("ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡Bienvenido a Bella Imagen!");
        bienvenida.setFont(Font.font("Arial", FontWeight.BOLD, 27));
        bienvenida.setTextFill(Color.web("#493B46"));

        Label descripcion = new Label(
                "Administra tu estetica desde un solo lugar."
        );
        descripcion.setFont(Font.font("Arial", 14));
        descripcion.setTextFill(Color.web("#93838E"));

        VBox bienvenidaBox = new VBox(9, bienvenida, descripcion);

        Label resumenTitulo = new Label("Resumen del sistema");
        resumenTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        resumenTitulo.setTextFill(Color.web("#493B46"));

        HBox tarjetas = new HBox(18);
        tarjetas.getChildren().addAll(
                crearTarjeta("Clientes", "Gestion de clientes"),
                crearTarjeta("Especialistas", "Personal de la estetica"),
                crearTarjeta("Servicios", "Catalogo de servicios"),
                crearTarjeta("Citas", "Agenda de citas")
        );

        for (var nodo : tarjetas.getChildren()) {
            HBox.setHgrow(nodo, Priority.ALWAYS);
        }

        VBox informacion = new VBox(12);
        informacion.setPadding(new Insets(25));
        informacion.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #F0E4EB;" +
                "-fx-border-radius: 12;"
        );

        Label informacionTitulo = new Label("Sistema Bella Imagen");
        informacionTitulo.setFont(
                Font.font("Arial", FontWeight.BOLD, 17)
        );
        informacionTitulo.setTextFill(Color.web(ROSA));

        Label informacionTexto = new Label(
                "Bienvenido al sistema de gestion de Bella Imagen.\n\n" +
                "Desde el menu lateral puedes acceder a los diferentes " +
                "modulos para administrar clientes, especialistas, " +
                "servicios, citas y permutas de turnos.\n\n" +
                "La informacion se almacenara en nuestra base de datos MySQL."
        );

        informacionTexto.setWrapText(true);
        informacionTexto.setFont(Font.font("Arial", 13));
        informacionTexto.setTextFill(Color.web("#746674"));

        informacion.getChildren().addAll(
                informacionTitulo,
                informacionTexto
        );

        contenido.getChildren().addAll(
                bienvenidaBox,
                resumenTitulo,
                tarjetas,
                informacion
        );

        panelPrincipal.setCenter(contenido);
    }

    private VBox crearTarjeta(String titulo, String descripcion) {

        VBox tarjeta = new VBox(12);
        tarjeta.setPadding(new Insets(20));
        tarjeta.setMinWidth(150);
        tarjeta.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #F0E4EB;" +
                "-fx-border-radius: 12;"
        );

        Label nombre = new Label(titulo);
        nombre.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        nombre.setTextFill(Color.web(ROSA));

        Label detalle = new Label(descripcion);
        detalle.setFont(Font.font("Arial", 11));
        detalle.setTextFill(Color.web("#93838E"));
        detalle.setWrapText(true);

        tarjeta.getChildren().addAll(nombre, detalle);

        return tarjeta;
    }

    private void mostrarModulo(String titulo, String descripcion) {

        VBox contenido = new VBox(18);
        contenido.setPadding(new Insets(40));
        contenido.setStyle("-fx-background-color: " + FONDO + ";");

        Label encabezado = new Label(titulo);
        encabezado.setFont(Font.font("Arial", FontWeight.BOLD, 27));
        encabezado.setTextFill(Color.web("#493B46"));

        Label texto = new Label(descripcion);
        texto.setFont(Font.font("Arial", 14));
        texto.setTextFill(Color.web("#93838E"));

        Label estado = new Label(
                "Este modulo esta preparado para su desarrollo."
        );
        estado.setFont(Font.font("Arial", 13));
        estado.setTextFill(Color.web(ROSA));

        VBox tarjeta = new VBox(15, texto, estado);
        tarjeta.setPadding(new Insets(25));
        tarjeta.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #F0E4EB;" +
                "-fx-border-radius: 12;"
        );

        contenido.getChildren().addAll(encabezado, tarjeta);

        panelPrincipal.setCenter(contenido);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
