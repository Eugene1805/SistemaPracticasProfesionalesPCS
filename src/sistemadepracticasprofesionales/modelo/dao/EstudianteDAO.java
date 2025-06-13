package sistemadepracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sistemadepracticasprofesionales.modelo.ConexionBD;
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
        Estudiante estudiante = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "SELECT e.id_estudiante, e.nombre, e.apellido_paterno, e.apellido_materno, e.matricula, "
                + "e.correo_institucional, e.id_periodo_escolar, pe.nombre_periodo AS periodo_escolar, "
                + "e.id_experiencia_educativa, ee.nrc AS nrc_experiencia_educativa, "
                + "e.id_proyecto, p.nombre AS nombre_proyecto "
                + "FROM estudiante e "
                + "LEFT JOIN periodo_escolar pe ON e.id_periodo_escolar = pe.id_periodo_escolar "
                + "LEFT JOIN experiencia_educativa ee ON e.id_experiencia_educativa = ee.id_experiencia_educativa "
                + "LEFT JOIN proyecto p ON e.id_proyecto = p.id_proyecto "
                + "WHERE e.id_estudiante = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, id);
            ResultSet resultado = sentencia.executeQuery();
            if(resultado.next()){
                estudiante = convertirEstudiante(resultado);
            }
            conexionBD.close();
        }else{
            throw new SQLException();
        }
        
        return estudiante;
    }
    
    public static List<Estudiante> obtenerEstudiantesSinProyectoAsignado() throws SQLException{
        List<Estudiante> estudiantes = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "SELECT e.id_estudiante, e.nombre, e.apellido_paterno, e.apellido_materno, e.matricula, "
                + "e.correo_institucional, e.id_periodo_escolar, pe.nombre_periodo AS periodo_escolar, "
                + "e.id_experiencia_educativa, ee.nrc AS nrc_experiencia_educativa, "
                + "e.id_proyecto, p.nombre AS nombre_proyecto "
                + "FROM estudiante e "
                + "LEFT JOIN periodo_escolar pe ON e.id_periodo_escolar = pe.id_periodo_escolar "
                + "LEFT JOIN experiencia_educativa ee ON e.id_experiencia_educativa = ee.id_experiencia_educativa "
                + "LEFT JOIN proyecto p ON e.id_proyecto = p.id_proyecto "
                + "WHERE e.id_proyecto IS NULL";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            System.err.print("Se ejecuto la consulta");
            while(resultado.next()){
                estudiantes.add(convertirEstudiante(resultado));
                System.err.print("Se convirtio el estudiante");
            }
            conexionBD.close();
        }else{
            throw new SQLException();
        }
        return estudiantes;
    }
    
    public static List<Estudiante> obtenerEstudiantesConEntregasSinValidar() throws SQLException {
        List<Estudiante> estudiantes = new ArrayList<>();
        Connection conexion = ConexionBD.abrirConexion();
        if(conexion!= null){
            String consulta = "SELECT DISTINCT e.id_estudiante, e.nombre, e.apellido_paterno, e.apellido_materno, e.matricula " +
                          "FROM estudiante e " +
                          "JOIN expediente exp ON e.id_estudiante = exp.id_estudiante " +
                          "LEFT JOIN entrega_reporte er ON exp.id_expediente = er.id_expediente " +
                          "LEFT JOIN reporte r ON er.id_reporte = r.id_reporte " +
                          "LEFT JOIN entrega_documento ed ON exp.id_expediente = ed.id_expediente " +
                          "LEFT JOIN documento d ON ed.id_documento = d.id_documento " +
                          "WHERE (r.archivo IS NOT NULL AND r.fecha_revisado IS NULL) OR (d.archivo IS NOT NULL AND d.fecha_revisado IS NULL)";

            try (PreparedStatement declaracion = conexion.prepareStatement(consulta);
                 ResultSet resultado = declaracion.executeQuery()) {
                while (resultado.next()) {
                    Estudiante estudiante = new Estudiante();
                    estudiante.setId(resultado.getInt("id_estudiante"));
                    estudiante.setNombre(resultado.getString("nombre") + " " + resultado.getString("apellido_paterno") + " " + resultado.getString("apellido_materno"));
                    estudiante.setMatricula(resultado.getString("matricula"));
                    estudiantes.add(estudiante);
                }
            } finally {
                conexion.close();
            }
        }else{
            throw new SQLException();
        }
        return estudiantes;
    }
    
    public static ResultadoOperacion guardarAsignacion(int idEstudiante, int idProyecto) throws SQLException{
        ResultadoOperacion resultadoOperacion = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "UPDATE estudiante SET id_proyecto = ? WHERE id_estudiante = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idProyecto);
            sentencia.setInt(2, idEstudiante);
            int filasAfectadas = sentencia.executeUpdate();
            if(filasAfectadas > 0){
                resultadoOperacion.setError(false);
                resultadoOperacion.setMensaje("Asignación de proyecto guardada correctamente");
            }else{
                resultadoOperacion.setError(true);
                resultadoOperacion.setMensaje("No se pudo guardar la asignación del proyecto");
            }
            conexionBD.close();
        }else{
            throw new SQLException();
        }
        return resultadoOperacion;
    }
    
    private static Estudiante convertirEstudiante(ResultSet resultado) throws SQLException{
        Estudiante estudiante = new Estudiante();
        estudiante.setId(resultado.getInt("id_estudiante"));
        estudiante.setNombre(resultado.getString("nombre"));
        estudiante.setApellidoPaterno(resultado.getString("apellido_paterno"));
        estudiante.setApellidoMaterno(resultado.getString("apellido_materno") != null ?
                resultado.getString("apellido_materno") : " ");
        estudiante.setMatricula(resultado.getString("matricula"));
        estudiante.setCorreoInstitucional(resultado.getString("correo_institucional"));
        estudiante.setIdPeriodoEscolar(resultado.getInt("id_periodo_escolar"));
        estudiante.setPeriodoEscolar(resultado.getString("periodo_escolar"));
        estudiante.setIdExperienciaEducativa(resultado.getInt("id_experiencia_educativa"));
        estudiante.setNrcExperienciaEducativa(resultado.getString("nrc_experiencia_educativa"));
        estudiante.setIdProyecto(resultado.getInt("id_proyecto"));
        estudiante.setNombreProyecto(resultado.getString("nombre_proyecto"));
        
        return estudiante;
    }
}
