package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import sistemadepracticasprofesionales.modelo.dao.EstudianteDAO;
import sistemadepracticasprofesionales.modelo.dao.EvaluacionOrganizacionDAO;
import sistemadepracticasprofesionales.modelo.dao.OrganizacionVinculadaDAO;
import sistemadepracticasprofesionales.modelo.pojo.Estudiante;
import sistemadepracticasprofesionales.modelo.pojo.EvaluacionOrganizacion;
import sistemadepracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;
import sistemadepracticasprofesionales.utilidades.Utilidad;
import sistemadepracticasprofesionales.utilidades.validacion.ValidadorFormulario;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.ComboValidationStrategy;

/**
 * FXML Controller class
 *
 * @author eugen
 * Fecha: 24/05/25
 * Descripcion: Gestiona las interacciones entre la vista de la Evaluacion a la Organizacion Vinculada y el DAO
 * correspondiente para guardar dicha Evaluacion hecha por parte de un alumno
 */
public class FXMLEvaluarOrganizacionVinculadaController implements Initializable { //TODO asociar la evaluacion al expediente

    @FXML
    private Label lbNombreOV;
    @FXML
    private ComboBox<Integer> cbAmbiente_laboral;
    @FXML
    private ComboBox<Integer> cbOportunidadesAprendizaje;
    @FXML
    private ComboBox<Integer> cbClaridadActividades;
    @FXML
    private ComboBox<Integer> cbNivelRelacionActividades;
    @FXML
    private ComboBox<Integer> cbAccesibilidad;
    @FXML
    private ComboBox<Integer> cbAccesoRecursos;

    private ValidadorFormulario validarFormulario;
    
    private Usuario estudiante;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarComboBoxes();
        
        inicializarValidaciones();
    }    

    @FXML
    private void btnClicAceptar(ActionEvent event) {
        if(validarFormulario.validate()){
            EvaluacionOrganizacion evaluacionOrganizacion = obtenerEvaluacion();
            guardarEvaluacionOrganizacion(evaluacionOrganizacion);
        }else{
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING,"Datos invalidos",
                    "Hay campos con datos invalidos");
        }
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        Alert alerta = Utilidad.mostrarAlertaConfirmacion("Confirmacion Cancelar",
                "¿Estás seguro de que deseas cancelar?");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if(resultado.get() == ButtonType.APPLY){
            regresarAlDashboard();
        }
    }
    
    private void cargarComboBoxes(){
        ObservableList<Integer> grados = FXCollections.observableArrayList();
        grados.addAll(1,2,3,4,5);
        cbAmbiente_laboral.setItems(grados);
        cbOportunidadesAprendizaje.setItems(grados);
        cbClaridadActividades.setItems(grados);
        cbNivelRelacionActividades.setItems(grados);
        cbAccesibilidad.setItems(grados);
        cbAccesoRecursos.setItems(grados);
    }
    
    private EvaluacionOrganizacion obtenerEvaluacion(){
        EvaluacionOrganizacion evaluacionOrganizacion = new EvaluacionOrganizacion();
        return evaluacionOrganizacion;
    }
    
    private void guardarEvaluacionOrganizacion(EvaluacionOrganizacion evaluacionOrganizacion){
        try {
            ResultadoOperacion resultadoOperacion = EvaluacionOrganizacionDAO.
                    registrarEvaluacionOrganizacion(evaluacionOrganizacion);
            if(!resultadoOperacion.isError()){
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Operacion exitosa", 
                        "Evaluacion de la Organizacion Vinculada registrada con exito");
                regresarAlDashboard();
            }else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "No se pudo registrar", 
                        "No fue posible guardar el registro de la evaluacion a la organizacion vinculada");
            }
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "No hay conexion",
                    "Lo sentimos no fue posible conectarnos a la base de datos");
        }
        
    }
    
    private void inicializarValidaciones(){
        validarFormulario = new ValidadorFormulario();
        validarFormulario.addValidation(cbAccesibilidad, new ComboValidationStrategy(true));
        validarFormulario.addValidation(cbAccesoRecursos, new ComboValidationStrategy(true));
        validarFormulario.addValidation(cbAmbiente_laboral, new ComboValidationStrategy(true));
        validarFormulario.addValidation(cbClaridadActividades, new ComboValidationStrategy(true));
        validarFormulario.addValidation(cbNivelRelacionActividades, new ComboValidationStrategy(true));
        validarFormulario.addValidation(cbOportunidadesAprendizaje, new ComboValidationStrategy(true));
        validarFormulario.addCleanupAction(()->{
            Stream.of(cbAccesibilidad,cbAccesoRecursos,cbAmbiente_laboral,cbClaridadActividades,
                        cbNivelRelacionActividades,cbOportunidadesAprendizaje)
          .filter(tf -> !tf.getStyle().isEmpty())
          .findFirst()
          .ifPresent(Control::requestFocus);//Le da el foco al primer campo con texto vacio
        });
    }
    
    private void regresarAlDashboard(){
        Utilidad.abrirVentana("Estudiante", lbNombreOV);
    }

    void inicializarInformacion(Usuario estudiante) {
        this.estudiante = estudiante;
        cargarNombreOV();
    }
    
    private void cargarNombreOV(){
        try {
            Estudiante estudianteSesion= EstudianteDAO.obtenerEstudiantePorMatricula(estudiante.getUsername());
            OrganizacionVinculada organizacionVinculadaEstudiante = OrganizacionVinculadaDAO.
                    obtenerOrganizacionVinculadaPorProyecto(estudianteSesion.getIdProyecto());
            lbNombreOV.setText(organizacionVinculadaEstudiante.getRazonSocial());
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "No hay conexion",
                    "Lo sentimos no fue posible conectarnos a la base de datos para"
                            + " obtener el nombre de la Organizacion Vinculada");
        }
    }
}
