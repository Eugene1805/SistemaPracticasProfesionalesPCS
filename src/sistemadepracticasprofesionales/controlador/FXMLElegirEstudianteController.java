package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author eugen
 * Fecha: 10/06/25
 * Descripcion: Controller para que un profesor eliga el estudiante del que deseea validar entregas
 */
public class FXMLElegirEstudianteController implements Initializable {

    @FXML
    private TableView<?> tvEstudiantes;
    @FXML
    private TableColumn<?, ?> tcNombre;
    @FXML
    private TableColumn<?, ?> tcMatricula;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnClicCancelar(ActionEvent event) {
    }
    
}
