package sistemadepracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sistemadepracticasprofesionales.modelo.ConexionBD;
import sistemadepracticasprofesionales.modelo.pojo.Usuario;

/**
 *
 * @author eugen
 * Fecha:21/05/25
 * Descripcion: DAO para manejar el inicio de sesion del usuario a traves de la validacion de credenciales 
 * guardadas en la base de datos
 */
public class InicioSesionDAO {
    
    public static Usuario validarCredenciales(String username, String password) throws SQLException{
        Usuario usuario = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "SELECT id_usuario, nombre, apellido_paterno, apellido_materno, username, "
                    + "tipo_usuario FROM usuario WHERE username = ? AND password = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, username);
            sentencia.setString(2, password);
            ResultSet resultado = sentencia.executeQuery();
            if(resultado.next()){
                usuario = new Usuario();
                usuario.setId(resultado.getInt("id_usuario"));
                usuario.setNombre(resultado.getString("nombre"));
                usuario.setApellidoPaterno(resultado.getString("apellido_paterno"));
                usuario.setApellidoMaterno((resultado.getString("apellido_materno")) != null ? 
                resultado.getString("apellido_materno"): "");
                usuario.setUsername(resultado.getString("username"));
                usuario.setTipoUsuario(resultado.getString("tipo_usuario"));
                conexionBD.close();
            }
        }else{
            throw new SQLException("No hay conexion");
        }
        return usuario;
    }
}
