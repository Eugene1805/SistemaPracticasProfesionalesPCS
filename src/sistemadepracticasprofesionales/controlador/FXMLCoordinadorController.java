package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;
import sistemadepracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author eugen
 * Fecha:27/05/25
 * Descripcion: Controlador para configurar las acciones del coordinador
 */
public class FXMLCoordinadorController implements Initializable, Dashboard {

    @FXML
    private Label lbUsuario;
    
    private Parent vista;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void btnCerrarSesion(ActionEvent event) {
        Utilidad.cerrarSesion(lbUsuario);
    }

    @FXML
    private void clicAsignarProyectos(MouseEvent event) {
        Utilidad.abrirVentana("AsignarProyectos", lbUsuario);
    }

    @FXML
    private void clicGenerarOficios(MouseEvent event) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Funcionalidad en desarrollo",
                "La funcionalidad de este apartado sigue en desarrollo");
    }

    @FXML
    private void clicRegistrarOV(MouseEvent event) {
        Utilidad.abrirVentana("RegistrarOrganizacionVinculada", lbUsuario);
    }

    @FXML
    private void clicRegistrarResponsable(MouseEvent event) {
        Utilidad.abrirVentana("RegistrarResponsable", lbUsuario);
    }

    @FXML
    private void clicRegistrarProyecto(MouseEvent event) {
        Utilidad.abrirVentana("RegistrarProyecto", lbUsuario);
    }

    @FXML
    private void clicActualizarResponsable(MouseEvent event) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Funcionalidad en desarrollo",
                "La funcionalidad de este apartado sigue en desarrollo");
    }

    @FXML
    private void clicActualizarProyecto(MouseEvent event) {
        Utilidad.abrirVentana("ActualizarProyecto", lbUsuario);
    }

    @Override
    public void inicializar(Usuario usuario) {
        lbUsuario.setText(usuario.getNombre() +" "+ usuario.getApellidoPaterno());
    } 
}
