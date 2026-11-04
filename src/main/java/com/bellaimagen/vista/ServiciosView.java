package com.bellaimagen.vista;

import com.bellaimagen.dao.ServicioDAO;
import com.bellaimagen.modelo.Servicio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ServiciosView {

    private static final ServicioDAO dao = new ServicioDAO();

    private static final ObservableList<Servicio> listaServicios =
            FXCollections.observableArrayList();

    private static TableView<Servicio> tabla;

    private static TextField txtNombre;
    private static TextField txtDuracion;
    private static TextField txtBuffer;
    private static TextField txtPrecio;

    private static Servicio servicioSeleccionado;

    public static void mostrar() {

        Stage ventana = new Stage();
        ventana.setTitle("Bella Imagen - Gestión de Servicios");

        BorderPane principal = new BorderPane();
        principal.setStyle("-fx-background-color: #FAF8F5;");

        Label titulo = new Label("Gestión de Servicios");
        titulo.setFont(Font.font("Arial", 26));
        titulo.setTextFill(Color.web("#581825"));

        Label subtitulo = new Label(
                "Administra los tratamientos y servicios de Bella Imagen"
        );
        subtitulo.setTextFill(Color.web("#777777"));

        VBox encabezado = new VBox(8, titulo, subtitulo);
        encabezado.setPadding(new Insets(25, 30, 15, 30));

        principal.setTop(encabezado);

        VBox contenido = new VBox(18);
        contenido.setPadding(new Insets(10, 30, 25, 30));

        // Formulario
        Label lblFormulario = new Label("Información del servicio");
        lblFormulario.setFont(Font.font("Arial", 17));
        lblFormulario.setTextFill(Color.web("#581825"));

        txtNombre = new TextField();
        txtNombre.setPromptText("Nombre del servicio");

        txtDuracion = new TextField();
        txtDuracion.setPromptText("Duración en minutos");

        txtBuffer = new TextField();
        txtBuffer.setPromptText("Tiempo de preparación en minutos");

        txtPrecio = new TextField();
        txtPrecio.setPromptText("Precio en pesos");

        GridPane formulario = new GridPane();
        formulario.setHgap(15);
        formulario.setVgap(12);

        formulario.add(new Label("Nombre:"), 0, 0);
        formulario.add(txtNombre, 1, 0);

        formulario.add(new Label("Duración (min):"), 2, 0);
        formulario.add(txtDuracion, 3, 0);

        formulario.add(new Label("Tiempo buffer (min):"), 0, 1);
        formulario.add(txtBuffer, 1, 1);

        formulario.add(new Label("Precio ($):"), 2, 1);
        formulario.add(txtPrecio, 3, 1);

        ColumnConstraints columna1 = new ColumnConstraints();
        columna1.setPercentWidth(17);

        ColumnConstraints columna2 = new ColumnConstraints();
        columna2.setPercentWidth(33);

        ColumnConstraints columna3 = new ColumnConstraints();
        columna3.setPercentWidth(17);

        ColumnConstraints columna4 = new ColumnConstraints();
        columna4.setPercentWidth(33);

        formulario.getColumnConstraints().addAll(
                columna1, columna2, columna3, columna4
        );

        formulario.setMaxWidth(Double.MAX_VALUE);

        // Botones
        Button btnRegistrar = new Button("Registrar");
        Button btnActualizar = new Button("Actualizar");
        Button btnEliminar = new Button("Eliminar");
        Button btnLimpiar = new Button("Limpiar");

        String estiloBoton =
                "-fx-background-color: #581825;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 13px;"
                + "-fx-padding: 10 18;"
                + "-fx-background-radius: 6;";

        btnRegistrar.setStyle(estiloBoton);
        btnActualizar.setStyle(estiloBoton);
        btnEliminar.setStyle(
                "-fx-background-color: #A94442;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 13px;"
                + "-fx-padding: 10 18;"
                + "-fx-background-radius: 6;"
        );

        btnLimpiar.setStyle(
                "-fx-background-color: #E2ECE9;"
                + "-fx-text-fill: #333333;"
                + "-fx-font-size: 13px;"
                + "-fx-padding: 10 18;"
                + "-fx-background-radius: 6;"
        );

        HBox botones = new HBox(
                10, btnRegistrar, btnActualizar, btnEliminar, btnLimpiar
        );
        botones.setAlignment(Pos.CENTER_LEFT);

        VBox panelFormulario = new VBox(
                15, lblFormulario, formulario, botones
        );
        panelFormulario.setPadding(new Insets(20));
        panelFormulario.setStyle(
                "-fx-background-color: white;"
                + "-fx-background-radius: 10;"
                + "-fx-border-color: #E8DDD8;"
                + "-fx-border-radius: 10;"
        );

        // Tabla
        Label lblTabla = new Label("Servicios registrados");
        lblTabla.setFont(Font.font("Arial", 18));
        lblTabla.setTextFill(Color.web("#581825"));

        tabla = new TableView<>();
        tabla.setItems(listaServicios);
        tabla.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        TableColumn<Servicio, Integer> colId =
                new TableColumn<>("ID");

        colId.setCellValueFactory(
                new PropertyValueFactory<>("idServicio")
        );
        colId.setMaxWidth(65);

        TableColumn<Servicio, String> colNombre =
                new TableColumn<>("Servicio");

        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre")
        );

        TableColumn<Servicio, Integer> colDuracion =
                new TableColumn<>("Duración (min)");

        colDuracion.setCellValueFactory(
                new PropertyValueFactory<>("duracionEstimada")
        );

        TableColumn<Servicio, Integer> colBuffer =
                new TableColumn<>("Buffer (min)");

        colBuffer.setCellValueFactory(
                new PropertyValueFactory<>("tiempoBuffer")
        );

        TableColumn<Servicio, Double> colPrecio =
                new TableColumn<>("Precio ($)");

        colPrecio.setCellValueFactory(
                new PropertyValueFactory<>("precio")
        );

        tabla.getColumns().addAll(
                colId, colNombre, colDuracion, colBuffer, colPrecio
        );

        tabla.setPlaceholder(
                new Label("No hay servicios registrados.")
        );

        tabla.setPrefHeight(350);

        // Seleccionar un servicio
        tabla.getSelectionModel().selectedItemProperty()
                .addListener((observable, anterior, seleccionado) -> {

                    if (seleccionado != null) {
                        servicioSeleccionado = seleccionado;

                        txtNombre.setText(seleccionado.getNombre());
                        txtDuracion.setText(
                                String.valueOf(
                                        seleccionado.getDuracionEstimada()
                                )
                        );
                        txtBuffer.setText(
                                String.valueOf(
                                        seleccionado.getTiempoBuffer()
                                )
                        );
                        txtPrecio.setText(
                                String.valueOf(seleccionado.getPrecio())
                        );
                    }
                });

        // Acciones
        btnRegistrar.setOnAction(e -> registrar());
        btnActualizar.setOnAction(e -> actualizar());
        btnEliminar.setOnAction(e -> eliminar());
        btnLimpiar.setOnAction(e -> limpiar());

        contenido.getChildren().addAll(
                panelFormulario, lblTabla, tabla
        );

        principal.setCenter(contenido);

        Scene escena = new Scene(principal, 1100, 720);

        ventana.setScene(escena);
        ventana.show();

        cargarServicios();
    }

    private static Servicio obtenerDatosFormulario() {

        String nombre = txtNombre.getText().trim();
        String duracionTexto = txtDuracion.getText().trim();
        String bufferTexto = txtBuffer.getText().trim();
        String precioTexto = txtPrecio.getText().trim();

        if (nombre.isEmpty() || duracionTexto.isEmpty()
                || bufferTexto.isEmpty() || precioTexto.isEmpty()) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Campos incompletos",
                    "Completa todos los campos."
            );
            return null;
        }

        try {
            int duracion = Integer.parseInt(duracionTexto);
            int buffer = Integer.parseInt(bufferTexto);
            double precio = Double.parseDouble(precioTexto);

            if (duracion <= 0 || buffer < 0 || precio < 0) {
                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Valores incorrectos",
                        "La duración debe ser mayor que cero. "
                        + "El buffer y el precio no pueden ser negativos."
                );
                return null;
            }

            return new Servicio(nombre, duracion, buffer, precio);

        } catch (NumberFormatException ex) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Formato incorrecto",
                    "Duración y buffer deben ser números enteros. "
                    + "El precio debe ser numérico."
            );
            return null;
        }
    }

    private static void registrar() {

        Servicio servicio = obtenerDatosFormulario();

        if (servicio == null) {
            return;
        }

        try {
            dao.insertar(servicio);

            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "Registro exitoso",
                    "El servicio se registró correctamente."
            );

            cargarServicios();
            limpiar();

        } catch (SQLException ex) {
            mostrarError("No se pudo registrar el servicio.", ex);
        }
    }

    private static void actualizar() {

        if (servicioSeleccionado == null) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Selecciona un servicio",
                    "Selecciona un servicio de la tabla para actualizarlo."
            );
            return;
        }

        Servicio datos = obtenerDatosFormulario();

        if (datos == null) {
            return;
        }

        datos.setIdServicio(servicioSeleccionado.getIdServicio());

        try {
            dao.actualizar(datos);

            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "Actualización exitosa",
                    "El servicio se actualizó correctamente."
            );

            cargarServicios();
            limpiar();

        } catch (SQLException ex) {
            mostrarError("No se pudo actualizar el servicio.", ex);
        }
    }

    private static void eliminar() {

        if (servicioSeleccionado == null) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Selecciona un servicio",
                    "Selecciona un servicio de la tabla."
            );
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("Eliminar servicio");
        confirmacion.setContentText(
                "¿Deseas eliminar el servicio "
                + servicioSeleccionado.getNombre() + "?"
        );

        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL)
                != ButtonType.OK) {
            return;
        }

        try {
            dao.eliminar(servicioSeleccionado.getIdServicio());

            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "Eliminación exitosa",
                    "El servicio fue eliminado."
            );

            cargarServicios();
            limpiar();

        } catch (SQLException ex) {
            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "No se puede eliminar",
                    "Este servicio podría estar relacionado con una cita. "
                    + "No se eliminó ningún registro."
            );
        }
    }

    private static void cargarServicios() {

        try {
            listaServicios.setAll(dao.listarTodos());

        } catch (SQLException ex) {
            mostrarError("No se pudieron cargar los servicios.", ex);
        }
    }

    private static void limpiar() {

        txtNombre.clear();
        txtDuracion.clear();
        txtBuffer.clear();
        txtPrecio.clear();

        servicioSeleccionado = null;
        tabla.getSelectionModel().clearSelection();
    }

    private static void mostrarAlerta(
            Alert.AlertType tipo, String titulo, String mensaje) {

        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private static void mostrarError(String mensaje, Exception ex) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(mensaje);
        alerta.setContentText(ex.getMessage());
        alerta.showAndWait();

        ex.printStackTrace();
    }
}