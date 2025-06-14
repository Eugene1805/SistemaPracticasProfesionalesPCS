/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sistemadepracticasprofesionales.dominio.EntregaDM;
import sistemadepracticasprofesionales.modelo.dao.EntregaDAO;
import sistemadepracticasprofesionales.modelo.dao.TipoDocumentoInicialDAO;
import sistemadepracticasprofesionales.modelo.pojo.Entrega;
import sistemadepracticasprofesionales.modelo.pojo.PeriodoEscolar;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemadepracticasprofesionales.modelo.pojo.TipoDocumentoInicial;
import sistemadepracticasprofesionales.utilidades.Utilidad;
import sistemadepracticasprofesionales.utilidades.validacion.ValidadorFormulario;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.ComboValidationStrategy;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.DateValidationStrategy;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.TextValidationStrategy;

/**
 * FXML Controller class
 *
 * @author Nash
 */
public class FXMLGenerarEntregasController implements Initializable {

    @FXML
    private ComboBox<TipoDocumentoInicial> cbTiposDocumentos;
    @FXML
    private DatePicker dpFechaInicio;
    @FXML
    private DatePicker dpFechaEntrega;
    @FXML
    private TextField tfTitulo;
    @FXML
    private TextArea taDescripcion;

    private ValidadorFormulario validadorFormulario;
    private PeriodoEscolar periodoEscolarActual;
    private String tipoEntrega;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarValidaciones();
    }    
    
    public void inicializarDatos(String tipoEntrega, PeriodoEscolar periodoEscolarActual){
        this.tipoEntrega = tipoEntrega;
        this.periodoEscolarActual = periodoEscolarActual;
        
        if ("Inicial".equals(tipoEntrega)) {
            cargarTiposDocumentosIniciales();
        }
    }

    private void cargarTiposDocumentosIniciales(){
        try {
            List<TipoDocumentoInicial> tipos = TipoDocumentoInicialDAO.obtenerTiposDisponibles(periodoEscolarActual.getId());
            cbTiposDocumentos.setItems(FXCollections.observableArrayList(tipos));
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Carga",
                    "No se pudieron cargar los tipos de documento iniciales disponibles.");
        }
    }

    @FXML
    private void btnClicAceptar(ActionEvent event) {
        if (validadorFormulario.validate()){
            ResultadoOperacion resultado = EntregaDM.verificarFechasEntrega(dpFechaInicio.getValue(), dpFechaEntrega.getValue(), periodoEscolarActual);
            if (!resultado.isError()) {
                Alert alerta = Utilidad.mostrarAlertaConfirmacion("Confirmación Generar Entrega", 
                        "¿Está seguro de generar la entrega?");
                Optional<ButtonType> opcion = alerta.showAndWait();
                if (opcion.get() == ButtonType.APPLY) {
                    guardarEntrega();
                }
                Utilidad.abrirVentana("Coordinador", tfTitulo);            
            }else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Verificar datos", resultado.getMensaje());
            }
            
        }else{
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Datos invalidos",
                    "Hay campos con datos invalidos");            
        }
    }
    
    private void guardarEntrega(){
        String titulo = tfTitulo.getText().trim();
        String descripcion = taDescripcion.getText().trim();
        Date fechaInicio = Date.valueOf(dpFechaInicio.getValue());
        Date fechaFin = Date.valueOf(dpFechaEntrega.getValue());
        TipoDocumentoInicial tipoSeleccionado = cbTiposDocumentos.getValue();
        try {
            ResultadoOperacion resultado = EntregaDAO.programarEntregaIniciales(
                    titulo, descripcion , fechaInicio, fechaFin, tipoSeleccionado, periodoEscolarActual.getId());
            if (!resultado.isError()) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION,
                        "Éxito", "Operación exitosa");   
            }else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al guardar", 
                       "No fue posible guardar la entrega, intente de nuevo");
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "No hay conexión",
                    "Lo sentimos no fue posible conectarnos a la base de datos");
        }
    }

    public FXMLGenerarEntregasController() {
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        
    }
    @FXML
    private void btnClicRegresar(ActionEvent event) {
        Utilidad.abrirVentana("ProgramarEntregas", tfTitulo);
    }
    private void inicializarValidaciones(){
        validadorFormulario = new ValidadorFormulario();
        validadorFormulario.addValidation(cbTiposDocumentos, new ComboValidationStrategy(true));
        validadorFormulario.addValidation(tfTitulo, new TextValidationStrategy(100, true));
        validadorFormulario.addValidation(taDescripcion, new TextValidationStrategy(65535, true));
        validadorFormulario.addValidation(dpFechaInicio,new DateValidationStrategy(true));
        validadorFormulario.addValidation(dpFechaEntrega,new DateValidationStrategy(true));
        validadorFormulario.addCleanupAction(()->{
           Stream.of(cbTiposDocumentos, tfTitulo, taDescripcion, dpFechaInicio, dpFechaEntrega)
          .filter(tf -> !tf.getStyle().isEmpty())
          .findFirst()
          .ifPresent(Control::requestFocus);
        });
    }
    
}
