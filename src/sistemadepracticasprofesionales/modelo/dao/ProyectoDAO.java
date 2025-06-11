package sistemadepracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sistemadepracticasprofesionales.modelo.ConexionBD;
import sistemadepracticasprofesionales.modelo.pojo.Proyecto;

/**
 *
 * @author eugen
 * Fecha:24/05/25
 * Descripcion: DAO para el acceso a la base de datos con metodos relacionados a un Proyecto
 */
public class ProyectoDAO {
    
    public static Proyecto obtenerProyecto(int id) throws SQLException{
        Proyecto proyecto = new Proyecto();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "SELECT p.id_proyecto, p.nombre, p.descripcion, p.estado, p.cupo, "
                + "p.fecha_inicio, p.fecha_fin, p.id_organizacion_vinculada, ov.razon_social AS nombre_organizacion "
                + "FROM proyecto p "
                + "INNER JOIN organizacion_vinculada ov ON p.id_organizacion_vinculada = ov.id_organizacion_vinculada "
                + "WHERE p.id_proyecto = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, id);
            ResultSet resultado = sentencia.executeQuery();
            if(resultado.next()){
                proyecto = convertirProyecto(resultado);
            }
            conexionBD.close();
        }else{
            throw new SQLException();
        }
        return proyecto;
    }
    
    public static List<Proyecto> obtenerProyectosConCupoDisponible() throws SQLException{
        List<Proyecto> proyectosConCupoDisponible = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "SELECT p.id_proyecto, p.nombre, p.descripcion, p.estado, p.cupo, "
                + "p.fecha_inicio, p.fecha_fin, p.id_organizacion_vinculada, ov.razon_social AS nombre_organizacion, "
                + "(SELECT COUNT(*) FROM estudiante e WHERE e.id_proyecto = p.id_proyecto) AS estudiantes_asignados "
                + "FROM proyecto p "
                + "INNER JOIN organizacion_vinculada ov ON p.id_organizacion_vinculada = ov.id_organizacion_vinculada "
                + "WHERE p.estado = 'Activo' "
                + "HAVING estudiantes_asignados < p.cupo";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                proyectosConCupoDisponible.add(convertirProyecto(resultado));
            }
            conexionBD.close();
        }else{
            throw new SQLException();
        }
        return proyectosConCupoDisponible;
    }
    
    private static Proyecto convertirProyecto(ResultSet resultado) throws SQLException{
        Proyecto proyecto = new Proyecto();
        proyecto.setId(resultado.getInt("id_proyecto"));
        proyecto.setNombre(resultado.getString("nombre"));
        proyecto.setDescripcion(resultado.getString("descripcion"));
        proyecto.setEstado(resultado.getString("estado"));
        proyecto.setCupo(resultado.getInt("cupo"));
        proyecto.setFechaInicio(resultado.getString("fecha_inicio"));
        proyecto.setFechaFin(resultado.getString("fecha_fin"));
        proyecto.setIdOrganizacionVinculada(resultado.getInt("id_organizacion_vinculada"));
        proyecto.setNombreOrganizacionVinculada(resultado.getString("nombre_organizacion"));
        
        return proyecto;
    }
}
