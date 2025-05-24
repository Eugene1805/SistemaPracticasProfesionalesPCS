package sistemadepracticasprofesionales.modelo.pojo;

/**
 *
 * @author eugen
 * Fecha:24/05/25
 * Descripcion: Modelo de los campos correspondientes a evaluar por parte de un estudiante que cursa practicas
 * sobre el papel de una Organizacion Vinculada
 */
public class EvaluacionOrganizacion {
    private int id;
    private int claridadActividades;
    private int nivelRelacionActividades;
    private int accesibilidad;
    private int ambienteLaboral;
    private int oportunidades_aprendizaje;
    private int accesoRecursos;

    public EvaluacionOrganizacion() {
    }

    public EvaluacionOrganizacion(int id, int claridadActividades, int nivelRelacionActividades, int accesibilidad, int ambienteLaboral, int oportunidades_aprendizaje, int accesoRecursos) {
        this.id = id;
        this.claridadActividades = claridadActividades;
        this.nivelRelacionActividades = nivelRelacionActividades;
        this.accesibilidad = accesibilidad;
        this.ambienteLaboral = ambienteLaboral;
        this.oportunidades_aprendizaje = oportunidades_aprendizaje;
        this.accesoRecursos = accesoRecursos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClaridadActividades() {
        return claridadActividades;
    }

    public void setClaridadActividades(int claridadActividades) {
        this.claridadActividades = claridadActividades;
    }

    public int getNivelRelacionActividades() {
        return nivelRelacionActividades;
    }

    public void setNivelRelacionActividades(int nivelRelacionActividades) {
        this.nivelRelacionActividades = nivelRelacionActividades;
    }

    public int getAccesibilidad() {
        return accesibilidad;
    }

    public void setAccesibilidad(int accesibilidad) {
        this.accesibilidad = accesibilidad;
    }

    public int getAmbienteLaboral() {
        return ambienteLaboral;
    }

    public void setAmbienteLaboral(int ambienteLaboral) {
        this.ambienteLaboral = ambienteLaboral;
    }

    public int getOportunidades_aprendizaje() {
        return oportunidades_aprendizaje;
    }

    public void setOportunidades_aprendizaje(int oportunidades_aprendizaje) {
        this.oportunidades_aprendizaje = oportunidades_aprendizaje;
    }

    public int getAccesoRecursos() {
        return accesoRecursos;
    }

    public void setAccesoRecursos(int accesoRecursos) {
        this.accesoRecursos = accesoRecursos;
    }
}
