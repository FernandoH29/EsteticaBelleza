package com.bellaimagen.vista;

import com.bellaimagen.dao.EspecialistaDAO;
import com.bellaimagen.modelo.Especialista;

import java.sql.SQLException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EspecialistasView {

    private final EspecialistaDAO especialistaDAO = new EspecialistaDAO();

    private final TableView<Especialista> tabla = new TableView<>();
    private final ObservableList<Especialista> datos =
            FXCollections.observableArrayList();

    private final TextField txtNombre = new TextField();
    private final ComboBox<String> cmbEspecialidad = new ComboBox<>();
    private final ComboBox<String> cmbEstado = new ComboBox<>();

    private final Label lblEstado = new Label();

    private Especialista especialistaSeleccionado;

    public static void mostrar() {
        new EspecialistasView().abrirVentana();
    }

    private void abrirVentana() {

        Stage ventana = new Stage();
        ventana.setTitle("Bella Imagen - Gestion de Especialistas");

        BorderPane principal = new BorderPane();
        principal.setStyle("-fx-background-color: #FAF8F5;");

        principal.setTop(crearEncabezado());
        principal.setCenter(crearContenido());
        principal.setBottom(crearPie());

        cargarEspecialistas();

        Scene escena = new Scene(principal, 1050, 680);
        ventana.setScene(escena);
        ventana.setMinWidth(900);
        ventana.setMinHeight(600);
        ventana.show();
    }

    private VBox crearEncabezado() {

        Label titulo = new Label("Gestion de Especialistas");
        titulo.setStyle(
            "-fx-font-size: 27px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #581825;"
        );

        Label subtitulo = new Label(
            "Administra al personal y sus especialidades"
        );
        subtitulo.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #777777;"
        );

        VBox encabezado = new VBox(7, titulo, subtitulo);
        encabezado.setPadding(new Insets(25, 30, 20, 30));
        encabezado.setStyle("-fx-background-color: white;");

        return encabezado;
    }

    private VBox crearContenido() {

        VBox contenedor = new VBox(18);
        contenedor.setPadding(new Insets(25, 30, 25, 30));

        Label tituloFormulario = new Label("Datos del especialista");
        tituloFormulario.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #581825;"
        );

        GridPane formulario = new GridPane();
        formulario.setHgap(15);
        formulario.setVgap(12);

        configurarCampo(txtNombre, "Nombre completo");

        cmbEspecialidad.getItems().addAll(
            "Uñas",
            "Cabello",
            "Maquillaje",
            "Pestañas",
            "Cejas",
            "Faciales",
            "Depilación",
            "Otra"
        );
        cmbEspecialidad.setPromptText("Seleccionar especialidad");
        cmbEspecialidad.setPrefWidth(220);

        cmbEstado.getItems().addAll("Activo", "Inactivo");
        cmbEstado.setValue("Activo");
        cmbEstado.setPrefWidth(150);

        formulario.add(crearEtiqueta("Nombre:"), 0, 0);
        formulario.add(txtNombre, 1, 0);

        formulario.add(crearEtiqueta("Especialidad:"), 2, 0);
        formulario.add(cmbEspecialidad, 3, 0);

        formulario.add(crearEtiqueta("Estado:"), 0, 1);
        formulario.add(cmbEstado, 1, 1);

        Button btnRegistrar = crearBoton("Registrar", "#581825");
        Button btnActualizar = crearBoton("Actualizar", "#9A596B");
        Button btnEstado = crearBoton("Activar / Desactivar", "#B65C52");
        Button btnLimpiar = crearBoton("Limpiar", "#777777");

        btnRegistrar.setOnAction(e -> registrarEspecialista());
        btnActualizar.setOnAction(e -> actualizarEspecialista());
        btnEstado.setOnAction(e -> cambiarEstado());
        btnLimpiar.setOnAction(e -> limpiarFormulario());

        HBox botones = new HBox(
            12, btnRegistrar, btnActualizar, btnEstado, btnLimpiar
        );
        botones.setAlignment(Pos.CENTER_LEFT);

        Label tituloTabla = new Label("Lista de especialistas");
        tituloTabla.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #581825;"
        );

        configurarTabla();

        VBox.setVgrow(tabla, javafx.scene.layout.Priority.ALWAYS);

        contenedor.getChildren().addAll(
            tituloFormulario,
            formulario,
            botones,
            tituloTabla,
            tabla
        );

        return contenedor;
    }

    private Label crearEtiqueta(String texto) {
        Label etiqueta = new Label(texto);
        etiqueta.setStyle(
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #581825;"
        );
        return etiqueta;
    }

    private void configurarCampo(TextField campo, String ayuda) {
        campo.setPromptText(ayuda);
        campo.setPrefWidth(220);
        campo.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #D9C7C7;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;" +
            "-fx-padding: 9px;"
        );
    }

    private Button crearBoton(String texto, String color) {
        Button boton = new Button(texto);
        boton.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10px 18px;" +
            "-fx-background-radius: 5px;" +
            "-fx-cursor: hand;"
        );
        return boton;
    }

    private void configurarTabla() {

        TableColumn<Especialista, Integer> colId =
                new TableColumn<>("ID");
        colId.setCellValueFactory(
            new PropertyValueFactory<>("idEspecialista")
        );
        colId.setPrefWidth(70);

        TableColumn<Especialista, String> colNombre =
                new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(
            new PropertyValueFactory<>("nombre")
        );
        colNombre.setPrefWidth(250);

        TableColumn<Especialista, String> colEspecialidad =
                new TableColumn<>("Especialidad");
        colEspecialidad.setCellValueFactory(
            new PropertyValueFactory<>("especialidad")
        );
        colEspecialidad.setPrefWidth(220);

        TableColumn<Especialista, String> colEstado =
                new TableColumn<>("Estado");
        colEstado.setCellValueFactory(
            new PropertyValueFactory<>("estado")
        );
        colEstado.setPrefWidth(150);

        tabla.getColumns().addAll(
            colId, colNombre, colEspecialidad, colEstado
        );

        tabla.setItems(datos);
        tabla.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY
        );

        tabla.getSelectionModel().selectedItemProperty().addListener(
            (observable, anterior, seleccionado) -> {

                if (seleccionado != null) {

                    especialistaSeleccionado = seleccionado;

                    txtNombre.setText(seleccionado.getNombre());
                    cmbEspecialidad.setValue(
                        seleccionado.getEspecialidad()
                    );
                    cmbEstado.setValue(seleccionado.getEstado());
                }
            }
        );
    }

    private void cargarEspecialistas() {

        try {

            List<Especialista> especialistas =
                    especialistaDAO.listarTodos();

            datos.setAll(especialistas);

            lblEstado.setText(
                "Especialistas registrados: " + especialistas.size()
                + " | Datos actualizados desde Railway"
            );

        } catch (SQLException e) {
            mostrarError("No se pudieron cargar los especialistas.", e);
        }
    }

    private void registrarEspecialista() {

        if (!validarFormulario()) {
            return;
        }

        Especialista especialista = new Especialista(
            txtNombre.getText().trim(),
            cmbEspecialidad.getValue(),
            cmbEstado.getValue()
        );

        try {

            especialistaDAO.insertar(especialista);

            mostrarInformacion(
                "Especialista registrado correctamente."
            );

            limpiarFormulario();
            cargarEspecialistas();

        } catch (SQLException e) {
            mostrarError("No se pudo registrar el especialista.", e);
        }
    }

    private void actualizarEspecialista() {

        if (especialistaSeleccionado == null) {
            mostrarAdvertencia(
                "Selecciona un especialista de la tabla."
            );
            return;
        }

        if (!validarFormulario()) {
            return;
        }

        Especialista especialista = new Especialista(
            especialistaSeleccionado.getIdEspecialista(),
            txtNombre.getText().trim(),
            cmbEspecialidad.getValue(),
            cmbEstado.getValue()
        );

        try {

            especialistaDAO.actualizar(especialista);

            mostrarInformacion(
                "Especialista actualizado correctamente."
            );

            limpiarFormulario();
            cargarEspecialistas();

        } catch (SQLException e) {
            mostrarError("No se pudo actualizar el especialista.", e);
        }
    }

    private void cambiarEstado() {

        if (especialistaSeleccionado == null) {
            mostrarAdvertencia(
                "Selecciona un especialista de la tabla."
            );
            return;
        }

        String nuevoEstado =
                especialistaSeleccionado.getEstado().equalsIgnoreCase("Activo")
                ? "Inactivo"
                : "Activo";

        Alert confirmacion = new Alert(
            Alert.AlertType.CONFIRMATION
        );

        confirmacion.setTitle("Cambiar estado");
        confirmacion.setHeaderText("Confirmar cambio de estado");
        confirmacion.setContentText(
            "¿Deseas cambiar el estado de "
            + especialistaSeleccionado.getNombre()
            + " a " + nuevoEstado + "?"
        );

        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL)
                == ButtonType.OK) {

            try {

                especialistaDAO.cambiarEstado(
                    especialistaSeleccionado.getIdEspecialista(),
                    nuevoEstado
                );

                mostrarInformacion(
                    "Estado cambiado a " + nuevoEstado + "."
                );

                limpiarFormulario();
                cargarEspecialistas();

            } catch (SQLException e) {
                mostrarError("No se pudo cambiar el estado.", e);
            }
        }
    }

    private boolean validarFormulario() {

        if (txtNombre.getText().trim().isEmpty()) {
            mostrarAdvertencia("El nombre es obligatorio.");
            txtNombre.requestFocus();
            return false;
        }

        if (cmbEspecialidad.getValue() == null) {
            mostrarAdvertencia("Selecciona una especialidad.");
            cmbEspecialidad.requestFocus();
            return false;
        }

        if (cmbEstado.getValue() == null) {
            mostrarAdvertencia("Selecciona un estado.");
            cmbEstado.requestFocus();
            return false;
        }

        return true;
    }

    private void limpiarFormulario() {

        txtNombre.clear();
        cmbEspecialidad.setValue(null);
        cmbEstado.setValue("Activo");

        especialistaSeleccionado = null;
        tabla.getSelectionModel().clearSelection();
    }

    private VBox crearPie() {

        VBox pie = new VBox(lblEstado);
        pie.setPadding(new Insets(12, 30, 12, 30));

        pie.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #E8DEDA;" +
            "-fx-border-width: 1 0 0 0;"
        );

        lblEstado.setStyle("-fx-text-fill: #777777;");

        return pie;
    }

    private void mostrarInformacion(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Bella Imagen");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarAdvertencia(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Atencion");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarError(String mensaje, Exception e) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(mensaje);
        alerta.setContentText(e.getMessage());
        alerta.showAndWait();
    }
}