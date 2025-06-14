/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemadepracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sistemadepracticasprofesionales.SistemaDePracticasProfesionales;
import sistemadepracticasprofesionales.modelo.dao.PeriodoEscolarDAO;
import sistemadepracticasprofesionales.modelo.pojo.PeriodoEscolar;
import sistemadepracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author Nash
 */
public class FXMLProgramarEntregasController implements Initializable {

    @FXML
    private Label lblPeriodoEscolar;
    
    private PeriodoEscolar periodoEscolarActual;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarPeriodoEscolarActual();
    }    
    
    private void cargarPeriodoEscolarActual(){
        try{
            periodoEscolarActual = PeriodoEscolarDAO.obtenerPeriodoEscolarActual();
            lblPeriodoEscolar.setText(periodoEscolarActual.getNombrePeriodo());
        }catch(SQLException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error en la carga",
                "Lo sentimos, por el momento no fue posible cargar el Perido Escolar Actual");
        }   
    }

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        Utilidad.abrirVentana("Coordinador", lblPeriodoEscolar);
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        Alert alerta = Utilidad.mostrarAlertaConfirmacion("Confirmacion Cancelar", 
                "¿Está seguro de que desea cancelar?");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if(resultado.get() == ButtonType.APPLY){
            Utilidad.abrirVentana("Coordinador", lblPeriodoEscolar);                               
        }
    }

    @FXML
    private void btnClicEntregaIniciales(ActionEvent event) {
        
        if(periodoEscolarActual != null){
            irFormularioGenerarEntregas("Inicial");
        }
        
    
    }

    private void irFormularioGenerarEntregas(String tipoEntrega){
        try{
            Stage escenaActual = Utilidad.obtenerEscenario(lblPeriodoEscolar);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.getResource("/sistemadepracticasprofesionales/vista/FXMLGenerarEntregas.fxml"));
            Parent vista = cargador.load();
            FXMLGenerarEntregasController controlador = cargador.getController();
            controlador.inicializarDatos(tipoEntrega, periodoEscolarActual);
            Scene escena = new Scene(vista);
            escenaActual.setScene(escena);
            escenaActual.setTitle("Generar Entrega Iniciales");
        }catch(IOException e){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error al cargar la ventana Generar Entregas",
                    "Lo sentimos no fue posible cargar la ventana");              
        }
    }
    
    @FXML
    private void btnClicEntregaFinales(ActionEvent event) {
    }

    @FXML
    private void btnClicEntregaIntermedios(ActionEvent event) {
    }

    @FXML
    private void btnClicEntregaReportes(ActionEvent event) {
    }
    
}
