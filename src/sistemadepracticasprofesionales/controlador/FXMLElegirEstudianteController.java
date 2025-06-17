package sistemadepracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sistemadepracticasprofesionales.SistemaDePracticasProfesionales;
import sistemadepracticasprofesionales.modelo.dao.EstudianteDAO;
import sistemadepracticasprofesionales.modelo.dao.PeriodoEscolarDAO;
import sistemadepracticasprofesionales.modelo.pojo.Estudiante;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;
import sistemadepracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author eugen
 * Fecha: 10/06/25
 * Descripcion: Controller para que un profesor eliga el estudiante del que deseea validar entregas
 */
public class FXMLElegirEstudianteController implements Initializable {

    @FXML
    private TableView<Estudiante> tvEstudiantes;
    @FXML
    private TableColumn<Estudiante, String> tcNombre;
    @FXML
    private TableColumn<Estudiante, String> tcMatricula;
    
    private ObservableList<Estudiante> estudiantes;
    private Usuario profesor;
    private int idProfesor;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        Alert alerta = Utilidad.mostrarAlertaConfirmacion("Confirmacion Cancelar",
                "¿Estás seguro de que deseas cancelar?");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if(resultado.get() == ButtonType.APPLY){
            irAlDashboard();
        }
    }
    
    private void configurarTabla(){
        tcNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        tcMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        tvEstudiantes.setRowFactory(tv -> {
            TableRow<Estudiante> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!row.isEmpty())){
                    irAElegirEntrega(tvEstudiantes.getSelectionModel().getSelectedItem());
                }
            }
            );
            return row;
        });
    }
    
    private void cargarInformacionTabla(){
        try {
            estudiantes = FXCollections.observableArrayList();
            List<Estudiante> estudiantesDAO = EstudianteDAO.obtenerEstudiantesConEntregasSinValidar(
                    PeriodoEscolarDAO.obtenerPeriodoEscolarActual().getId(),idProfesor);
            estudiantes.addAll(estudiantesDAO);
            tvEstudiantes.setItems(estudiantes);
        } catch (SQLException ex) {
            ex.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar los datos",
                    "No fue posible cargar la informacion, intente mas tarde");
        }
    }
    
    private void irAElegirEntrega(Estudiante estudiante){
        try {
            Stage escenarioBase = Utilidad.obtenerEscenario(tvEstudiantes);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                    getResource("vista/FXMLElegirEntrega.fxml"));
            Parent vista = cargador.load();
            FXMLElegirEntregaController controlador = cargador.getController();
            controlador.inicializar(estudiante, profesor,idProfesor);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Elegir Entrega");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error al cargar la pagina de entregas del estudiante",
                    "Lo sentimos no fue posible cargar la informacion del estudiante");
        }
    }
    
    public void inicializar(Usuario usuario, int idProfesor){
        this.profesor = usuario;
        this.idProfesor = idProfesor;
        configurarTabla();
        cargarInformacionTabla();
    }
    
    private void irAlDashboard(){
        try {
            Stage escenarioBase = Utilidad.obtenerEscenario(tvEstudiantes);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                    getResource("vista/FXMLProfesor.fxml"));
            Parent vista = cargador.load();
            FXMLProfesorController controlador = cargador.getController();
            controlador.inicializar(profesor);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Dashboard Profesor");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error al cargar el dashboard del profesor",
                    "Lo sentimos no fue posible cargar la informacion del profesor");
        }
    }
}
