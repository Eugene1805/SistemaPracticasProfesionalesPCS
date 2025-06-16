/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemadepracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sistemadepracticasprofesionales.modelo.ConexionBD;
import sistemadepracticasprofesionales.modelo.pojo.Profesor;

/**
 *
 * @author Nash
 * Fecha: 10/06/2025
 * Descripcion: DAO para el acceso a la base de datos con metodos relacionados a un Profesor
 */
public class ProfesorDAO {
    public static Profesor obtenerProfesorPorUsername(String username) throws SQLException{
        Profesor profesor = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT id_profesor, numero_personal, nombre, apellido_paterno, apellido_materno, correo_institucional " +
                          "FROM profesor WHERE correo_institucional LIKE ?";
            try(PreparedStatement sentencia = conexionBD.prepareStatement(consulta)){
                sentencia.setString(1, username + "@%");
                try(ResultSet resultado = sentencia.executeQuery()){
                    if (resultado.next()) {
                        profesor = new Profesor();
                        profesor.setIdProfesor(resultado.getInt("id_profesor"));
                        profesor.setNombre(resultado.getString("nombre"));
                        profesor.setApellidoPaterno(resultado.getString("apellido_paterno"));
                        profesor.setApellidoMaterno(resultado.getString("apellido_materno"));
                        profesor.setCorreoInstitucional(resultado.getString("correo_institucional"));       
                    }
                }
            } finally {
                conexionBD.close();
            }
        }
        return profesor;
    }
}
