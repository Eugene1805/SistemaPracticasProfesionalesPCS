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
            String consulta = "INSERT INTO organizacio_vinculada () VALUES ()";
            PreparedStatement sentencia = conexionBD.prepareCall(consulta);
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
