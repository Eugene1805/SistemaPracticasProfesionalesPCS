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
import sistemadepracticasprofesionales.modelo.dao.ExperienciaEducativaDAO;
import sistemadepracticasprofesionales.modelo.dao.PeriodoEscolarDAO;
import sistemadepracticasprofesionales.modelo.dao.ProfesorDAO;
import sistemadepracticasprofesionales.modelo.pojo.ExperienciaEducativa;
import sistemadepracticasprofesionales.modelo.pojo.PeriodoEscolar;
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
        try {
            int idProfesor = ProfesorDAO.obtenerProfesorPorUsername(profesor.getUsername()).getIdProfesor();
            Stage escenarioBase = Utilidad.obtenerEscenario(lbUsuario);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                    getResource("vista/FXMLElegirEstudiante.fxml"));
            Parent vista = cargador.load();
            FXMLElegirEstudianteController controlador = cargador.getController();
            controlador.inicializar(profesor, idProfesor);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Consultar Avance");
            escenarioBase.show();
        } catch (IOException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Carga", 
                    "No se pudo abrir la ventana de seleccion de estudiantes");
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de conexio",
                    "No fue posible cargar la infromacion del profesor");
        }
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
                    escenarioBase.setTitle("Buscar Estudiante");
                    escenarioBase.show();
                } catch (IOException e) {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Carga", 
                            "No se pudo abrir la ventana de Búsqueda de estudiantes.");
                }
            }else {
               Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Profesor no encontrado",
                        "No pudimos encontrar un profesor asociado al usuario " + profesor.getUsername()); 
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Conexión",
                    "Lo sentimos, por el momento no pudimos consultar su información con la base de datos, inténtelo más tarde.");
        }
    }  
        
    @Override
    public void inicializar(Usuario usuario) {
        lbUsuario.setText(usuario.getNombre() + " " + usuario.getApellidoPaterno());
        profesor = usuario;
    }    
}
