package sistemadepracticasprofesionales.modelo.pojo;

/**
 * @author Nash
 * Fecha: 4/06/2025
 * Descripcion: POJO que modela un Expedientede un Estudiante de practicas profesionales
 */
public class Expediente {
    private int idExpediente;
    private int horasAcumuladas;
    private Float calificacionDocumento;
    private Float calificacionEvaluacion;
    private Float calificacionEvaluacionOrganizacionVinculada;
    private Float calificacionTotal;
    private String estado;
    private int idPeriodoEscolar;
    private int idEvaluacionOrganizacion;
    private int idEstudiante;

    public Expediente() {
    }

    public int getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(int idExpediente) {
        this.idExpediente = idExpediente;
    }

    public int getHorasAcumuladas() {
        return horasAcumuladas;
    }

    public void setHorasAcumuladas(int horasAcumuladas) {
        this.horasAcumuladas = horasAcumuladas;
    }

    public Float getCalificacionDocumento() {
        return calificacionDocumento;
    }

    public void setCalificacionDocumento(Float calificacionDocumento) {
        this.calificacionDocumento = calificacionDocumento;
    }

    public Float getCalificacionEvaluacion() {
        return calificacionEvaluacion;
    }

    public void setCalificacionEvaluacion(Float calificacionEvaluacion) {
        this.calificacionEvaluacion = calificacionEvaluacion;
    }

    public Float getCalificacionEvaluacionOrganizacionVinculada() {
        return calificacionEvaluacionOrganizacionVinculada;
    }

    public void setCalificacionEvaluacionOrganizacionVinculada(Float calificacionEvaluacionOrganizacionVinculada) {
        this.calificacionEvaluacionOrganizacionVinculada = calificacionEvaluacionOrganizacionVinculada;
    }

    public Float getCalificacionTotal() {
        return calificacionTotal;
    }

    public void setCalificacionTotal(Float calificacionTotal) {
        this.calificacionTotal = calificacionTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdPeriodoEscolar() {
        return idPeriodoEscolar;
    }

    public void setIdPeriodoEscolar(int idPeriodoEscolar) {
        this.idPeriodoEscolar = idPeriodoEscolar;
    }

    public int getIdEvaluacionOrganizacion() {
        return idEvaluacionOrganizacion;
    }

    public void setIdEvaluacionOrganizacion(int idEvaluacionOrganizacion) {
        this.idEvaluacionOrganizacion = idEvaluacionOrganizacion;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }    
}
