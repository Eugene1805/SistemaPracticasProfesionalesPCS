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
import javafx.stage.Modality; // Importante para la navegación correcta
import javafx.stage.Stage;
import sistemadepracticasprofesionales.SistemaDePracticasProfesionales;
import sistemadepracticasprofesionales.modelo.dao.ProyectoDAO;
import sistemadepracticasprofesionales.modelo.pojo.Proyecto;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;
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
    private Usuario coordinador;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        tvProyecto.setPlaceholder(new Label("No se han realizado búsquedas."));
    }
    
    // Este método es crucial. Se llama desde FXMLCoordinadorController
    public void inicializar(Usuario usuario){
        this.coordinador = usuario;
    }
    
    private void configurarTabla() {
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcCupo.setCellValueFactory(new PropertyValueFactory<>("cupo"));
        tcEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }
    
    @FXML
    private void btnClicRegresar(ActionEvent event) {
        // --- DEFENSA CONTRA NULLPOINTEREXCEPTION ---
        // Primero, verificamos si tenemos la información del coordinador.
        if (this.coordinador == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Sesión", 
                    "No se pudo recuperar la información del coordinador. Volviendo a la pantalla de inicio de sesión.");
            // Si el estado se pierde, la opción más segura es cerrar sesión.
            Utilidad.cerrarSesion(tfBuscar); 
            return; // Detenemos la ejecución del método aquí.
        }
        
        // Si todo está bien, procedemos a regresar.
        try {
            Stage escenarioBase = Utilidad.obtenerEscenario(tfBuscar);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.getResource("vista/FXMLCoordinador.fxml"));
            Parent vista = cargador.load();
            FXMLCoordinadorController controlador = cargador.getController();
            controlador.inicializar(this.coordinador); // Usamos el objeto guardado
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Menú Coordinador");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error al cargar el dashboard",
                    "Lo sentimos, no fue posible cargar la información del coordinador.");
        }
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
            // Si se busca con el campo vacío, se pueden mostrar todos los proyectos
            try {
                List<Proyecto> resultados = ProyectoDAO.buscarProyectosPorNombre(filtro); // Asumiendo que tienes este método
                proyectos = FXCollections.observableArrayList(resultados);
                tvProyecto.setItems(proyectos);
            } catch (SQLException ex) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de conexión", "No fue posible obtener los proyectos");
            }
        }
    }

    
    @FXML
    private void btnClicAceptar(ActionEvent event) {
        Proyecto proyectoSeleccionado = tvProyecto.getSelectionModel().getSelectedItem();
        if (proyectoSeleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección", "Selecciona un proyecto para continuar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistemadepracticasprofesionales/vista/FXMLActualizarProyecto.fxml"));
            Parent vista = loader.load();

            FXMLActualizarProyectoController controlador = loader.getController();
            Proyecto proyectoCompleto = ProyectoDAO.obtenerProyecto(proyectoSeleccionado.getIdProyecto());
            controlador.inicializarFormulario(proyectoCompleto, this.coordinador);

            Stage escenario = new Stage();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Actualizar Proyecto");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();

            // Refrescar la tabla al volver
            btnClicBuscar(null);

        } catch (IOException | SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "No se pudo abrir la ventana para actualizar el proyecto.");
            e.printStackTrace();
        }
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        Alert alerta = Utilidad.mostrarAlertaConfirmacion("Confirmacion Cancelar", 
                "¿Está seguro de que desea cancelar y volver al menú?");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if(resultado.isPresent() && resultado.get() == ButtonType.APPLY){
            btnClicRegresar(event); // Reutilizamos la lógica del botón regresar           
        }
    }
}