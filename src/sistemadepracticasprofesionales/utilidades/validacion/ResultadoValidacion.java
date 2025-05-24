package sistemadepracticasprofesionales.utilidades.validacion;

/**
 *
 * @author eugen
 * Fecha: 24/05/25
 * Descripcion: Ayuda a pasar el resultado de una validacion junto a un mensaje personalizado
 */
public class ResultadoValidacion {
    private final boolean valido;
    private final String mensaje;

    public ResultadoValidacion(boolean valido, String mensaje) {
        this.valido = valido;
        this.mensaje = mensaje;
    }

    public boolean isValido() {
        return valido;
    }

    public String getMensaje() {
        return mensaje;
    }
}
