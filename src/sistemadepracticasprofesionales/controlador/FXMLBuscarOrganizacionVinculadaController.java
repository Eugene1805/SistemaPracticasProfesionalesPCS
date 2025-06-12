/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sistemadepracticasprofesionales.modelo.dao.OrganizacionVinculadaDAO;
import sistemadepracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemadepracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author Nash
 */
public class FXMLBuscarOrganizacionVinculadaController implements Initializable {

    @FXML
    private TableView<OrganizacionVinculada> tvOrganizacionesVinculadas;
    @FXML
    private TableColumn tcRazonSocial;
    @FXML
    private TextField tfBuscar;
    @FXML
    private TableColumn tcTelefono;
    @FXML
    private TableColumn tcDireccion;
    @FXML
    private TableColumn tcCiudad;
    @FXML
    private TableColumn tcEstado;
    @FXML
    private Button btnBuscar;
    
    private ObservableList<OrganizacionVinculada> organizaciones;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacionTabla();
    }
    
    private void configurarTabla(){
        tcRazonSocial.setCellValueFactory(new PropertyValueFactory("razonSocial"));
        tcTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        tcDireccion.setCellValueFactory(new PropertyValueFactory("direccion"));
        tcCiudad.setCellValueFactory(new PropertyValueFactory("ciudad"));
        tcEstado.setCellValueFactory(new PropertyValueFactory("estado"));
    }    
    
    private void cargarInformacionTabla(){
        try {
            organizaciones = FXCollections.observableArrayList();
            ArrayList<OrganizacionVinculada> organizacionesDAO = OrganizacionVinculadaDAO.obtenerOrganizacionesVinculadas();
            organizaciones.addAll(organizacionesDAO);
            tvOrganizacionesVinculadas.setItems(organizaciones);
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar",
                    "Lo sentimos, por el momento no se pueden mostrar las Organizaciones Vinculadas");
            cerrarVentana();
        }
    }
    
    

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        Utilidad.abrirVentana("Coordinador", tfBuscar);
    }

    @FXML
    private void btnClicBuscar(ActionEvent event) {
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        Alert alerta = Utilidad.mostrarAlertaConfirmacion("Confirmacion Cancelar", 
                "¿Estás seguro de que deseas cancelar?");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if(resultado.get() == ButtonType.APPLY){
            Utilidad.obtenerEscenario(tfBuscar).close();
        }
    }

    @FXML
    private void btnClicAceptar(ActionEvent event) {
    }

    private void cerrarVentana() {
        ((Stage) tfBuscar.getScene().getWindow()).close();
    }
    
}
