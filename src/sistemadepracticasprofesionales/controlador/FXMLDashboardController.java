package sistemadepracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sistemadepracticasprofesionales.SistemaDePracticasProfesionales;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;
import sistemadepracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author eugen
 * Fecha:27/05/25
 * Descripcion: Controlador de la vista principal a la que seran difigidos los usuarios despues de un log in exitoso
 * mostrara opciones diferentes dependiendo del tipo de usuario registrado en la base de datos
 */
public class FXMLDashboardController implements Initializable {

    @FXML
    private Button btnCerrarSesion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCerrarSesion.setOnAction(event -> Utilidad.cerrarSesion(btnCerrarSesion));
    }    
    
    public void inicializarInformacion(Usuario usuario){
        //TODO cambiar las opciones que se muestra dependiento el tipo de usuario
        switch (usuario.getTipoUsuario()) {
            case "COORDINADOR":
                irDashboard(usuario, "Coordinador");
                break;
            case "PROFESOR":
                irDashboard(usuario, "Profesor");
                break;
            case "ESTUDIANTE":
                irDashboard(usuario, "Estudiante");
                break;
            case "EVALUADOR":
                irDashboard(usuario, "Evaluador");
                break;
            default:
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Usuario no sin tipo",
                        "No pudimos reconocer el tipo de usuario, intente con otro usuario");
                Utilidad.cerrarSesion(btnCerrarSesion);
                break;
        }
    }

    
    private void irDashboard(Usuario usuario, String fxml){
        try {
            Stage escenarioBase = Utilidad.obtenerEscenario(btnCerrarSesion);
            StringBuilder ubicacion = new StringBuilder("vista/");
            ubicacion.append(fxml);
            ubicacion.append(".fxml");
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                    getResource(String.valueOf(ubicacion)));
            Parent vista = cargador.load();
            FXMLDashboardController controlador = cargador.getController();
            controlador.inicializarInformacion(usuario);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle(fxml);
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error al cargar la pagina principal",
                    "Lo sentimos no fue posible cargar la pagina principal");
        }
    }
}
