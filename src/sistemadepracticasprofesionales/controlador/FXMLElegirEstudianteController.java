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
import sistemadepracticasprofesionales.modelo.pojo.Estudiante;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacionTabla();
    }    

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        Alert alerta = Utilidad.mostrarAlertaConfirmacion("Confirmacion Cancelar",
                "¿Estás seguro de que deseas cancelar?");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if(resultado.get() == ButtonType.APPLY){
            Utilidad.abrirVentana("Profesor", tvEstudiantes);                    
        }
    }
    
    private void configurarTabla(){
        tcNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        tcMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        tvEstudiantes.setRowFactory(tv -> {
            TableRow<Estudiante> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!row.isEmpty())){
                    System.err.println("Intentando ir a ElegirEntrega...");
                    irAElegirEntrega(tvEstudiantes.getSelectionModel().getSelectedItem());
                    System.err.println("Se ejecuto el irAElegirEntrega");
                }
            }
            );
            return row;
        });
    }
    
    private void cargarInformacionTabla(){
        try {
            estudiantes = FXCollections.observableArrayList();
            List<Estudiante> estudiantesDAO = EstudianteDAO.obtenerEstudiantesConEntregasSinValidar();
            estudiantes.addAll(estudiantesDAO);
            tvEstudiantes.setItems(estudiantes);
        } catch (SQLException ex) {
            ex.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar los datos",
                    "No fue posible cargar la informacion, intente mas tarde");
            Utilidad.obtenerEscenario(tvEstudiantes).close();
        }
    }
    
    private void irAElegirEntrega(Estudiante estudiante){
        try {
            Stage escenarioBase = Utilidad.obtenerEscenario(tvEstudiantes);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                    getResource("vista/FXMLElegirEntrega.fxml"));
            Parent vista = cargador.load();
            FXMLElegirEntregaController controlador = cargador.getController();
            controlador.inicializarInformacion(estudiante);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Elegir Entrega");
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error al cargar la pagina de entregas del estudiante",
                    "Lo sentimos no fue posible cargar la informacion del estudiante");
        }
    }
}
