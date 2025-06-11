package sistemadepracticasprofesionales.modelo.pojo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author eugen
 * Fecha: 11/06/25
 * Descripcion: Clase que modela los tipos de documentos para las entregas
 * 
 */
public class TipoDocumento {
    private final String tipoEntrega;
    private final ObservableList<String> documentos;

    public TipoDocumento(String tipoEntrega, String... documentos) {
        this.tipoEntrega = tipoEntrega;
        this.documentos = FXCollections.observableArrayList(documentos);
    }

    // Getters
    public String getTipoEntrega() { return tipoEntrega; }
    public ObservableList<String> getDocumentos() { return documentos; }
}
