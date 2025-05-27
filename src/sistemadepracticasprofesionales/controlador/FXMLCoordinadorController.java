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
 * Descripcion: Controlador para configurar las acciones del coordinador
 */
public class FXMLCoordinadorController implements Initializable {

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
    private void clicAsignarProyectos(MouseEvent event) {
    }

    @FXML
    private void clicGenerarOficios(MouseEvent event) {
    }

    @FXML
    private void clicRegistrarOV(MouseEvent event) {
    }

    @FXML
    private void clicRegistrarResponsable(MouseEvent event) {
    }

    @FXML
    private void clicRegistrarProyecto(MouseEvent event) {
    }

    @FXML
    private void clicActualizarResponsable(MouseEvent event) {
    }

    @FXML
    private void clicActualizarProyecto(MouseEvent event) {
    }
    
}
