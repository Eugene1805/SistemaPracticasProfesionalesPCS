package sistemadepracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import sistemadepracticasprofesionales.modelo.pojo.Usuario;
import sistemadepracticasprofesionales.utilidades.Utilidad;
import sistemadepracticasprofesionales.utilidades.validacion.ValidadorFormulario;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.ComboValidationStrategy;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.NumericValidationStrategy;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.TextValidationStrategy;

/**
 * FXML Controller class
 *
 * @author meler
 * 
 * Fecha:12/06/25
 * Descripcion: Gestiona las interacciones entre su vista y los DAO de Proyecto, OrganizacionVinculada y ResponsableProyecto para poder hacer 
 * registros de un Proyecto en el formulario mostrado en la vista
 */

public class FXMLRegistrarProyectoController implements Initializable {

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

    private ObservableList<OrganizacionVinculada> listaOrganizaciones;
    private Usuario coordinador;
    private final ValidadorFormulario validador = new ValidadorFormulario();
    private ObservableList<ResponsableProyecto> listaResponsables;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarOrganizaciones();
        cargarResponsables();
        cargarEstados();
        
        validador.addValidation(tfNombre, new TextValidationStrategy(45, true));
        validador.addValidation(tfDescripcion, new TextValidationStrategy(255, true));
        validador.addValidation(cbEstado, new ComboValidationStrategy(true));//
        validador.addValidation(tfCupo, new NumericValidationStrategy(true));
        validador.addValidation(cbOrganizacionVinculada, new ComboValidationStrategy(true));
        validador.addValidation(cbResponsableProyecto, new ComboValidationStrategy(true));
    }
    
    private void cargarEstados() {
        cbEstado.setItems(FXCollections.observableArrayList(EstadoProyecto.values()));
    }
    
    private void cargarOrganizaciones() {
        try {
            List<OrganizacionVinculada> organizaciones = OrganizacionVinculadaDAO.obtenerOrganizacionesVinculadas();
            listaOrganizaciones = FXCollections.observableArrayList(organizaciones);
            cbOrganizacionVinculada.setItems(listaOrganizaciones);
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar organizaciones", e.getMessage());
        }
    }

    private void cargarResponsables() {
        cbOrganizacionVinculada.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    List<ResponsableProyecto> responsables = ResponsableProyectoDAO
                            .obtenerResponsablesPorOrganizacion(newVal.getId());
                    listaResponsables = FXCollections.observableArrayList(responsables);
                    cbResponsableProyecto.setItems(listaResponsables);
                    cbResponsableProyecto.getSelectionModel().clearSelection();
                } catch (SQLException e) {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar responsables", e.getMessage());
                }
            } else {
                cbResponsableProyecto.getItems().clear();
            }
        });
    }
    
    public void inicializar(Usuario usuario){
        this.coordinador = usuario;
    }

    @FXML
    private void btnClicRegresar(ActionEvent event) {
         try {
            Stage escenarioBase = Utilidad.obtenerEscenario(tfNombre);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                    getResource("vista/FXMLCoordinador.fxml"));
            Parent vista = cargador.load();
            FXMLCoordinadorController controlador = cargador.getController();
            controlador.inicializar(coordinador);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Dasboard Coordinador");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error al cargar el dashboard del coordinador",
                    "Lo sentimos no fue posible cargar la informacion del coordinador");
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

    @FXML
    private void btnClicAceptar(ActionEvent event) {
        if (validarCampos()) {
            Proyecto proyecto = construirProyectoDesdeFormulario();
            try {
                ResultadoOperacion resultado = ProyectoDAO.registrarProyecto(proyecto);
                if (!resultado.isError()) {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Registro exitoso", "Operación exitosa.");
                    limpiarFormulario();
                } else {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", resultado.getMensaje());
                }
            } catch (SQLException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de base de datos", "No hay conexión.");
            }
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Campos inválidos", "Hay campos con datos inválidos.");
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

        return proyecto;
    }
    
    private boolean validarCampos() {
        boolean camposValidos = validador.validate();

        if (!camposValidos) {
            return false;
        }

        if (dpFechaInicio.getValue() == null || dpFechaFin.getValue() == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Fechas inválidas",
                    "Debes seleccionar ambas fechas.");
            return false;
        }

        if (dpFechaFin.getValue().isBefore(dpFechaInicio.getValue())) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Fechas inconsistentes",
                    "La fecha de fin no puede ser anterior a la fecha de inicio.");
            dpFechaFin.setStyle("-fx-border-color: red; -fx-background-color: #FFEEEE;");
            return false;
        } else {
            dpFechaFin.setStyle(""); // Quitar estilo de error si está corregido
        }

        return true;
    }
    
    private void limpiarFormulario() {
        tfNombre.clear();
        tfDescripcion.clear();
        tfCupo.clear();
        cbEstado.getSelectionModel().clearSelection();
        cbOrganizacionVinculada.getSelectionModel().clearSelection();
        cbResponsableProyecto.getItems().clear();
        cbResponsableProyecto.getSelectionModel().clearSelection();
        dpFechaInicio.setValue(null);
        dpFechaFin.setValue(null);
    }

}
