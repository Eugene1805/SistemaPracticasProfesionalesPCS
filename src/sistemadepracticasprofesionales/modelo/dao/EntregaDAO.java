package sistemadepracticasprofesionales.modelo.dao;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import sistemadepracticasprofesionales.modelo.ConexionBD;
import sistemadepracticasprofesionales.modelo.pojo.Entrega;
import sistemadepracticasprofesionales.modelo.pojo.Entrega.SubtipoDocumento;
import sistemadepracticasprofesionales.modelo.pojo.Entrega.Tipo;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemadepracticasprofesionales.modelo.pojo.TipoDocumentoInicial;
/**
 *
 * @author eugen
 * Fecha:12/06/25
 * Descripcion: DAO para manejar todo lo relacionado a las entregas
 */
public class EntregaDAO {
    
    public static List<Entrega> obtenerEntregasSinValidarPorEstudiante(int idEstudiante) throws SQLException {
        List<Entrega> entregas = new ArrayList<>();
        Connection conexion = ConexionBD.abrirConexion();
        if (conexion == null) {
            throw new SQLException("No se pudo conectar a la base de datos.");
        }

        String consulta = 
            // 1. REPORTES
            "(SELECT er.id_entrega_reporte AS id_entrega, r.id_reporte AS id_archivo, er.titulo, er.descripcion, er.fecha_inicio, er.fecha_fin, r.fecha_entregado, 'REPORTE' AS tipo, 'NINGUNO' AS subtipo_doc " +
            " FROM entrega_reporte er " +
            " JOIN reporte r ON er.id_reporte = r.id_reporte " +
            " JOIN expediente exp ON er.id_expediente = exp.id_expediente " +
            " WHERE exp.id_estudiante = ? AND r.archivo IS NOT NULL AND r.fecha_revisado IS NULL) " +
            "UNION " +
            // 2. DOCUMENTOS INICIALES
            "(SELECT ed.id_entrega_documento, di.id_documento_inicial, ed.titulo, ed.descripcion, ed.fecha_inicio, ed.fecha_fin, di.fecha_entregado, 'DOCUMENTO', 'INICIAL' " +
            " FROM entrega_documento ed " +
            " JOIN documento_inicial di ON ed.id_documento_inicial = di.id_documento_inicial " +
            " JOIN expediente exp ON ed.id_expediente = exp.id_expediente " +
            " WHERE exp.id_estudiante = ? AND ed.tipo_entrega = 'Inicial' AND di.archivo IS NOT NULL AND di.fecha_revisado IS NULL) " +
            "UNION " +
            // 3. DOCUMENTOS INTERMEDIOS
            "(SELECT ed.id_entrega_documento, dm.id_documento_intermedio, ed.titulo, ed.descripcion, ed.fecha_inicio, ed.fecha_fin, dm.fecha_entregado, 'DOCUMENTO', 'INTERMEDIO' " +
            " FROM entrega_documento ed " +
            " JOIN documento_intermedio dm ON ed.id_documento_intermedio = dm.id_documento_intermedio " +
            " JOIN expediente exp ON ed.id_expediente = exp.id_expediente " +
            " WHERE exp.id_estudiante = ? AND ed.tipo_entrega = 'Intermedio' AND dm.archivo IS NOT NULL AND dm.fecha_revisado IS NULL) " +
            "UNION " +
            // 4. DOCUMENTOS FINALES
            "(SELECT ed.id_entrega_documento, df.id_documento_final, ed.titulo, ed.descripcion, ed.fecha_inicio, ed.fecha_fin, df.fecha_entregado, 'DOCUMENTO', 'FINAL' " +
            " FROM entrega_documento ed " +
            " JOIN documento_final df ON ed.id_documento_final = df.id_documento_final " +
            " JOIN expediente exp ON ed.id_expediente = exp.id_expediente " +
            " WHERE exp.id_estudiante = ? AND ed.tipo_entrega = 'Final' AND df.archivo IS NOT NULL AND df.fecha_revisado IS NULL)";

        try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
            // Se asignan los parámetros para cada subconsulta del UNION
            declaracion.setInt(1, idEstudiante);
            declaracion.setInt(2, idEstudiante);
            declaracion.setInt(3, idEstudiante);
            declaracion.setInt(4, idEstudiante);
            try (ResultSet resultado = declaracion.executeQuery()) {
                while (resultado.next()) {
                    Entrega entrega = new Entrega();
                    entrega.setIdEntrega(resultado.getInt(1)); // Por índice para evitar ambigüedad de nombre
                    entrega.setIdArchivo(resultado.getInt(2));
                    entrega.setTitulo(resultado.getString(3));
                    entrega.setDescripcion(resultado.getString(4));
                    entrega.setFechaInicio(resultado.getDate(5).toString());
                    entrega.setFechaFin(resultado.getDate(6).toString());
                    entrega.setFechaEntregado(resultado.getDate(7).toString());
                    entrega.setTipo(Tipo.valueOf(resultado.getString(8)));
                    entrega.setSubtipoDoc(SubtipoDocumento.valueOf(resultado.getString(9)));
                    entregas.add(entrega);
                }
            }
        } finally {
            conexion.close();
        }
        return entregas;
    }

    public static byte[] obtenerArchivo(int idArchivo, Tipo tipo, SubtipoDocumento subtipo) throws SQLException {
        byte[] archivoBytes = null;
        Connection conexion = ConexionBD.abrirConexion();
        if (conexion == null) throw new SQLException("No se pudo conectar a la base de datos.");
        
        String sql;
        if (tipo == Tipo.REPORTE) {
            sql = "SELECT archivo FROM reporte WHERE id_reporte = ?";
        } else {
            switch (subtipo) {
                case INICIAL:    sql = "SELECT archivo FROM documento_inicial WHERE id_documento_inicial = ?"; break;
                case INTERMEDIO: sql = "SELECT archivo FROM documento_intermedio WHERE id_documento_intermedio = ?"; break;
                case FINAL:      sql = "SELECT archivo FROM documento_final WHERE id_documento_final = ?"; break;
                default: throw new SQLException("Subtipo de documento no válido.");
            }
        }

        try (PreparedStatement declaracion = conexion.prepareStatement(sql)) {
            declaracion.setInt(1, idArchivo);
            try (ResultSet resultado = declaracion.executeQuery()) {
                if (resultado.next()) {
                    Blob blob = resultado.getBlob("archivo");
                    if (blob != null) archivoBytes = blob.getBytes(1, (int) blob.length());
                }
            }
        } finally {
            conexion.close();
        }
        return archivoBytes;
    }

    public static int guardarObservacion(String descripcion) throws SQLException {
        int idObservacion = -1;
        Connection conexion = ConexionBD.abrirConexion();
        if (conexion != null) {
            String sql = "INSERT INTO observacion (descripcion, fecha_observacion) VALUES (?, ?)";
        
            try(PreparedStatement declaracion = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                declaracion.setString(1, descripcion);
                declaracion.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
                declaracion.executeUpdate();
                try(ResultSet generatedKeys = declaracion.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idObservacion = generatedKeys.getInt(1);
                    }
                }
            } finally {
                conexion.close();
            }
        }else{
            throw new SQLException();
        }
        return idObservacion;
    }
    
    public static ResultadoOperacion validarEntrega(int idEntrega, int idArchivo, Tipo tipo, SubtipoDocumento subtipo, int calificacion, Integer idObservacion) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexion = ConexionBD.abrirConexion();
        if (conexion == null) {
            resultado.setError(true);
            resultado.setMensaje("No se pudo conectar a la base de datos.");
            return resultado;
        }

        String sqlUpdateEntrega;
        String sqlUpdateArchivo;

        if (tipo == Tipo.REPORTE) {
            sqlUpdateEntrega = "UPDATE entrega_reporte SET calificacion = ?, id_observacion = ? WHERE id_entrega_reporte = ?";
            sqlUpdateArchivo = "UPDATE reporte SET fecha_revisado = ? WHERE id_reporte = ?";
        } else { // Si es DOCUMENTO
            sqlUpdateEntrega = "UPDATE entrega_documento SET calificacion = ?, id_observacion = ? WHERE id_entrega_documento = ?";
            switch (subtipo) {
                case INICIAL:    sqlUpdateArchivo = "UPDATE documento_inicial SET fecha_revisado = ? WHERE id_documento_inicial = ?"; break;
                case INTERMEDIO: sqlUpdateArchivo = "UPDATE documento_intermedio SET fecha_revisado = ? WHERE id_documento_intermedio = ?"; break;
                case FINAL:      sqlUpdateArchivo = "UPDATE documento_final SET fecha_revisado = ? WHERE id_documento_final = ?"; break;
                default: throw new SQLException("Subtipo de documento no válido para la validación.");
            }
        }
        
        try {
            conexion.setAutoCommit(false);
            
            // 1. Actualizar la tabla de entrega (entrega_reporte o entrega_documento)
            try(PreparedStatement psEntrega = conexion.prepareStatement(sqlUpdateEntrega)) {
                psEntrega.setInt(1, calificacion);
                if (idObservacion != null) psEntrega.setInt(2, idObservacion);
                else psEntrega.setNull(2, java.sql.Types.INTEGER);
                psEntrega.setInt(3, idEntrega);
                if (psEntrega.executeUpdate() == 0) throw new SQLException("No se actualizó la entrega, ID no encontrado: " + idEntrega);
            }
            
            // 2. Actualizar la tabla del archivo (reporte, documento_inicial, etc.)
            try(PreparedStatement psArchivo = conexion.prepareStatement(sqlUpdateArchivo)) {
                psArchivo.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
                psArchivo.setInt(2, idArchivo);
                if (psArchivo.executeUpdate() == 0) throw new SQLException("No se actualizó el archivo, ID no encontrado: " + idArchivo);
            }
            
            conexion.commit();
            resultado.setError(false);
            resultado.setMensaje("La entrega ha sido validada correctamente.");
            
        } catch (SQLException ex) {
            conexion.rollback();
            resultado.setError(true);
            resultado.setMensaje("Error en la transacción: " + ex.getMessage());
        } finally {
            conexion.setAutoCommit(true);
            conexion.close();
        }
        return resultado;
    }
    
    public static ResultadoOperacion programarEntregaIniciales(String titulo, String descripcion, java.sql.Date fechaInicio, java.sql.Date fechaFin, 
            TipoDocumentoInicial tipoDocumento, int idPeriodoEscolar) throws SQLException {

        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD == null) {
            resultado.setError(true);
            resultado.setMensaje("No se pudo conectar a la base de datos.");
            return resultado;
        }

        try {
            conexionBD.setAutoCommit(false);

            List<Integer> idsExpediente = ExpedienteDAO.obtenerIdsExpedientesActivosPorPeriodo(idPeriodoEscolar);

            if (idsExpediente.isEmpty()) {
                throw new SQLException("No se encontraron expedientes activos para el periodo escolar actual. No se puede crear la entrega.");
            }

            String consultaInsertDocumento = "INSERT INTO documento_inicial (tipo_documento) VALUES (?)";
            String consultaInsertEntrega = "INSERT INTO entrega_documento (titulo, descripcion, fecha_inicio, fecha_fin, tipo_entrega, id_expediente, id_documento_inicial) " +
                                      "VALUES (?, ?, ?, ?, 'Inicial', ?, ?)";
            //Iterar sobre cada expediente para crear su propio par de registros (documento y entrega)
            for (Integer idExpediente : idsExpediente) {
                int idDocumentoGenerado;
                try (PreparedStatement psDoc = conexionBD.prepareStatement(consultaInsertDocumento, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    psDoc.setString(1, tipoDocumento.getNombreEnBD()); 
                    psDoc.executeUpdate();

                    try (ResultSet rs = psDoc.getGeneratedKeys()) {
                        if (rs.next()) {
                            idDocumentoGenerado = rs.getInt(1);
                        } else {
                            throw new SQLException("Fallo al crear el documento inicial, no se obtuvo ID.");
                        }
                    }
                }

                try (PreparedStatement psEntrega = conexionBD.prepareStatement(consultaInsertEntrega)) {
                    psEntrega.setString(1, titulo);
                    psEntrega.setString(2, descripcion);
                    psEntrega.setDate(3, fechaInicio); 
                    psEntrega.setDate(4, fechaFin);   
                    psEntrega.setInt(5, idExpediente);
                    psEntrega.setInt(6, idDocumentoGenerado);

                    if (psEntrega.executeUpdate() == 0) {
                         throw new SQLException("Fallo al crear la entrega para el expediente ID: " + idExpediente);
                    }
                }
            }

            conexionBD.commit();
            resultado.setError(false);
            resultado.setFilasAfectadas(idsExpediente.size());
            resultado.setMensaje("Entrega programada exitosamente para " + idsExpediente.size() + " estudiantes.");

        } catch (SQLException ex) {
            conexionBD.rollback();
            resultado.setError(true);
            resultado.setMensaje("Error al programar la entrega: " + ex.getMessage());
            ex.printStackTrace(); 
        } finally {
            if (conexionBD != null) {
                conexionBD.setAutoCommit(true);
                conexionBD.close();
            }
        }

        return resultado;
    }
    
    // sistemadepracticasprofesionales.modelo.dao.EntregaDAO.java

    public static List<Entrega> obtenerEntregasPorTipo(int idExpediente, String tipoEntrega) throws SQLException {
        List<Entrega> entregas = new ArrayList<>();
        Connection conexion = ConexionBD.abrirConexion();

        if (conexion == null) {
            throw new SQLException("No se pudo conectar a la base de datos.");
        }

        // FIX: Cambiado a LEFT JOIN para incluir entregas sin archivo subido
        String consulta = "SELECT ed.id_entrega_documento, ed.titulo, ed.descripcion, " +
                          "ed.fecha_inicio, ed.fecha_fin, ed.id_documento_inicial, di.fecha_entregado " +
                          "FROM entrega_documento ed " +
                          "LEFT JOIN documento_inicial di ON ed.id_documento_inicial = di.id_documento_inicial " +
                          "WHERE ed.id_expediente = ? AND ed.tipo_entrega = ?";

        try (PreparedStatement ps = conexion.prepareStatement(consulta)) {
            ps.setInt(1, idExpediente);
            ps.setString(2, tipoEntrega);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Entrega entrega = new Entrega();
                    entrega.setIdEntrega(rs.getInt("id_entrega_documento"));
                    entrega.setTitulo(rs.getString("titulo"));
                    entrega.setDescripcion(rs.getString("descripcion"));
                    entrega.setFechaInicio(rs.getDate("fecha_inicio").toString());
                    entrega.setFechaFin(rs.getDate("fecha_fin").toString());

                    // IMPORTANTE: id_documento_inicial se guarda en el campo idArchivo del POJO
                    entrega.setIdArchivo(rs.getInt("id_documento_inicial"));

                    // fecha_entregado puede ser NULL si nunca se ha subido un archivo
                    entrega.setFechaEntregado(rs.getDate("fecha_entregado") != null ? rs.getDate("fecha_entregado").toString() : null);

                    entrega.setTipo(Entrega.Tipo.DOCUMENTO);
                    entrega.setSubtipoDoc(Entrega.SubtipoDocumento.INICIAL);
                    entregas.add(entrega);
                }
            }
        } finally {
            conexion.close();
        }
        return entregas;
    }

}
