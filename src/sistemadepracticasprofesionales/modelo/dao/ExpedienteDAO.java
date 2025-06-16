package sistemadepracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sistemadepracticasprofesionales.modelo.ConexionBD;
import sistemadepracticasprofesionales.modelo.pojo.Expediente;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;

/**
 *
 * @author eugen
 * Fecha: 14/06/25
 * Descripcion: DAO para interactura con la tabla del Expediente en la base de datos
 */
public class ExpedienteDAO {
    public static ResultadoOperacion guardarEvaluacionOrganizacionVinculada(int idEvaluacionOrganizacionVinculada,
            int idEstudiante) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexion = ConexionBD.abrirConexion();
        if (conexion == null) {
            resultado.setError(true);
            resultado.setMensaje("No se pudo conectar a la base de datos.");
            return resultado;
        }
        String sqlUpdateExpediente = "UPDATE Expediente SET id_evaluacion_organizacion = ? WHERE id_estudiante = ?";
        PreparedStatement sentencia = conexion.prepareStatement(sqlUpdateExpediente);
        sentencia.setInt(1, idEvaluacionOrganizacionVinculada);
        sentencia.setInt(2, idEstudiante);
        int filasAfectadas = sentencia.executeUpdate();
        if(filasAfectadas > 0 ){
            resultado.setError(false);
            resultado.setMensaje("Expediente actualizado con exito");
        }else{
            resultado.setError(true);
            resultado.setMensaje("No fue posible agregar la evaluacion el expediente");
        }
        return resultado;
    }
    
    public static List<Integer> obtenerIdsExpedientePorPeriodo(int idPeriodoEscolar) throws SQLException{
        List<Integer> ids = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT id_expediente FROM expediente WHERE id_periodo_escolar = ?";    
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idPeriodoEscolar);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {                
                ids.add(resultado.getInt("id_expediente"));
            }
            sentencia.close();
            conexionBD.close();
        }else{
            throw new SQLException("No hay conexión");
        }
        return ids;
    }
    
    public static Expediente obtenerExpedientePorEstudiante(int idEstudiante) throws SQLException {
        Expediente expediente = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT id_expediente, horas_acomuladas, calificacion_documento, calificacion_evaluacion, calificacion_evaluacion_organizacion_vinculada, " +
                              "calificacion_total, estado, id_periodo_escolar, id_evaluacion_organizacion, id_estudiante FROM expediente WHERE id_estudiante = ?";

            try (PreparedStatement sentencia = conexionBD.prepareStatement(consulta)) {
                sentencia.setInt(1, idEstudiante);
                try (ResultSet resultado = sentencia.executeQuery()) {
                    if (resultado.next()) {
                        expediente = new Expediente();
                        expediente.setIdExpediente(resultado.getInt("id_expediente"));
                        expediente.setHorasAcumuladas(resultado.getInt("horas_acomuladas"));
                        expediente.setCalificacionDocumento((Float) resultado.getObject("calificacion_documento"));
                        expediente.setCalificacionEvaluacion((Float) resultado.getObject("calificacion_evaluacion"));
                        expediente.setCalificacionEvaluacionOrganizacionVinculada((Float) resultado.getObject("calificacion_evaluacion_organizacion_vinculada"));
                        expediente.setCalificacionTotal((Float) resultado.getObject("calificacion_total"));
                        expediente.setEstado(resultado.getString("estado"));
                        expediente.setIdPeriodoEscolar(resultado.getInt("id_periodo_escolar"));
                        expediente.setIdEvaluacionOrganizacion(resultado.getInt("id_evaluacion_organizacion"));
                        expediente.setIdEstudiante(resultado.getInt("id_estudiante"));
                    }
                }
            } finally {
                conexionBD.close();
            }
        }
        return expediente;
        }
}
