package sistemadepracticasprofesionales.controlador;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sistemadepracticasprofesionales.modelo.dao.DocumentoInicialDAO;
import sistemadepracticasprofesionales.modelo.dao.EntregaDAO;
import sistemadepracticasprofesionales.modelo.dao.EstudianteDAO;
import sistemadepracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemadepracticasprofesionales.modelo.pojo.Entrega;
import sistemadepracticasprofesionales.modelo.pojo.Estudiante;
import sistemadepracticasprofesionales.modelo.pojo.Expediente;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemadepracticasprofesionales.utilidades.Utilidad;

/**
 * Autor: meler
 * Fecha: 15/06/25
 * Descripción: Controller para la gestión de la vista de SubirDocumentoInicial.
 */
public class FXMLSubirDocumentoInicialController implements Initializable {

    @FXML private Label lblPeriodoEscolar;
    @FXML private Label lbHorasAcumuladas;
    @FXML private ImageView ivFotoPerfil;
    @FXML private Label lbNombreEstudiante;
    @FXML private Label lbMatriculaEstudiante;
    @FXML private ProgressBar pbHorasAcumuladas;
    @FXML private TableView<Entrega> tvEntregasIniciales;
    @FXML private TableColumn<Entrega, String> tcTitulo;
    @FXML private TableColumn<Entrega, String> tcFechaInicio;
    @FXML private TableColumn<Entrega, String> tcFechaFin;
    @FXML private TableColumn<Entrega, String> tcDescripcion;
    @FXML private TableColumn<Entrega, Button> tcPDF;
    @FXML private Button btnClicSalir;

    private int idExpedienteActual;
    private Estudiante estudianteActual;
    private ObservableList<Entrega> listaEntregas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listaEntregas = FXCollections.observableArrayList();
        configurarTabla();
    }

    public void inicializarDatos(int idExpediente, Estudiante estudiante) {
        this.idExpedienteActual = idExpediente;
        this.estudianteActual = estudiante;
        
        cargarDatosGenerales();
        cargarEntregas();
    }
    
    private void cargarDatosGenerales() {
        if (estudianteActual != null) {
            lbNombreEstudiante.setText(estudianteActual.getNombre() + " " + estudianteActual.getApellidoPaterno());
            lbMatriculaEstudiante.setText(estudianteActual.getMatricula());
            lblPeriodoEscolar.setText(estudianteActual.getPeriodoEscolar());
            
            cargarFoto();
            cargarHoras();
        }
    }
    
    private void cargarFoto() {
        try {
            byte[] foto = EstudianteDAO.obtenerFotoEstudiante(this.estudianteActual.getId());
            if (foto != null && foto.length > 0) {
                ByteArrayInputStream inputFoto = new ByteArrayInputStream(foto);
                Image imagen = new Image(inputFoto);
                ivFotoPerfil.setImage(imagen);
            } else {
                System.out.println("Info: El estudiante no tiene una foto registrada.");
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Conexión", 
                    "No se pudo obtener la foto del estudiante: " + e.getMessage());
        }
    }
    
    private void cargarHoras() {
        try {
            Expediente expediente = ExpedienteDAO.obtenerExpedienteActivoPorEstudiante(this.estudianteActual.getId());

            if (expediente != null) {
                int horas = expediente.getHorasAcumuladas();
                int horasTotales = 420; 
                
                pbHorasAcumuladas.setProgress((double) horas / horasTotales);
                lbHorasAcumuladas.setText(horas + " / " + horasTotales + " horas");
            } else {
                pbHorasAcumuladas.setProgress(0.0);
                lbHorasAcumuladas.setText("Sin expediente activo");
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Conexión", 
                    "No se pudo cargar la información de las horas acumuladas.");
            pbHorasAcumuladas.setProgress(0.0);
            lbHorasAcumuladas.setText("Error al cargar");
        }
    }
    
    private void configurarTabla() {
        tcTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tcFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        tcFechaFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        tcDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        
        tcPDF.setCellFactory(param -> new TableCell<Entrega, Button>() {
        final Button btnAccion = new Button();

        @Override
        protected void updateItem(Button item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                Entrega entrega = getTableView().getItems().get(getIndex());
                LocalDate fechaFin = LocalDate.parse(entrega.getFechaFin());
                boolean entregado = entrega.getFechaEntregado() != null;
                boolean fueraDeTiempo = LocalDate.now().isAfter(fechaFin);
                
                boolean esEntregaValida = entrega.getIdArchivo() > 0;

                if (!esEntregaValida) {
                    btnAccion.setText("Error Conf.");
                    btnAccion.setTooltip(new Tooltip("Esta entrega no está configurada correctamente. Contacte al coordinador."));
                    btnAccion.setDisable(true);
                } else if (entregado) {
                    btnAccion.setText("Entregado");
                    btnAccion.setDisable(true);
                } else if (fueraDeTiempo) {
                    btnAccion.setText("Plazo Vencido");
                    btnAccion.setDisable(true);
                } else {
                    btnAccion.setText("Subir Archivo");
                    btnAccion.setDisable(false);
                    btnAccion.setOnAction(event -> subirArchivoParaEntrega(entrega));
                }
                setGraphic(btnAccion);
            }
        }
    });
    }
    
    private void cargarEntregas() {
        try {
            List<Entrega> entregasBD = EntregaDAO.obtenerEntregasPorTipo(idExpedienteActual, "Inicial");
            listaEntregas.setAll(entregasBD);
            tvEntregasIniciales.setItems(listaEntregas);
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Conexión", 
                    "No se pudieron cargar las entregas: " + e.getMessage());
        }
    }

    private void subirArchivoParaEntrega(Entrega entrega) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Documento para: " + entrega.getTitulo());
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Documentos PDF", "*.pdf"));
        
        File archivoSeleccionado = fileChooser.showOpenDialog(Utilidad.obtenerEscenario(tvEntregasIniciales));

        if (archivoSeleccionado != null) {
            Alert alertaConfirmacion = Utilidad.mostrarAlertaConfirmacion("Confirmar Guardado", 
                    "¿Quiere guardar definitivamente el documento seleccionado?");
            
            Optional<ButtonType> resultado = alertaConfirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.APPLY) {
                guardarArchivoEnBD(archivoSeleccionado, entrega);
            }
        }
    }

    private void guardarArchivoEnBD(File archivo, Entrega entrega) {
        try {
            byte[] archivoBytes = Files.readAllBytes(archivo.toPath());
            
            ResultadoOperacion resultadoDAO = DocumentoInicialDAO.guardarArchivo(entrega.getIdArchivo(), archivoBytes);
            
            if (!resultadoDAO.isError()) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", resultadoDAO.getMensaje());
                cargarEntregas(); // Recargar la tabla para mostrar el estado "Entregado"
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al Guardar", resultadoDAO.getMensaje());
            }
            
        } catch (IOException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Archivo", 
                    "No se pudo leer el archivo seleccionado: " + e.getMessage());
        }
    }
    
    @FXML
    private void btnClicSalir(ActionEvent event) {
        Alert confirmacion = Utilidad.mostrarAlertaConfirmacion("Confirmar Salida", "¿Está seguro de que desea salir?");
        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.APPLY) {
            Utilidad.obtenerEscenario(btnClicSalir).close();
        }
    }

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        Stage escenario = Utilidad.obtenerEscenario(tvEntregasIniciales);
        escenario.close();
    }
}