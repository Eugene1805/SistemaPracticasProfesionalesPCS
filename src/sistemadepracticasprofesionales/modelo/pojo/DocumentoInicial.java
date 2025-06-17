package sistemadepracticasprofesionales.modelo.pojo;

import java.util.Date;
/**
 *
 * @author meler
 * Fecha:12/06/25
 * Descripcion: POJO que modela un documento inicial
 */
public class DocumentoInicial {
    private int idDocumentoInicial;
    private Date fechaEntregado;
    private String tipoDocumento;
    private byte[] archivo;
    private Date fechaRevisado;

    public DocumentoInicial() {}

    public int getIdDocumentoInicial() {
        return idDocumentoInicial;
    }

    public void setIdDocumentoInicial(int idDocumentoInicial) {
        this.idDocumentoInicial = idDocumentoInicial;
    }

    public Date getFechaEntregado() {
        return fechaEntregado;
    }

    public void setFechaEntregado(Date fechaEntregado) {
        this.fechaEntregado = fechaEntregado;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public Date getFechaRevisado() {
        return fechaRevisado;
    }

    public void setFechaRevisado(Date fechaRevisado) {
        this.fechaRevisado = fechaRevisado;
    }
}