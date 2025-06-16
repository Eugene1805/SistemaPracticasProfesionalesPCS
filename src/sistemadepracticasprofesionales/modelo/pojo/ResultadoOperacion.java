package sistemadepracticasprofesionales.modelo.pojo;

/**
 *
 * @author eugen
 * Fecha: 23/05/25
 * Descripcion: POJO para modelar un mensaje personalizado del resultado de una operacion en la base de datos
 * y un resultado de error en caso de existir facilitando los flujos
 */
public class ResultadoOperacion {
    private String mensaje;
    private boolean error;
    private int filasAfectadas;

    public ResultadoOperacion() {
    }

    public ResultadoOperacion(String mensaje, boolean error) {
        this.mensaje = mensaje;
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getFilasAfectadas() {
        return filasAfectadas;
    }

    public void setFilasAfectadas(int filasAfectadas) {
        this.filasAfectadas = filasAfectadas;
    }
    
    
}
