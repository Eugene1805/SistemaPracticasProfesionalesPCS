package sistemadepracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sistemadepracticasprofesionales.modelo.ConexionBD;
import sistemadepracticasprofesionales.modelo.pojo.EvaluacionOrganizacion;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;

/**
 *
 * @author eugen
 * Fecha: 24/05/25
 * Descripcion: DAO para guardar le evaluacion del estudiante a la Organizacion vinculada
 */
public class EvaluacionOrganizacionDAO {
    public static int registrarEvaluacionOrganizacion(
            EvaluacionOrganizacion evaluacionOrganizacion) throws SQLException{
        int idEvaluacionOrganizacionVinculada = -1;
        Connection conexionDB = ConexionBD.abrirConexion();
        if(conexionDB != null){
            String consulta = "INSERT INTO evaluacion_organizacion (claridad_actividades, "
                    + "nivel_relacion_actividades, accesibilidad, ambiente_laboral, oportunidades_aprendizaje "
                    + "acceso_recursos) VALUES (?,?,?,?,?,?)";
            PreparedStatement sentencia = conexionDB.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);
            sentencia.setInt(1, evaluacionOrganizacion.getClaridadActividades());
            sentencia.setInt(2, evaluacionOrganizacion.getNivelRelacionActividades());
            sentencia.setInt(3, evaluacionOrganizacion.getAccesibilidad());
            sentencia.setInt(4, evaluacionOrganizacion.getAmbienteLaboral());
            sentencia.setInt(5, evaluacionOrganizacion.getOportunidades_aprendizaje());
            sentencia.setInt(6, evaluacionOrganizacion.getAccesoRecursos());
            sentencia.executeUpdate();
            try(ResultSet generatedKeys = sentencia.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idEvaluacionOrganizacionVinculada = generatedKeys.getInt(1);
                    }
                }
            conexionDB.close();
        }else{
            throw new SQLException("No hay conexion");
        }
        return idEvaluacionOrganizacionVinculada;        
    }
}
