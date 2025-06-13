/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemadepracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    
}
