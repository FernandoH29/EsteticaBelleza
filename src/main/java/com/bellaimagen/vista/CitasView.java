package com.bellaimagen.vista;

import com.bellaimagen.dao.CitaDAO;
import com.bellaimagen.dao.ClienteDAO;
import com.bellaimagen.dao.EspecialistaDAO;
import com.bellaimagen.dao.ServicioDAO;

import com.bellaimagen.modelo.Cita;
import com.bellaimagen.modelo.Cliente;
import com.bellaimagen.modelo.Especialista;
import com.bellaimagen.modelo.Servicio;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CitasView {

    private static final CitaDAO citaDAO = new CitaDAO();
    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final EspecialistaDAO especialistaDAO = new EspecialistaDAO();
    private static final ServicioDAO servicioDAO = new ServicioDAO();

    private static TextField txtNombreCliente;
    private static TextField txtTelefonoCliente;
    private static TextField txtEmailCliente;

    private static ComboBox<Especialista> cmbEspecialista;
    private static DatePicker fechaCita;
    private static TextField txtHora;

    private static VBox contenedorServicios;
    private static Label lblResumen;
    private static Label lblDisponibilidad;

    private static final Map<CheckBox, Servicio> serviciosSeleccionables =
            new HashMap<>();

    public static void mostrar() {

        Stage ventana = new Stage();
        ventana.setTitle("Bella Imagen - Agendar Cita");

        BorderPane principal = new BorderPane();
        principal.setStyle("-fx-background-color: #FAF8F5;");

        Label titulo = new Label("Agenda tu cita");
        titulo.setFont(Font.font("Arial", 26));
        titulo.setTextFill(Color.web("#581825"));

        Label subtitulo = new Label(
                "Completa tus datos y selecciona los servicios que deseas."
        );
        subtitulo.setTextFill(Color.web("#777777"));

        VBox encabezado = new VBox(8, titulo, subtitulo);
        encabezado.setPadding(new Insets(25, 30, 15, 30));

        principal.setTop(encabezado);

        VBox contenido = new VBox(18);
        contenido.setPadding(new Insets(10, 30, 25, 30));

        // Datos del cliente
        Label lblDatosCliente = new Label("Tus datos");
        lblDatosCliente.setFont(Font.font("Arial", 17));
        lblDatosCliente.setTextFill(Color.web("#581825"));

        txtNombreCliente = new TextField();
        txtNombreCliente.setPromptText("Nombre completo");

        txtTelefonoCliente = new TextField();
        txtTelefonoCliente.setPromptText("Teléfono");

        txtEmailCliente = new TextField();
        txtEmailCliente.setPromptText("Correo electrónico (opcional)");

        // Especialista
        Label lblEspecialista = new Label("Especialista");
        cmbEspecialista = new ComboBox<>();
        cmbEspecialista.setPromptText("Selecciona un especialista");
        cmbEspecialista.setMaxWidth(Double.MAX_VALUE);

        cmbEspecialista.setConverter(new StringConverter<>() {
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
        });

        // Fecha y hora
        Label lblFecha = new Label("Fecha");
        fechaCita = new DatePicker(LocalDate.now());
        fechaCita.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate fecha, boolean vacio) {
                super.updateItem(fecha, vacio);
                setDisable(vacio || fecha.isBefore(LocalDate.now()));
            }
        });
        fechaCita.setMaxWidth(Double.MAX_VALUE);

        Label lblHora = new Label("Hora de inicio");
        txtHora = new TextField();
        txtHora.setPromptText("Ejemplo: 10:00");

        GridPane datos = new GridPane();
        datos.setHgap(15);
        datos.setVgap(10);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);

        datos.getColumnConstraints().addAll(col1, col2);

        datos.add(lblDatosCliente, 0, 0, 2, 1);

        datos.add(txtNombreCliente, 0, 1);
        datos.add(txtTelefonoCliente, 1, 1);

        datos.add(txtEmailCliente, 0, 2, 2, 1);

        datos.add(lblEspecialista, 0, 3);
        datos.add(cmbEspecialista, 0, 4, 2, 1);

        datos.add(lblFecha, 0, 5);
        datos.add(lblHora, 1, 5);

        datos.add(fechaCita, 0, 6);
        datos.add(txtHora, 1, 6);

        // Servicios disponibles
        Label lblServicios = new Label("Selecciona los servicios");
        lblServicios.setFont(Font.font("Arial", 17));
        lblServicios.setTextFill(Color.web("#581825"));

        contenedorServicios = new VBox(10);
        contenedorServicios.setPadding(new Insets(12));

        ScrollPane scrollServicios = new ScrollPane(contenedorServicios);
        scrollServicios.setFitToWidth(true);
        scrollServicios.setPrefHeight(180);
        scrollServicios.setStyle(
                "-fx-background: white;"
                + "-fx-background-color: white;"
        );

        // Resumen
        lblResumen = new Label(
                "Selecciona uno o varios servicios para calcular el total."
        );
        lblResumen.setWrapText(true);
        lblResumen.setFont(Font.font("Arial", 14));
        lblResumen.setTextFill(Color.web("#581825"));

        VBox panelResumen = new VBox(10, lblResumen);
        panelResumen.setPadding(new Insets(15));
        panelResumen.setStyle(
                "-fx-background-color: #E2ECE9;"
                + "-fx-background-radius: 8;"
        );

        // Disponibilidad
        lblDisponibilidad = new Label(
                "Completa tus datos y consulta la disponibilidad."
        );
        lblDisponibilidad.setWrapText(true);
        lblDisponibilidad.setTextFill(Color.web("#555555"));

        Button btnDisponibilidad = new Button("Consultar disponibilidad");
        Button btnConfirmar = new Button("Confirmar cita");
        Button btnLimpiar = new Button("Limpiar");

        String estiloPrincipal =
                "-fx-background-color: #581825;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 13px;"
                + "-fx-padding: 10 18;"
                + "-fx-background-radius: 6;";

        btnDisponibilidad.setStyle(estiloPrincipal);
        btnConfirmar.setStyle(estiloPrincipal);

        btnLimpiar.setStyle(
                "-fx-background-color: #E2ECE9;"
                + "-fx-text-fill: #333333;"
                + "-fx-font-size: 13px;"
                + "-fx-padding: 10 18;"
                + "-fx-background-radius: 6;"
        );

        HBox botones = new HBox(
                12, btnDisponibilidad, btnConfirmar, btnLimpiar
        );
        botones.setAlignment(Pos.CENTER_LEFT);

        VBox panelDatos = new VBox(12, datos);
        panelDatos.setPadding(new Insets(18));
        panelDatos.setStyle(
                "-fx-background-color: white;"
                + "-fx-background-radius: 8;"
                + "-fx-border-color: #E8DDD8;"
                + "-fx-border-radius: 8;"
        );

        VBox panelServicios = new VBox(
                12, lblServicios, scrollServicios
        );
        panelServicios.setPadding(new Insets(18));
        panelServicios.setStyle(
                "-fx-background-color: white;"
                + "-fx-background-radius: 8;"
                + "-fx-border-color: #E8DDD8;"
                + "-fx-border-radius: 8;"
        );

        contenido.getChildren().addAll(
                panelDatos,
                panelServicios,
                panelResumen,
                lblDisponibilidad,
                botones
        );

        ScrollPane scrollContenido = new ScrollPane(contenido);
        scrollContenido.setFitToWidth(true);
        scrollContenido.setStyle("-fx-background: #FAF8F5;");

        principal.setCenter(scrollContenido);

        btnDisponibilidad.setOnAction(e -> consultarDisponibilidad());
        btnConfirmar.setOnAction(e -> confirmarCita());
        btnLimpiar.setOnAction(e -> limpiar());

        cargarDatos();

        Scene escena = new Scene(principal, 1050, 850);
        ventana.setScene(escena);
        ventana.show();
    }

    private static void cargarDatos() {

        try {
            List<Especialista> activos = new ArrayList<>();

            for (Especialista especialista : especialistaDAO.listarTodos()) {
                if ("Activo".equalsIgnoreCase(especialista.getEstado())) {
                    activos.add(especialista);
                }
            }

            cmbEspecialista.setItems(
                    FXCollections.observableArrayList(activos)
            );

            serviciosSeleccionables.clear();
            contenedorServicios.getChildren().clear();

            List<Servicio> servicios = servicioDAO.listarTodos();

            for (Servicio servicio : servicios) {

                CheckBox check = new CheckBox(
                        servicio.getNombre()
                        + " | $"
                        + String.format("%.2f", servicio.getPrecio())
                        + " | "
                        + servicio.getDuracionEstimada()
                        + " min"
                );

                serviciosSeleccionables.put(check, servicio);

                check.selectedProperty().addListener(
                        (observable, anterior, seleccionado) ->
                                actualizarResumen()
                );

                contenedorServicios.getChildren().add(check);
            }

        } catch (SQLException ex) {
            mostrarError("No se pudieron cargar los datos.", ex);
        }
    }

    private static List<Servicio> obtenerServiciosSeleccionados() {

        List<Servicio> seleccionados = new ArrayList<>();

        for (Map.Entry<CheckBox, Servicio> entrada :
                serviciosSeleccionables.entrySet()) {

            if (entrada.getKey().isSelected()) {
                seleccionados.add(entrada.getValue());
            }
        }

        return seleccionados;
    }

    private static void actualizarResumen() {

        List<Servicio> seleccionados = obtenerServiciosSeleccionados();

        if (seleccionados.isEmpty()) {
            lblResumen.setText(
                    "Selecciona uno o varios servicios para calcular el total."
            );
            return;
        }

        int duracion = citaDAO.calcularDuracionTotal(seleccionados);
        double total = 0;

        for (Servicio servicio : seleccionados) {
            total += servicio.getPrecio();
        }

        int horas = duracion / 60;
        int minutos = duracion % 60;

        lblResumen.setText(
                "Servicios seleccionados: " + seleccionados.size()
                + "\nDuración total con buffers: "
                + horas + " h " + minutos + " min"
                + "\nPrecio total: $" + String.format("%.2f", total)
        );
    }

    private static LocalDateTime obtenerFechaHora() {

        if (fechaCita.getValue() == null) {
            throw new IllegalArgumentException("Selecciona una fecha.");
        }

        String hora = txtHora.getText().trim();

        if (hora.isEmpty()) {
            throw new IllegalArgumentException(
                    "Escribe la hora de inicio. Ejemplo: 10:00."
            );
        }

        LocalTime tiempo;

        try {
            tiempo = LocalTime.parse(
                    hora,
                    DateTimeFormatter.ofPattern("HH:mm")
            );
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "La hora debe tener formato HH:mm. Ejemplo: 10:00."
            );
        }

        LocalDateTime fechaHora = LocalDateTime.of(
                fechaCita.getValue(), tiempo
        );

        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "No puedes agendar una cita en una fecha u hora pasada."
            );
        }

        return fechaHora;
    }

    private static void validarFormulario() {

        String nombre = txtNombreCliente.getText().trim();
        String telefono = txtTelefonoCliente.getText().trim();
        String email = txtEmailCliente.getText().trim();

        if (nombre.isEmpty()) {
            throw new IllegalArgumentException(
                    "Escribe tu nombre completo."
            );
        }

        if (telefono.isEmpty()) {
            throw new IllegalArgumentException(
                    "Escribe tu número de teléfono."
            );
        }

        if (telefono.length() > 20) {
            throw new IllegalArgumentException(
                    "El teléfono no puede superar los 20 caracteres."
            );
        }

        if (!email.isEmpty()
                && !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException(
                    "Escribe un correo electrónico válido."
            );
        }

        if (cmbEspecialista.getValue() == null) {
            throw new IllegalArgumentException(
                    "Selecciona un especialista."
            );
        }

        if (obtenerServiciosSeleccionados().isEmpty()) {
            throw new IllegalArgumentException(
                    "Selecciona al menos un servicio."
            );
        }

        obtenerFechaHora();
    }

    private static Cliente obtenerOCrearCliente() throws SQLException {

        String nombre = txtNombreCliente.getText().trim();
        String telefono = txtTelefonoCliente.getText().trim();
        String email = txtEmailCliente.getText().trim();

        if (email.isEmpty()) {
            email = null;
        }

        Cliente existente = clienteDAO.buscarPorTelefonoOCorreo(
                telefono, email
        );

        if (existente != null) {
            return existente;
        }

        Cliente nuevo = new Cliente(nombre, telefono, email);

        return clienteDAO.insertarYObtener(nuevo);
    }

    private static boolean horarioOcupado(
            Especialista especialista,
            LocalDateTime inicio,
            List<Servicio> servicios) throws SQLException {

        LocalDateTime fin = citaDAO.calcularFechaHoraFin(
                inicio, servicios
        );

        return citaDAO.hayConflicto(
                especialista.getIdEspecialista(),
                inicio,
                fin
        );
    }

    private static void consultarDisponibilidad() {

        try {
            validarFormulario();

            Especialista especialista = cmbEspecialista.getValue();
            List<Servicio> seleccionados = obtenerServiciosSeleccionados();
            LocalDateTime inicio = obtenerFechaHora();

            LocalDateTime fin = citaDAO.calcularFechaHoraFin(
                    inicio, seleccionados
            );

            boolean ocupado = citaDAO.hayConflicto(
                    especialista.getIdEspecialista(),
                    inicio,
                    fin
            );

            if (ocupado) {

                lblDisponibilidad.setText(
                        "HORARIO NO DISPONIBLE. El especialista tiene "
                        + "otra cita que se cruza con este horario."
                );

                lblDisponibilidad.setTextFill(Color.web("#A94442"));

                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Conflicto de horario",
                        "El especialista ya tiene una cita en ese intervalo."
                );

            } else {

                lblDisponibilidad.setText(
                        "HORARIO DISPONIBLE. Inicio: "
                        + inicio.format(
                                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                        )
                        + " | Fin estimado: "
                        + fin.format(DateTimeFormatter.ofPattern("HH:mm"))
                );

                lblDisponibilidad.setTextFill(Color.web("#287A52"));

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Horario disponible",
                        "El especialista está disponible para ese horario."
                );
            }

        } catch (IllegalArgumentException ex) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Revisa los datos",
                    ex.getMessage()
            );

        } catch (SQLException ex) {

            mostrarError(
                    "No se pudo consultar la disponibilidad.", ex
            );
        }
    }

    private static void confirmarCita() {

        try {
            validarFormulario();

            Especialista especialista = cmbEspecialista.getValue();
            List<Servicio> seleccionados = obtenerServiciosSeleccionados();
            LocalDateTime inicio = obtenerFechaHora();

            // Verificar disponibilidad antes de crear un cliente nuevo.
            if (horarioOcupado(especialista, inicio, seleccionados)) {

                lblDisponibilidad.setText(
                        "HORARIO NO DISPONIBLE. Selecciona otra hora."
                );
                lblDisponibilidad.setTextFill(Color.web("#A94442"));

                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Conflicto de horario",
                        "El especialista ya tiene una cita en ese horario. "
                        + "Consulta otra hora disponible."
                );
                return;
            }

            // Buscar al cliente o registrarlo automáticamente.
            Cliente cliente = obtenerOCrearCliente();

            Cita cita = new Cita(
                    inicio,
                    cliente.getIdCliente(),
                    especialista.getIdEspecialista(),
                    seleccionados
            );

            int idGenerado = citaDAO.registrar(cita);

            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "¡Cita registrada!",
                    "Tu cita #" + idGenerado
                    + " se registró correctamente."
                    + "\nCliente: " + cliente.getNombre()
                    + "\nEspecialista: " + especialista.getNombre()
                    + "\nFecha: "
                    + inicio.format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    )
                    + "\nFin estimado: "
                    + cita.getFechaHoraFin().format(
                            DateTimeFormatter.ofPattern("HH:mm")
                    )
            );

            limpiar();

        } catch (IllegalArgumentException ex) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Revisa los datos",
                    ex.getMessage()
            );

        } catch (SQLException ex) {

            if (ex.getMessage() != null
                    && ex.getMessage().contains("CONFLICTO")) {

                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Conflicto de horario",
                        "El especialista ya tiene una cita en ese horario. "
                        + "Consulta otra hora disponible."
                );

                lblDisponibilidad.setText(
                        "HORARIO NO DISPONIBLE. Selecciona otra hora."
                );

                lblDisponibilidad.setTextFill(Color.web("#A94442"));

            } else {
                mostrarError("No se pudo registrar la cita.", ex);
            }
        }
    }

    private static void limpiar() {

        txtNombreCliente.clear();
        txtTelefonoCliente.clear();
        txtEmailCliente.clear();

        cmbEspecialista.getSelectionModel().clearSelection();

        fechaCita.setValue(LocalDate.now());
        txtHora.clear();

        for (CheckBox check : serviciosSeleccionables.keySet()) {
            check.setSelected(false);
        }

        lblDisponibilidad.setText(
                "Completa tus datos y consulta la disponibilidad."
        );

        lblDisponibilidad.setTextFill(Color.web("#555555"));

        actualizarResumen();
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

    private static void mostrarError(String mensaje, Exception ex) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(mensaje);
        alerta.setContentText(ex.getMessage());
        alerta.showAndWait();

        ex.printStackTrace();
    }
}