package com.bellaimagen.vista;

import com.bellaimagen.dao.CitaDAO;
import com.bellaimagen.modelo.Cita;

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

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AgendaView {

    private static final CitaDAO citaDAO = new CitaDAO();

    private static TableView<Cita> tabla;
    private static ComboBox<String> cmbEstado;
    private static Label lblSeleccion;
    private static Label lblTotal;

    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void mostrar() {

        Stage ventana = new Stage();
        ventana.setTitle("Bella Imagen - Agenda de Citas");

        BorderPane principal = new BorderPane();
        principal.setStyle("-fx-background-color: #FAF8F5;");

        Label titulo = new Label("Agenda de Citas");
        titulo.setFont(Font.font("Arial", 27));
        titulo.setTextFill(Color.web("#581825"));

        Label subtitulo = new Label(
                "Consulta y administra las citas registradas"
        );
        subtitulo.setTextFill(Color.web("#777777"));

        VBox encabezado = new VBox(8, titulo, subtitulo);
        encabezado.setPadding(new Insets(25, 30, 20, 30));

        principal.setTop(encabezado);

        tabla = new TableView<>();
        tabla.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        TableColumn<Cita, Integer> colId =
                new TableColumn<>("ID");

        colId.setCellValueFactory(
                new PropertyValueFactory<>("idCita")
        );
        colId.setMinWidth(55);

        TableColumn<Cita, String> colFecha =
                new TableColumn<>("Fecha y hora");

        colFecha.setCellValueFactory(c -> {
            LocalDateTime fecha = c.getValue().getFechaHoraInicio();

            return new javafx.beans.property.SimpleStringProperty(
                    fecha == null ? "" : fecha.format(FORMATO)
            );
        });

        TableColumn<Cita, String> colFin =
                new TableColumn<>("Fin");

        colFin.setCellValueFactory(c -> {
            LocalDateTime fecha = c.getValue().getFechaHoraFin();

            return new javafx.beans.property.SimpleStringProperty(
                    fecha == null ? "" :
                    fecha.format(
                            DateTimeFormatter.ofPattern("HH:mm")
                    )
            );
        });

        TableColumn<Cita, String> colCliente =
                new TableColumn<>("Cliente");

        colCliente.setCellValueFactory(
                new PropertyValueFactory<>("nombreCliente")
        );

        TableColumn<Cita, String> colEspecialista =
                new TableColumn<>("Especialista");

        colEspecialista.setCellValueFactory(
                new PropertyValueFactory<>("nombreEspecialista")
        );

        TableColumn<Cita, String> colServicios =
                new TableColumn<>("Servicios");

        colServicios.setCellValueFactory(
                new PropertyValueFactory<>("nombresServicios")
        );
        colServicios.setMinWidth(180);

        TableColumn<Cita, String> colEstado =
                new TableColumn<>("Estado");

        colEstado.setCellValueFactory(
                new PropertyValueFactory<>("estado")
        );

        tabla.getColumns().addAll(
                colId,
                colFecha,
                colFin,
                colCliente,
                colEspecialista,
                colServicios,
                colEstado
        );

        tabla.setPlaceholder(
                new Label("No hay citas registradas.")
        );

        tabla.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, anterior, seleccionada) -> {

                    if (seleccionada != null) {

                        lblSeleccion.setText(
                                "Cita seleccionada: #"
                                + seleccionada.getIdCita()
                                + " - "
                                + seleccionada.getNombreCliente()
                        );

                        cmbEstado.setValue(
                                seleccionada.getEstado()
                        );

                    } else {

                        lblSeleccion.setText(
                                "Selecciona una cita de la tabla."
                        );
                    }
                });

        VBox panelTabla = new VBox(12, tabla);
        panelTabla.setPadding(new Insets(0, 30, 15, 30));
        VBox.setVgrow(tabla, Priority.ALWAYS);

        Button btnActualizar = new Button("Actualizar agenda");
        Button btnCambiarEstado = new Button("Guardar estado");

        String estiloBoton =
                "-fx-background-color: #581825;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 13px;"
                + "-fx-padding: 10 18;"
                + "-fx-background-radius: 6;";

        btnActualizar.setStyle(estiloBoton);
        btnCambiarEstado.setStyle(estiloBoton);

        cmbEstado = new ComboBox<>(
                FXCollections.observableArrayList(
                        "Programada",
                        "En Atención",
                        "Concluida"
                )
        );

        cmbEstado.setPromptText("Selecciona estado");

        lblSeleccion = new Label(
                "Selecciona una cita de la tabla."
        );

        lblSeleccion.setTextFill(Color.web("#581825"));

        lblTotal = new Label("Total de citas: 0");
        lblTotal.setFont(Font.font("Arial", 14));
        lblTotal.setTextFill(Color.web("#581825"));

        HBox controles = new HBox(
                12,
                new Label("Nuevo estado:"),
                cmbEstado,
                btnCambiarEstado,
                btnActualizar
        );

        controles.setAlignment(Pos.CENTER_LEFT);

        VBox pie = new VBox(
                12,
                lblSeleccion,
                controles,
                lblTotal
        );

        pie.setPadding(new Insets(15, 30, 25, 30));
        pie.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: #E8DDD8;"
                + "-fx-border-width: 1 0 0 0;"
        );

        principal.setCenter(panelTabla);
        principal.setBottom(pie);

        btnActualizar.setOnAction(e -> cargarCitas());

        btnCambiarEstado.setOnAction(
                e -> cambiarEstado()
        );

        cargarCitas();

        Scene escena = new Scene(principal, 1250, 720);

        ventana.setScene(escena);
        ventana.show();
    }

    private static void cargarCitas() {

        try {

            List<Cita> citas = citaDAO.listarTodas();

            tabla.setItems(
                    FXCollections.observableArrayList(citas)
            );

            lblTotal.setText(
                    "Total de citas: " + citas.size()
            );

            lblSeleccion.setText(
                    "Selecciona una cita de la tabla."
            );

            cmbEstado.getSelectionModel().clearSelection();

        } catch (SQLException ex) {

            mostrarError(
                    "No se pudieron cargar las citas.",
                    ex
            );
        }
    }

    private static void cambiarEstado() {

        Cita seleccionada = tabla.getSelectionModel()
                .getSelectedItem();

        if (seleccionada == null) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Selecciona una cita",
                    "Debes seleccionar una cita de la tabla."
            );

            return;
        }

        String nuevoEstado = cmbEstado.getValue();

        if (nuevoEstado == null) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Selecciona un estado",
                    "Elige el nuevo estado de la cita."
            );

            return;
        }

        if (nuevoEstado.equals(seleccionada.getEstado())) {

            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "Sin cambios",
                    "La cita ya tiene ese estado."
            );

            return;
        }

        Alert confirmacion = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        confirmacion.setTitle("Confirmar cambio");
        confirmacion.setHeaderText("Actualizar estado de la cita");
        confirmacion.setContentText(
                "Cita #" + seleccionada.getIdCita()
                + "\nEstado actual: " + seleccionada.getEstado()
                + "\nNuevo estado: " + nuevoEstado
        );

        if (confirmacion.showAndWait().orElse(
                ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        try {

            boolean actualizado = citaDAO.actualizarEstado(
                    seleccionada.getIdCita(),
                    nuevoEstado
            );

            if (actualizado) {

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Estado actualizado",
                        "El estado de la cita se modificó correctamente."
                );

                cargarCitas();

            } else {

                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Sin cambios",
                        "No se encontró la cita seleccionada."
                );
            }

        } catch (SQLException ex) {

            mostrarError(
                    "No se pudo actualizar el estado.",
                    ex
            );
        }
    }

    private static void mostrarAlerta(
            Alert.AlertType tipo,
            String titulo,
            String mensaje) {

        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private static void mostrarError(
            String mensaje,
            Exception ex) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(mensaje);
        alerta.setContentText(ex.getMessage());
        alerta.showAndWait();

        ex.printStackTrace();
    }
}