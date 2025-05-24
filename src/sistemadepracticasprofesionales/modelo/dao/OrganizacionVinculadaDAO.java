package sistemadepracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import sistemadepracticasprofesionales.modelo.pojo.OrganizacionVinculada;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;
import sistemadepracticasprofesionales.modelo.ConexionBD;

/**
 *
 * @author eugen
 * Fecha: 23/05/25
 * Descripcion: DAO para el acceso a la base de datos con metodos relacionados con una Organizacion Vinculada
 */
public class OrganizacionVinculadaDAO {
    
    public static ResultadoOperacion registrarOrganizacionVinculada(OrganizacionVinculada organizacionVinculada)
            throws SQLException{
        ResultadoOperacion resultadoOperacion = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "INSERT INTO organizacion_vinculada (razon_social, telefono, direccion, "
                    + "ciudad, estado, sector, numero_usuarios_directos, numero_usuarios_indirectos) "
                    + "VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement sentencia = conexionBD.prepareCall(consulta);
            sentencia.setString(1, organizacionVinculada.getRazonSocial());
            sentencia.setString(2, organizacionVinculada.getTelefono());
            sentencia.setString(3, organizacionVinculada.getDireccion());
            sentencia.setString(4, organizacionVinculada.getCiudad());
            sentencia.setString(5, organizacionVinculada.getEstado());
            sentencia.setString(6, organizacionVinculada.getSector());
            sentencia.setInt(7, organizacionVinculada.getNummeroUsuariosDirectos());
            sentencia.setInt(8, organizacionVinculada.getNumeroUsuariosIndirectos());
            
            int filasAfectadas = sentencia.executeUpdate();
            resultadoOperacion.setError(filasAfectadas <= 0);
            resultadoOperacion.setMensaje(resultadoOperacion.isError() ?
                    "Lo sentimos no fue posible registar los datos de la Organizacion Vinculada" :
                    "La informacion de la Organizacion Vinculada fue registrada correctamente");
        }else{
            throw new SQLException("No hay conexion");
        }
        return resultadoOperacion;
    }
}
