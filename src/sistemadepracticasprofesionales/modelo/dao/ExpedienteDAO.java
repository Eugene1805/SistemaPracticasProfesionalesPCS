package sistemadepracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import sistemadepracticasprofesionales.modelo.ConexionBD;
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
        String sqlUpdateExpediente = "UPDATE Expediente SET id_evaluacion_organizacion_vinculada = ? WHERE id_estudiante = ?";
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
}
