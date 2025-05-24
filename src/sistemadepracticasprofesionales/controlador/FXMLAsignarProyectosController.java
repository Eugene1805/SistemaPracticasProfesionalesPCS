package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import sistemadepracticasprofesionales.modelo.dao.ProyectoDAO;
import sistemadepracticasprofesionales.modelo.dao.EstudianteDAO;
import sistemadepracticasprofesionales.modelo.pojo.Estudiante;
import sistemadepracticasprofesionales.modelo.pojo.Proyecto;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;
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
    
    private Estudiante estudianteSeleccionado;
    private Proyecto proyectoSeleccionado;
    @FXML
    private ComboBox<Estudiante> cbEstudiantes;
    @FXML
    private ComboBox<Proyecto> cbProyectos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarEstudiantes();
        seleccionarMatricula();
        cargarProyectos();
        seleccionarNombreProyecto();
    }    

    @FXML
    private void btnClicAsignar(ActionEvent event) {
        if(validadorFormulario.validate()){
            Alert alerta = Utilidad.mostrarAlertaConfirmacion("Confirmacion continuar", 
                    "¿Estás seguro de que deseas continuar?");
            Optional<ButtonType> resultado = alerta.showAndWait();
            if(resultado.get() == ButtonType.APPLY){
                guardarAsignacion(obtenerEstudianteSeleccionado().getId(), obtenerProyectoSeleccionado().getId());
            }
        }else{
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING,"Datos invalidos",
                    "Hay campos con datos invalidos");
        }
        
    }
    
    private void cargarEstudiantes(){
        try {
            ObservableList<Estudiante> estudiantes = FXCollections.observableArrayList();
            estudiantes.addAll(EstudianteDAO.obtenerEstudiantesSinProyectoAsignado());
            cbEstudiantes.setItems(estudiantes);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error en la carga",
                    "Lo sentimos, por el momento no fue posible cargar los estudiantes");
        }
    }
    
    private void cargarProyectos(){
        try {
            ObservableList<Proyecto> proyectos = FXCollections.observableArrayList();
            proyectos.addAll(ProyectoDAO.obtenerProyectosConCupoDisponible());
            cbProyectos.setItems(proyectos);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error en la carga",
                    "Lo sentimos, por el momento no fue posible cargar los proyectos");
        
        }
    }
    
    private void seleccionarMatricula(){
        cbEstudiantes.valueProperty().addListener(new ChangeListener<Estudiante>() {
            @Override
            public void changed(ObservableValue<? extends Estudiante> observable, Estudiante oldValue,
                                                                                Estudiante newValue) {
                if(newValue != null){
                    cargarInformacionAlumno(newValue.getId());
                }
            }
        });
    }
    
    private void seleccionarNombreProyecto(){
        cbProyectos.valueProperty().addListener(new ChangeListener<Proyecto>() {
            @Override
            public void changed(ObservableValue<? extends Proyecto> observable, Proyecto oldValue,
                                                                                Proyecto newValue) {
                if(newValue != null){
                    cargarInformacionProyecto(newValue.getId());
                }
            }
        });
    }
    
    private void guardarAsignacion(int idEstudiante, int idProyecto){
        try {
            ResultadoOperacion resultadoOperacion = EstudianteDAO.guardarAsignacion(idEstudiante,idProyecto);
            if(!resultadoOperacion.isError()){
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Estudiante" +
                        obtenerEstudianteSeleccionado().getNombre()
                        + " asignado al proyecot"+ obtenerProyectoSeleccionado().getNombre() +" con exito",
                        resultadoOperacion.getMensaje());
                
            }else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "No se pudo guardar la asignacion",
                        resultadoOperacion.getMensaje());
            }
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "No hay conexion", 
                    "Lo sentimos por el momento no se pudo guardar la asignacion");
        }
    }
    
    private void cargarInformacionAlumno(int idEstudiante){
        try {
            estudianteSeleccionado = EstudianteDAO.obtenerEstudiante(idEstudiante);
            lbMatricula.setText(estudianteSeleccionado.getMatricula());
            lbNombre.setText(estudianteSeleccionado.getNombre() +" "+ estudianteSeleccionado.getApellidoPaterno()
                    + " " +estudianteSeleccionado.getApellidoMaterno());
            lbNRC.setText(estudianteSeleccionado.getNrcExperienciaEducativa());
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error en la carga",
                    "Lo sentimos, por el momento no fue posible cargar la informacion del estudiante");
        }
    }
    
    private void cargarInformacionProyecto(int idProyecto){
        try {
            proyectoSeleccionado = ProyectoDAO.obtenerProyecto(idProyecto);
            lbNombreProyecto.setText(proyectoSeleccionado.getNombre());
            lbOrganizacionVinculada.setText(proyectoSeleccionado.getNombreOrganizacionVinculada());
            lbCuposDisponibles.setText(String.valueOf(proyectoSeleccionado.getCupo()));
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error en la carga",
                    "Lo sentimos, por el momento no fue posible cargar la informacion del proyecto");
        }
    }
    
    private Estudiante obtenerEstudianteSeleccionado(){
        return cbEstudiantes.getSelectionModel().getSelectedItem();
    }
    
    private Proyecto obtenerProyectoSeleccionado(){
        return cbProyectos.getSelectionModel().getSelectedItem();
    }
}
