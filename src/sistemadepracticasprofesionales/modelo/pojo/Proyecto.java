package sistemadepracticasprofesionales.modelo.pojo;

import sistemadepracticasprofesionales.modelo.enums.EstadoProyecto;

/**
 *
 * @author meler
 * Fecha:12/06/25
 * Descripcion: POJO que modela un Proyecto
 */
public class Proyecto {
    private int idProyecto;
    private String nombre;
    private String descripcion;
    private EstadoProyecto estado;
    private int cupo;
    private String fechaInicio;
    private String fechaFin;
    private int idOrganizacionVinculada;
    private String nombreOrganizacionVinculada;
    private int idResponsableProyecto;
    private String nombreResponsableProyecto;

    public Proyecto() {
    }

    public Proyecto(int idProyecto, String nombre, String descripcion, EstadoProyecto estado, int cupo, String fechaInicio, String fechaFin, int idOrganizacionVinculada, String nombreOrganizacionVinculada, int idResponsableProyecto, String nombreResponsableProyecto) {
        this.idProyecto = idProyecto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.cupo = cupo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.idOrganizacionVinculada = idOrganizacionVinculada;
        this.nombreOrganizacionVinculada = nombreOrganizacionVinculada;
        this.idResponsableProyecto = idResponsableProyecto;
        this.nombreResponsableProyecto = nombreResponsableProyecto;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
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

    public EstadoProyecto getEstado() {
        return estado;
    }

    public void setEstado(EstadoProyecto estado) {
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

    public int getIdResponsableProyecto() {
        return idResponsableProyecto;
    }

    public void setIdResponsableProyecto(int idResponsableProyecto) {
        this.idResponsableProyecto = idResponsableProyecto;
    }

    public String getNombreResponsableProyecto() {
        return nombreResponsableProyecto;
    }

    public void setNombreResponsableProyecto(String nombreResponsableProyecto) {
        this.nombreResponsableProyecto = nombreResponsableProyecto;
    }
    
    @Override
    public String toString() {
        return  nombre;
    }
}
