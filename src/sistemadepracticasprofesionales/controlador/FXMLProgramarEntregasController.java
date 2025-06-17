package sistemadepracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sistemadepracticasprofesionales.SistemaDePracticasProfesionales;
import sistemadepracticasprofesionales.modelo.dao.PeriodoEscolarDAO;
import sistemadepracticasprofesionales.modelo.dao.TipoDocumentoInicialDAO;
import sistemadepracticasprofesionales.modelo.pojo.PeriodoEscolar;
import sistemadepracticasprofesionales.modelo.pojo.TipoDocumentoInicial;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;
import sistemadepracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author Nash
 * Fecha: 10/06/2025
 * Descripción: Controller para gestionar las interacciones entre su vista, que sirve de puente para indicar
 * el tipo de entrega que se deberá programar
 */
public class FXMLProgramarEntregasController implements Initializable {

    @FXML
    private Label lblPeriodoEscolar;
    @FXML
    private Button btnEntregasIniciales;
    
    private PeriodoEscolar periodoEscolarActual;
    private Usuario coordinador;
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarPeriodoEscolarActual();
    }    
    
    public void inicializar(Usuario usuario){
        this.coordinador = usuario;
    }
    
    private void cargarPeriodoEscolarActual(){
        try{
            periodoEscolarActual = PeriodoEscolarDAO.obtenerPeriodoEscolarActual();
            if (periodoEscolarActual != null) {
                lblPeriodoEscolar.setText(periodoEscolarActual.getNombrePeriodo());
            }else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin Periodo Escolar",
                    "No hay un periodo escolar activo configurado. No se pueden programar entregas.");
                btnEntregasIniciales.setDisable(true);
            }
        }catch(SQLException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error en la carga",
                "Lo sentimos, por el momento no fue posible cargar el Perido Escolar Actual");
        }   
    }
    
    @FXML
    private void btnClicRegresar(ActionEvent event) {
        regresarAlDashbord();
    }

    @FXML
    private void btnClicEntregaIniciales(ActionEvent event) {
            try {
                List<TipoDocumentoInicial> tiposInicialesDisponibles = TipoDocumentoInicialDAO.obtenerTiposDisponibles(
                periodoEscolarActual.getId());
                if (tiposInicialesDisponibles.isEmpty()) {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Entegas Iniciales Programadas", 
                            "Todas las entregas inicales han sido programadas");

                } else{
                    irFormularioGenerarEntregas("Inicial");
                }
            } catch (SQLException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al Verificar Entregas",
                    "No se pudo verificar el estado de las entregas programadas.");
            }     
    }

    private void irFormularioGenerarEntregas(String tipoEntrega){
        try{
            Stage escenaActual = Utilidad.obtenerEscenario(lblPeriodoEscolar);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.getResource("/sistemadepracticasprofesionales/vista/FXMLGenerarEntregas.fxml"));
            Parent vista = cargador.load();
            FXMLGenerarEntregasController controlador = cargador.getController();
            controlador.inicializarDatos(tipoEntrega, periodoEscolarActual);
            controlador.inicializar(coordinador);
            Scene escena = new Scene(vista);
            escenaActual.setScene(escena);
            escenaActual.setTitle("Generar Entrega Iniciales");
        }catch(IOException e){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error al cargar la ventana Generar Entregas",
                    "Lo sentimos no fue posible cargar la ventana");              
        }
    }
    
    @FXML
    private void btnClicEntregaFinales(ActionEvent event) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Funcionalidad en desarrollo",
                "La funcionalidad de este apartado sigue en desarrollo");
    }

    @FXML
    private void btnClicEntregaIntermedios(ActionEvent event) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Funcionalidad en desarrollo",
                "La funcionalidad de este apartado sigue en desarrollo");
    }

    @FXML
    private void btnClicEntregaReportes(ActionEvent event) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Funcionalidad en desarrollo",
                "La funcionalidad de este apartado sigue en desarrollo");
    }
    
    private void regresarAlDashbord() {
        try{
            Stage escenarioBase = Utilidad.obtenerEscenario(lblPeriodoEscolar);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                    getResource("vista/FXMLCoordinador.fxml"));
            Parent vista = cargador.load();
            FXMLCoordinadorController controlador = cargador.getController();
            controlador.inicializar(coordinador);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Dashboard Coordinador");
            escenarioBase.show();
        } catch (IOException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error al cargar el dashboard del coordinador",
                "Lo sentimos no fue posible cargar la informacion del coordinador");
        }     
    }
}
