package sistemadepracticasprofesionales.modelo.pojo;

/**
 *
 * @author eugen
 * Fecha: 24/05/25
 * Descripcion: POJO que modela la Experiencia Educativa de practicas profesionales
 */
public class ExperienciaEducativa {
    private int id;
    private String nombre;
    private String nrc;
    private String bloque;
    private String seccion;
    private int idProfesor;
    private String nombreProfesor;

    public ExperienciaEducativa() {
    }

    public ExperienciaEducativa(int id, String nombre, String nrc, String bloque, String seccion, int idProfesor, String nombreProfesor) {
        this.id = id;
        this.nombre = nombre;
        this.nrc = nrc;
        this.bloque = bloque;
        this.seccion = seccion;
        this.idProfesor = idProfesor;
        this.nombreProfesor = nombreProfesor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNrc() {
        return nrc;
    }

    public void setNrc(String nrc) {
        this.nrc = nrc;
    }

    public String getBloque() {
        return bloque;
    }

    public void setBloque(String bloque) {
        this.bloque = bloque;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }
}
