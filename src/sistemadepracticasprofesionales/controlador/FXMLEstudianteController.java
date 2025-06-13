package sistemadepracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sistemadepracticasprofesionales.SistemaDePracticasProfesionales;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;
import sistemadepracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author eugen
 * Fecha:27/05/25
 * Descripcion: Controlador para manejar las aciones del estudiante
 */
public class FXMLEstudianteController implements Initializable, Dashboard {

    @FXML
    private Label lbUsuario;
    
    private Usuario estudiante;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnCerrarSesion(ActionEvent event) {
        Utilidad.cerrarSesion(lbUsuario);
    }

    @FXML
    private void clicConsultarAvance(MouseEvent event) {
        Utilidad.abrirVentana("ConsultarAvance", lbUsuario);
    }

    @FXML
    private void clicActualizarExpediente(MouseEvent event) {
        Utilidad.abrirVentana("ActualizarExpediente", lbUsuario);
    }

    @FXML
    private void clicEvaluarOV(MouseEvent event) {
        try {
            Stage escenarioBase = Utilidad.obtenerEscenario(lbUsuario);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                    getResource("vista/FXMLEvaluarOrganizacionVinculada.fxml"));
            Parent vista = cargador.load();
            FXMLEvaluarOrganizacionVinculadaController controlador = cargador.getController();
            controlador.inicializarInformacion(estudiante);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Evaluar Organizacion Vinculada");
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error al cargar la evaluacion de la organizacio vinculada",
                    "Lo sentimos no fue posible cargar la informacion del estudiante");
        }
    }

    @FXML
    private void clicGenerarFormatoEvalOV(MouseEvent event) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Funcionalidad en desarrollo",
                "La funcionalidad de este apartado sigue en desarrollo");
    }

    @Override
    public void inicializar(Usuario usuario) {
        lbUsuario.setText(usuario.getNombre() + " " + usuario.getApellidoPaterno());
        estudiante = usuario;
    }    
}
