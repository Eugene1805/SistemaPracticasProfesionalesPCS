
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
 * Fecha: 12/06/2025
 * Descripcion: DAO para el acceso a la base de datos con metodos relacionados a la obtención 
 * de los avances que llevan los estudiantes en el perido escolar actual
 */
public class AvanceDAO {
     private static List<AvanceEntrega> obtenerAvanceGenerico(int idEstudiante, int idPeriodoEscolar, String tipoEntrega, String tablaDocumento, String idColumnaDocumento) throws SQLException {
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
                "WHERE exp.id_estudiante = ? AND exp.id_periodo_escolar = ? AND ed.tipo_entrega = ? AND exp.estado = 'Activo'", 
                idColumnaDocumento, tablaDocumento, idColumnaDocumento, idColumnaDocumento
            );
            try (PreparedStatement sentencia = conexionBD.prepareStatement(consulta)) {
                sentencia.setInt(1, idEstudiante);
                sentencia.setInt(2, idPeriodoEscolar);
                sentencia.setString(3, tipoEntrega);
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
     
    public static List<AvanceEntrega> obtenerAvanceReportes(int idEstudiante, int idPeriodoEscolar) throws SQLException {
        List<AvanceEntrega> avances = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT er.id_entrega_reporte AS id_entrega_documento, r.id_reporte AS id_archivo, " +
                    "er.titulo, r.fecha_entregado, r.fecha_revisado, er.calificacion, obs.descripcion AS observacion " +
                    "FROM entrega_reporte er " +
                    "JOIN expediente exp ON er.id_expediente = exp.id_expediente " +
                    "JOIN reporte r ON er.id_reporte = r.id_reporte " +
                    "LEFT JOIN observacion obs ON er.id_observacion = obs.id_observacion " +
                    "WHERE exp.id_estudiante = ? AND exp.id_periodo_escolar = ? AND exp.estado = 'Activo'";
            try(PreparedStatement sentencia = conexionBD.prepareStatement(consulta)) {
                sentencia.setInt(1, idEstudiante);
                sentencia.setInt(2, idPeriodoEscolar);
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
    
    public static byte[] obtenerArchivoDeEntrega(int idArchivo, String tipo, String subtipo) throws SQLException {
        Connection conexion = ConexionBD.abrirConexion();
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        String tabla;
        String columnaId;

        if ("REPORTE".equalsIgnoreCase(tipo)) {
            tabla = "reporte";
            columnaId = "id_reporte";
        } else {
            switch (subtipo.toUpperCase()) {
                case "INICIAL":
                    tabla = "documento_inicial";
                    columnaId = "id_documento_inicial";
                    break;
                case "INTERMEDIO":
                    tabla = "documento_intermedio";
                    columnaId = "id_documento_intermedio";
                    break;
                case "FINAL":
                    tabla = "documento_final";
                    columnaId = "id_documento_final";
                    break;
                default:
                    throw new SQLException("Subtipo de documento no válido: " + subtipo);
            }
        }

        String sql = String.format("SELECT archivo FROM %s WHERE %s = ?", tabla, columnaId);
        byte[] archivoBytes = null;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idArchivo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    archivoBytes = rs.getBytes("archivo");
                }
            }
        } finally {
            conexion.close();
        }

        return archivoBytes;
    }    
    
    public static List<AvanceEntrega> obtenerAvanceDocumentosIniciales(int idEstudiante, int idPeriodoEscolar) throws SQLException {
        return obtenerAvanceGenerico(idEstudiante, idPeriodoEscolar, "Inicial", "documento_inicial", "id_documento_inicial");
    }
    
    public static List<AvanceEntrega> obtenerAvanceDocumentosIntermedios(int idEstudiante, int idPeriodoEscolar) throws SQLException {
        return obtenerAvanceGenerico(idEstudiante, idPeriodoEscolar, "Intermedio", "documento_intermedio", "id_documento_intermedio");
    }
    
    public static List<AvanceEntrega> obtenerAvanceDocumentosFinales(int idEstudiante, int idPeriodoEscolar) throws SQLException {
        return obtenerAvanceGenerico(idEstudiante, idPeriodoEscolar, "Final", "documento_final", "id_documento_final");
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
