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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import sistemadepracticasprofesionales.modelo.dao.EntregaDAO;
import sistemadepracticasprofesionales.modelo.pojo.Entrega;
import sistemadepracticasprofesionales.utilidades.Utilidad;
import sistemadepracticasprofesionales.utilidades.validacion.ValidadorFormulario;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.TextValidationStrategy;

/**
 * FXML Controller class
 *
 * @author eugen
 * Fecha:10/06/25
 * Descripcion: Controlador para descargar la entrega seleccionada y guardar su calificacion correspondiente
 */
public class FXMLValidarEntregaController implements Initializable { //FIX Validaciones y booleano de validado

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
        if (validarCalificacion()) {
            int calificacion = Integer.parseInt(tfCalificacion.getText());
            procesarValidacion(calificacion, false); // false = no es rechazo
        }
    }

    @FXML
    private void btnClicRechazar(ActionEvent event) {
        if (validarObservacionObligatoria()) {
             procesarValidacion(0, true); // true = es rechazo, calificación 0
        }
    }

    public void inicializarInformacion(Entrega entrega) {
        this.entregaAValidar = entrega;
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
        validadorFormulario.addCleanupAction(()->{
           Stream.of(taObservacion)
          .filter(tf -> !tf.getStyle().isEmpty())
          .findFirst()
          .ifPresent(Control::requestFocus);//Le da el foco al primer campo con texto vacio
        });
    }

    private void guardarObservacion() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void guardarEntrega() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @FXML
    private void btnClicDescargar(ActionEvent event) {
        try {
            byte[] archivoBytes = EntregaDAO.obtenerArchivo(entregaAValidar.getIdArchivo(), entregaAValidar.getTipo());
            if (archivoBytes != null) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Guardar Archivo");
                fileChooser.setInitialFileName(entregaAValidar.getTitulo() + ".pdf"); // Asume PDF, puedes cambiarlo
                File archivo = fileChooser.showSaveDialog(Utilidad.obtenerEscenario(lbNombreDocumento));
                if (archivo != null) {
                    try (FileOutputStream fos = new FileOutputStream(archivo)) {
                        fos.write(archivoBytes);
                        Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Descarga Completa", "El archivo se ha guardado correctamente.");
                    }
                }
            } else {
                 Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin Archivo", "No se encontró un archivo para esta entrega.");
            }
        } catch (SQLException | IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Descarga", "No se pudo descargar el archivo.");
        }
    }
    private void procesarValidacion(int calificacion, boolean esRechazo) {
        try {
            Integer idObservacion = null;
            if (taObservacion.isVisible() && !taObservacion.getText().trim().isEmpty()) {
                idObservacion = EntregaDAO.guardarObservacion(taObservacion.getText());
            }
            
            boolean resultado = EntregaDAO.validarEntrega(
                entregaAValidar.getIdEntrega(), 
                entregaAValidar.getIdArchivo(), 
                entregaAValidar.getTipo(), 
                calificacion, 
                idObservacion
            );
            
            if (resultado) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Operación Exitosa", "La entrega ha sido validada correctamente.");
                Utilidad.abrirVentana("Profesor", lbNombreDocumento);
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error en la Operación", "No se pudo guardar la validación de la entrega.");
            }

        } catch (SQLException ex) {
             Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Conexión", "No se pudo conectar con la base de datos para guardar los cambios.");
        }
    }

    private boolean validarCalificacion() {
        String calificacionTexto = tfCalificacion.getText();
        if (calificacionTexto.isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Campo Vacío", "El campo de calificación no puede estar vacío.");
            return false;
        }
        try {
            int calificacion = Integer.parseInt(calificacionTexto);
            if (calificacion < 0 || calificacion > 10) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Calificación Inválida", "La calificación debe ser un número entre 0 y 10.");
                return false;
            }
        } catch (NumberFormatException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Formato Inválido", "La calificación debe ser un número entero.");
            return false;
        }
        return true;
    }
    
    private boolean validarObservacionObligatoria() {
        if (taObservacion.getText().trim().isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Campo Incompleto", "La observación no puede quedar vacía si la entrega es rechazada.");
            return false;
        }
        return true;
    }
}
