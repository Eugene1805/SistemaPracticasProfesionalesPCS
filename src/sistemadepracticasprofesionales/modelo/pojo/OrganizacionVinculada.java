package sistemadepracticasprofesionales.modelo.pojo;

/**
 *
 * @author eugen
 * Fecha: 23/05/25
 * Descripcion: POJO que modela una Organizacion Vinculada
 */
public class OrganizacionVinculada {
    private String nombre;
    private String telefono;
    private String direccion;
    private String ciudad;
    private String estado;
    private String sector;
    private int nummeroUsuariosDirectos;
    private int numeroUsuariosIndirectos;

    public OrganizacionVinculada() {
    }

    public OrganizacionVinculada(String nombre, String telefono, String direccion, String ciudad, String estado, String sector, int nummeroUsuariosDirectos, int numeroUsuariosIndirectos) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.estado = estado;
        this.sector = sector;
        this.nummeroUsuariosDirectos = nummeroUsuariosDirectos;
        this.numeroUsuariosIndirectos = numeroUsuariosIndirectos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public int getNummeroUsuariosDirectos() {
        return nummeroUsuariosDirectos;
    }

    public void setNummeroUsuariosDirectos(int nummeroUsuariosDirectos) {
        this.nummeroUsuariosDirectos = nummeroUsuariosDirectos;
    }

    public int getNumeroUsuariosIndirectos() {
        return numeroUsuariosIndirectos;
    }

    public void setNumeroUsuariosIndirectos(int numeroUsuariosIndirectos) {
        this.numeroUsuariosIndirectos = numeroUsuariosIndirectos;
    }
}
