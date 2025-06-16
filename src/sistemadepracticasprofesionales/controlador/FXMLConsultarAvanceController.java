package sistemadepracticasprofesionales.controlador;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sistemadepracticasprofesionales.SistemaDePracticasProfesionales;
import sistemadepracticasprofesionales.modelo.dao.AvanceDAO;
import sistemadepracticasprofesionales.modelo.dao.EstudianteDAO;
import sistemadepracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemadepracticasprofesionales.modelo.dao.ProfesorDAO;
import sistemadepracticasprofesionales.modelo.pojo.AvanceEntrega;
import sistemadepracticasprofesionales.modelo.pojo.Estudiante;
import sistemadepracticasprofesionales.modelo.pojo.Expediente;
import sistemadepracticasprofesionales.modelo.pojo.PeriodoEscolar;
import sistemadepracticasprofesionales.modelo.pojo.Profesor;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;
import sistemadepracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author Nash
 * Fecha: 10/06/2025
 * Descripción: Controller para gestionar las interacciones entre su vista, para mostrar todo el avance de las entregas 
 * del estudiante dado, permitiendo la descarga de los archivos entregados.
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
    private String origenLlamada;
    private Usuario usuarioActual;
    private PeriodoEscolar periodoActual;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
    
    public void inicializarInformacion(Estudiante estudiante, String origen, PeriodoEscolar periodo, Usuario usuario){
        this.estudianteConsulta = estudiante;
        this.origenLlamada = origen;
        this.periodoActual = periodo;
        this.usuarioActual = usuario;
        
        configurarTablas();
        cargarDatosGenerales();
        cargarDatosTablas();
    }
    
    private void cargarDatosGenerales(){
        lblNombreEstudiante.setText(estudianteConsulta.getNombre() + " " + estudianteConsulta.getApellidoPaterno() + " " + 
                estudianteConsulta.getApellidoMaterno());
        lblMatriculaEstudiante.setText(estudianteConsulta.getMatricula());
        cargarFoto();
        try{
            Expediente expediente = ExpedienteDAO.obtenerExpedienteActivoPorEstudiante(estudianteConsulta.getId());
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
    
    private void cargarFoto(){
        try{
            byte[] foto = EstudianteDAO.obtenerFotoEstudiante(estudianteConsulta.getId());
            if (foto != null && foto.length > 0) {
                ByteArrayInputStream inputFoto = new ByteArrayInputStream(foto);
                Image imagen = new Image(inputFoto);
                ivFotoEstudiante.setImage(imagen);
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Sin foto", 
                        "El estudiante no tiene una foto registrada");
               
            }
        }catch (SQLException e){
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "ERROR", 
                        "Error al obtener la foto del estudiante");
        }
    }
    private void cargarDatosTablas(){
        if(estudianteConsulta != null && periodoActual != null){
            try {
                documentosIniciales = FXCollections.observableArrayList(AvanceDAO.obtenerAvanceDocumentosIniciales(
                estudianteConsulta.getId(), periodoActual.getId()));
                tvDocumentosIniciales.setItems(documentosIniciales);
                tvDocumentosIniciales.setPlaceholder(new Label("No hay entregas de documentos iniciales en este periodo."));

                documentosIntermedios = FXCollections.observableArrayList(AvanceDAO.obtenerAvanceDocumentosIntermedios(
                        estudianteConsulta.getId(), periodoActual.getId()));
                tvDocumentosIntermedios.setItems(documentosIntermedios);
                tvDocumentosIntermedios.setPlaceholder(new Label("No hay entregas de documentos intermedios en este periodo."));

                documentosFinales = FXCollections.observableArrayList(AvanceDAO.obtenerAvanceDocumentosFinales(
                        estudianteConsulta.getId(), periodoActual.getId()));
                tvDocumentosFinales.setItems(documentosFinales);
                tvDocumentosFinales.setPlaceholder(new Label("No hay entregas de documentos finales en este periodo."));

                reportes = FXCollections.observableArrayList(AvanceDAO.obtenerAvanceReportes(
                        estudianteConsulta.getId(), periodoActual.getId()));
                tvReportes.setItems(reportes);
                tvReportes.setPlaceholder(new Label("No hay entregas de reportes en este perido."));
            } catch (SQLException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Carga", 
                        "No se pudo cargar el detalle de las entregas.");
            }
        }
    }

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        if ("Profesor".equals(origenLlamada)) {
            regresarABuscarEstudiante();
        } else {
            regresarAlDashboard();
        }
    }

    @FXML
    private void btnClicPDFInicial(ActionEvent event) {
        AvanceEntrega seleccion = tvDocumentosIniciales.getSelectionModel().getSelectedItem();
        if (seleccion != null) {
            descargarPDF(seleccion, "DOCUMENTO", "INICIAL");
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección", 
                    "Por favor, seleccione un documento inicial de la tabla para descargar.");
        }
    }

    @FXML
    private void btnClicPDFReporte(ActionEvent event) {
        AvanceEntrega seleccion = tvReportes.getSelectionModel().getSelectedItem();
        if (seleccion != null) {
            descargarPDF(seleccion, "REPORTE", "NINGUNO"); // Para reportes, el subtipo no importa
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección", 
                    "Por favor, seleccione un reporte de la tabla para descargar.");
        }
    }

    @FXML
    private void btnClicPDFIntermedio(ActionEvent event) {
        AvanceEntrega seleccion = tvDocumentosIntermedios.getSelectionModel().getSelectedItem();
        if (seleccion != null) {
            descargarPDF(seleccion, "DOCUMENTO", "INTERMEDIO");
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección", 
                    "Por favor, seleccione un documento intermedio de la tabla para descargar.");
        }
    }

    @FXML
    private void btnClicPDFFinal(ActionEvent event) {
        AvanceEntrega seleccion = tvDocumentosFinales.getSelectionModel().getSelectedItem();
        if (seleccion != null) {
            descargarPDF(seleccion, "DOCUMENTO", "FINAL");
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección", 
                    "Por favor, seleccione un documento final de la tabla para descargar.");
        }
    }
    
     private void descargarPDF(AvanceEntrega entrega, String tipo, String subtipo) {
        if (entrega.getFechaEntregado() == null || "N/A".equals(entrega.getFechaEntregado())) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Archivo no Disponible",
                    "El estudiante aún no ha subido un archivo para esta entrega. No se puede descargar.");
            return;
        }
        
        try {
            byte[] archivoBytes = AvanceDAO.obtenerArchivoDeEntrega(entrega.getIdArchivo(), tipo, subtipo);
            
            if (archivoBytes != null && archivoBytes.length > 0) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Guardar Archivo");

                String nombreLimpio = entrega.getTitulo().replaceAll("[^a-zA-Z0-9.-]", "_");
                fileChooser.setInitialFileName(nombreLimpio + ".pdf"); 
                
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Todos los archivos", "*.*"));

                File archivo = fileChooser.showSaveDialog(Utilidad.obtenerEscenario(lblNombreEstudiante));
                
                if (archivo != null) {
                    try (FileOutputStream fos = new FileOutputStream(archivo)) {
                        fos.write(archivoBytes);
                        Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Descarga Completa",
                                "El archivo se ha guardado correctamente en:\n" + archivo.getAbsolutePath());
                    } catch (IOException e) {
                        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Escritura", "No se pudo escribir el archivo en el disco. Verifique los permisos.");
                        e.printStackTrace();
                    }
                }
                
            } else {
                 Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Archivo Vacío o Corrupto",
                         "Se encontró un registro de entrega, pero el archivo está vacío o no se pudo leer de la base de datos.");
            }
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Base de Datos", "No se pudo obtener el archivo de la base de datos.");
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void btnClicSalir(ActionEvent event) {
        Alert alerta = Utilidad.mostrarAlertaConfirmacion("Confirmacion Salir", 
                "¿Está seguro de que desea salir?");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if(resultado.get() == ButtonType.APPLY){
                    regresarAlDashboard();
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
    
    private void regresarABuscarEstudiante() {
        try {
            Stage escenario = Utilidad.obtenerEscenario(lblNombreEstudiante);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.getResource(
                    "vista/FXMLBuscarEstudiante.fxml"));
            Parent vista = cargador.load();
            FXMLBuscarEstudianteController controlador = cargador.getController();
            Profesor profesor = ProfesorDAO.obtenerProfesorPorUsername(usuarioActual.getUsername());
            controlador.inicializarInformacion(profesor, usuarioActual);
            
            Scene escena = new Scene(vista);
            escenario.setScene(escena);
            escenario.setTitle("Buscar Estudiante");
        } catch (IOException | SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Navegación", "No se pudo volver a la pantalla de búsqueda.");
            e.printStackTrace();
        }
    }
    
    private void regresarAlDashboard() {
        try {
            Stage escenario = Utilidad.obtenerEscenario(lblNombreEstudiante);
            String fxmlDestino = "Profesor".equals(origenLlamada) ? 
                    "vista/FXMLProfesor.fxml" : "vista/FXMLEstudiante.fxml";
            
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.getResource(fxmlDestino));
            Parent vista = cargador.load();
            
            Dashboard dashboardControlador = cargador.getController();
            dashboardControlador.inicializar(this.usuarioActual);
            
            Scene escena = new Scene(vista);
            escenario.setScene(escena);
            escenario.setTitle("Dashboard");
        } catch (IOException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Navegación", "No se pudo volver a la pantalla principal.");
            e.printStackTrace();
        }
    }
    
}
