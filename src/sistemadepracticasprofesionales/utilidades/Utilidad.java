package sistemadepracticasprofesionales.utilidades;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
    
    public static Alert mostrarAlertaConfirmacion(String titulo, String encabezado){
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.getButtonTypes().clear();
        alerta.getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
        return alerta;
    }
    
    public static Stage obtenerEscenario(Control component){
        return ((Stage) component.getScene().getWindow());
    }
}
