package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author eugen
 * 
 * Fecha:23/05/25
 * Descripcion: Gestiona las interacciones entre la vista y el DAO de OrganizacionVinculada
 */
public class FXMLRegistrarOrganizacionVinculadaController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfDireccion;
    @FXML
    private TextField tfCiudad;
    @FXML
    private TextField tfSector;
    @FXML
    private TextField tfNumUsuariosDirectos;
    @FXML
    private TextField tfNumUsuariosIndirectos;
    @FXML
    private ComboBox<?> cbEstado; //Checar si queda como tf para no tener que cargar estados al base de datos

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicBtnRegresar(ActionEvent event) {
        //Regresar al dashboard
    }

    @FXML
    private void btnClicAceptar(ActionEvent event) {
        //TODO
        //Verificar que los campos esten llenos y en dado caso
        //Obtener los campos y guardarlos en el POJO
        //Guardar la OV con el DAO en la base de datos
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        //TODO cerrar ventana actual y regresar al dashboard
    }
    
}
