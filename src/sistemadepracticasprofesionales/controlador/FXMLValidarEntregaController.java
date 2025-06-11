package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author eugen
 * Fecha:10/06/25
 * Descripcion: Controlador para descargar la entrega seleccionada y guardar su calificacion correspondiente
 */
public class FXMLValidarEntregaController implements Initializable {

    @FXML
    private Label lbNombreDocumento;
    @FXML
    private Label lbFechaInicio;
    @FXML
    private Label lbFechaFin;
    @FXML
    private Label lbDescripcion;
    @FXML
    private Label lbFechaEntregado;
    @FXML
    private TextArea taObservacion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnClicAgregarObservacion(ActionEvent event) {
    }

    @FXML
    private void btnClicValidar(ActionEvent event) {
    }

    @FXML
    private void btnClicRechazar(ActionEvent event) {
    }
    
}
