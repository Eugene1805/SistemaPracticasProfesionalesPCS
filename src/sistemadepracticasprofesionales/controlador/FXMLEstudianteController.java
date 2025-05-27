package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author eugen
 * Fecha:27/05/25
 * Descripcion: Controlador para manejar las aciones del estudiante
 */
public class FXMLEstudianteController implements Initializable {

    @FXML
    private Label lbUsuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnCerrarSesion(ActionEvent event) {
    }

    @FXML
    private void clicConsultarAvance(MouseEvent event) {
    }

    @FXML
    private void clicActualizarExpediente(MouseEvent event) {
    }

    @FXML
    private void clicEvaluarOV(MouseEvent event) {
    }

    @FXML
    private void clicGenerarFormatoEvalOV(MouseEvent event) {
    }
    
}
