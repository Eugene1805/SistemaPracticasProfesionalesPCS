package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;

/**
 * FXML Controller class
 *
 * @author eugen
 * Fecha:27/05/25
 * Descripcion: Controlador de la vista principal a la que seran difigidos los usuarios despues de un log in exitoso
 * mostrara opciones diferentes dependiendo del tipo de usuario registrado en la base de datos
 */
public class FXMLDashboardController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializarInformacion(Usuario usuario){
        //TODO cambiar las opciones que se muestra dependiento el tipo de usuario
    }

    @FXML
    private void btnCerrarSesion(ActionEvent event) {
    }
    
}
