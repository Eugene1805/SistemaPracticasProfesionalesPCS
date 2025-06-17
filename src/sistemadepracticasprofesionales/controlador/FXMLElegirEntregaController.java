package sistemadepracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sistemadepracticasprofesionales.SistemaDePracticasProfesionales;
import sistemadepracticasprofesionales.modelo.dao.EntregaDAO;
import sistemadepracticasprofesionales.modelo.pojo.Entrega;
import sistemadepracticasprofesionales.modelo.pojo.Estudiante;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;
import sistemadepracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author eugen
 * Fecha: 10/06/25
 * Descripcion: Controller para mostrar las entregas disponibles para calificar dado un estudiante seleccionado
 */
public class FXMLElegirEntregaController implements Initializable {

    @FXML
    private TableView<Entrega> tvEntregas;
    @FXML
    private TableColumn<Entrega, String> tcTipoEntrega;
    @FXML
    private TableColumn<Entrega, String> tcDocumento;

    private Estudiante estudianteSeleccionado;
    private ObservableList<Entrega> listaEntregas;
    
    private Usuario profesor;
    private int idProfesor;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
    }    

    public void inicializar(Estudiante estudiante, Usuario profesor, int idProfesor) {
        this.estudianteSeleccionado = estudiante;
        this.profesor = profesor;
        this.idProfesor = idProfesor;
        if (estudianteSeleccionado != null) {
            cargarInformacionTabla();
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, 
                "Error", "No se recibió información del estudiante");
        }
    }
    
    private void configurarTabla(){
        tcTipoEntrega.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        tcDocumento.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        
        tvEntregas.setRowFactory(tv -> {
            TableRow<Entrega> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!row.isEmpty())){
                    irAValidarEntrega(tvEntregas.getSelectionModel().getSelectedItem());
                }
            });
            return row;
        });
    }
    
    private void cargarInformacionTabla(){
        try {
            listaEntregas = FXCollections.observableArrayList(
                EntregaDAO.obtenerEntregasSinValidarPorEstudiante(estudianteSeleccionado.getId(),idProfesor)
            );
            tvEntregas.setItems(listaEntregas);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de conexión",
                    "No se pudo conectar a la base de datos.");
        }
    }
    
    private void irAValidarEntrega(Entrega entrega) {
        try {
            Stage escenarioBase = Utilidad.obtenerEscenario(tvEntregas);
            FXMLLoader cargador = new FXMLLoader(getClass()
                .getResource("/sistemadepracticasprofesionales/vista/FXMLValidarEntrega.fxml"));
            Parent vista = cargador.load();

            FXMLValidarEntregaController controlador = cargador.getController();
            controlador.inicializarInformacion(estudianteSeleccionado, entrega, profesor, idProfesor);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Validar Entrega");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, 
                "Error", "No se pudo cargar la ventana: " + ex.getMessage());
        }
    }

    @FXML
    private void clicBtnRegresar(ActionEvent event) {
        try {
            Stage escenarioBase = Utilidad.obtenerEscenario(tvEntregas);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                    getResource("vista/FXMLElegirEstudiante.fxml"));
            Parent vista = cargador.load();
            FXMLElegirEstudianteController controlador = cargador.getController();
            controlador.inicializar(profesor,idProfesor);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Elegir Estudiante");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error al cargar",
                    "No se pudo abrir la ventana de seleccion de estudiantes");
        }
    }
}
