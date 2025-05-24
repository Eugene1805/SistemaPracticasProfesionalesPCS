package sistemadepracticasprofesionales.modelo.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sistemadepracticasprofesionales.modelo.pojo.Estudiante;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;

/**
 *
 * @author eugen
 * Fecha:24/05/25
 * Descripcion: DAO para el acceso a la base de datos con metodos relacionados a un Estudiante
 */
public class EstudianteDAO {
    
    public static Estudiante obtenerEstudiante(int id) throws SQLException{
        Estudiante estudiante = new Estudiante();
        //FIX
        return estudiante;
    }
    
    public static List<Estudiante> obtenerEstudiantesSinProyectoAsignado() throws SQLException{
        List<Estudiante> estudiantes = new ArrayList();
        //FIX
        return estudiantes;
    }
    
    public static ResultadoOperacion guardarAsignacion(int idEstudiante, int idProyecto) throws SQLException{
        ResultadoOperacion resultadoOperacion = new ResultadoOperacion();
        //FIX
        return resultadoOperacion;
    }
}
