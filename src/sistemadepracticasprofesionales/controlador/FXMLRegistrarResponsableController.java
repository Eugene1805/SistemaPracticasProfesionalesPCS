/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import sistemadepracticasprofesionales.utilidades.Utilidad;
import sistemadepracticasprofesionales.utilidades.validacion.ValidadorFormulario;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.TextValidationStrategy;

/**
 * FXML Controller class
 *
 * @author Nash
 */
public class FXMLRegistrarResponsableController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private TextField tfCorreo;
    @FXML
    private TextField tfDepartamento;
    @FXML
    private TextField tfPuesto;
    
    private ValidadorFormulario validadorFormulario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarValidaciones();
    }    

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        Utilidad.abrirVentana("BuscarOrganizacionVinculada", tfNombre);
    }

    @FXML
    private void btnClicAceptar(ActionEvent event) {
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        Alert alerta = Utilidad.mostrarAlertaConfirmacion("Confirmacion Cancelar",
                "¿Estás seguro de que deseas cancelar?");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if(resultado.get() == ButtonType.APPLY){
            Utilidad.obtenerEscenario(tfCorreo).close();
        }
    }
    
    private void inicializarValidaciones(){
        validadorFormulario = new ValidadorFormulario();
        validadorFormulario.addValidation(tfNombre, new TextValidationStrategy(45, true));
        validadorFormulario.addValidation(tfApellidoPaterno, new TextValidationStrategy(45, true));
//PREGUNTAR QUE VALIDACION ES PARA EL MATERNO          validadorFormulario.addValidation(tfApellidoMaterno, new TextValidationStrategy(45, true));
        validadorFormulario.addValidation(tfDepartamento, new TextValidationStrategy(35, true));
        //TODO Continuar validaciones
    }
    
}
