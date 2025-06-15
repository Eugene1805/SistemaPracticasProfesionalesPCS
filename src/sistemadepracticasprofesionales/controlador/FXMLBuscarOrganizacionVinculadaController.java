/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemadepracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sistemadepracticasprofesionales.SistemaDePracticasProfesionales;
import sistemadepracticasprofesionales.dominio.OrganizacionVinculadaDM;
import sistemadepracticasprofesionales.modelo.dao.OrganizacionVinculadaDAO;
import sistemadepracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;
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
    
    private ObservableList<OrganizacionVinculada> organizaciones;
    private FilteredList<OrganizacionVinculada> filtroOrganizaciones;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacionTabla();
        
        tvOrganizacionesVinculadas.setPlaceholder(new Label("No se encontraron organizaciones con ese nombre"));
        configurarBusqueda();
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
            
            filtroOrganizaciones = new FilteredList<>(organizaciones, p -> true);
            SortedList<OrganizacionVinculada> listaOrdenada = new SortedList<>(filtroOrganizaciones);
            listaOrdenada.comparatorProperty().bind(tvOrganizacionesVinculadas.comparatorProperty());
            
            tvOrganizacionesVinculadas.setItems(listaOrdenada);
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar",
                    "Lo sentimos, por el momento no se pueden mostrar las Organizaciones Vinculadas");
            Utilidad.obtenerEscenario(tfBuscar).close();
        }
    }
    
    private void configurarBusqueda(){
        if (filtroOrganizaciones != null) {
            tfBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
                actualizarFiltro(newValue);
            });
        }
    }
    
    @FXML
    private void btnClicRegresar(ActionEvent event) {
        Utilidad.abrirVentana("Coordinador", tfBuscar);
    }

    @FXML
    private void tfbuscarOrganizacionVinculada(ActionEvent event) {
                actualizarFiltro(tfBuscar.getText());
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) { 
        Alert alerta = Utilidad.mostrarAlertaConfirmacion("Confirmacion Cancelar", 
                "¿Está seguro de que desea cancelar?");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if(resultado.get() == ButtonType.APPLY){
            Utilidad.abrirVentana("Coordinador", tfBuscar);                               
        }
    }

    @FXML
    private void btnClicAceptar(ActionEvent event) {
        OrganizacionVinculada organizacionSeleccionada = tvOrganizacionesVinculadas.getSelectionModel().getSelectedItem();        
        if (organizacionSeleccionada != null) {
            ResultadoOperacion resultado = OrganizacionVinculadaDM.verificarExistenciaResponsableAsignado(organizacionSeleccionada.getId());
            if (!resultado.isError()) {
                irFormularioResponsable(organizacionSeleccionada);                          
            }else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Verificar datos", resultado.getMensaje());
            }
        } else{
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Seleccione una Organización Vinculada",
                    "Para registrar a un Responsable del Proyecto debe seleccionar primero a una Organizacion Vinculada");
        }
            
    }
    
    private void irFormularioResponsable(OrganizacionVinculada organizacionSeleccionada){
        try{
            Stage escenaActual = Utilidad.obtenerEscenario(tfBuscar);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.getResource("/sistemadepracticasprofesionales/vista/FXMLRegistrarResponsable.fxml"));
            Parent vista = cargador.load();
            FXMLRegistrarResponsableController controlador = cargador.getController();
            controlador.setOrganizacionSeleccionada(organizacionSeleccionada);
            Scene escena = new Scene(vista);
            escenaActual.setScene(escena);
            escenaActual.setTitle("Registrar Responsable");
        }catch (IOException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error al cargar la ventana Registrar Responsable",
                    "Lo sentimos no fue posible cargar la ventana");            
        }
    }
    
    private static String normalizarBusqueda(String texto){
        if (texto == null) {
            return "";
        }
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return texto.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
    
    private void actualizarFiltro(String textoFiltrado){
        String filtroNormalizado = normalizarBusqueda(textoFiltrado).toLowerCase();

        filtroOrganizaciones.setPredicate(ov -> {
            if (filtroNormalizado.isEmpty()) {
                return true;
            }

            String razonSocialNormalizada = normalizarBusqueda(ov.getRazonSocial()).toLowerCase();

            return razonSocialNormalizada.contains(filtroNormalizado);
        });
    }    
}
