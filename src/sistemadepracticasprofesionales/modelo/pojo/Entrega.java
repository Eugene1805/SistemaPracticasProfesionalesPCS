package sistemadepracticasprofesionales.modelo.pojo;

import java.sql.Blob;

/**
 *
 * @author eugen
 * Fecha:12/06/25
 * Descripcion: Clase para modelar los datos de una entrega y sus relaciones
 */
public class Entrega {

    public enum Tipo {
        DOCUMENTO,
        REPORTE
    }
    
    // Nuevo Enum para diferenciar los tipos de documento
    public enum SubtipoDocumento {
        INICIAL,
        INTERMEDIO,
        FINAL,
        NINGUNO // Para cuando el tipo es REPORTE
    }

    private int idEntrega;
    private int idArchivo;
    private String titulo;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private String fechaEntregado;
    private Tipo tipo;
    private SubtipoDocumento subtipoDoc; // Nuevo campo

    // ... otros campos y constructor ...

    // Getters y Setters para todos los campos...
    public int getIdEntrega() { return idEntrega; }
    public void setIdEntrega(int idEntrega) { this.idEntrega = idEntrega; }
    public int getIdArchivo() { return idArchivo; }
    public void setIdArchivo(int idArchivo) { this.idArchivo = idArchivo; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }
    public String getFechaEntregado() { return fechaEntregado; }
    public void setFechaEntregado(String fechaEntregado) { this.fechaEntregado = fechaEntregado; }
    public Tipo getTipo() { return tipo; }
    public void setTipo(Tipo tipo) { this.tipo = tipo; }
    public SubtipoDocumento getSubtipoDoc() { return subtipoDoc; }
    public void setSubtipoDoc(SubtipoDocumento subtipoDoc) { this.subtipoDoc = subtipoDoc; }
}