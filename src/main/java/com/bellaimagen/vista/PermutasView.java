package com.bellaimagen.vista;

import com.bellaimagen.dao.PermutaDAO;
import com.bellaimagen.dao.EspecialistaDAO;
import com.bellaimagen.modelo.Permuta;
import com.bellaimagen.modelo.Especialista;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.beans.property.SimpleStringProperty;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class PermutasView {

    private static final PermutaDAO permutaDAO = new PermutaDAO();
    private static final EspecialistaDAO especialistaDAO =
            new EspecialistaDAO();

    private static TableView<Permuta> tabla;
    private static DatePicker dpFecha;
    private static ComboBox<Especialista> cmbOrigen;
    private static ComboBox<Especialista> cmbDestino;
    private static ComboBox<String> cmbEstado;
    private static Label lblTotal;

    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void mostrar() {

        Stage ventana = new Stage();
        ventana.setTitle("Bella Imagen - Permutas de turnos");

        BorderPane principal = new BorderPane();
        principal.setStyle("-fx-background-color: #FAF8F5;");

        Label titulo = new Label("Permutas de turnos");
        titulo.setFont(Font.font("Arial", 27));
        titulo.setTextFill(Color.web("#581825"));

        Label subtitulo = new Label(
                "Administra las solicitudes de intercambio entre especialistas.");
        subtitulo.setTextFill(Color.web("#777777"));

        VBox encabezado = new VBox(8, titulo, subtitulo);
        encabezado.setPadding(new Insets(25, 30, 15, 30));

        principal.setTop(encabezado);

        VBox contenido = new VBox(18);
        contenido.setPadding(new Insets(10, 30, 25, 30));

        Label tituloFormulario = new Label("Registrar nueva permuta");
        tituloFormulario.setFont(Font.font("Arial", 18));
        tituloFormulario.setTextFill(Color.web("#581825"));

        dpFecha = new DatePicker();
        dpFecha.setPromptText("Selecciona la fecha");
        dpFecha.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate fecha, boolean vacio) {
                super.updateItem(fecha, vacio);
                if (!vacio && fecha.isBefore(LocalDate.now())) {
                    setDisable(true);
                }
            }
        });

        cmbOrigen = new ComboBox<>();
        cmbOrigen.setPromptText("Especialista que entrega");
        cmbOrigen.setPrefWidth(230);

        cmbDestino = new ComboBox<>();
        cmbDestino.setPromptText("Especialista que recibe");
        cmbDestino.setPrefWidth(230);

        configurarComboEspecialistas(cmbOrigen);
        configurarComboEspecialistas(cmbDestino);

        Button btnRegistrar = new Button("Registrar permuta");
        btnRegistrar.setStyle(
                "-fx-background-color: #581825;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10 18 10 18;" +
                "-fx-background-radius: 7;"
        );

        btnRegistrar.setOnAction(e -> registrarPermuta());

        GridPane formulario = new GridPane();
        formulario.setHgap(15);
        formulario.setVgap(12);

        formulario.add(new Label("Fecha del intercambio"), 0, 0);
        formulario.add(new Label("Especialista de origen"), 1, 0);
        formulario.add(new Label("Especialista de destino"), 2, 0);

        formulario.add(dpFecha, 0, 1);
        formulario.add(cmbOrigen, 1, 1);
        formulario.add(cmbDestino, 2, 1);
        formulario.add(btnRegistrar, 0, 2);

        VBox tarjetaFormulario = new VBox(15,
                tituloFormulario, formulario);
        tarjetaFormulario.setPadding(new Insets(20));
        tarjetaFormulario.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: #E5E0D8;" +
                "-fx-border-radius: 10;"
        );

        Label tituloTabla = new Label("Solicitudes registradas");
        tituloTabla.setFont(Font.font("Arial", 18));
        tituloTabla.setTextFill(Color.web("#581825"));

        tabla = new TableView<>();
        tabla.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Permuta, Integer> colId =
                new TableColumn<>("ID");
        colId.setCellValueFactory(
                new PropertyValueFactory<>("idPermuta"));
        colId.setMinWidth(50);

        TableColumn<Permuta, String> colFecha =
                new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getFecha().format(FORMATO)));

        TableColumn<Permuta, String> colOrigen =
                new TableColumn<>("Especialista de origen");
        colOrigen.setCellValueFactory(
                new PropertyValueFactory<>("nombreOrigen"));

        TableColumn<Permuta, String> colDestino =
                new TableColumn<>("Especialista de destino");
        colDestino.setCellValueFactory(
                new PropertyValueFactory<>("nombreDestino"));

        TableColumn<Permuta, String> colEstado =
                new TableColumn<>("Estado");
        colEstado.setCellValueFactory(
                new PropertyValueFactory<>("estado"));

        tabla.getColumns().addAll(
                colId, colFecha, colOrigen, colDestino, colEstado);

        Label tituloEstado = new Label("Administrar estado");
        tituloEstado.setFont(Font.font("Arial", 16));
        tituloEstado.setTextFill(Color.web("#581825"));

        cmbEstado = new ComboBox<>(
                FXCollections.observableArrayList(
                        "Pendiente", "Aprobada", "Rechazada"));
        cmbEstado.setPromptText("Selecciona un estado");

        Button btnActualizar = new Button("Actualizar estado");
        btnActualizar.setStyle(
                "-fx-background-color: #E2ECE9;" +
                "-fx-text-fill: #234E42;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 9 16 9 16;" +
                "-fx-background-radius: 7;"
        );
        btnActualizar.setOnAction(e -> actualizarEstado());

        Button btnActualizarLista = new Button("Actualizar lista");
        btnActualizarLista.setOnAction(e -> cargarPermutas());

        HBox controlesEstado = new HBox(12,
                cmbEstado, btnActualizar, btnActualizarLista);
        controlesEstado.setAlignment(Pos.CENTER_LEFT);

        lblTotal = new Label("Total de permutas: 0");
        lblTotal.setTextFill(Color.web("#777777"));

        tabla.getSelectionModel().selectedItemProperty()
                .addListener((observable, anterior, seleccionada) -> {
                    if (seleccionada != null) {
                        cmbEstado.setValue(seleccionada.getEstado());
                    }
                });

        contenido.getChildren().addAll(
                tarjetaFormulario,
                tituloTabla,
                tabla,
                tituloEstado,
                controlesEstado,
                lblTotal
        );

        VBox.setVgrow(tabla, Priority.ALWAYS);

        principal.setCenter(contenido);

        Scene escena = new Scene(principal, 1100, 750);
        ventana.setScene(escena);
        ventana.show();

        cargarEspecialistas();
        cargarPermutas();
    }

    private static void configurarComboEspecialistas(
            ComboBox<Especialista> combo) {

        StringConverter<Especialista> convertidor =
                new StringConverter<>() {
                    @Override
                    public String toString(Especialista especialista) {
                        return especialista == null
                                ? ""
                                : especialista.getNombre()
                                  + " - " + especialista.getEspecialidad();
                    }

                    @Override
                    public Especialista fromString(String texto) {
                        return null;
                    }
                };

        combo.setConverter(convertidor);

        combo.setCellFactory(lista -> new ListCell<>() {
            @Override
            protected void updateItem(
                    Especialista especialista, boolean vacio) {
                super.updateItem(especialista, vacio);
                setText(vacio || especialista == null
                        ? null : convertidor.toString(especialista));
            }
        });

        combo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(
                    Especialista especialista, boolean vacio) {
                super.updateItem(especialista, vacio);
                setText(vacio || especialista == null
                        ? null : convertidor.toString(especialista));
            }
        });
    }

    private static void cargarEspecialistas() {

        try {
            List<Especialista> todos = especialistaDAO.listarTodos();

            List<Especialista> activos = todos.stream()
                    .filter(e -> "Activo".equalsIgnoreCase(e.getEstado()))
                    .collect(Collectors.toList());

            cmbOrigen.setItems(
                    FXCollections.observableArrayList(activos));
            cmbDestino.setItems(
                    FXCollections.observableArrayList(activos));

        } catch (SQLException ex) {
            mostrarError("No se pudieron cargar los especialistas: "
                    + ex.getMessage());
        }
    }

    private static void cargarPermutas() {

        try {
            List<Permuta> permutas = permutaDAO.listarTodas();

            tabla.setItems(
                    FXCollections.observableArrayList(permutas));

            lblTotal.setText(
                    "Total de permutas: " + permutas.size());

        } catch (SQLException ex) {
            mostrarError("No se pudieron consultar las permutas: "
                    + ex.getMessage());
        }
    }

    private static void registrarPermuta() {

        LocalDate fecha = dpFecha.getValue();
        Especialista origen = cmbOrigen.getValue();
        Especialista destino = cmbDestino.getValue();

        if (fecha == null || origen == null || destino == null) {
            mostrarAdvertencia(
                    "Completa la fecha y selecciona ambos especialistas.");
            return;
        }

        if (fecha.isBefore(LocalDate.now())) {
            mostrarAdvertencia(
                    "No puedes registrar una permuta con fecha pasada.");
            return;
        }

        if (origen.getIdEspecialista()
                == destino.getIdEspecialista()) {
            mostrarAdvertencia(
                    "Debes seleccionar dos especialistas diferentes.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar permuta");
        confirmacion.setHeaderText("Registrar solicitud de permuta");
        confirmacion.setContentText(
                "Fecha: " + fecha.format(FORMATO)
                + "\nOrigen: " + origen.getNombre()
                + "\nDestino: " + destino.getNombre()
                + "\n\nEl estado inicial será Pendiente.");

        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL)
                != ButtonType.OK) {
            return;
        }

        try {
            Permuta permuta = new Permuta();
            permuta.setFecha(fecha);
            permuta.setIdEspecialistaOrigen(
                    origen.getIdEspecialista());
            permuta.setIdEspecialistaDestino(
                    destino.getIdEspecialista());

            permutaDAO.insertar(permuta);

            mostrarInformacion(
                    "La permuta se registró correctamente.");

            dpFecha.setValue(null);
            cmbOrigen.setValue(null);
            cmbDestino.setValue(null);

            cargarPermutas();

        } catch (SQLException ex) {
            mostrarError("No se pudo registrar la permuta: "
                    + ex.getMessage());
        }
    }

    private static void actualizarEstado() {

        Permuta seleccionada =
                tabla.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAdvertencia(
                    "Selecciona una permuta de la tabla.");
            return;
        }

        String nuevoEstado = cmbEstado.getValue();

        if (nuevoEstado == null) {
            mostrarAdvertencia(
                    "Selecciona el nuevo estado.");
            return;
        }

        if (nuevoEstado.equals(seleccionada.getEstado())) {
            mostrarInformacion(
                    "La permuta ya tiene ese estado.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Actualizar permuta");
        confirmacion.setHeaderText("Confirmar cambio de estado");
        confirmacion.setContentText(
                "Permuta: " + seleccionada.getIdPermuta()
                + "\nEstado actual: " + seleccionada.getEstado()
                + "\nNuevo estado: " + nuevoEstado);

        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL)
                != ButtonType.OK) {
            return;
        }

        try {
            permutaDAO.actualizarEstado(
                    seleccionada.getIdPermuta(), nuevoEstado);

            mostrarInformacion(
                    "El estado de la permuta se actualizó correctamente.");

            cargarPermutas();

        } catch (SQLException ex) {
            mostrarError("No se pudo actualizar el estado: "
                    + ex.getMessage());
        }
    }

    private static void mostrarInformacion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Bella Imagen");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private static void mostrarAdvertencia(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Revisar información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private static void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
