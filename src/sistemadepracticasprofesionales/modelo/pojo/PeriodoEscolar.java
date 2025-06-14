package sistemadepracticasprofesionales.modelo.pojo;

/**
 *
 * @author eugen
 * Fecha:14/05/25
 * Descripcion: Pojo para modelar la informacion del periodo escolar
 */
public class PeriodoEscolar {
    private int id;
    private String nombrePeriodo;
    private String fechaInicio;
    private String fechaFin;

    public PeriodoEscolar() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombrePeriodo() {
        return nombrePeriodo;
    }

    public void setNombrePeriodo(String nombrePeriodo) {
        this.nombrePeriodo = nombrePeriodo;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    
}
