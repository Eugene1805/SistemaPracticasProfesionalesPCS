package sistemadepracticasprofesionales.controlador;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sistemadepracticasprofesionales.SistemaDePracticasProfesionales;
import sistemadepracticasprofesionales.modelo.dao.EntregaDAO;
import sistemadepracticasprofesionales.modelo.pojo.Entrega;
import sistemadepracticasprofesionales.modelo.pojo.Estudiante;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;
import sistemadepracticasprofesionales.utilidades.Utilidad;
import sistemadepracticasprofesionales.utilidades.validacion.ValidadorFormulario;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.NumericValidationStrategy;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.TextValidationStrategy;

/**
 * FXML Controller class
 *
 * @author eugen
 * Fecha:10/06/25
 * Descripcion: Controlador para descargar la entrega seleccionada y guardar su calificacion correspondiente
 */
public class FXMLValidarEntregaController implements Initializable {

    @FXML
    private Label lbNombreDocumento;
    @FXML
    private Label lbFechaInicio;
    @FXML
    private Label lbFechaFin;
    @FXML
    private Label lbDescripcion;
    @FXML
    private Label lbFechaEntregado;
    @FXML
    private TextArea taObservacion;
    @FXML
    private TextField tfCalificacion;
    @FXML
    private Button btnRechazar;
    
    private Estudiante estudianteAValidar;
    private Usuario profesor;
    private Entrega entregaAValidar;
    private ValidadorFormulario validadorFormulario;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarValidaciones();
    }    

    @FXML
    private void btnClicAgregarObservacion(ActionEvent event) {
        taObservacion.setVisible(true);
        btnRechazar.setVisible(true);
    }

    @FXML
    private void btnClicValidar(ActionEvent event) {
        finalizarOperacion();
    }

    @FXML
    private void btnClicRechazar(ActionEvent event) {
        finalizarOperacion();
    }

    public void inicializarInformacion(Estudiante estudiante,Entrega entrega, Usuario profesor) {
        this.entregaAValidar = entrega;
        this.estudianteAValidar = estudiante;
        this.profesor = profesor;
        cargarDatosEntrega();
    }
    
    private void cargarDatosEntrega() {
        lbNombreDocumento.setText(entregaAValidar.getTitulo());
        lbFechaInicio.setText(entregaAValidar.getFechaInicio());
        lbFechaFin.setText(entregaAValidar.getFechaFin());
        lbDescripcion.setText(entregaAValidar.getDescripcion());
        lbFechaEntregado.setText(entregaAValidar.getFechaEntregado());
    }

    private void inicializarValidaciones(){
        validadorFormulario = new ValidadorFormulario();
        validadorFormulario.addValidation(taObservacion, new TextValidationStrategy(65535, true));
        validadorFormulario.addValidation(tfCalificacion, new NumericValidationStrategy(true));
        validadorFormulario.addCleanupAction(()->{
           Stream.of(taObservacion, tfCalificacion)
          .filter(tf -> !tf.getStyle().isEmpty())
          .findFirst()
          .ifPresent(Control::requestFocus);//Le da el foco al primer campo con texto vacio
        });
    }

    @FXML
    private void btnClicDescargar(ActionEvent event) {
        try {
            byte[] archivoBytes = EntregaDAO.obtenerArchivo(entregaAValidar.getIdArchivo(), entregaAValidar.getTipo(),
                    entregaAValidar.getSubtipoDoc());
            if (archivoBytes != null) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Guardar Archivo");
                fileChooser.setInitialFileName(entregaAValidar.getTitulo() + ".pdf"); 
                File archivo = fileChooser.showSaveDialog(Utilidad.obtenerEscenario(lbNombreDocumento));
                if (archivo != null) {
                    try (FileOutputStream fos = new FileOutputStream(archivo)) {
                        fos.write(archivoBytes);
                        Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Descarga Completa",
                                "El archivo se ha guardado correctamente.");
                    }
                }
            } else {
                 Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin Archivo",
                         "No se encontró un archivo para esta entrega.");
            }
        } catch (SQLException | IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Descarga", "No se pudo descargar el archivo.");
        }
    }
    
    private void finalizarOperacion(){
        if (validadorFormulario.validate()) {
             procesarValidacion(Integer.parseInt(tfCalificacion.getText()));
        }else{
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Campos con datos invalidos", 
                    "Hay campos con dativos invalidos o incompletos");
        }
    }
    
    private void procesarValidacion(int calificacion) {
        try {
            Integer idObservacion = null;
            if (taObservacion.isVisible() && !taObservacion.getText().trim().isEmpty()) {
                idObservacion = EntregaDAO.guardarObservacion(taObservacion.getText());
            }
            ResultadoOperacion resultadoOperacion = (EntregaDAO.validarEntrega(
                entregaAValidar.getIdEntrega(),
                entregaAValidar.getIdArchivo(),
                entregaAValidar.getTipo(),
                entregaAValidar.getSubtipoDoc(), 
                calificacion,
                idObservacion
            ));
            
            if (!resultadoOperacion.isError()) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Operación Exitosa",
                        resultadoOperacion.getMensaje());
                Utilidad.abrirVentana("Profesor", lbNombreDocumento);
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error en la Operación", resultadoOperacion.getMensaje());
            }

        } catch (SQLException ex) {
             Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Conexión",
                     "No se pudo conectar con la base de datos para guardar los cambios.");
        }
    }

    @FXML
    private void btnRegresar(ActionEvent event) {
        try {
            Stage escenarioBase = Utilidad.obtenerEscenario(btnRechazar);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                    getResource("vista/FXMLElegirEntrega.fxml"));
            Parent vista = cargador.load();
            FXMLElegirEntregaController controlador = cargador.getController();
            controlador.inicializar(estudianteAValidar, profesor);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Elegir Entrega");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error al cargar la pagina de entregas del estudiante",
                    "Lo sentimos no fue posible cargar la informacion del estudiante");
        }
    }
}
