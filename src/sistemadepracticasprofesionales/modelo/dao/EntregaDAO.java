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
import sistemadepracticasprofesionales.modelo.pojo.Entrega.Tipo;
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
        if (conexion != null) {
            String consulta = 
            "(SELECT er.id_entrega_reporte AS id_entrega, r.id_reporte AS id_archivo, er.titulo, er.descripcion, er.fecha_inicio, er.fecha_fin, r.fecha_entregado, 'REPORTE' AS tipo " +
            "FROM entrega_reporte er " +
            "JOIN reporte r ON er.id_reporte = r.id_reporte " +
            "JOIN expediente exp ON er.id_expediente = exp.id_expediente " +
            "WHERE exp.id_estudiante = ? AND r.archivo IS NOT NULL AND r.fecha_revisado IS NULL) " +
            "UNION " +
            "(SELECT ed.id_entrega_documento AS id_entrega, d.id_documento AS id_archivo, ed.titulo, ed.descripcion, ed.fecha_inicio, ed.fecha_fin, d.fecha_entregado, 'DOCUMENTO' AS tipo " +
            "FROM entrega_documento ed " +
            "JOIN documento d ON ed.id_documento = d.id_documento " +
            "JOIN expediente exp ON ed.id_expediente = exp.id_expediente " +
            "WHERE exp.id_estudiante = ? AND d.archivo IS NOT NULL AND d.fecha_revisado IS NULL)";

            try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
                declaracion.setInt(1, idEstudiante);
                declaracion.setInt(2, idEstudiante);
                try (ResultSet resultado = declaracion.executeQuery()) {
                    while (resultado.next()) {
                        Entrega entrega = new Entrega();
                        entrega.setIdEntrega(resultado.getInt("id_entrega"));
                        entrega.setIdArchivo(resultado.getInt("id_archivo"));
                        entrega.setTitulo(resultado.getString("titulo"));
                        entrega.setDescripcion(resultado.getString("descripcion"));
                        entrega.setFechaInicio(resultado.getDate("fecha_inicio").toString());
                        entrega.setFechaFin(resultado.getDate("fecha_fin").toString());
                        entrega.setFechaEntregado(resultado.getDate("fecha_entregado").toString());
                        entrega.setTipo(Tipo.valueOf(resultado.getString("tipo")));
                        entregas.add(entrega);
                    }
                }
            } finally {
                conexion.close();
            }
        }else{
            throw new SQLException();
        }
        return entregas;
    }

    public static byte[] obtenerArchivo(int idArchivo, Tipo tipo) throws SQLException {
        byte[] archivoBytes = null;
        Connection conexion = ConexionBD.abrirConexion();
        if(conexion != null){
            String sql = "";
            if (tipo == Tipo.DOCUMENTO) {
                sql = "SELECT archivo FROM documento WHERE id_documento = ?";
            } else {
                sql = "SELECT archivo FROM reporte WHERE id_reporte = ?";
            }

            try (PreparedStatement declaracion = conexion.prepareStatement(sql)) {
                declaracion.setInt(1, idArchivo);
                try (ResultSet resultado = declaracion.executeQuery()) {
                    if (resultado.next()) {
                        Blob blob = resultado.getBlob("archivo");
                        if (blob != null) {
                            archivoBytes = blob.getBytes(1, (int) blob.length());
                        }
                    }
                }
            } finally {
                conexion.close();
            }
        }else{
            throw new SQLException();
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
    
    public static boolean validarEntrega(int idEntrega, int idArchivo, Tipo tipo, int calificacion, Integer idObservacion) throws SQLException {
        boolean resultado = false;
        Connection conexion = ConexionBD.abrirConexion();
        String sqlUpdateEntrega;
        String sqlUpdateArchivo;

        if (tipo == Tipo.DOCUMENTO) {
            sqlUpdateEntrega = "UPDATE entrega_documento SET calificacion = ?, id_observacion = ? WHERE id_entrega_documento = ?";
            sqlUpdateArchivo = "UPDATE documento SET fecha_revisado = ? WHERE id_documento = ?";
        } else {
            sqlUpdateEntrega = "UPDATE entrega_reporte SET calificacion = ?, id_observacion = ? WHERE id_entrega_reporte = ?";
            sqlUpdateArchivo = "UPDATE reporte SET fecha_revisado = ? WHERE id_reporte = ?";
        }
        
        try {
            conexion.setAutoCommit(false);
            
            try(PreparedStatement psEntrega = conexion.prepareStatement(sqlUpdateEntrega)) {
                psEntrega.setInt(1, calificacion);
                if (idObservacion != null) {
                    psEntrega.setInt(2, idObservacion);
                } else {
                    psEntrega.setNull(2, java.sql.Types.INTEGER);
                }
                psEntrega.setInt(3, idEntrega);
                psEntrega.executeUpdate();
            }
            
            try(PreparedStatement psArchivo = conexion.prepareStatement(sqlUpdateArchivo)) {
                psArchivo.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
                psArchivo.setInt(2, idArchivo);
                psArchivo.executeUpdate();
            }
            
            conexion.commit();
            resultado = true;
            
        } catch (SQLException ex) {
            conexion.rollback();
            throw ex;
        } finally {
            if(conexion != null) {
                conexion.setAutoCommit(true);
                conexion.close();
            }
        }
        return resultado;
    }
}
