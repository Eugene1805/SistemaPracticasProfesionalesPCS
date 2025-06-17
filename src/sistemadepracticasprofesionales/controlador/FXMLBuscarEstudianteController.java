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
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sistemadepracticasprofesionales.SistemaDePracticasProfesionales;
import sistemadepracticasprofesionales.dominio.AvanceDM;
import sistemadepracticasprofesionales.modelo.dao.EstudianteDAO;
import sistemadepracticasprofesionales.modelo.dao.ExperienciaEducativaDAO;
import sistemadepracticasprofesionales.modelo.dao.PeriodoEscolarDAO;
import sistemadepracticasprofesionales.modelo.pojo.Estudiante;
import sistemadepracticasprofesionales.modelo.pojo.ExperienciaEducativa;
import sistemadepracticasprofesionales.modelo.pojo.PeriodoEscolar;
import sistemadepracticasprofesionales.modelo.pojo.Profesor;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;
import sistemadepracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author Nash
 * Fecha: 10/06/2025
 * Descripción: Controller para gestionar las interacciones entre su vista, donde se recuperarán los estudiantes 
 * del profesor logueado en el periodo escolar actual para consultar sus avances
 */
public class FXMLBuscarEstudianteController implements Initializable {

    @FXML
    private TableView<Estudiante> tvEstudiantes;
    @FXML
    private TableColumn tcMatricula;
    @FXML
    private TableColumn tcNombre;
    @FXML
    private TableColumn tcApellidoPaterno;
    @FXML
    private TableColumn tcApellidoMaterno;
    @FXML
    private TableColumn tcProyecto;

    private ObservableList<Estudiante> estudiantes;
    private Profesor profesorLogueado;
    private Usuario profesorUsuario;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
    }    
    
    public void inicializarInformacion(Profesor profesor, Usuario usuario){
        this.profesorLogueado = profesor;
        this.profesorUsuario = usuario;
        cargarInformacionTabla();  
    }

    private void configurarTabla(){
        tcMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        tcNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        tcApellidoPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        tcApellidoMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        tcProyecto.setCellValueFactory(new PropertyValueFactory("nombreProyecto"));
    }
    
    private void cargarInformacionTabla(){
        try{ 
            PeriodoEscolar periodoActual = PeriodoEscolarDAO.obtenerPeriodoEscolarActual();
                if (periodoActual != null) {
                    ExperienciaEducativa experienciaEducativa = ExperienciaEducativaDAO.obtenerEEPorProfesor(profesorLogueado.getIdProfesor());
                    if (experienciaEducativa != null) {
                        List<Estudiante> estudiantesDAO = EstudianteDAO.obtenerEstudiantesPorProfesorYPeriodo(experienciaEducativa.getId(),
                            periodoActual.getId());
                        if (estudiantesDAO != null && !estudiantesDAO.isEmpty()) {
                            estudiantes = FXCollections.observableArrayList(estudiantesDAO);
                            tvEstudiantes.setItems(estudiantes);
                        } else{
                            tvEstudiantes.setPlaceholder(new javafx.scene.control.Label("No hay estudiantes asignados a este profesor en el periodo actual."));
                        }                       
                    }else{
                        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error",
                                "El profesor no tiene una experiencia educativa asignada");
                        tvEstudiantes.setPlaceholder(new javafx.scene.control.Label("Profesor sin Experiencia Educativa asignada."));
                    }
                }else{
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", 
                            "No se pudo determinar el periodo escolar actual.");
                    tvEstudiantes.setPlaceholder(new javafx.scene.control.Label("No hay un periodo escolar activo en el sistema."));
                }    
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Carga", 
                    "No se pudieron cargar los estudiantes.");
            e.printStackTrace();
        }
    }
    @FXML
    private void btnClicRegresar(ActionEvent event) {
       regresarAlDashbord();
    }

    @FXML
    private void btnClicAceptar(ActionEvent event) {
        Estudiante estudianteSeleccionado = tvEstudiantes.getSelectionModel().getSelectedItem();
        if(estudianteSeleccionado != null){
            try {
                PeriodoEscolar periodoActual = PeriodoEscolarDAO.obtenerPeriodoEscolarActual();
                if (periodoActual != null) {
                    ResultadoOperacion resultado = AvanceDM.verificarExistenciaDeEntregas(
                            estudianteSeleccionado.getId(), periodoActual.getId());
                    if (!resultado.isError()) {
                        abrirConsultarAvance(estudianteSeleccionado, periodoActual);
                    }else{
                        Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Sin Entregas",
                                resultado.getMensaje());
                    }
                    
                }
            } catch (SQLException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Conexión", 
                        "No se pudo verificar el expediente.");
            }
        }else{
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección", 
                  "Por favor, seleccione un estudiante de la tabla.");  
        }
    }

    private void abrirConsultarAvance(Estudiante estudianteSeleccionado, PeriodoEscolar periodoActual) {
        try {
            Stage escenarioBase = Utilidad.obtenerEscenario(tvEstudiantes);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                        getResource("vista/FXMLConsultarAvance.fxml"));
            Parent vista = cargador.load();
            FXMLConsultarAvanceController controlador = cargador.getController();
            controlador.inicializarInformacion(estudianteSeleccionado, "Profesor", periodoActual, profesorUsuario);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Consultar Avance");
            escenarioBase.show();
        } catch (IOException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de carga",
                    "No se pudo abrir la ventana de Consultar Avance");
        }
    }
    
    private void regresarAlDashbord(){
         try{
            Stage escenarioBase = Utilidad.obtenerEscenario(tvEstudiantes);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                    getResource("vista/FXMLProfesor.fxml"));
            Parent vista = cargador.load();
            FXMLProfesorController controlador = cargador.getController();
            controlador.inicializar(profesorUsuario);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Dashboard Profesor");
            escenarioBase.show();
        } catch (IOException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error al cargar el dashboard del profesor",
                "Lo sentimos no fue posible cargar la informacion del profesor");
        } 
    }
}
