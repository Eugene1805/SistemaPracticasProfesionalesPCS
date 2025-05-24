package sistemadepracticasprofesionales.utilidades;

import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.stage.Stage;

/**
 *
 * @author eugen
 * Fecha: 21/05/25
 * Descripcion: Clase altamente reutilizable usada para operaciones repetitivas como mostrar ventanas emergentes
 */
public class Utilidad {
    public static void mostrarAlertaSimple(Alert.AlertType tipo, String titulo, String contenido){
        Alert alerta = new Alert(tipo);
                alerta.setTitle(titulo);
                alerta.setHeaderText(null);
                alerta.setContentText(contenido);
                alerta.showAndWait();
    }
    
    public static Stage obtenerEscenario(Control component){
        return ((Stage) component.getScene().getWindow());
    }
}
