package sistemadepracticasprofesionales.controlador;

import javafx.scene.Parent;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;

/**
 *
 * @author eugen
 * Fecha:11/06/25
 * Descripcion: Interfaz para definir metodos comunes para los dashboard
 */
public interface Dashboard {
    void inicializar(Usuario usuario);
    Parent obtenerVista();
}
