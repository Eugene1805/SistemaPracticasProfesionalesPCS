package sistemadepracticasprofesionales.controlador;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import sistemadepracticasprofesionales.modelo.pojo.Estudiante;
import sistemadepracticasprofesionales.modelo.pojo.TipoDocumento;

/**
 * FXML Controller class
 *
 * @author eugen
 * Fecha: 10/06/25
 * Descripcion: Controller para mostrar las entregas disponibles para calificar dado un estudiante seleccionado
 */
public class FXMLElegirEntregaController implements Initializable {

    @FXML
    private TableView<TipoDocumento> tvEntregas;
    @FXML
    private TableColumn<TipoDocumento, String> tcTipoEntrega;
    @FXML
    private TableColumn<TipoDocumento, String> tcDocumento;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacion();
    }    

    public void inicializarInformacion(Estudiante estudiante) {
        //TODO
        //Pasar el estudiante a la consulta de la entrega para que cargue la correspondiente
    }
    
    private void configurarTabla(){
        tcTipoEntrega.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoEntrega()));
        
        // Celda personalizada para la columna de documentos
        tcDocumento.setCellFactory(column -> new TableCell<TipoDocumento, String>() {
            private final VBox container = new VBox(5);
            private final List<Hyperlink> links = new ArrayList<>();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    TipoDocumento tipoDoc = (TipoDocumento) getTableRow().getItem();
                    container.getChildren().clear();
                    links.clear();
                    
                    // Crear un hipervínculo para cada documento
                    for (String documento : tipoDoc.getDocumentos()) {
                        Hyperlink link = new Hyperlink(documento);
                        link.setOnAction(e -> abrirVentanaValidarEntrega(tipoDoc.getTipoEntrega(), documento));
                        links.add(link);
                        container.getChildren().add(link);
                    }
                    
                    setGraphic(container);
                }
            }
        });

    }
    
    private void cargarInformacion(){
        // Llenar la tabla con datos
        ObservableList<TipoDocumento> datos = FXCollections.observableArrayList(
            new TipoDocumento("Reporte", "Primer Reporte", "Segundo reporte"),
            new TipoDocumento("Documento inicial", "Carta aceptacion", "Constancia seguro", 
                            "Horario", "Cronograma Actividades", "Oficio de asignacion"),
            new TipoDocumento("Documento intermedio", "Reporte parcial", "Presentacion 210hrs"),
            new TipoDocumento("Documento final", "Reporte final", "Presentacion 420hrs", 
                           "Carta liberacion", "Autoevaluacion", "Evaluacion OV", "Evaluacion parcial OV")
        );
        
        tvEntregas.setItems(datos);
    }
    
    private void abrirVentanaValidarEntrega(String tipoEntrega, String documento){
        //TODO
    }
}
