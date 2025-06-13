package sistemadepracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    public static ResultadoOperacion registrarEvaluacionOrganizacion(
            EvaluacionOrganizacion evaluacionOrganizacion) throws SQLException{
        ResultadoOperacion resultadoOperacion = new ResultadoOperacion();
        Connection conexionDB = ConexionBD.abrirConexion();
        if(conexionDB != null){
            String consulta = "INSERT INTO evaluacion_organizacion (claridad_actividades, "
                    + "nivel_relacion_actividades, accesibilidad, ambiente_laboral, oportunidades_aprendizaje "
                    + "acceso_recursos) VALUES (?,?,?,?,?,?)";
            PreparedStatement sentencia = conexionDB.prepareStatement(consulta);
            sentencia.setInt(1, evaluacionOrganizacion.getClaridadActividades());
            sentencia.setInt(2, evaluacionOrganizacion.getNivelRelacionActividades());
            sentencia.setInt(3, evaluacionOrganizacion.getAccesibilidad());
            sentencia.setInt(4, evaluacionOrganizacion.getAmbienteLaboral());
            sentencia.setInt(5, evaluacionOrganizacion.getOportunidades_aprendizaje());
            sentencia.setInt(6, evaluacionOrganizacion.getAccesoRecursos());
            
            int filasAfectadas = sentencia.executeUpdate();
            resultadoOperacion.setError(filasAfectadas <= 0);
            resultadoOperacion.setMensaje(resultadoOperacion.isError() ?
                    "Lo sentimos no fue posible registar los datos de la Evaluacion a la Organizacion Vinculada" :
                    "La informacion de la Evaluacion a la Organizacion Vinculada fue registrada correctamente");
        }else{
            throw new SQLException("No hay conexion");
        }
        return resultadoOperacion;        
    }
}
