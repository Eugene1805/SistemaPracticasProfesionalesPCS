package sistemadepracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sistemadepracticasprofesionales.modelo.ConexionBD;
import sistemadepracticasprofesionales.modelo.enums.EstadoProyecto;
import sistemadepracticasprofesionales.modelo.pojo.Proyecto;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;

/**
 *
 * @author meler
 * Fecha:12/06/25
 * Descripcion: DAO para el acceso a la base de datos con metodos relacionados a un Proyecto
 */
public class ProyectoDAO {
    
    public static Proyecto obtenerProyecto(int idProyecto) throws SQLException {
        Proyecto proyecto = null;
        Connection conexion = ConexionBD.abrirConexion();

        if (conexion != null) {
            String query = "SELECT p.*, o.razon_social AS nombre_organizacion, " +
                           "rp.nombre AS nombre_responsable, rp.apellido_paterno AS apellido_responsable " +
                           "FROM proyecto p " +
                           "LEFT JOIN organizacion_vinculada o ON p.id_organizacion_vinculada = o.id_organizacion_vinculada " +
                           "LEFT JOIN responsable_proyecto rp ON p.id_responsable_proyecto = rp.id_responsable_proyecto " +
                           "WHERE p.id_proyecto = ?";

            PreparedStatement ps = conexion.prepareStatement(query);
            ps.setInt(1, idProyecto);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                proyecto = new Proyecto();
                proyecto.setIdProyecto(rs.getInt("id_proyecto"));
                proyecto.setNombre(rs.getString("nombre"));
                proyecto.setDescripcion(rs.getString("descripcion"));
                proyecto.setCupo(rs.getInt("cupo"));
                proyecto.setFechaInicio(rs.getString("fecha_inicio"));
                proyecto.setFechaFin(rs.getString("fecha_fin"));

                String estadoStr = rs.getString("estado");
                if (estadoStr != null) {
                    proyecto.setEstado(EstadoProyecto.valueOf(estadoStr));
                }

                proyecto.setIdOrganizacionVinculada(rs.getInt("id_organizacion_vinculada"));
                proyecto.setNombreOrganizacionVinculada(rs.getString("nombre_organizacion"));

                proyecto.setIdResponsableProyecto(rs.getInt("id_responsable_proyecto"));
                String nombreResp = rs.getString("nombre_responsable");
                String apellidoResp = rs.getString("apellido_responsable");
                proyecto.setNombreResponsableProyecto(nombreResp != null && apellidoResp != null
                    ? nombreResp + " " + apellidoResp
                    : "");
            }
            rs.close();
            ps.close();
            conexion.close();
        }

        return proyecto;
    }

    
    public static List<Proyecto> obtenerProyectosConCupoDisponible() throws SQLException{
        List<Proyecto> proyectosConCupoDisponible = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "SELECT p.id_proyecto, p.nombre, p.descripcion, p.estado, p.cupo, "
                + "p.fecha_inicio, p.fecha_fin, p.id_organizacion_vinculada, ov.razon_social AS nombre_organizacion "
                + "FROM proyecto p "
                + "INNER JOIN organizacion_vinculada ov ON p.id_organizacion_vinculada = ov.id_organizacion_vinculada "
                + "WHERE p.estado = 'Activo' "
                + "HAVING p.cupo > 0";
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
        proyecto.setIdProyecto(resultado.getInt("id_proyecto"));
        proyecto.setNombre(resultado.getString("nombre"));
        proyecto.setDescripcion(resultado.getString("descripcion"));
        proyecto.setEstado(EstadoProyecto.valueOf(resultado.getString("estado")));
        proyecto.setCupo(resultado.getInt("cupo"));
        proyecto.setFechaInicio(resultado.getString("fecha_inicio"));
        proyecto.setFechaFin(resultado.getString("fecha_fin"));
        proyecto.setIdOrganizacionVinculada(resultado.getInt("id_organizacion_vinculada"));
        proyecto.setNombreOrganizacionVinculada(resultado.getString("nombre_organizacion"));

        return proyecto;
    }
    
    public static ResultadoOperacion registrarProyecto(Proyecto proyecto) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            String consulta = "INSERT INTO proyecto (nombre, descripcion, estado, cupo, fecha_inicio, fecha_fin, "
                            + "id_organizacion_vinculada, id_responsable_proyecto) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, proyecto.getNombre());
            sentencia.setString(2, proyecto.getDescripcion());
            sentencia.setString(3, proyecto.getEstado().name());
            sentencia.setInt(4, proyecto.getCupo());
            sentencia.setDate(5, Date.valueOf(proyecto.getFechaInicio()));
            sentencia.setDate(6, Date.valueOf(proyecto.getFechaFin()));
            sentencia.setInt(7, proyecto.getIdOrganizacionVinculada());
            sentencia.setInt(8, proyecto.getIdResponsableProyecto());

            int filasAfectadas = sentencia.executeUpdate();
            resultado.setError(filasAfectadas <= 0);
            resultado.setMensaje(resultado.isError()
                    ? "No se pudo registrar el proyecto"
                    : "Proyecto registrado correctamente");

            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("No hay conexión con la base de datos");
        }

        return resultado;
    }
    
    public static List<Proyecto> buscarProyectosPorNombre(String nombre) throws SQLException {
        List<Proyecto> proyectos = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT id_proyecto, nombre, estado, cupo FROM proyecto WHERE nombre LIKE ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, "%" + nombre + "%");
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                Proyecto proyecto = new Proyecto();
                proyecto.setIdProyecto(resultado.getInt("id_proyecto"));
                proyecto.setNombre(resultado.getString("nombre"));
                proyecto.setEstado(EstadoProyecto.valueOf(resultado.getString("estado")));
                proyecto.setCupo(resultado.getInt("cupo"));
                proyectos.add(proyecto);
            }
            conexionBD.close();
        } else {
            throw new SQLException("No hay conexión con la base de datos.");
        }
        return proyectos;
    }
    
    public static ResultadoOperacion actualizarProyecto(Proyecto proyecto) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            String consulta = "UPDATE proyecto SET nombre = ?, descripcion = ?, estado = ?, cupo = ?, "
                            + "fecha_inicio = ?, fecha_fin = ?, id_organizacion_vinculada = ?, "
                            + "id_responsable_proyecto = ? WHERE id_proyecto = ?";

            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, proyecto.getNombre());
            sentencia.setString(2, proyecto.getDescripcion());
            sentencia.setString(3, proyecto.getEstado().name());
            sentencia.setInt(4, proyecto.getCupo());
            sentencia.setDate(5, Date.valueOf(proyecto.getFechaInicio()));
            sentencia.setDate(6, Date.valueOf(proyecto.getFechaFin()));
            sentencia.setInt(7, proyecto.getIdOrganizacionVinculada());
            sentencia.setInt(8, proyecto.getIdResponsableProyecto());
            sentencia.setInt(9, proyecto.getIdProyecto());

            int filasAfectadas = sentencia.executeUpdate();
            resultado.setError(filasAfectadas <= 0);
            resultado.setMensaje(resultado.isError()
                    ? "No se pudo actualizar el proyecto"
                    : "Proyecto actualizado correctamente");

            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("No hay conexión con la base de datos");
        }

        return resultado;
    }




}
