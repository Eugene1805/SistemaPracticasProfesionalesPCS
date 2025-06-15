package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sistemadepracticasprofesionales.modelo.dao.EstudianteDAO;
import sistemadepracticasprofesionales.modelo.pojo.Estudiante;

/**
 * FXML Controller class
 *
 * @author Nash
 */
public class FXMLBuscarEstudianteController implements Initializable {

    @FXML
    private TableView<Estudiante> tvEstudiantes;
    @FXML
    private TableColumn tcMatricula;
    @FXML
    private TableColumn tcNombre;
    @FXML
    private TableColumn tcApellidoPaterno;
    @FXML
    private TableColumn tcApellidoMaterno;
    @FXML
    private TableColumn tcProyecto;

    private ObservableList<Estudiante> estudiantes;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacionTabla();
        
        
    }    

    private void configurarTabla(){
        tcMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        tcNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        tcApellidoPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        tcApellidoMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        tcProyecto.setCellValueFactory(new PropertyValueFactory("nombreProyecto"));
    }
    
    private void cargarInformacionTabla(){
        estudiantes = FXCollections.observableArrayList();
        ArrayList<Estudiante> estudiantesDAO = EstudianteDAO.//obtener estudiantes con proyecto asignado en el periodo escolar actual
        
    }
    @FXML
    private void btnClicRegresar(ActionEvent event) {
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
    }

    @FXML
    private void btnClicAceptar(ActionEvent event) {
    }
    
}
