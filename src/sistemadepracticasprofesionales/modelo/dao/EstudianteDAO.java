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
    
    public static Estudiante obtenerEstudiantePorMatricula(String matricula) throws SQLException {
        Estudiante estudiante = null;
        Connection conexion = ConexionBD.abrirConexion();
        
        if (conexion != null) {
            String consulta = "SELECT e.*, " +
                              "pe.nombre_periodo AS periodo_escolar, " +
                              "ee.nrc AS nrc_experiencia_educativa, " +
                              "p.nombre AS nombre_proyecto " +
                              "FROM estudiante e " +
                              "LEFT JOIN periodo_escolar pe ON e.id_periodo_escolar = pe.id_periodo_escolar " +
                              "LEFT JOIN experiencia_educativa ee ON e.id_experiencia_educativa = ee.id_experiencia_educativa " +
                              "LEFT JOIN proyecto p ON e.id_proyecto = p.id_proyecto " +
                              "WHERE e.matricula = ?";
            try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
                declaracion.setString(1, matricula);
                try (ResultSet resultado = declaracion.executeQuery()) {
                    if (resultado.next()) {
                        estudiante = convertirEstudiante(resultado);
                    }
                }
            } finally {
                conexion.close();
            }
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
        if (conexion == null) {
            throw new SQLException("No se pudo conectar a la base de datos.");
        }

        // La consulta usa UNION para combinar los resultados de 4 búsquedas separadas en una sola lista.
        // UNION elimina automáticamente los estudiantes duplicados.
        String consulta = 
            // 1. Estudiantes con REPORTES sin validar
            "(SELECT e.id_estudiante, e.nombre, e.apellido_paterno, e.apellido_materno, e.matricula " +
            " FROM estudiante e" +
            " JOIN expediente exp ON e.id_estudiante = exp.id_estudiante" +
            " JOIN entrega_reporte er ON exp.id_expediente = er.id_expediente" +
            " JOIN reporte r ON er.id_reporte = r.id_reporte" +
            " WHERE r.archivo IS NOT NULL AND r.fecha_revisado IS NULL)" +

            " UNION " +

            // 2. Estudiantes con DOCUMENTOS INICIALES sin validar
            "(SELECT e.id_estudiante, e.nombre, e.apellido_paterno, e.apellido_materno, e.matricula " +
            " FROM estudiante e" +
            " JOIN expediente exp ON e.id_estudiante = exp.id_estudiante" +
            " JOIN entrega_documento ed ON exp.id_expediente = ed.id_expediente" +
            " JOIN documento_inicial di ON ed.id_documento_inicial = di.id_documento_inicial" + // Asumiendo que el FK apunta a la tabla correcta
            " WHERE di.archivo IS NOT NULL AND di.fecha_revisado IS NULL)" +

            " UNION " +
            
            // 3. Estudiantes con DOCUMENTOS INTERMEDIOS sin validar
            "(SELECT e.id_estudiante, e.nombre, e.apellido_paterno, e.apellido_materno, e.matricula " +
            " FROM estudiante e" +
            " JOIN expediente exp ON e.id_estudiante = exp.id_estudiante" +
            " JOIN entrega_documento ed ON exp.id_expediente = ed.id_expediente" +
            " JOIN documento_intermedio dm ON ed.id_documento_intermedio = dm.id_documento_intermedio" +
            " WHERE dm.archivo IS NOT NULL AND dm.fecha_revisado IS NULL)" +

            " UNION " +

            // 4. Estudiantes con DOCUMENTOS FINALES sin validar
            "(SELECT e.id_estudiante, e.nombre, e.apellido_paterno, e.apellido_materno, e.matricula " +
            " FROM estudiante e" +
            " JOIN expediente exp ON e.id_estudiante = exp.id_estudiante" +
            " JOIN entrega_documento ed ON exp.id_expediente = ed.id_expediente" +
            " JOIN documento_final df ON ed.id_documento_final = df.id_documento_final" +
            " WHERE df.archivo IS NOT NULL AND df.fecha_revisado IS NULL)";

        try (PreparedStatement declaracion = conexion.prepareStatement(consulta);
             ResultSet resultado = declaracion.executeQuery()) {
            
            while (resultado.next()) {
                Estudiante estudiante = new Estudiante();
                estudiante.setId(resultado.getInt("id_estudiante"));
                String nombreCompleto = resultado.getString("nombre") + " " + 
                                        resultado.getString("apellido_paterno") + " " + 
                                        (resultado.getString("apellido_materno") != null ? resultado.getString("apellido_materno") : "");
                estudiante.setNombre(nombreCompleto.trim());
                estudiante.setMatricula(resultado.getString("matricula"));
                estudiantes.add(estudiante);
            }
        } finally {
            conexion.close();
        }
        
        return estudiantes;
    }
    
    public static ResultadoOperacion guardarAsignacion(int idEstudiante, int idProyecto) {
        ResultadoOperacion resultadoOperacion = new ResultadoOperacion();
        resultadoOperacion.setError(true);
        Connection conexionBD = null;

        try {
            conexionBD = ConexionBD.abrirConexion();
            if (conexionBD == null) {
                resultadoOperacion.setMensaje("No se pudo conectar a la base de datos.");
                return resultadoOperacion;
            }

            // 1. Iniciar la transacción
            conexionBD.setAutoCommit(false);

            // 2. Asignar el proyecto al estudiante
            String consultaEstudiante = "UPDATE estudiante SET id_proyecto = ? WHERE id_estudiante = ?";
            PreparedStatement sentenciaEstudiante = conexionBD.prepareStatement(consultaEstudiante);
            sentenciaEstudiante.setInt(1, idProyecto);
            sentenciaEstudiante.setInt(2, idEstudiante);
            int filasEstudiante = sentenciaEstudiante.executeUpdate();

            // 3. Restar 1 al cupo del proyecto.
            //"AND cupo > 0" para evitar asignar proyectos sin cupo.
            String consultaProyecto = "UPDATE proyecto SET cupo = cupo - 1 WHERE id_proyecto = ? AND cupo > 0";
            PreparedStatement sentenciaProyecto = conexionBD.prepareStatement(consultaProyecto);
            sentenciaProyecto.setInt(1, idProyecto);
            int filasProyecto = sentenciaProyecto.executeUpdate();

            // 4. Verificar que ambas operaciones tuvieron éxito
            if (filasEstudiante > 0 && filasProyecto > 0) {
                // 5. Si todo salió bien, confirmar la transacción
                conexionBD.commit();
                resultadoOperacion.setError(false);
                resultadoOperacion.setMensaje("Asignación de proyecto guardada correctamente.");
            } else {
                // 6. Si algo falló (estudiante no encontrado, o proyecto sin cupo), revertir
                conexionBD.rollback();
                if (filasProyecto == 0) {
                    resultadoOperacion.setMensaje("No se pudo asignar el proyecto. El proyecto no tiene cupo disponible.");
                } else {
                    resultadoOperacion.setMensaje("No se pudo guardar la asignación, el estudiante no fue encontrado.");
                }
            }
        } catch (SQLException e) {
            resultadoOperacion.setMensaje("Error de base de datos: " + e.getMessage());
            // 7. Si ocurre una excepción SQL, también revertir
            if (conexionBD != null) {
                try {
                    conexionBD.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al intentar hacer rollback: " + ex.getMessage());
                }
            }
        } finally {
            // 8. Cerrar la conexión y restaurar el autoCommit
            if (conexionBD != null) {
                try {
                    conexionBD.setAutoCommit(true); // Restaurar el modo por defecto
                    conexionBD.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
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
