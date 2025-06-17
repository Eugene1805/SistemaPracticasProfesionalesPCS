package sistemadepracticasprofesionales.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sistemadepracticasprofesionales.dominio.AvanceDM;
import sistemadepracticasprofesionales.modelo.dao.PeriodoEscolarDAO;
import sistemadepracticasprofesionales.modelo.dao.EstudianteDAO;
import sistemadepracticasprofesionales.modelo.dao.ExpedienteDAO;
import sistemadepracticasprofesionales.SistemaDePracticasProfesionales; 
import sistemadepracticasprofesionales.modelo.pojo.Estudiante;
import sistemadepracticasprofesionales.modelo.pojo.Expediente;
import sistemadepracticasprofesionales.modelo.pojo.PeriodoEscolar;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;
import sistemadepracticasprofesionales.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author eugen
 * Fecha:27/05/25
 * Descripcion: Controlador para manejar las aciones del estudiante
 */
public class FXMLEstudianteController implements Initializable, Dashboard {

    @FXML
    private Label lbUsuario;
    
    private Usuario estudiante;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnCerrarSesion(ActionEvent event) {
        Utilidad.cerrarSesion(lbUsuario);
    }

    @FXML
    private void clicConsultarAvance(MouseEvent event) {
        try {
            Estudiante estudianteLogueado = EstudianteDAO.obtenerEstudiantePorMatricula(estudiante.getUsername());
            PeriodoEscolar periodoActual = PeriodoEscolarDAO.obtenerPeriodoEscolarActual();
            if (estudianteLogueado != null && periodoActual != null) {
                ResultadoOperacion resultado = AvanceDM.verificarExistenciaDeEntregas(estudianteLogueado.getId(),
                        periodoActual.getId());
                if (!resultado.isError()) {
                    Stage escenarioBase = Utilidad.obtenerEscenario(lbUsuario);
                    FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                            getResource("vista/FXMLConsultarAvance.fxml"));
                    Parent vista = cargador.load();
                    FXMLConsultarAvanceController controlador = cargador.getController();
                    controlador.inicializarInformacion(estudianteLogueado, "Estudiante", periodoActual, estudiante);
                    Scene escenaPrincipal = new Scene(vista);
                    escenarioBase.setScene(escenaPrincipal);
                    escenarioBase.setTitle("Consultar Avance");
                    escenarioBase.show();
                }else{
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Sin Entregas",
                            resultado.getMensaje());
                }
            }else{
                if (estudianteLogueado == null) {
                 Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Datos", "No se encontró el perfil del estudiante.");
                }
                if (periodoActual == null) {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Sistema", "No hay un periodo escolar activo configurado.");
                }            
            }
        } catch(SQLException ex){
             Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Sin conexión",
                    "No hay conexión con la base de datos");
        } catch (IOException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error al cargar la consulta de avance",
                    "Lo sentimos no fue posible cargar la informacion del estudiante");
        }
    }

    @FXML
    private void clicActualizarExpediente(MouseEvent event) {
        try {
            Estudiante estudianteLogueado = EstudianteDAO.obtenerEstudiantePorMatricula(estudiante.getUsername());
            if (estudianteLogueado == null) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Datos", 
                        "No se pudo encontrar el perfil completo del estudiante.");
                return;
            }

            Expediente expedienteActivo = ExpedienteDAO.obtenerExpedienteActivoPorEstudiante(estudianteLogueado.getId());
            if (expedienteActivo == null) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin Expediente",
                        "No tienes un expediente de prácticas profesionales activo en este momento.");
                return;
            }

            Stage escenarioBase = Utilidad.obtenerEscenario(lbUsuario);
            FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.getResource("vista/FXMLActualizarExpediente.fxml"));
            Parent vista = cargador.load();

            FXMLActualizarExpedienteController controladorDestino = cargador.getController();
            controladorDestino.inicializarDatos(this.estudiante, estudianteLogueado, expedienteActivo);

            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Actualizar Expediente");
            escenarioBase.show();

        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Conexión",
                    "No se pudo conectar con la base de datos. Por favor, inténtelo más tarde.");
            ex.printStackTrace();
        } catch (IOException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Carga",
                    "No fue posible cargar la ventana de actualización de expediente.");
            e.printStackTrace();
        }
    }


    @FXML
    private void clicEvaluarOV(MouseEvent event) {
        try {
            Estudiante estudianteDAO = EstudianteDAO.obtenerEstudiantePorMatricula(estudiante.getUsername());
            if(ExpedienteDAO.obtenerExpedienteActivoPorEstudiante(estudianteDAO.getId()).getHorasAcumuladas() < 420){
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Horas incompletas",
                        "Para hacer la evaluacion debe haber terminado las 420hrs");
                return;
            }
            try {
                Stage escenarioBase = Utilidad.obtenerEscenario(lbUsuario);
                FXMLLoader cargador = new FXMLLoader(SistemaDePracticasProfesionales.class.
                        getResource("vista/FXMLEvaluarOrganizacionVinculada.fxml"));
                Parent vista = cargador.load();
                FXMLEvaluarOrganizacionVinculadaController controlador = cargador.getController();
                controlador.inicializarInformacion(estudiante);
                Scene escenaPrincipal = new Scene(vista);
                escenarioBase.setScene(escenaPrincipal);
                escenarioBase.setTitle("Evaluar Organizacion Vinculada");
                escenarioBase.show();
            } catch (IOException ex) {
                ex.printStackTrace();
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error al cargar la evaluacion de la organizacio vinculada",
                        "Lo sentimos no fue posible cargar la informacion del estudiante");
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLEstudianteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void clicGenerarFormatoEvalOV(MouseEvent event) {
        try {
            Estudiante estudianteLogueado = EstudianteDAO.obtenerEstudiantePorMatricula(estudiante.getUsername());

            if (estudianteLogueado != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistemadepracticasprofesionales/vista/FXMLGenerarFormatoParaOV.fxml"));
                Parent root = loader.load();

                FXMLGenerarFormatoParaOVController controller = loader.getController();
                controller.inicializarDatos(estudianteLogueado.getId(), estudiante);

                Stage escenarioBase = Utilidad.obtenerEscenario(lbUsuario);
                Scene nuevaEscena = new Scene(root);
                escenarioBase.setScene(nuevaEscena);
                escenarioBase.setTitle("Generar Formato");
                escenarioBase.show();

            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Estudiante no encontrado",
                        "No se encontró un estudiante con matrícula " + estudiante.getUsername());
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de base de datos",
                    "No se pudo acceder a la base de datos.");
            e.printStackTrace();
        } catch (IOException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar la ventana",
                    "No se pudo abrir la ventana para generar el formato.");
            e.printStackTrace();
        }
    }


    @Override
    public void inicializar(Usuario usuario) {
        lbUsuario.setText(usuario.getNombre() + " " + usuario.getApellidoPaterno());
        estudiante = usuario;
    }    
}
