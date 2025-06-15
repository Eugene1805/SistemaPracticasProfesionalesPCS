package sistemadepracticasprofesionales.dominio;

import java.sql.SQLException;
import sistemadepracticasprofesionales.modelo.dao.ResponsableProyectoDAO;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;

/**
 *
 * @author Nash
 */
public class ResponsableProyectoDM {
    public static ResultadoOperacion verificarExistenciaCorreo(String correo){
        ResultadoOperacion resultado = new ResultadoOperacion();
        try {
            boolean existe = ResponsableProyectoDAO.existeResponsableCorreo(correo);
            resultado.setError(existe);
            if (existe) {
                resultado.setMensaje("El correo ingresado del Responsable del Proyecto ya existe en los registros, ingrese otro");
            }
        }catch (SQLException e) {
            resultado.setError(true);
            resultado.setMensaje("Por el momento no se puede validar el correo inténtelo más tarde");
        }
        return resultado;
    }
    
}
