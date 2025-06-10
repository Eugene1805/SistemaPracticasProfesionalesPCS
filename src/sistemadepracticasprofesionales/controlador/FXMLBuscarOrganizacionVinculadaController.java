/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Nash
 */
public class FXMLBuscarOrganizacionVinculadaController implements Initializable {

    @FXML
    private TableView<?> tvOrganizacionesVinculadas;
    @FXML
    private TableColumn<?, ?> tcRazonSocial;
    @FXML
    private TextField tfBuscar;
    @FXML
    private TableColumn<?, ?> tcTelefono;
    @FXML
    private TableColumn<?, ?> tcDireccion;
    @FXML
    private TableColumn<?, ?> tcCiudad;
    @FXML
    private TableColumn<?, ?> tcEstado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
