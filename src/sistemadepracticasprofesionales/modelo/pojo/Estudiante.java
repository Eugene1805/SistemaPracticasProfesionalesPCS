package sistemadepracticasprofesionales.modelo.pojo;

/**
 *
 * @author eugen
 * Fecha: 24/05/25
 * Descripion: POJO que modela un Estudiante
 */
public class Estudiante {
    private int id;
    private String matricula;
    private String correoInstitucional;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private byte[] foto;
    private int idPeriodoEscolar;
    private String periodoEscolar;
    private int idExperienciaEducativa;
    private String nrcExperienciaEducativa;
    private int idProyecto;
    private String nombreProyecto;

    public Estudiante() {
    }

    public Estudiante(int id, String matricula, String correoInstitucional, String nombre, String apellidoPaterno, String apellidoMaterno, byte[] foto, int idPeriodoEscolar, String periodoEscolar, int idExperienciaEducativa, String nrcExperienciaEducativa, int idProyecto, String nombreProyecto) {
        this.id = id;
        this.matricula = matricula;
        this.correoInstitucional = correoInstitucional;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.foto = foto;
        this.idPeriodoEscolar = idPeriodoEscolar;
        this.periodoEscolar = periodoEscolar;
        this.idExperienciaEducativa = idExperienciaEducativa;
        this.nrcExperienciaEducativa = nrcExperienciaEducativa;
        this.idProyecto = idProyecto;
        this.nombreProyecto = nombreProyecto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public int getIdPeriodoEscolar() {
        return idPeriodoEscolar;
    }

    public void setIdPeriodoEscolar(int idPeriodoEscolar) {
        this.idPeriodoEscolar = idPeriodoEscolar;
    }

    public String getPeriodoEscolar() {
        return periodoEscolar;
    }

    public void setPeriodoEscolar(String periodoEscolar) {
        this.periodoEscolar = periodoEscolar;
    }

    public int getIdExperienciaEducativa() {
        return idExperienciaEducativa;
    }

    public void setIdExperienciaEducativa(int idExperienciaEducativa) {
        this.idExperienciaEducativa = idExperienciaEducativa;
    }

    public String getNrcExperienciaEducativa() {
        return nrcExperienciaEducativa;
    }

    public void setNrcExperienciaEducativa(String nrcExperienciaEducativa) {
        this.nrcExperienciaEducativa = nrcExperienciaEducativa;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    @Override
    public String toString() {
        return matricula;
    }
    
    
}
