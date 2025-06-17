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
 * Descripcion: Controlador para configurar las acciones del coordinador
 */
public class FXMLCoordinadorController implements Initializable, Dashboard {

    @FXML
    private Label lbUsuario;
    
    private Usuario coordinador;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void btnCerrarSesion(ActionEvent event) {
        Utilidad.cerrarSesion(lbUsuario);
    }

    @FXML
    private void clicAsignarProyectos(MouseEvent event) {
       try {
            Stage escenarioBase = Utilidad.obtenerEscenario(lbUsuario);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                    getResource("vista/FXMLAsignarProyectos.fxml"));
            Parent vista = cargador.load();
            FXMLAsignarProyectosController controlador = cargador.getController();
            controlador.inicializar(coordinador);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Dasboard Estudiante");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error al cargar la ventana",
                    "Lo sentimos no fue posible cargar la informacion");
        }
    }

    @FXML
    private void clicGenerarOficios(MouseEvent event) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Funcionalidad en desarrollo",
                "La funcionalidad de este apartado sigue en desarrollo");
    }

    @FXML
    private void clicRegistrarOV(MouseEvent event) {
        try {
            Stage escenarioBase = Utilidad.obtenerEscenario(lbUsuario);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                    getResource("vista/FXMLRegistrarOrganizacionVinculada.fxml"));
            Parent vista = cargador.load();
            FXMLRegistrarOrganizacionVinculadaController controlador = cargador.getController();
            controlador.inicializar(coordinador);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Dasboard Estudiante");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error al cargar la ventana",
                    "Lo sentimos no fue posible cargar la informacion");
        }
    }

    @FXML
    private void clicRegistrarResponsable(MouseEvent event) {
        Utilidad.abrirVentana("BuscarOrganizacionVinculada", lbUsuario);
    }

    @FXML
    private void clicRegistrarProyecto(MouseEvent event) {
        Utilidad.abrirVentana("RegistrarProyecto", lbUsuario);
    }
    
    @FXML
    private void clicGenerarEntregas(MouseEvent event) {
        Utilidad.abrirVentana("ProgramarEntregas", lbUsuario);
    }

    @FXML
    private void clicActualizarResponsable(MouseEvent event) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Funcionalidad en desarrollo",
                "La funcionalidad de este apartado sigue en desarrollo");
    }

    @FXML
    private void clicActualizarProyecto(MouseEvent event) {
        Utilidad.abrirVentana("BuscarProyecto", lbUsuario);
    }

    @Override
    public void inicializar(Usuario usuario) {
        lbUsuario.setText(usuario.getNombre() +" "+ usuario.getApellidoPaterno());
        this.coordinador = usuario;
    } 

}
