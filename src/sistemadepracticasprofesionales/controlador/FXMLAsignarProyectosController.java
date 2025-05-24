package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import sistemadepracticasprofesionales.modelo.pojo.Estudiante;
import sistemadepracticasprofesionales.modelo.pojo.Proyecto;
import sistemadepracticasprofesionales.utilidades.Utilidad;
import sistemadepracticasprofesionales.utilidades.validacion.ValidadorFormulario;

/**
 * FXML Controller class
 *
 * @author eugen
 * Fecha:24/05/25
 * Descripcion: Controlador para la comunicacion entre los dao de los proyectos y estudiantes para asignarles
 * un proyecto
 */
public class FXMLAsignarProyectosController implements Initializable {

    @FXML
    private ComboBox<Estudiante> cbMatricula;
    @FXML
    private ComboBox<Proyecto> cbNombreProyecto;
    @FXML
    private Label lbMatricula;
    @FXML
    private Label lbNombre;
    @FXML
    private Label lbNRC;
    @FXML
    private Label lbNombreProyecto;
    @FXML
    private Label lbOrganizacionVinculada;
    @FXML
    private Label lbCuposDisponibles;
    
    private ValidadorFormulario validadorFormulario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarMatriculas();
        cargarNombresProyectos();
    }    

    @FXML
    private void btnClicAsignar(ActionEvent event) {
        if(validadorFormulario.validate()){
            Alert alerta = Utilidad.mostrarAlertaConfirmacion("Confirmacion continuar", 
                    "¿Estás seguro de que deseas continuar?");
            Optional<ButtonType> resultado = alerta.showAndWait();
            if(resultado.get() == ButtonType.APPLY){
                guardarAsignacion("matricula", "nombre del proyecto");
            }
            if(resultado.get() == ButtonType.CANCEL){
                //TODO limpiar seleccion de los combos
            }
        }else{
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING,"Datos invalidos",
                    "Hay campos con datos invalidos");
        }
        
    }
    
    private void cargarMatriculas(){
        //TODO Obtener lista de estudiantes sin proyecto asignado
    }
    
    private void cargarNombresProyectos(){
        //TODO Obtener lista de proyectos con cupo disponible
    }
    
    private void seleccionarMatricula(){
        cbMatricula.valueProperty().addListener(new ChangeListener<Estudiante>() {
            @Override
            public void changed(ObservableValue<? extends Estudiante> observable, Estudiante oldValue,
                                                                                Estudiante newValue) {
                //TODO Actualizar los labels
            }
        });
    }
    
    private void seleccionarNombreProyecto(){
        cbNombreProyecto.valueProperty().addListener(new ChangeListener<Proyecto>() {
            @Override
            public void changed(ObservableValue<? extends Proyecto> observable, Proyecto oldValue,
                                                                                Proyecto newValue) {
                //TODO Actualizar los labels
            }
        });
    }
    
    private void guardarAsignacion(String matricula, String nombreProyecto){
        //TODO
    }
}
