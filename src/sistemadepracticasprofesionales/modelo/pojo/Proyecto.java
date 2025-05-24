package sistemadepracticasprofesionales.modelo.pojo;

/**
 *
 * @author eugen
 * Fecha:24/05/25
 * Descripcion: POJO que modela un Proyecto
 */
public class Proyecto {
    private int id;
    private String nombre;
    private String descripcion;
    private String estado;
    private int cupo;
    private String fechaInicio;
    private String fechaFin;
    private int idOrganizacionVinculada;
    private String nombreOrganizacionVinculada;

    public Proyecto() {
    }

    public Proyecto(int id, String nombre, String descripcion, String estado, int cupo, String fechaInicio, String fechaFin, int idOrganizacionVinculada, String nombreOrganizacionVinculada) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.cupo = cupo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.idOrganizacionVinculada = idOrganizacionVinculada;
        this.nombreOrganizacionVinculada = nombreOrganizacionVinculada;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
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

    public int getIdOrganizacionVinculada() {
        return idOrganizacionVinculada;
    }

    public void setIdOrganizacionVinculada(int idOrganizacionVinculada) {
        this.idOrganizacionVinculada = idOrganizacionVinculada;
    }

    public String getNombreOrganizacionVinculada() {
        return nombreOrganizacionVinculada;
    }

    public void setNombreOrganizacionVinculada(String nombreOrganizacionVinculada) {
        this.nombreOrganizacionVinculada = nombreOrganizacionVinculada;
    }  

    @Override
    public String toString() {
        return  nombre;
    }
}
