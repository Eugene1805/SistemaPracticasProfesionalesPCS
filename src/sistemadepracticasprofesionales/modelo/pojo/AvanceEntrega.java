package sistemadepracticasprofesionales.modelo.pojo;

/**
 *
 * @author Nash
 * Fecha: 10/06/2025
 * Descripcion: Clase que modela los campos correspondientes que se visualizan al consultar el
 * avance de los estudiantes
 */
public class AvanceEntrega {
    private int idEntrega;
    private int idArchivo;
    private String titulo;
    private String fechaEntregado;
    private String fechaRevisado;
    private Integer calificacion;
    private String observacion;

    public AvanceEntrega() {
    }

    public int getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(int idEntrega) {
        this.idEntrega = idEntrega;
    }

    public int getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(int idArchivo) {
        this.idArchivo = idArchivo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFechaEntregado() {
        return fechaEntregado;
    }

    public void setFechaEntregado(String fechaEntregado) {
        this.fechaEntregado = fechaEntregado;
    }

    public String getFechaRevisado() {
        return fechaRevisado;
    }

    public void setFechaRevisado(String fechaRevisado) {
        this.fechaRevisado = fechaRevisado;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }   
}
