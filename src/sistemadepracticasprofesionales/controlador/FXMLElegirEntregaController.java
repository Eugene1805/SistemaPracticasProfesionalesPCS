package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author eugen
 * Fecha: 10/06/25
 * Descripcion: Controller para mostrar las entregas disponibles para calificar dado un estudiante seleccionado
 */
public class FXMLElegirEntregaController implements Initializable {

    @FXML
    private TableView<?> tvEntregas;
    @FXML
    private TableColumn<?, ?> tcTipoEntrega;
    @FXML
    private TableColumn<?, ?> tcDocumento;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
