package sistemadepracticasprofesionales.modelo.dao;

import sistemadepracticasprofesionales.modelo.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;
/**
 * Autor: meler
 * Fecha: 12/06/25
 * Descripción: DAO para el acceso a la base de datos con metodos relacionados a un DocumentoInicial
 */
public class DocumentoInicialDAO {

    public static ResultadoOperacion guardarArchivo(int idDocumentoInicial, byte[] archivo) {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexion = ConexionBD.abrirConexion();
        
        String sql = "UPDATE documento_inicial SET archivo = ?, fecha_entregado = CURRENT_DATE WHERE id_documento_inicial = ?";

        if (conexion != null) {
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setBytes(1, archivo);
                ps.setInt(2, idDocumentoInicial);

                int filasAfectadas = ps.executeUpdate();
                if (filasAfectadas > 0) {
                    resultado.setError(false);
                    resultado.setMensaje("Archivo guardado exitosamente.");
                } else {
                    resultado.setError(true);
                    resultado.setMensaje("No se pudo guardar el archivo. El documento no fue encontrado.");
                }
            } catch (SQLException e) {
                resultado.setError(true);
                resultado.setMensaje("Error de SQL: " + e.getMessage());
            } finally {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    // Log o manejo de la excepción al cerrar la conexión
                }
            }
        } else {
            resultado.setError(true);
            resultado.setMensaje("No fue posible conectar con la base de datos.");
        }
        return resultado;
    }
}