/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemadepracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sistemadepracticasprofesionales.modelo.ConexionBD;
import sistemadepracticasprofesionales.modelo.pojo.ResponsableProyecto;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;

/**
 *
 * @author Nash
 */
public class ResponsableProyectoDAO {
    public static ResultadoOperacion registrarResponsableProyecto(ResponsableProyecto responsable) throws  SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "INSERT INTO responsable_proyecto (nombre, apellido_paterno, apellido_materno, "
                    +"telefono, correo, departamento, puesto, id_organizacion_vinculada) "
                    +"VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, responsable.getNombre());
            sentencia.setString(2, responsable.getApellidoPaterno());
            sentencia.setString(3, responsable.getApellidoMaterno());
            sentencia.setString(4, responsable.getTelefono());
            sentencia.setString(5, responsable.getCorreo());
            sentencia.setString(6, responsable.getDepartamento());
            sentencia.setString(7, responsable.getPuesto());
            sentencia.setInt(8, responsable.getIdOrganizacionVinculada());
            int filasAfectadas = sentencia.executeUpdate();
            resultado.setError(filasAfectadas <= 0);
            resultado.setMensaje(resultado.isError() ?
                    "No se pudo registrar al Responssable del Proyecto"
                    : "Responsable del Proyecto registrado correctamente");
            sentencia.close();
            conexionBD.close();
        }else{
            throw new SQLException("No hay conexión");
        }
        return resultado;
    }
    
    public static boolean existeResponsableCorreo(String correo) throws SQLException{
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT COUNT(*) AS total FROM responsable_proyecto WHERE correo = ?";
            PreparedStatement sentencia = conexionBD.prepareCall(consulta);
            sentencia.setString(1, correo);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                return resultado.getInt("total") > 0;
            }
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("No hay conexión");        
        }
        return false;
    }
    
    public static List<ResponsableProyecto> obtenerResponsablesPorOrganizacion(int idOrganizacion) throws SQLException {
        List<ResponsableProyecto> responsables = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            String consulta = "SELECT id_responsable_proyecto, nombre, apellido_paterno, apellido_materno, correo "
                            + "FROM responsable_proyecto WHERE id_organizacion_vinculada = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idOrganizacion);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                ResponsableProyecto responsable = new ResponsableProyecto();
                responsable.setIdResponsable(resultado.getInt("id_responsable_proyecto"));
                responsable.setNombre(resultado.getString("nombre"));
                responsable.setApellidoPaterno(resultado.getString("apellido_paterno"));
                responsable.setApellidoMaterno(resultado.getString("apellido_materno"));
                responsable.setCorreo(resultado.getString("correo"));
                responsables.add(responsable);
            }

            resultado.close();
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("No hay conexión a la base de datos");
        }

        return responsables;
    }

}
