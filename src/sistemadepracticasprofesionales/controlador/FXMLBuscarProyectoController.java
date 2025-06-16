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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sistemadepracticasprofesionales.modelo.dao.ProyectoDAO;
import sistemadepracticasprofesionales.modelo.pojo.Proyecto;
import sistemadepracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author meler
 * Fecha:12/06/25
 * Descripcion: Gestiona las interacciones entre la vista y el DAO del Proyecto para realizar las funcionalidades pertinentes
 * 
 */
public class FXMLBuscarProyectoController implements Initializable {

    @FXML
    private TextField tfBuscar;
    @FXML
    private Button btnBuscar;
    @FXML
    private TableView<Proyecto> tvProyecto;
    @FXML
    private TableColumn<Proyecto, String> tcNombre;
    @FXML
    private TableColumn<Proyecto, Integer> tcCupo;
    @FXML
    private TableColumn<Proyecto, String> tcEstado;
    
    private ObservableList<Proyecto> proyectos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        tvProyecto.setPlaceholder(new Label("No se han realizado búsquedas."));
    }
    
    private void configurarTabla() {
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcCupo.setCellValueFactory(new PropertyValueFactory<>("cupo"));
        tcEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }
    

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        Utilidad.abrirVentana("Coordinador", tfBuscar);
    }

    @FXML
    private void btnClicBuscar(ActionEvent event) {
        String filtro = tfBuscar.getText().trim();
        if (!filtro.isEmpty()) {
            try {
                List<Proyecto> resultados = ProyectoDAO.buscarProyectosPorNombre(filtro);
                proyectos = FXCollections.observableArrayList(resultados);
                tvProyecto.setItems(proyectos);
            } catch (SQLException ex) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de conexión", "No fue posible obtener los proyectos");
            }
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Campo vacío", "Introduce un nombre para buscar.");
        }
        }

    @FXML
    private void btnClicAceptar(ActionEvent event) throws SQLException {
        Proyecto proyectoSeleccionado = tvProyecto.getSelectionModel().getSelectedItem();

        if (proyectoSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistemadepracticasprofesionales/vista/FXMLActualizarProyecto.fxml"));
                Parent root = loader.load();

                // Obtener el controller y pasarle el proyecto
                Proyecto proyectoCompleto = ProyectoDAO.obtenerProyecto(proyectoSeleccionado.getIdProyecto());

                FXMLActualizarProyectoController controlador = loader.getController();
                controlador.inicializarFormulario(proyectoCompleto);


                // Mostrar la nueva ventana
                Scene escena = new Scene(root);
                Stage escenario = new Stage();
                escenario.setScene(escena);
                escenario.setTitle("Actualizar Proyecto");
                escenario.show();

                // Cerrar la ventana actual si lo deseas:
                Stage ventanaActual = (Stage) tfBuscar.getScene().getWindow();
                ventanaActual.close();

            } catch (IOException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR,
                    "Error al cargar la ventana",
                    "No se pudo abrir la ventana para actualizar el proyecto.");
            }
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING,
                "Sin selección",
                "Selecciona un proyecto para continuar.");
        }
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        Alert alerta = Utilidad.mostrarAlertaConfirmacion("Confirmacion Cancelar", 
                "¿Está seguro de que desea cancelar?");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if(resultado.get() == ButtonType.APPLY){
            Utilidad.abrirVentana("Coordinador", tfBuscar);                    
        }
    }
    
}
