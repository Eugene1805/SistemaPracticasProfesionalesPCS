package sistemadepracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import sistemadepracticasprofesionales.modelo.dao.ProfesorDAO;
import sistemadepracticasprofesionales.modelo.pojo.Profesor;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;
import sistemadepracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author eugen
 * Fecha:27/05/25
 * Descripcion: Controlador para las acciones disponibles del profesor
 */
public class FXMLProfesorController implements Initializable, Dashboard {

    @FXML
    private Label lbUsuario;
    private Usuario profesor;

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
    private void clicValidarEntregas(MouseEvent event) {
        Utilidad.abrirVentana("ElegirEstudiante", lbUsuario);
    }

    @FXML
    private void clicConsultarExpediente(MouseEvent event) {
        try {
            Profesor profesorLogueado = ProfesorDAO.obtenerProfesorPorUsername(profesor.getUsername());
            if (profesorLogueado != null) {
                try {
                    Stage escenarioBase = Utilidad.obtenerEscenario(lbUsuario);
                    FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                            getResource("vista/FXMLBuscarEstudiante.fxml"));
                    Parent vista = cargador.load();
                    FXMLBuscarEstudianteController controlador = cargador.getController();
                    controlador.inicializarInformacion(profesorLogueado, profesor);
                    Scene escenaPrincipal = new Scene(vista);
                    escenarioBase.setScene(escenaPrincipal);
                    escenarioBase.setTitle("Consultar Avance");
                    escenarioBase.show();
                } catch (IOException e) {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Carga", 
                            "No se pudo abrir la ventana de consultar avance.");
                }
            }else {
               Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Profesor no encontrado",
                        "No se encontró un registro de profesor para el usuario " + profesor.getUsername()); 
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Conexión",
                    "No se pudo consultar la información del profesor.");
        }
    }

    @Override
    public void inicializar(Usuario usuario) {
        lbUsuario.setText(usuario.getNombre() + " " + usuario.getApellidoPaterno() );
        profesor = usuario;
    }    
}
