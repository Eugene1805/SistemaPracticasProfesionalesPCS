package sistemadepracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistemadepracticasprofesionales.SistemaDePracticasProfesionales;
import sistemadepracticasprofesionales.dominio.ResponsableProyectoDM;
import sistemadepracticasprofesionales.modelo.dao.ResponsableProyectoDAO;
import sistemadepracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemadepracticasprofesionales.modelo.pojo.ResponsableProyecto;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;
import sistemadepracticasprofesionales.utilidades.Utilidad;
import sistemadepracticasprofesionales.utilidades.validacion.ValidadorFormulario;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.MailValidationStrategy;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.PhoneValidationStrategy;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.TextLetterValidationStrategy;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.TextValidationStrategy;

/**
 * FXML Controller class
 *
 * @author Nash
 * Fecha: 06/06/2025
 * Descripción: Gestiona las interacciones entre la vista y el DAO de Responsable Proyecto para poder hacer 
 * registros de un Responsable del Proyecto en el formulario mostrado en la vista
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
    @FXML
    private TextField tfTelefono;
        
    private OrganizacionVinculada organizacionSeleccionada;
    private ValidadorFormulario validadorFormulario;
    
    private Usuario coordinador;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarValidaciones();
    }    
    
    public void inicializar(Usuario usuario){
        this.coordinador = usuario;
    }
    
    public void setOrganizacionSeleccionada(OrganizacionVinculada organizacionSeleccionada){
        this.organizacionSeleccionada = organizacionSeleccionada;
    }

    @FXML
    private void btnClicGuardar(ActionEvent event) {
        if (validadorFormulario.validate()) {
            ResultadoOperacion resultado = ResponsableProyectoDM.verificarExistenciaCorreo(tfCorreo.getText());
            if (!resultado.isError()) {
                ResponsableProyecto responsableProyecto = obtenerResponsableProyecto();
                guardarResponsableProyecto(responsableProyecto);   
            }else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Verificar datos", resultado.getMensaje());
            }
                 
        }else{
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Datos invalidos",
                    "Hay campos con datos invalidos");
        }
    }
    
    @FXML
    private void btnClicRegresar(ActionEvent event) {
        try{
            Stage escenarioBase = Utilidad.obtenerEscenario(tfApellidoMaterno);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                    getResource("vista/FXMLBuscarOrganizacionVinculada.fxml"));
            Parent vista = cargador.load();
            FXMLBuscarOrganizacionVinculadaController controlador = cargador.getController();
            controlador.inicializar(coordinador);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Buscar Organizacion Vinculada");
            escenarioBase.show();
        } catch (IOException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Navegación",
                "Lo sentimos no fue posible volver a la ventana de Buscar Organización Vinculada");
        }   
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        Alert alerta = Utilidad.mostrarAlertaConfirmacion("Confirmacion Cancelar", 
                "¿Está seguro de que desea cancelar?");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if(resultado.get() == ButtonType.APPLY){
            regresarAlDashbord();
        }
    }
    
    private ResponsableProyecto obtenerResponsableProyecto(){
        ResponsableProyecto responsableProyecto = new ResponsableProyecto();
        responsableProyecto.setNombre(tfNombre.getText());
        responsableProyecto.setApellidoPaterno(tfApellidoPaterno.getText());
        responsableProyecto.setApellidoMaterno(tfApellidoMaterno.getText());
        responsableProyecto.setDepartamento(tfDepartamento.getText());
        responsableProyecto.setPuesto(tfPuesto.getText());
        responsableProyecto.setCorreo(tfCorreo.getText());
        responsableProyecto.setTelefono(tfTelefono.getText());
        responsableProyecto.setIdOrganizacionVinculada(organizacionSeleccionada.getId());
        return responsableProyecto;
    } 
    
    private void guardarResponsableProyecto(ResponsableProyecto responsableProyecto){
        try {
            ResultadoOperacion resultadoInsertar = ResponsableProyectoDAO.registrarResponsableProyecto(responsableProyecto);
            if (!resultadoInsertar.isError()) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Operacion exitosa", 
                        "Responsable del Proyecto registrado con exito");
                regresarAlDashbord();
            } else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "No se pudo registrar", 
                        "No fue posible guardar el registro de " + responsableProyecto.getNombre());                
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "No hay conexión", 
                    "Lo sentimos no fue posible conectarnos a la base de datos, inténtelo más tarde");
        }
    }
    
    private void inicializarValidaciones(){
        validadorFormulario = new ValidadorFormulario();
        validadorFormulario.addValidation(tfNombre, new TextLetterValidationStrategy(45, true));
        validadorFormulario.addValidation(tfApellidoPaterno, new TextLetterValidationStrategy(45, true));
        validadorFormulario.addValidation(tfApellidoMaterno, new TextLetterValidationStrategy(45, false));
        validadorFormulario.addValidation(tfDepartamento, new TextLetterValidationStrategy(45, true));
        validadorFormulario.addValidation(tfPuesto, new TextLetterValidationStrategy(45, true));
        validadorFormulario.addValidation(tfCorreo, new MailValidationStrategy(45, true));
        validadorFormulario.addValidation(tfTelefono, new PhoneValidationStrategy(10, true));
        validadorFormulario.addCleanupAction(()->{
           Stream.of(tfNombre, tfApellidoPaterno, tfApellidoMaterno, tfDepartamento,
                   tfPuesto,tfCorreo,tfTelefono)
          .filter(tf -> !tf.getStyle().isEmpty())
          .findFirst()
          .ifPresent(Control::requestFocus);
        });
    }

    private void regresarAlDashbord() {
        try{
            Stage escenarioBase = Utilidad.obtenerEscenario(tfApellidoMaterno);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                    getResource("vista/FXMLCoordinador.fxml"));
            Parent vista = cargador.load();
            FXMLCoordinadorController controlador = cargador.getController();
            controlador.inicializar(coordinador);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Dashboard Coordinador");
            escenarioBase.show();
        } catch (IOException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Navegación",
                "Lo sentimos no fue posible regresar al Dashbord");
        }     
    }
    
}
