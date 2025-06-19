package sistemadepracticasprofesionales.dominio;

import java.sql.SQLException;
import sistemadepracticasprofesionales.modelo.dao.ResponsableProyectoDAO;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;

/**
 *
 * @author Nash
 * Fecha: 07/06/2025
 * Descricpión: Clase para gestionar las reglas de negocio relacionadas a la gestión de los Responsables del Proyecto
 */
public class ResponsableProyectoDM {
    public static ResultadoOperacion verificarExistenciaCorreo(String correo){
        ResultadoOperacion resultado = new ResultadoOperacion();
        try {
            boolean existe = ResponsableProyectoDAO.existeResponsableCorreo(correo);
            resultado.setError(existe);
            if (existe) {
                resultado.setMensaje("El correo ingresado del Responsable del Proyecto ya existe en nuestros registros, por favor ingrese otro");
            }
        }catch (SQLException e) {
            resultado.setError(true);
            resultado.setMensaje("Por el momento no se puede validar el correo inténtelo más tarde");
        }
        return resultado;
    }
    
}
