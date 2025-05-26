package sistemadepracticasprofesionales;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sistemadepracticasprofesionales.utilidades.Utilidad;

/**
 *
 * @author eugen
 * Fecha:21/05/25
 * Descripcion:Punto de entrada al sistema a traves de un log in
 * 
 */
public class SistemaDePracticasProfesionales extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try{
            Parent vista = FXMLLoader.load(getClass().getResource("vista/FXMLInicioSesion.fxml"));
            Scene escenaInicioSesion = new Scene(vista);
            primaryStage.setScene(escenaInicioSesion);
            primaryStage.setTitle("Inicio de sesion");
            primaryStage.show();
        }catch(IOException e){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "No se pudo cargar el sistema",
                    "Lo sentimos no fue posible cargar el inicio de sesion");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
