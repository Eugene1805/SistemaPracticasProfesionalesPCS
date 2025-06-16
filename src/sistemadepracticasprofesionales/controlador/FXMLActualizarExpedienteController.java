package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author meler
 */
public class FXMLActualizarExpedienteController implements Initializable {

    @FXML
    private ProgressBar pbHorasAcumuladas;
    @FXML
    private ImageView ivFotoEstudiante;
    @FXML
    private Label lblNombreEstudiante;
    @FXML
    private Label lblMatriculaEstudiante;
    @FXML
    private Label lbNombreProyecto;
    @FXML
    private Label lbNombreExperiencia;
    @FXML
    private Label lbNombreProfesor;
    @FXML
    private Label lbNombrePeriodo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnSubirDocumentosIniciales(ActionEvent event) {
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
    }
    
}
