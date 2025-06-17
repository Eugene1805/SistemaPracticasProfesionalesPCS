package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sistemadepracticasprofesionales.modelo.dao.ProyectoDAO;
import sistemadepracticasprofesionales.modelo.pojo.Proyecto;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;
import sistemadepracticasprofesionales.utilidades.Utilidad;

/*
 * @author meler
 * Fecha:12/0/25
 * Descripcion: Controller para la ventana DatosActualizados y sus eventos
 */
public class FXMLDatosActualizadosController implements Initializable {

    @FXML 
    private Label lblNombre;
    @FXML 
    private TextArea taDescripcion;
    @FXML 
    private Label lblEstado;
    @FXML 
    private Label lblCupo;
    @FXML 
    private Label lblFechaInicio;
    @FXML 
    private Label lblFechaFin;
    @FXML 
    private Label lblOrganizacion;
    @FXML 
    private Label lblResponsable;
    @FXML 
    private Button btnAceptar;
    @FXML 
    private Button btnCancelar;

    private Proyecto proyecto;
    private Usuario coordinador; // Guardamos el coordinador
    private Stage escenarioPadre;

    public void setEscenarioPadre(Stage escenarioPadre) {
        this.escenarioPadre = escenarioPadre;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // No requiere lógica de inicio adicional
    }

    public void inicializarProyecto(Proyecto proyecto, Usuario coordinador) {
        this.proyecto = proyecto;
        this.coordinador = coordinador;

        lblNombre.setText("Nombre: " + proyecto.getNombre());
        taDescripcion.setText(proyecto.getDescripcion());
        lblEstado.setText("Estado: " + proyecto.getEstado());
        lblCupo.setText("Cupo: " + proyecto.getCupo());
        lblFechaInicio.setText("Fecha de Inicio: " + proyecto.getFechaInicio());
        lblFechaFin.setText("Fecha de Fin: " + proyecto.getFechaFin());
        lblOrganizacion.setText("Organización Vinculada: " + proyecto.getNombreOrganizacionVinculada());
        lblResponsable.setText("Responsable del Proyecto: " + proyecto.getNombreResponsableProyecto());
    }

    @FXML
    private void btnClicAceptar(ActionEvent event) {
        try {
            ResultadoOperacion resultado = ProyectoDAO.actualizarProyecto(proyecto);
            if (!resultado.isError()) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION,
                        "Operación Exitosa", "Los datos se actualizaron correctamente.");
                Stage ventanaActual = (Stage) btnAceptar.getScene().getWindow();
                ventanaActual.close();
                if (escenarioPadre != null) {
                    escenarioPadre.close();
                }
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", resultado.getMensaje());
            }
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de base de datos", "No fue posible guardar los cambios.");
        }
    }

    @FXML
    private void btnClicCancelar(ActionEvent event) {
        Stage ventana = (Stage) btnCancelar.getScene().getWindow();
        ventana.close();
    }
}