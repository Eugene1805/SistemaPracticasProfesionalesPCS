package sistemadepracticasprofesionales.controlador;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistemadepracticasprofesionales.SistemaDePracticasProfesionales;
import sistemadepracticasprofesionales.modelo.dao.EstudianteDAO;
import sistemadepracticasprofesionales.modelo.dao.ExperienciaEducativaDAO;
import sistemadepracticasprofesionales.modelo.pojo.Estudiante;
import sistemadepracticasprofesionales.modelo.pojo.Expediente;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;
import sistemadepracticasprofesionales.utilidades.Utilidad;


/**
 * Autor: meler 
 * Fecha: 12/06/25
 * Descripción: Controller para la ventana ActualizarExpediente y su interacción con los DAOs.
 */
public class FXMLActualizarExpedienteController implements Initializable {

    @FXML 
    private ProgressBar pbHorasAcumuladas;
    @FXML 
    private ImageView ivFotoEstudiante;
    @FXML 
    private Label lblNombreEstudiante;
    @FXML 
    private Label lblMatriculaEstudiante;
    @FXML 
    private Label lbNombreProyecto;
    @FXML 
    private Label lbNombreExperiencia;
    @FXML 
    private Label lbNombrePeriodo;

    private Usuario usuarioAnterior;
    private Expediente expedienteActual;
    private Estudiante estudianteActual;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // ...
    }

    public void inicializarDatos(Usuario usuario, Estudiante estudiante, Expediente expediente) {
        this.usuarioAnterior = usuario;
        this.estudianteActual = estudiante;
        this.expedienteActual = expediente;
        cargarDatosGenerales();
    }
    
    private void cargarDatosGenerales() {
        if (estudianteActual != null && expedienteActual != null) {
            lblNombreEstudiante.setText(estudianteActual.getNombre() + " " + estudianteActual.getApellidoPaterno());
            lblMatriculaEstudiante.setText(estudianteActual.getMatricula());
            lbNombrePeriodo.setText(estudianteActual.getPeriodoEscolar());
            lbNombreProyecto.setText("Proyecto: " + estudianteActual.getNombreProyecto());

            cargarFoto();
            cargarHoras();
            cargarDetallesAcademicos();
        }
    }

    private void cargarFoto() {
        if (estudianteActual != null) {
            try {
                byte[] foto = EstudianteDAO.obtenerFotoEstudiante(this.estudianteActual.getId());
                if (foto != null && foto.length > 0) {
                    ByteArrayInputStream inputFoto = new ByteArrayInputStream(foto);
                    Image imagen = new Image(inputFoto);
                    ivFotoEstudiante.setImage(imagen);
                } else {
                    System.out.println("Info: El estudiante no tiene una foto registrada.");
                }
            } catch (SQLException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Conexión", 
                        "No se pudo obtener la foto del estudiante: " + e.getMessage());
            }
        }
    }
    
    private void cargarHoras() {
        int horas = expedienteActual.getHorasAcumuladas();
        int horasTotales = 420;
        pbHorasAcumuladas.setProgress((double) horas / horasTotales);
    }
    
    private void cargarDetallesAcademicos() {
        try {
            String nombreExperiencia = ExperienciaEducativaDAO.obtenerNombrePorId(estudianteActual.getIdExperienciaEducativa());
            lbNombreExperiencia.setText(nombreExperiencia != null ? nombreExperiencia : "N/A");
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Datos", 
                    "No se pudo cargar el nombre de la experiencia educativa.");
            e.printStackTrace();
        }
    }

    @FXML
    private void btnSubirDocumentosIniciales(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistemadepracticasprofesionales/vista/FXMLSubirDocumentoInicial.fxml"));
            Parent root = loader.load();
            FXMLSubirDocumentoInicialController controller = loader.getController();
            controller.inicializarDatos(expedienteActual.getIdExpediente(), estudianteActual);
            Stage stage = new Stage();
            stage.setTitle("Subir Documentos Iniciales");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Interfaz",
                    "No se pudo cargar la ventana para subir documentos.");
            e.printStackTrace();
        }
    }
    
    private void btnClicCancelar(ActionEvent event) {
        Alert alerta = Utilidad.mostrarAlertaConfirmacion("Confirmar Cancelación",
                "¿Estás seguro de que deseas cancelar?");
        
        Optional<ButtonType> resultado = alerta.showAndWait();
        
        if (resultado.isPresent() && resultado.get() == ButtonType.APPLY) {
            regresarAlMenuEstudiante();
        }
    }
    
    @FXML
    private void btnClicRegresar(ActionEvent event) {
        regresarAlMenuEstudiante();
    }
    
    private void regresarAlMenuEstudiante() {
        try {
            Stage escenarioBase = Utilidad.obtenerEscenario(lbNombrePeriodo);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.getResource("vista/FXMLEstudiante.fxml"));
            Parent vista = cargador.load();
            
            FXMLEstudianteController controlador = cargador.getController();
            controlador.inicializar(this.usuarioAnterior);
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Menú Estudiante");
            escenarioBase.show();
            
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Navegación",
                    "Lo sentimos, no fue posible regresar a la pantalla principal.");
            ex.printStackTrace();
        }
    }
}