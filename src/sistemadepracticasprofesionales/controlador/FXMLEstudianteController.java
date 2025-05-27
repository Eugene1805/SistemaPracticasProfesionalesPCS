package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import sistemadepracticasprofesionales.utilidades.Utilidad;

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
        Utilidad.cerrarSesion(lbUsuario);
    }

    @FXML
    private void clicConsultarAvance(MouseEvent event) {
        Utilidad.abrirVentana("ConsultarAvance", lbUsuario);
    }

    @FXML
    private void clicActualizarExpediente(MouseEvent event) {
        Utilidad.abrirVentana("ActualizarExpediente", lbUsuario);
    }

    @FXML
    private void clicEvaluarOV(MouseEvent event) {
        Utilidad.abrirVentana("EvaluarOrganizacionVinculada", lbUsuario);
    }

    @FXML
    private void clicGenerarFormatoEvalOV(MouseEvent event) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Funcionalidad en desarrollo",
                "La funcionalidad de este apartado sigue en desarrollo");
    }
    
}
