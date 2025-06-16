package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import sistemadepracticasprofesionales.modelo.dao.AvanceDAO;
import sistemadepracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemadepracticasprofesionales.modelo.pojo.AvanceEntrega;
import sistemadepracticasprofesionales.modelo.pojo.Estudiante;
import sistemadepracticasprofesionales.modelo.pojo.Expediente;
import sistemadepracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author Nash
 */
public class FXMLConsultarAvanceController implements Initializable {
    @FXML
    private ImageView ivFotoEstudiante;
    @FXML
    private Label lblHorasAcumuladas;
    @FXML
    private Label lblNombreEstudiante;
    @FXML
    private Label lblMatriculaEstudiante;
    @FXML
    private ProgressBar pbHorasAcumuladas;
    @FXML
    private TableView<AvanceEntrega> tvDocumentosIniciales;
    @FXML
    private TableColumn tcTituloIniciales;
    @FXML
    private TableColumn tcEntregadoIniciales;
    @FXML
    private TableColumn tcRevisadoIniciales;
    @FXML
    private TableColumn tcCalificacionIniciales;
    @FXML
    private TableColumn tcObservacionIniciales;
    @FXML
    private TableView<AvanceEntrega> tvReportes;
    @FXML
    private TableColumn tcTituloReportes;
    @FXML
    private TableColumn tcEntregadoReportes;
    @FXML
    private TableColumn tcRevisadoReportes;
    @FXML
    private TableColumn tcCalificacionReportes;
    @FXML
    private TableColumn tcObservacionReportes;
    @FXML
    private TableView<AvanceEntrega> tvDocumentosIntermedios;
    @FXML
    private TableColumn tcTituloIntermedios;
    @FXML
    private TableColumn tcEntregadoIntermedios;
    @FXML
    private TableColumn tcRevisadoIntermedios;
    @FXML
    private TableColumn tcCalificacionIntermedios;
    @FXML
    private TableColumn tcObservacionIntermedios;
    @FXML
    private TableView<AvanceEntrega> tvDocumentosFinales;
    @FXML
    private TableColumn tcTituloFinales;
    @FXML
    private TableColumn tcEntregadoFinales;
    @FXML
    private TableColumn tcRevisadoFinales;
    @FXML
    private TableColumn tcCalificacionFinales;
    @FXML
    private TableColumn tcObservacionFinales;
    
    private ObservableList<AvanceEntrega> documentosIniciales;
    private ObservableList<AvanceEntrega> documentosIntermedios;
    private ObservableList<AvanceEntrega> documentosFinales;
    private ObservableList<AvanceEntrega> reportes;    
    
    private Estudiante estudianteConsulta;
    private String usuarioLlamada;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
    
    public void inicializarInformacion(Estudiante estudiante, String origen){
        this.estudianteConsulta = estudiante;
        this.usuarioLlamada = origen;
        
        configurarTablas();
        cargarDatosGenerales();
        cargarDatosTablas();
    }
    
    private void cargarDatosGenerales(){
        lblNombreEstudiante.setText(estudianteConsulta.getNombre() + " " + estudianteConsulta.getApellidoPaterno() + " " + 
                estudianteConsulta.getApellidoMaterno());
        lblMatriculaEstudiante.setText(estudianteConsulta.getMatricula());
        try{
            Expediente expediente = ExpedienteDAO.obtenerExpedientePorEstudiante(estudianteConsulta.getId());
            if (expediente != null) {
                int horas = expediente.getHorasAcumuladas();
                lblHorasAcumuladas.setText(horas + " / 420 horas");
                pbHorasAcumuladas.setProgress((double) horas / 420.0);
            }
            
        }catch(SQLException e){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Conexión", 
                    "No se pudo cargar la información del expediente."); 
        }
    }
    
    private void cargarDatosTablas(){
        try {
            documentosIniciales = FXCollections.observableArrayList(AvanceDAO.obtenerAvanceDocumentosIniciales(
            estudianteConsulta.getId()));
            tvDocumentosIniciales.setItems(documentosIniciales);
            tvDocumentosIniciales.setPlaceholder(new Label("No hay entregas de documentos iniciales programadas."));
        
            documentosIntermedios = FXCollections.observableArrayList(AvanceDAO.obtenerAvanceDocumentosIntermedios(
                    estudianteConsulta.getId()));
            tvDocumentosIntermedios.setItems(documentosIntermedios);
            tvDocumentosIntermedios.setPlaceholder(new Label("No hay entregas de documentos intermedios programadas."));
                
            documentosFinales = FXCollections.observableArrayList(AvanceDAO.obtenerAvanceDocumentosFinales(
                    estudianteConsulta.getId()));
            tvDocumentosFinales.setItems(documentosFinales);
            tvDocumentosFinales.setPlaceholder(new Label("No hay entregas de documentos finales programadas."));

            reportes = FXCollections.observableArrayList(AvanceDAO.obtenerAvanceReportes(
                    estudianteConsulta.getId()));
            tvReportes.setItems(reportes);
            tvReportes.setPlaceholder(new Label("No hay entregas de reportes programadas."));
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Carga", 
                    "No se pudo cargar el detalle de las entregas.");
        }
    }

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        if("Profesor".equals(usuarioLlamada)){
            Utilidad.abrirVentana("BuscarEstudiante", tvReportes);
        }else{
            Utilidad.abrirVentana("Estudiante", tvReportes);
        }
    }

    @FXML
    private void btnClicPDFInicial(ActionEvent event) {
    }

    @FXML
    private void btnClicPDFReporte(ActionEvent event) {
    }

    @FXML
    private void btnClicPDFIntermedio(ActionEvent event) {
    }

    @FXML
    private void btnClicPDFFinal(ActionEvent event) {
    }

    @FXML
    private void btnClicSalir(ActionEvent event) {
        Alert alerta = Utilidad.mostrarAlertaConfirmacion("Confirmacion Cancelar",
                "¿Está seguro de que desea cancelar?");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if(resultado.get() == ButtonType.APPLY){
            if("Profesor".equals(usuarioLlamada)){
                Utilidad.abrirVentana("BuscarEstudiante", tvReportes);
            }else{
                Utilidad.abrirVentana("Estudiante", tvReportes);
            }
        }
    }
    
    private void configurarTablas(){
        //Documentos Iniciales
        tcTituloIniciales.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tcEntregadoIniciales.setCellValueFactory(new PropertyValueFactory<>("fechaEntregado"));
        tcRevisadoIniciales.setCellValueFactory(new PropertyValueFactory<>("fechaRevisado"));
        tcCalificacionIniciales.setCellValueFactory(new PropertyValueFactory<>("calificacion"));
        tcObservacionIniciales.setCellValueFactory(new PropertyValueFactory<>("observacion"));
        
        //Reportes
        tcTituloReportes.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tcEntregadoReportes.setCellValueFactory(new PropertyValueFactory<>("fechaEntregado"));
        tcRevisadoReportes.setCellValueFactory(new PropertyValueFactory<>("fechaRevisado"));
        tcCalificacionReportes.setCellValueFactory(new PropertyValueFactory<>("calificacion"));
        tcObservacionReportes.setCellValueFactory(new PropertyValueFactory<>("observacion"));
        
        //Documentos Intermedios
        tcTituloIntermedios.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tcEntregadoIntermedios.setCellValueFactory(new PropertyValueFactory<>("fechaEntregado"));
        tcRevisadoIntermedios.setCellValueFactory(new PropertyValueFactory<>("fechaRevisado"));
        tcCalificacionIntermedios.setCellValueFactory(new PropertyValueFactory<>("calificacion"));
        tcObservacionIntermedios.setCellValueFactory(new PropertyValueFactory<>("observacion"));

        //Documentos Finales
        tcTituloFinales.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tcEntregadoFinales.setCellValueFactory(new PropertyValueFactory<>("fechaEntregado"));
        tcRevisadoFinales.setCellValueFactory(new PropertyValueFactory<>("fechaRevisado"));
        tcCalificacionFinales.setCellValueFactory(new PropertyValueFactory<>("calificacion"));
        tcObservacionFinales.setCellValueFactory(new PropertyValueFactory<>("observacion"));
    }
    
}
