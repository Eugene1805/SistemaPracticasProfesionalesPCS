package sistemadepracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sistemadepracticasprofesionales.modelo.ConexionBD;
import sistemadepracticasprofesionales.modelo.pojo.ExperienciaEducativa;

/**
 *
 * @author Nash
 * Fecha: 10/06/2025
 * Descripcion: DAO para el acceso a la base de datos con metodos relacionados a una Experiencia Educativa
 */
public class ExperienciaEducativaDAO {
    public static ExperienciaEducativa obtenerEEPorProfesor(int idProfesor) throws SQLException {
        ExperienciaEducativa experiencia = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT id_experiencia_educativa, nombre, nrc, bloque, seccion, id_profesor " +
                              "FROM experiencia_educativa WHERE id_profesor = ?";
            try (PreparedStatement sentencia = conexionBD.prepareStatement(consulta)) {
                sentencia.setInt(1, idProfesor);
                try (ResultSet resultado = sentencia.executeQuery()) {
                    if (resultado.next()) {
                        experiencia = new ExperienciaEducativa();
                        experiencia.setId(resultado.getInt("id_experiencia_educativa"));
                        experiencia.setNombre(resultado.getString("nombre"));
                        experiencia.setNrc(resultado.getString("nrc"));
                        experiencia.setBloque(resultado.getString("bloque"));
                        experiencia.setSeccion(resultado.getString("seccion"));
                        experiencia.setIdProfesor(resultado.getInt("id_profesor"));
                    }
                }
            } finally {
                conexionBD.close();
            }
        }
        return experiencia;
    }
    
    public static String obtenerNombrePorId(int idExperienciaEducativa) throws SQLException {
        String nombre = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT nombre FROM experiencia_educativa WHERE id_experiencia_educativa = ?";
            try (PreparedStatement sentencia = conexionBD.prepareStatement(consulta)) {
                sentencia.setInt(1, idExperienciaEducativa);
                try (ResultSet resultado = sentencia.executeQuery()) {
                    if (resultado.next()) {
                        nombre = resultado.getString("nombre");
                    }
                }
            } finally {
                conexionBD.close();
            }
        }
        return nombre;
    }
}
