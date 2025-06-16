
package sistemadepracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sistemadepracticasprofesionales.modelo.ConexionBD;
import sistemadepracticasprofesionales.modelo.pojo.AvanceEntrega;

/**
 *
 * @author Nash
 */
public class AvanceDAO {
     private static List<AvanceEntrega> obtenerAvanceGenerico(int idEstudiante, String tipoEntrega, String tablaDocumento, String idColumnaDocumento) throws SQLException {
        List<AvanceEntrega> avances = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = String.format(
                "SELECT ed.id_entrega_documento, doc.%s AS id_archivo, ed.titulo, doc.fecha_entregado, " +
                "doc.fecha_revisado, ed.calificacion, obs.descripcion AS observacion " +
                "FROM entrega_documento ed " +
                "JOIN expediente exp ON ed.id_expediente = exp.id_expediente " +
                "JOIN %s doc ON ed.%s = doc.%s " +
                "LEFT JOIN observacion obs ON ed.id_observacion = obs.id_observacion " +
                "WHERE exp.id_estudiante = ? AND ed.tipo_entrega = ?",
                idColumnaDocumento, tablaDocumento, idColumnaDocumento, idColumnaDocumento
            );
            try (PreparedStatement sentencia = conexionBD.prepareStatement(consulta)) {
                sentencia.setInt(1, idEstudiante);
                sentencia.setString(2, tipoEntrega);
                try (ResultSet resultado = sentencia.executeQuery()) {
                    while(resultado.next()) {
                        avances.add(convertirResultadoAvance(resultado));
                    }
                }
            } finally {
                conexionBD.close();
            }
        }
        return avances;
    }
     
    public static List<AvanceEntrega> obtenerAvanceReportes(int idEstudiante) throws SQLException {
        List<AvanceEntrega> avances = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT er.id_entrega_reporte AS id_entrega_documento, r.id_reporte AS id_archivo, " +
                    "er.titulo, r.fecha_entregado, r.fecha_revisado, er.calificacion, obs.descripcion AS observacion " +
                    "FROM entrega_reporte er " +
                    "JOIN expediente exp ON er.id_expediente = exp.id_expediente " +
                    "JOIN reporte r ON er.id_reporte = r.id_reporte " +
                    "LEFT JOIN observacion obs ON er.id_observacion = obs.id_observacion " +
                    "WHERE exp.id_estudiante = ?";
            try(PreparedStatement sentencia = conexionBD.prepareStatement(consulta)) {
                sentencia.setInt(1, idEstudiante);
                try(ResultSet resultado = sentencia.executeQuery()) {
                    while(resultado.next()) {
                        avances.add(convertirResultadoAvance(resultado));
                    }
                }
            } finally {
                conexionBD.close();
            }
        }
        return avances;
    } 
    
    public static List<AvanceEntrega> obtenerAvanceDocumentosIniciales(int idEstudiante) throws SQLException {
        return obtenerAvanceGenerico(idEstudiante, "Inicial", "documento_inicial", "id_documento_inicial");
    }
    
    public static List<AvanceEntrega> obtenerAvanceDocumentosIntermedios(int idEstudiante) throws SQLException {
        return obtenerAvanceGenerico(idEstudiante, "Intermedio", "documento_intermedio", "id_documento_intermedio");
    }
    
    public static List<AvanceEntrega> obtenerAvanceDocumentosFinales(int idEstudiante) throws SQLException {
        return obtenerAvanceGenerico(idEstudiante, "Final", "documento_final", "id_documento_final");
    }
    
    private static AvanceEntrega convertirResultadoAvance(ResultSet resultado) throws SQLException {
        AvanceEntrega avance = new AvanceEntrega();
        avance.setIdEntrega(resultado.getInt("id_entrega_documento"));
        avance.setIdArchivo(resultado.getInt("id_archivo"));
        avance.setTitulo(resultado.getString("titulo"));
         
        // Manejo de fechas y números nulos para evitar errores
        Date fechaEntregado = resultado.getDate("fecha_entregado");
        avance.setFechaEntregado(fechaEntregado != null ? fechaEntregado.toString() : "N/A");
        
        Date fechaRevisado = resultado.getDate("fecha_revisado");
        avance.setFechaRevisado(fechaRevisado != null ? fechaRevisado.toString() : "Pendiente");
        
        Integer calificacion = (Integer) resultado.getObject("calificacion");
        avance.setCalificacion(calificacion);
        
        avance.setObservacion(resultado.getString("observacion") != null ? resultado.getString("observacion") : "");
        
        return avance;
    }
}
