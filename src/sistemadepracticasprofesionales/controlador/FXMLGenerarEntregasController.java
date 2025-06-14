/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Nash
 */
public class FXMLGenerarEntregasController implements Initializable {

    @FXML
    private ComboBox<?> cbTiposDocumentos;
    @FXML
    private DatePicker dpFechaInicio;
    @FXML
    private DatePicker dpFechaEntrega;
    @FXML
    private TextField tfTitulo;
    @FXML
    private TextArea taDescripcion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnClicRegresar(ActionEvent event) {
    }

    @FXML
    private void btnClicAceptar(ActionEvent event) {
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
    }
    
}
