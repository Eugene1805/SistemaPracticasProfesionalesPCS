package sistemadepracticasprofesionales.controlador;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import sistemadepracticasprofesionales.SistemaDePracticasProfesionales;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;

/**
 *
 * @author eugen
 * Fecha: 11/06/25
 * Descripcion: Fabrica para los dashboards
 */
public class DashboardFactory {
    public static Scene crearDashboard(Usuario usuario) throws IOException {
        String fxmlPath;
        String titulo;
        
        switch(usuario.getTipoUsuario()) {
            case "COORDINADOR":
                fxmlPath = "vista/FXMLCoordinador.fxml";
                titulo = "Dashboard Coordinador";
                break;
            case "PROFESOR":
                fxmlPath = "vista/FXMLProfesor.fxml";
                titulo = "Dashboard Profesor";
                break;
            case "ESTUDIANTE":
                fxmlPath = "vista/FXMLEstudiante.fxml";
                titulo = "Dashboard Estudiante";
                break;
            case "EVALUADOR":
                fxmlPath = "vista/FXMLEvaluador.fxml";
                titulo = "Dashboard Evaluador";
                break;
            default:
                throw new IllegalArgumentException("Tipo de usuario no válido");
        }
        
        FXMLLoader loader = new FXMLLoader(SistemaDePracticasProfesionales.class.getResource(fxmlPath));
        Parent vista = loader.load();
        Dashboard controlador = loader.getController();
        controlador.inicializar(usuario);
        
        Scene escena = new Scene(vista);
        escena.getProperties().put("titulo", titulo); // Guardamos el título en la escena
        return escena;
    }
    
    public static class DashboardImpl implements Dashboard {
        private final Parent vista;
        private final Dashboard controlador;
        private final String titulo;
        
        public DashboardImpl(Parent vista, Dashboard controlador, String titulo) {
            this.vista = vista;
            this.controlador = controlador;
            this.titulo = titulo;
        }
        
        @Override
        public void inicializar(Usuario usuario) {
            controlador.inicializar(usuario);
        }
        
        public String getTitulo() {
            return titulo;
        }
    }
}
