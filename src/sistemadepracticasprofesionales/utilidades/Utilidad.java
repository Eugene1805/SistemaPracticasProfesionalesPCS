package sistemadepracticasprofesionales.utilidades;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import sistemadepracticasprofesionales.SistemaDePracticasProfesionales;

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
    
    public static void abrirVentana(String nombreVentana, Control component){
        try {
            Stage escenarioBase = Utilidad.obtenerEscenario(component);
            StringBuilder ubicacion = new StringBuilder("vista/");
            ubicacion.append("FXML");
            ubicacion.append(nombreVentana);
            ubicacion.append(".fxml");
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                    getResource(String.valueOf(ubicacion)));
            Parent vista = cargador.load();
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle(nombreVentana);
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error al cargar la ventana" + nombreVentana,
                    "Lo sentimos no fue posible cargar la ventana");
        }
    }
    
    public static void cerrarSesion(Control component){
        try{
            Stage escenarioBase =(Stage) component.getScene().getWindow();
            
            Parent vista = FXMLLoader.load(SistemaDePracticasProfesionales.class.
                    getResource("vista/FXMLInicioSesion.fxml"));
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Inicio Sesion");
            escenarioBase.showAndWait();
        }catch(IOException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Error al cerrar sesion", 
                    "No pudimos cerrar la sesion, por favor intente mas tarde");
        }
    }
}
