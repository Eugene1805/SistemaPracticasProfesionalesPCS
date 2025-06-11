package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;
import sistemadepracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author eugen
 * Fecha:27/05/25
 * Descripcion: Controlador para las acciones disponibles del profesor
 */
public class FXMLProfesorController implements Initializable, Dashboard {

    @FXML
    private Label lbUsuario;
    
    private Parent vista;

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

    @Override
    public void inicializar(Usuario usuario) {
        lbUsuario.setText(usuario.getNombre() + " " + usuario.getApellidoPaterno() );
    }

    @Override
    public Parent obtenerVista() {
        return vista;
    }
    
}
