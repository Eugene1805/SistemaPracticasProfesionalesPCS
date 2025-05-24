package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author eugen
 * Fecha: 24/05/25
 * Descripcion: Gestiona las interacciones entre la vista de la Evaluacion a la Organizacion Vinculada y el DAO
 * correspondiente para guardar dicha Evaluacion hecha por parte de un alumno
 */
public class FXMLEvaluarOrganizacionVinculadaController implements Initializable {

    @FXML
    private Label lbNombreOV;
    @FXML
    private ComboBox<Integer> cbAmbiente_laboral;
    @FXML
    private ComboBox<Integer> cbOportunidadesAprendizaje;
    @FXML
    private ComboBox<Integer> cbClaridadActividades;
    @FXML
    private ComboBox<Integer> cbNivelRelacionActividades;
    @FXML
    private ComboBox<Integer> cbAccesibilidad;
    @FXML
    private ComboBox<Integer> cbAccesoRecursos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnClicAceptar(ActionEvent event) {
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
    }
    
}
