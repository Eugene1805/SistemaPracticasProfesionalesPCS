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
import sistemadepracticasprofesionales.modelo.dao.OrganizacionVinculadaDAO;
import sistemadepracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;
import sistemadepracticasprofesionales.utilidades.Utilidad;
import sistemadepracticasprofesionales.utilidades.validacion.ValidadorFormulario;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.NumericValidationStrategy;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.PhoneValidationStrategy;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.TextLetterValidationStrategy;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.TextValidationStrategy;

/**
 * FXML Controller class
 *
 * @author eugen
 * 
 * Fecha:23/05/25
 * Descripcion: Gestiona las interacciones entre la vista y el DAO de OrganizacionVinculada para poder hacer 
 * registros de una Organizacion VInculada en el formulario mostrado en la vista
 */
public class FXMLRegistrarOrganizacionVinculadaController implements Initializable {

    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfDireccion;
    @FXML
    private TextField tfCiudad;
    @FXML
    private TextField tfSector;
    @FXML
    private TextField tfNumUsuariosDirectos;
    @FXML
    private TextField tfNumUsuariosIndirectos;
    @FXML
    private TextField tfEstado; 
    @FXML
    private TextField tfRazonSocial;

    private ValidadorFormulario validadorFormulario;
    private Usuario coordinador;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarValidaciones();
    }    

    @FXML
    private void clicBtnRegresar(ActionEvent event) {
        try {
            Stage escenarioBase = Utilidad.obtenerEscenario(tfCiudad);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                    getResource("vista/FXMLCoordinador.fxml"));
            Parent vista = cargador.load();
            FXMLCoordinadorController controlador = cargador.getController();
            controlador.inicializar(coordinador);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Dasboard Coordinador");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error al cargar el dashboard del estudiante",
                    "Lo sentimos no fue posible cargar la informacion del coordinador");
        }
    }
    
    public void inicializar(String nombre){
        this.coordinador = new Usuario();
        this.coordinador.setNombre(nombre);
    }

    @FXML
    private void btnClicAceptar(ActionEvent event) {
        if(validadorFormulario.validate()){
            OrganizacionVinculada organizacionVinculada = obtenerOrganizacionVinculada();
            guardarOrganizacionVinculada(organizacionVinculada);
        }else{
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Datos invalidos",
                    "Hay campos con datos invalidos");
        }
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        Alert alerta = Utilidad.mostrarAlertaConfirmacion("Confirmacion Cancelar",
                "¿Estás seguro de que deseas cancelar?");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if(resultado.get() == ButtonType.APPLY){
           limpiarCampos();
        }
    }
    
    private OrganizacionVinculada obtenerOrganizacionVinculada(){
        OrganizacionVinculada organizacionVinculada = new OrganizacionVinculada();
        organizacionVinculada.setRazonSocial(tfRazonSocial.getText());
        organizacionVinculada.setTelefono(tfTelefono.getText());
        organizacionVinculada.setDireccion(tfDireccion.getText());
        organizacionVinculada.setCiudad(tfCiudad.getText());
        organizacionVinculada.setEstado(tfEstado.getText());
        organizacionVinculada.setSector(tfSector.getText());
        organizacionVinculada.setNumeroUsuariosIndirectos(Integer.parseInt(tfNumUsuariosIndirectos.getText()));
        organizacionVinculada.setNummeroUsuariosDirectos(Integer.parseInt(tfNumUsuariosDirectos.getText()));
        return organizacionVinculada;
    } 
    
    private void guardarOrganizacionVinculada(OrganizacionVinculada organizacionVinculada){
        try {
            ResultadoOperacion resultadoOperacion = OrganizacionVinculadaDAO.
                    registrarOrganizacionVinculada(organizacionVinculada);
            if(!resultadoOperacion.isError()){
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Operacion exitosa", 
                        resultadoOperacion.getMensaje());
                Utilidad.abrirVentana("Coordinador", tfTelefono);
            }else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "No se pudo registrar", 
                        resultadoOperacion.getMensaje());
            }
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "No hay conexion",
                    "Lo sentimos no fue posible conectarnos a la base de datos");
        }
    }
    
    private void inicializarValidaciones(){
        validadorFormulario = new ValidadorFormulario();
        validadorFormulario.addValidation(tfTelefono, new PhoneValidationStrategy(10, true));
        validadorFormulario.addValidation(tfDireccion, new TextValidationStrategy(255, true));
        validadorFormulario.addValidation(tfCiudad, new TextLetterValidationStrategy(30, true));
        validadorFormulario.addValidation(tfEstado, new TextLetterValidationStrategy(30, true));
        validadorFormulario.addValidation(tfSector, new TextLetterValidationStrategy(100, true));
        validadorFormulario.addValidation(tfRazonSocial, new TextValidationStrategy(45, true));
        validadorFormulario.addValidation(tfNumUsuariosDirectos, new NumericValidationStrategy(true));
        validadorFormulario.addValidation(tfNumUsuariosIndirectos, new NumericValidationStrategy(true));
        validadorFormulario.addCleanupAction(()->{
           Stream.of(tfTelefono, tfDireccion, tfRazonSocial, tfNumUsuariosDirectos,tfNumUsuariosIndirectos,
                   tfCiudad,tfEstado,tfSector)
          .filter(tf -> !tf.getStyle().isEmpty())
          .findFirst()
          .ifPresent(Control::requestFocus);//Le da el foco al primer campo con texto vacio
        });
    }
    
    private void limpiarCampos(){
        tfTelefono.setText("");
        tfDireccion.setText("");
        tfCiudad.setText("");
        tfEstado.setText("");
        tfSector.setText("");
        tfRazonSocial.setText("");
        tfNumUsuariosDirectos.setText("");
        tfNumUsuariosIndirectos.setText("");
    }
}
