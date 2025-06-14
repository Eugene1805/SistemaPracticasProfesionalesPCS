package sistemadepracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistemadepracticasprofesionales.modelo.dao.InicioSesionDAO;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;
import sistemadepracticasprofesionales.utilidades.Utilidad;
import sistemadepracticasprofesionales.utilidades.validacion.ValidadorFormulario;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.TextValidationStrategy;

/**
 * FXML Controller class
 *
 * @author eugen
 * Fecha: 21/05/25
 * Descripcion: Gestiona las interacciones en entre la vista y el DAO del Usuario
 */
public class FXMLInicioSesionController implements Initializable {

    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPassword;

    private ValidadorFormulario validadorFormulario;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        validadorFormulario = new ValidadorFormulario();
        validadorFormulario.addValidation(tfUsername, new TextValidationStrategy(25, true));
        validadorFormulario.addValidation(pfPassword, new TextValidationStrategy(20, true));
    }    

    @FXML
    private void btnLogin(ActionEvent event) {
        if(validadorFormulario.validate()){
            validarCredenciales(tfUsername.getText(), pfPassword.getText());
        }
    }
    
    private void validarCredenciales(String username, String password){
        try {
            Usuario usuarioSesion = InicioSesionDAO.validarCredenciales(username, password);
            if(usuarioSesion != null){
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Credenciales correctas",
                        "Bienvenido(a) " + usuarioSesion.getUsername() + " al sistema");
                irPantallaPrincipal(usuarioSesion);
            }else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Credenciales incorrectas",
                        "Usuario y/o password incorrectos, por favor verifica tu informacion");
            }
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Error de conexion", 
                    "Lo sentimos no pudimos verificar las credenciales");
        }
    }
    
    private void irPantallaPrincipal(Usuario usuario){
        try {
            Stage escenarioBase = (Stage) tfUsername.getScene().getWindow();
            Scene escenaDashboard = DashboardFactory.crearDashboard(usuario);

            escenarioBase.setScene(escenaDashboard);
            escenarioBase.setTitle((String) escenaDashboard.getProperties().get("titulo"));
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", 
                "No se pudo cargar el dashboard: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Usuario sin tipo", 
                ex.getMessage());
            Utilidad.cerrarSesion(tfUsername);
        }
    }
    
}
