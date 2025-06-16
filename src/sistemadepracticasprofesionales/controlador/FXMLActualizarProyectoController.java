package sistemadepracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistemadepracticasprofesionales.SistemaDePracticasProfesionales;
import sistemadepracticasprofesionales.modelo.dao.OrganizacionVinculadaDAO;
import sistemadepracticasprofesionales.modelo.dao.ProyectoDAO;
import sistemadepracticasprofesionales.modelo.dao.ResponsableProyectoDAO;
import sistemadepracticasprofesionales.modelo.enums.EstadoProyecto;
import sistemadepracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemadepracticasprofesionales.modelo.pojo.Proyecto;
import sistemadepracticasprofesionales.modelo.pojo.ResponsableProyecto;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemadepracticasprofesionales.utilidades.Utilidad;
import sistemadepracticasprofesionales.utilidades.validacion.ValidadorFormulario;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.ComboValidationStrategy;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.NumericValidationStrategy;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.TextValidationStrategy;

/**
 * FXML Controller class
 *
 * @author meler
 * Fecha:12/06/25
 * Descripcion: Gestiona las interacciones entre su vista y los DAO de Proyecto, OrganizacionVinculada y ResponsableProyecto para poder actualizar 
 * los datos de un Proyecto en el formulario mostrado en la vista
 */
public class FXMLActualizarProyectoController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfDescripcion;
    @FXML
    private ComboBox<EstadoProyecto> cbEstado;
    @FXML
    private TextField tfCupo;
    @FXML
    private ComboBox<OrganizacionVinculada> cbOrganizacionVinculada;
    @FXML
    private ComboBox<ResponsableProyecto> cbResponsableProyecto;
    @FXML
    private DatePicker dpFechaInicio;
    @FXML
    private DatePicker dpFechaFin;

    private Proyecto proyectoActual;
    private ValidadorFormulario validador = new ValidadorFormulario();
    private ObservableList<OrganizacionVinculada> listaOrganizaciones;
    private ObservableList<ResponsableProyecto> listaResponsables;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarOrganizaciones();
        cargarEstados();
        configurarCambioOrganizacion();
        
        validador.addValidation(tfNombre, new TextValidationStrategy(45, true));
        validador.addValidation(tfDescripcion, new TextValidationStrategy(255, true)); // o lo que tengas
        validador.addValidation(tfCupo, new NumericValidationStrategy(true));
        validador.addValidation(cbEstado, new ComboValidationStrategy(true));
        validador.addValidation(cbOrganizacionVinculada, new ComboValidationStrategy(true));
        validador.addValidation(cbResponsableProyecto, new ComboValidationStrategy(true));

    }

    public void inicializarFormulario(Proyecto proyecto) {
        this.proyectoActual = proyecto;

        tfNombre.setText(proyecto.getNombre());
        tfDescripcion.setText(proyecto.getDescripcion());
        tfCupo.setText(String.valueOf(proyecto.getCupo()));
        if (proyecto.getFechaInicio() != null && !proyecto.getFechaInicio().trim().isEmpty()) {
            dpFechaInicio.setValue(LocalDate.parse(proyecto.getFechaInicio()));
        }

        if (proyecto.getFechaFin() != null && !proyecto.getFechaFin().trim().isEmpty()) {
            dpFechaFin.setValue(LocalDate.parse(proyecto.getFechaFin()));
        }

        cbEstado.setValue(proyecto.getEstado());
        cbOrganizacionVinculada.getSelectionModel().select(
                listaOrganizaciones.stream()
                    .filter(org -> org.getId() == proyecto.getIdOrganizacionVinculada())
                    .findFirst().orElse(null)
        );
    }

    private void cargarOrganizaciones() {
        try {
            List<OrganizacionVinculada> organizaciones = OrganizacionVinculadaDAO.obtenerOrganizacionesVinculadas();
            listaOrganizaciones = FXCollections.observableArrayList(organizaciones);
            cbOrganizacionVinculada.setItems(listaOrganizaciones);
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(javafx.scene.control.Alert.AlertType.ERROR,
                    "Error al cargar organizaciones",
                    "No fue posible obtener las organizaciones vinculadas.");
        }
    }
    
    private void cargarEstados() {
        cbEstado.setItems(FXCollections.observableArrayList(EstadoProyecto.values()));
    }

    private void configurarCambioOrganizacion() {
        cbOrganizacionVinculada.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    List<ResponsableProyecto> responsables = ResponsableProyectoDAO
                            .obtenerResponsablesPorOrganizacion(newVal.getId());
                    listaResponsables = FXCollections.observableArrayList(responsables);
                    cbResponsableProyecto.setItems(listaResponsables);

                    // Seleccionar el responsable si coincide
                    if (proyectoActual != null) {
                        cbResponsableProyecto.getSelectionModel().select(
                            listaResponsables.stream()
                                .filter(r -> r.getIdResponsable() == proyectoActual.getIdResponsableProyecto())
                                .findFirst().orElse(null)
                        );
                    }
                } catch (SQLException e) {
                    Utilidad.mostrarAlertaSimple(javafx.scene.control.Alert.AlertType.ERROR,
                            "Error al cargar responsables",
                            "No fue posible obtener los responsables del proyecto.");
                }
            } else {
                cbResponsableProyecto.getItems().clear();
            }
        });
    }
    
    private boolean validarCampos() {
        boolean camposValidos = validador.validate(); // Validador ya aplica a los campos registrados

        if (dpFechaInicio.getValue() == null) {
            dpFechaInicio.setStyle("-fx-border-color: red; -fx-background-color: #FFEEEE;");
            camposValidos = false;
        } else {
            dpFechaInicio.setStyle("");
        }

        if (dpFechaFin.getValue() == null) {
            dpFechaFin.setStyle("-fx-border-color: red; -fx-background-color: #FFEEEE;");
            camposValidos = false;
        } else if (dpFechaFin.getValue().isBefore(dpFechaInicio.getValue())) {
            dpFechaFin.setStyle("-fx-border-color: red; -fx-background-color: #FFEEEE;");
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING,
                "Fechas inconsistentes",
                "La fecha de fin no puede ser anterior a la fecha de inicio.");
            return false;
        } else {
            dpFechaFin.setStyle("");
        }

        if (!camposValidos) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING,
                "Campos inválidos",
                "Verifica que todos los campos estén completos y correctos.");
        }

        return camposValidos;
    }


    @FXML
    private void btnClicRegresar(ActionEvent event) {
        Utilidad.abrirVentana("BuscarProyecto", tfNombre);
    }

    @FXML
    private void btnClicAceptar(ActionEvent event) {
        try {
            if (validarCampos()) {
                Proyecto proyectoActualizado = construirProyectoDesdeFormulario();
                abrirVentanaConfirmacion(proyectoActualizado);
            }
        } catch (IllegalArgumentException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Error en el formulario", "Hay un error en los datos ingresados");
        }
    }


    
    private Proyecto construirProyectoDesdeFormulario() {
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(tfNombre.getText());
        proyecto.setDescripcion(tfDescripcion.getText());
        proyecto.setEstado(cbEstado.getValue());
        proyecto.setCupo(Integer.parseInt(tfCupo.getText()));
        proyecto.setFechaInicio(dpFechaInicio.getValue().toString());
        proyecto.setFechaFin(dpFechaFin.getValue().toString());

        OrganizacionVinculada org = cbOrganizacionVinculada.getSelectionModel().getSelectedItem();
        proyecto.setIdOrganizacionVinculada(org.getId());
        proyecto.setNombreOrganizacionVinculada(org.getRazonSocial());

        ResponsableProyecto resp = cbResponsableProyecto.getSelectionModel().getSelectedItem();
        proyecto.setIdResponsableProyecto(resp.getIdResponsable());
        proyecto.setNombreResponsableProyecto(resp.getNombre() + " " + resp.getApellidoPaterno());
        
        proyecto.setIdProyecto(proyectoActual.getIdProyecto());

        return proyecto;
    }
    
    private void abrirVentanaConfirmacion(Proyecto proyecto) {
        try {
            FXMLLoader loader = new FXMLLoader(SistemaDePracticasProfesionales.class.getResource(
                    "/sistemadepracticasprofesionales/vista/FXMLDatosActualizados.fxml"));
            Parent vista = loader.load();

            FXMLDatosActualizadosController controlador = loader.getController();
            controlador.inicializarProyecto(proyecto);
            controlador.setEscenarioPadre((Stage) tfNombre.getScene().getWindow());

            

            Scene escena = new Scene(vista);
            Stage escenarioConfirmacion = new Stage();
            escenarioConfirmacion.setScene(escena);
            escenarioConfirmacion.setTitle("Confirmar Actualización");
            escenarioConfirmacion.show();

            // Puedes cerrar la ventana actual si quieres:
            // Utilidad.obtenerEscenario(tfNombre).close();

        } catch (IOException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR,
                    "Error al mostrar confirmación", "No fue posible cargar la ventana de confirmación.");
        }
    }



    @FXML
    private void btnClicCancelar(ActionEvent event) {
        Alert alerta = Utilidad.mostrarAlertaConfirmacion("Confirmacion Cancelar", 
                "¿Está seguro de que desea cancelar?");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if(resultado.get() == ButtonType.APPLY){
            Utilidad.abrirVentana("Coordinador", tfDescripcion);                    
        }
    }
    
}