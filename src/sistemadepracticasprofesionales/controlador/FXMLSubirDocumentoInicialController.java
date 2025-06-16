/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author meler
 */
public class FXMLSubirDocumentoInicialController implements Initializable {

    @FXML
    private Button btnBack;
    @FXML
    private ImageView imgPerfil;
    @FXML
    private Label lblNombreEstudiante;
    @FXML
    private Label lblMatriculaEstudiante;
    @FXML
    private Label lblPeriodoEscolar;
    @FXML
    private Label lblHorasAcumuladas;
    @FXML
    private ProgressBar barraProgresoHoras;
    @FXML
    private TableView<?> tablaEntregas;
    @FXML
    private TableColumn<?, ?> colTitulo;
    @FXML
    private TableColumn<?, ?> colFechaInicio;
    @FXML
    private TableColumn<?, ?> colFechaFin;
    @FXML
    private TableColumn<?, ?> colDescripcion;
    @FXML
    private TableColumn<?, ?> colSubirPDF;
    @FXML
    private Button btnSalir;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
