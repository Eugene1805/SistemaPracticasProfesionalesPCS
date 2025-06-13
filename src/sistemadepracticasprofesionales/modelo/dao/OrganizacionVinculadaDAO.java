package sistemadepracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    
    public static OrganizacionVinculada obtenerOrganizacionVinculadaPorProyecto(int idProyecto) throws SQLException {
        OrganizacionVinculada organizacion = null;
        Connection conexion = ConexionBD.abrirConexion();

        if (conexion != null) {
            String consulta = "SELECT ov.*, p.estado " +
                              "FROM organizacion_vinculada ov " +
                              "JOIN proyecto p ON ov.id_organizacion_vinculada = p.id_organizacion_vinculada " +
                              "WHERE p.id_proyecto = ?";
            try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
                declaracion.setInt(1, idProyecto);
                try (ResultSet resultado = declaracion.executeQuery()) {
                    if (resultado.next()) {
                        organizacion = convertirOrganizacionVinculada(resultado);
                    }
                }
            } finally {
                conexion.close();
            }
        }
        return organizacion;
    }    
    
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
    
    public static ArrayList<OrganizacionVinculada> obtenerOrganizacionesVinculadas () throws SQLException{
        ArrayList<OrganizacionVinculada> organizaciones = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "SELECT id_organizacion_vinculada, razon_social, telefono, direccion, ciudad, "
                    + "numero_usuarios_directos, numero_usuarios_indirectos, estado, sector FROM organizacion_vinculada";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                organizaciones.add(convertirOrganizacionVinculada(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
            
        }else{
            throw new SQLException("No hay conexion");
        }
        return organizaciones;
    }
    
    private static OrganizacionVinculada convertirOrganizacionVinculada(ResultSet resultado) throws SQLException{
        OrganizacionVinculada organizacion = new OrganizacionVinculada();
        organizacion.setId(resultado.getInt("id_organizacion_vinculada"));
        organizacion.setRazonSocial(resultado.getString("razon_social"));
        organizacion.setTelefono(resultado.getString("telefono"));
        organizacion.setDireccion(resultado.getString("direccion"));
        organizacion.setCiudad(resultado.getString("ciudad"));
        organizacion.setNummeroUsuariosDirectos(resultado.getInt("numero_usuarios_directos"));
        organizacion.setNumeroUsuariosIndirectos(resultado.getInt("numero_usuarios_indirectos"));
        organizacion.setEstado(resultado.getString("estado"));
        organizacion.setSector(resultado.getString("sector"));
        
        return organizacion;
    }
}
