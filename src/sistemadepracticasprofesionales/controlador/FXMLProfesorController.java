package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import sistemadepracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author eugen
 * Fecha:27/05/25
 * Descripcion: Controlador para las acciones disponibles del profesor
 */
public class FXMLProfesorController implements Initializable {

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
    private void clicValidarEntregas(MouseEvent event) {
        Utilidad.abrirVentana("ValidarEntregas", lbUsuario);
    }

    @FXML
    private void clicConsultarExpediente(MouseEvent event) {
        Utilidad.abrirVentana("ConsultarExpediente", lbUsuario);
    }
    
}
