/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import sistemadepracticasprofesionales.SistemaDePracticasProfesionales;
import sistemadepracticasprofesionales.modelo.dao.EstudianteDAO;
import sistemadepracticasprofesionales.modelo.dao.ExperienciaEducativaDAO;
import sistemadepracticasprofesionales.modelo.dao.OrganizacionVinculadaDAO;
import sistemadepracticasprofesionales.modelo.dao.ProyectoDAO;
import sistemadepracticasprofesionales.modelo.pojo.Estudiante;
import sistemadepracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemadepracticasprofesionales.modelo.pojo.Proyecto;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;
import sistemadepracticasprofesionales.utilidades.Utilidad;

/**
 *
 * @author meler
 * Fecha:14/06/25
 * Descripcion: Controller de la vista GenerarFormatoParaOV y su interacción con los DAO y clic.
 */
public class FXMLGenerarFormatoParaOVController implements Initializable {

    @FXML
    private ImageView ivFotoEstudiante;
    @FXML
    private Label lbNombreEstudiante;
    @FXML
    private Label lbMatriculaEstudiante;
    @FXML
    private Label lbNombrePeriodo;
    @FXML
    private Label lbNombreExperiencia;
    @FXML
    private Label lbNombreProyecto;
    @FXML
    private Label lbDescripcionProyecto;
    @FXML
    private Label lbOrganizacion;
    
    private Usuario estudiante;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void inicializarDatos(int idEstudiante, Usuario estudianteUsuario) {
        this.estudiante = estudianteUsuario;
        try {
            Estudiante estudiante = EstudianteDAO.obtenerEstudiante(idEstudiante);

            if (estudiante != null) {
                // Nombre completo
                String nombreCompleto = estudiante.getNombre() + " " +
                                        estudiante.getApellidoPaterno() + " " +
                                        estudiante.getApellidoMaterno();

                lbNombreEstudiante.setText(nombreCompleto.trim());
                lbMatriculaEstudiante.setText(estudiante.getMatricula());
                lbNombrePeriodo.setText(estudiante.getPeriodoEscolar());
                String nombreExperiencia = ExperienciaEducativaDAO.obtenerNombrePorId(estudiante.getIdExperienciaEducativa());
                lbNombreExperiencia.setText(nombreExperiencia != null ? nombreExperiencia : "Desconocida");
                lbNombreProyecto.setText(estudiante.getNombreProyecto());

                // Obtener detalles del proyecto
                Proyecto proyecto = ProyectoDAO.obtenerProyecto(estudiante.getIdProyecto());
                if (proyecto != null) {
                    lbDescripcionProyecto.setText(proyecto.getDescripcion());

                    // Obtener nombre de la organización vinculada
                    OrganizacionVinculada org = OrganizacionVinculadaDAO.obtenerOrganizacionVinculadaPorProyecto(proyecto.getIdProyecto());
                    if (org != null) {
                        lbOrganizacion.setText(org.getRazonSocial());
                    } else {
                        lbOrganizacion.setText("Sin organización vinculada");
                    }
                } else {
                    lbDescripcionProyecto.setText("No se encontró información del proyecto.");
                    lbOrganizacion.setText("Desconocida");
                }

            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "Estudiante no encontrado.");
            }

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar los datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        regresarAlDashboard();
    }

    @FXML
    private void btnClicGenerarFormato(ActionEvent event) {
        generarFormatoEvaluacion(event);
    }
    
    private void generarFormatoEvaluacion(ActionEvent event) {
        String nombreArchivo = "Evaluacion-de-la-organizacion.pdf";
        InputStream inputStream = getClass().getResourceAsStream("/sistemadepracticasprofesionales/recursos/" + nombreArchivo);

        if (inputStream == null) {
            mostrarAlerta(AlertType.ERROR, "No se encontró el archivo PDF.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Formato de Evaluación");
        fileChooser.setInitialFileName(nombreArchivo);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));

        File destino = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());
        if (destino == null) {
            return;
        }

        try (FileOutputStream outputStream = new FileOutputStream(destino)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            mostrarAlerta(AlertType.INFORMATION, "Formato guardado exitosamente:\n" + destino.getAbsolutePath());

            abrirVentanaFormatoGenerado();

        } catch (IOException e) {
            mostrarAlerta(AlertType.ERROR, "No fue posible guardar el archivo.");
        }
    }
    
    private void abrirVentanaFormatoGenerado() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistemadepracticasprofesionales/vista/FXMLFormatoGenerado.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Formato Generado");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "No fue posible abrir la ventana de confirmación.");
        }
    }

    private void mostrarAlerta(AlertType tipo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle("Generar Formato");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void btnClicCancelar(ActionEvent event) {
        Alert alerta = Utilidad.mostrarAlertaConfirmacion("Confirmacion Cancelar",
                "¿Estás seguro de que deseas cancelar?");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if(resultado.get() == ButtonType.APPLY){
            regresarAlDashboard();
        }
    }
    
    private void regresarAlDashboard(){
       try {
            Stage escenarioBase = Utilidad.obtenerEscenario(lbNombreEstudiante);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                    getResource("vista/FXMLEstudiante.fxml"));
            Parent vista = cargador.load();
            FXMLEstudianteController controlador = cargador.getController();
            controlador.inicializar(estudiante);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Dasboard Estudiante");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error al cargar el dashboard del estudiante",
                    "Lo sentimos no fue posible cargar la informacion del estudiante");
        }
    }
    
}
