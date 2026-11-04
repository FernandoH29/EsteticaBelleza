package com.bellaimagen;

import com.bellaimagen.vista.CitasView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {

    private final String VINO = "#581825";
    private final String ROSA = "#A85D83";
    private final String FONDO = "#FAF8F5";
    private final String DORADO = "#C6A15B";

    @Override
    public void start(Stage stage) {

        BorderPane raiz = new BorderPane();
        raiz.setStyle("-fx-background-color: " + FONDO + ";");

        VBox contenido = new VBox(25);
        contenido.setAlignment(Pos.TOP_CENTER);
        contenido.setPadding(new Insets(25, 45, 35, 45));

        HBox encabezado = crearEncabezado();

        VBox presentacion = new VBox(15);
        presentacion.setAlignment(Pos.CENTER);
        presentacion.setPadding(new Insets(35, 20, 20, 20));

        Label etiqueta = new Label("BIENVENIDA A BELLA IMAGEN");
        etiqueta.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        etiqueta.setTextFill(Color.web(ROSA));

        Label titulo = new Label("Tu belleza merece\nun momento especial");
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 38));
        titulo.setTextFill(Color.web(VINO));
        titulo.setAlignment(Pos.CENTER);
        titulo.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label descripcion = new Label(
                "Disfruta de nuestros servicios de belleza y cuidado personal.\n" +
                "Nos encantará atenderte."
        );
        descripcion.setFont(Font.font("Arial", 15));
        descripcion.setTextFill(Color.web("#746674"));
        descripcion.setAlignment(Pos.CENTER);
        descripcion.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Button agendar = new Button("AGENDAR MI CITA");
        agendar.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        agendar.setTextFill(Color.WHITE);
        agendar.setPadding(new Insets(16, 38, 16, 38));
        agendar.setStyle(
                "-fx-background-color: " + VINO + ";" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );

        agendar.setOnAction(e -> CitasView.mostrar());

        VBox bloquePresentacion = new VBox(18,
                etiqueta, titulo, descripcion, agendar);
        bloquePresentacion.setAlignment(Pos.CENTER);

        ImageView imagen = crearImagenPortada();

        HBox portada = new HBox(35, bloquePresentacion, imagen);
        portada.setAlignment(Pos.CENTER);
        portada.setPadding(new Insets(25, 10, 25, 10));
        HBox.setHgrow(bloquePresentacion, Priority.ALWAYS);
        HBox.setHgrow(imagen, Priority.ALWAYS);

        VBox servicios = crearSeccionServicios();

        Label pie = new Label(
                "BELLA IMAGEN  |  Belleza, cuidado y bienestar"
        );
        pie.setFont(Font.font("Arial", 11));
        pie.setTextFill(Color.web("#93838E"));
        pie.setPadding(new Insets(15));

        contenido.getChildren().addAll(
                encabezado,
                portada,
                servicios,
                pie
        );

        ScrollPane desplazamiento = new ScrollPane(contenido);
        desplazamiento.setFitToWidth(true);
        desplazamiento.setStyle(
                "-fx-background: " + FONDO + ";" +
                "-fx-background-color: " + FONDO + ";"
        );

        raiz.setCenter(desplazamiento);

        Scene escena = new Scene(raiz, 1200, 780);

        stage.setTitle("Bella Imagen - Estética y Belleza");
        stage.setScene(escena);
        stage.setMinWidth(900);
        stage.setMinHeight(650);
        stage.show();
    }

    private HBox crearEncabezado() {

        Label logo = new Label("BELLA IMAGEN");
        logo.setFont(Font.font("Georgia", FontWeight.BOLD, 25));
        logo.setTextFill(Color.web(VINO));

        Label subtitulo = new Label("ESTÉTICA Y BELLEZA");
        subtitulo.setFont(Font.font("Arial", 9));
        subtitulo.setTextFill(Color.web(ROSA));

        VBox marca = new VBox(3, logo, subtitulo);

        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);

        Button acceso = new Button("Acceso del personal");
        acceso.setFont(Font.font("Arial", 12));
        acceso.setTextFill(Color.web(VINO));
        acceso.setPadding(new Insets(11, 18, 11, 18));
        acceso.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: " + VINO + ";" +
                "-fx-border-radius: 7;" +
                "-fx-background-radius: 7;" +
                "-fx-cursor: hand;"
        );

        acceso.setOnAction(e -> {
            com.bellaimagen.vista.LoginView.mostrar();
        });

        HBox barra = new HBox(marca, espacio, acceso);
        barra.setAlignment(Pos.CENTER_LEFT);
        barra.setPadding(new Insets(10, 25, 20, 25));
        barra.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #F0E4EB;" +
                "-fx-border-width: 0 0 1 0;"
        );

        return barra;
    }

    private ImageView crearImagenPortada() {

        ImageView imagen = new ImageView();

        try {
            Image foto = new Image(
                    "https://images.unsplash.com/photo-1560066984-138dadb4c035?w=1000",
                    true
            );

            imagen.setImage(foto);
        } catch (Exception ex) {
            System.out.println("No se pudo cargar la imagen de portada.");
        }

        imagen.setFitWidth(470);
        imagen.setFitHeight(310);
        imagen.setPreserveRatio(false);
        imagen.setSmooth(true);

        imagen.setStyle("-fx-background-radius: 15;");

        return imagen;
    }

    private VBox crearSeccionServicios() {

        VBox seccion = new VBox(20);
        seccion.setAlignment(Pos.CENTER);
        seccion.setPadding(new Insets(20, 10, 25, 10));

        Label titulo = new Label("Un espacio para consentirte");
        titulo.setFont(Font.font("Georgia", FontWeight.BOLD, 25));
        titulo.setTextFill(Color.web(VINO));

        Label descripcion = new Label(
                "Conoce algunos de los servicios que tenemos para ti."
        );
        descripcion.setFont(Font.font("Arial", 13));
        descripcion.setTextFill(Color.web("#746674"));

        HBox tarjetas = new HBox(18);
        tarjetas.setAlignment(Pos.CENTER);

        tarjetas.getChildren().addAll(
                crearTarjeta("Manicure", "Cuidado y belleza para tus uñas"),
                crearTarjeta("Cabello", "Cortes, tintes y tratamientos"),
                crearTarjeta("Maquillaje", "Resalta tu belleza natural")
        );

        seccion.getChildren().addAll(titulo, descripcion, tarjetas);

        return seccion;
    }

    private VBox crearTarjeta(String titulo, String descripcion) {

        VBox tarjeta = new VBox(10);
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setPadding(new Insets(25));
        tarjeta.setPrefWidth(250);
        tarjeta.setMinHeight(115);
        tarjeta.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #F0E4EB;" +
                "-fx-border-radius: 12;"
        );

        Label nombre = new Label(titulo);
        nombre.setFont(Font.font("Georgia", FontWeight.BOLD, 19));
        nombre.setTextFill(Color.web(ROSA));

        Label detalle = new Label(descripcion);
        detalle.setFont(Font.font("Arial", 12));
        detalle.setTextFill(Color.web("#746674"));
        detalle.setWrapText(true);
        detalle.setAlignment(Pos.CENTER);

        tarjeta.getChildren().addAll(nombre, detalle);

        return tarjeta;
    }

    public static void main(String[] args) {
        launch(args);
    }
}