package sistemadepracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import sistemadepracticasprofesionales.modelo.ConexionBD;
import sistemadepracticasprofesionales.modelo.pojo.PeriodoEscolar;

/**
 *
 * @author eugen
 * Fecha:14/06/25
 * Descripcion: DAO para obtener el periodo escolar cursante o anteriores
 */
public class PeriodoEscolarDAO {
    public static PeriodoEscolar obtenerPeriodoEscolarActual() throws SQLException{
        PeriodoEscolar periodoActual = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "SELECT id_periodo_escolar, nombre_periodo, fecha_inicio, fecha_fin FROM periodo_escolar "
                    + "WHERE ? BETWEEN fecha_inicio AND fecha_fin";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            
            sentencia.setDate(1, java.sql.Date.valueOf(LocalDate.now()));

            ResultSet resultado = sentencia.executeQuery();

        if (resultado.next()) {
            periodoActual = new PeriodoEscolar();
            periodoActual.setId(resultado.getInt("id_periodo_escolar"));
            periodoActual.setNombrePeriodo(resultado.getString("nombre_periodo"));
            periodoActual.setFechaInicio(resultado.getString("fecha_inicio"));
            periodoActual.setFechaFin(resultado.getString("fecha_fin"));
        }

        resultado.close();
        sentencia.close();
        conexionBD.close();
        }else{
            throw new SQLException();
        }
        return periodoActual;
    }
}
