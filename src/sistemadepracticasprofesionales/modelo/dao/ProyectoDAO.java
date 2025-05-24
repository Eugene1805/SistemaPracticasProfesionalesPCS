package sistemadepracticasprofesionales.modelo.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sistemadepracticasprofesionales.modelo.pojo.Proyecto;

/**
 *
 * @author eugen
 * Fecha:24/05/25
 * Descripcion: DAO para el acceso a la base de datos con metodos relacionados a un Proyecto
 */
public class ProyectoDAO {
    
    public static Proyecto obtenerProyecto(int id)throws SQLException{
        Proyecto proyecto = new Proyecto();
        //FIX
        return proyecto;
    }
    public static List<Proyecto> obtenerProyectosConCupoDisponible() throws SQLException{
        List<Proyecto> proyectosConCupoDisponible= new ArrayList();
        //FIX
        return proyectosConCupoDisponible;
    }
}
