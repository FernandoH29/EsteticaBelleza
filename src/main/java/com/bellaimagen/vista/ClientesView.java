package com.bellaimagen.vista;

import com.bellaimagen.dao.ClienteDAO;
import com.bellaimagen.modelo.Cliente;

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

public class ClientesView {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    private final TableView<Cliente> tabla = new TableView<>();
    private final ObservableList<Cliente> datos = FXCollections.observableArrayList();

    private final TextField txtNombre = new TextField();
    private final TextField txtTelefono = new TextField();
    private final TextField txtEmail = new TextField();

    private final Label lblEstado = new Label("Sistema conectado a Railway");

    private Cliente clienteSeleccionado;

    public static void mostrar() {
        ClientesView vista = new ClientesView();
        vista.abrirVentana();
    }

    private void abrirVentana() {

        Stage ventana = new Stage();
        ventana.setTitle("Bella Imagen - Gestion de Clientes");

        BorderPane principal = new BorderPane();
        principal.setStyle("-fx-background-color: #FAF8F5;");

        principal.setTop(crearEncabezado());
        principal.setCenter(crearContenido());
        principal.setBottom(crearPie());

        cargarClientes();

        Scene escena = new Scene(principal, 1050, 680);
        ventana.setScene(escena);
        ventana.setMinWidth(900);
        ventana.setMinHeight(600);
        ventana.show();
    }

    private VBox crearEncabezado() {

        Label titulo = new Label("Gestion de Clientes");
        titulo.setStyle(
            "-fx-font-size: 27px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #581825;"
        );

        Label subtitulo = new Label(
            "Administra la informacion de tus clientes"
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

        Label tituloFormulario = new Label("Datos del cliente");
        tituloFormulario.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #581825;"
        );

        GridPane formulario = new GridPane();
        formulario.setHgap(15);
        formulario.setVgap(12);

        configurarCampo(txtNombre, "Nombre completo");
        configurarCampo(txtTelefono, "Numero de telefono");
        configurarCampo(txtEmail, "Correo electronico");

        formulario.add(crearEtiqueta("Nombre:"), 0, 0);
        formulario.add(txtNombre, 1, 0);

        formulario.add(crearEtiqueta("Telefono:"), 2, 0);
        formulario.add(txtTelefono, 3, 0);

        formulario.add(crearEtiqueta("Correo:"), 0, 1);
        formulario.add(txtEmail, 1, 1, 3, 1);

        Button btnRegistrar = crearBoton("Registrar", "#581825");
        Button btnActualizar = crearBoton("Actualizar", "#9A596B");
        Button btnEliminar = crearBoton("Eliminar", "#B65C52");
        Button btnLimpiar = crearBoton("Limpiar", "#777777");

        btnRegistrar.setOnAction(e -> registrarCliente());
        btnActualizar.setOnAction(e -> actualizarCliente());
        btnEliminar.setOnAction(e -> eliminarCliente());
        btnLimpiar.setOnAction(e -> limpiarFormulario());

        HBox botones = new HBox(12,
            btnRegistrar, btnActualizar, btnEliminar, btnLimpiar
        );
        botones.setAlignment(Pos.CENTER_LEFT);

        Label tituloTabla = new Label("Lista de clientes");
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
        campo.setPrefWidth(190);
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

        TableColumn<Cliente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(
            new PropertyValueFactory<>("idCliente")
        );
        colId.setPrefWidth(70);

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(
            new PropertyValueFactory<>("nombre")
        );
        colNombre.setPrefWidth(280);

        TableColumn<Cliente, String> colTelefono = new TableColumn<>("Telefono");
        colTelefono.setCellValueFactory(
            new PropertyValueFactory<>("telefono")
        );
        colTelefono.setPrefWidth(180);

        TableColumn<Cliente, String> colEmail = new TableColumn<>("Correo electronico");
        colEmail.setCellValueFactory(
            new PropertyValueFactory<>("email")
        );
        colEmail.setPrefWidth(300);

        tabla.getColumns().addAll(
            colId, colNombre, colTelefono, colEmail
        );

        tabla.setItems(datos);
        tabla.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY
        );

        tabla.getSelectionModel().selectedItemProperty().addListener(
            (observable, anterior, seleccionado) -> {
                if (seleccionado != null) {
                    clienteSeleccionado = seleccionado;
                    txtNombre.setText(seleccionado.getNombre());
                    txtTelefono.setText(seleccionado.getTelefono());
                    txtEmail.setText(seleccionado.getEmail());
                }
            }
        );
    }

    private void cargarClientes() {

        try {
            List<Cliente> clientes = clienteDAO.listarTodos();
            datos.setAll(clientes);
            lblEstado.setText(
                "Clientes registrados: " + clientes.size()
                + " | Datos actualizados desde Railway"
            );

        } catch (SQLException e) {
            mostrarError("No se pudieron cargar los clientes.", e);
        }
    }

    private void registrarCliente() {

        if (!validarFormulario()) {
            return;
        }

        Cliente cliente = new Cliente(
            txtNombre.getText().trim(),
            txtTelefono.getText().trim(),
            obtenerEmail()
        );

        try {
            clienteDAO.insertar(cliente);
            mostrarInformacion("Cliente registrado correctamente.");
            limpiarFormulario();
            cargarClientes();

        } catch (SQLException e) {
            mostrarError("No se pudo registrar el cliente.", e);
        }
    }

    private void actualizarCliente() {

        if (clienteSeleccionado == null) {
            mostrarAdvertencia("Selecciona un cliente de la tabla.");
            return;
        }

        if (!validarFormulario()) {
            return;
        }

        Cliente cliente = new Cliente(
            clienteSeleccionado.getIdCliente(),
            txtNombre.getText().trim(),
            txtTelefono.getText().trim(),
            obtenerEmail()
        );

        try {
            clienteDAO.actualizar(cliente);
            mostrarInformacion("Cliente actualizado correctamente.");
            limpiarFormulario();
            cargarClientes();

        } catch (SQLException e) {
            mostrarError("No se pudo actualizar el cliente.", e);
        }
    }

    private void eliminarCliente() {

        if (clienteSeleccionado == null) {
            mostrarAdvertencia("Selecciona un cliente de la tabla.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminacion");
        confirmacion.setHeaderText("Eliminar cliente");
        confirmacion.setContentText(
            "¿Deseas eliminar a " + clienteSeleccionado.getNombre() + "?"
        );

        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL)
                == ButtonType.OK) {

            try {
                clienteDAO.eliminar(clienteSeleccionado.getIdCliente());
                mostrarInformacion("Cliente eliminado correctamente.");
                limpiarFormulario();
                cargarClientes();

            } catch (SQLException e) {
                mostrarError(
                    "No se pudo eliminar el cliente. Puede tener citas relacionadas.",
                    e
                );
            }
        }
    }

    private boolean validarFormulario() {

        if (txtNombre.getText().trim().isEmpty()) {
            mostrarAdvertencia("El nombre es obligatorio.");
            txtNombre.requestFocus();
            return false;
        }

        if (txtTelefono.getText().trim().isEmpty()) {
            mostrarAdvertencia("El telefono es obligatorio.");
            txtTelefono.requestFocus();
            return false;
        }

        return true;
    }

    private String obtenerEmail() {
        String email = txtEmail.getText().trim();
        return email.isEmpty() ? null : email;
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtTelefono.clear();
        txtEmail.clear();
        clienteSeleccionado = null;
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