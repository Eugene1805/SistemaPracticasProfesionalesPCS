package sistemadepracticasprofesionales.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import sistemadepracticasprofesionales.modelo.ConexionBD;
import sistemadepracticasprofesionales.modelo.pojo.TipoDocumentoInicial;

/**
 *
 * @author Nash
 * Fecha: 12/06/2025
 * Descripcion: DAO para el acceso a la base de datos con metodos relacionados a los Tipos de documentos iniciales,
 * usado principalmente para verificar qué tipos de entregas ya han sido programadas en un periodo.
 */
public class TipoDocumentoInicialDAO {
    
    public static List<TipoDocumentoInicial> obtenerTiposDisponibles(int idPeriodoEscolar) throws SQLException {
        List<TipoDocumentoInicial> tiposExistentes = new ArrayList<>(Arrays.asList(TipoDocumentoInicial.values()));
        List<String> tiposUsados = new ArrayList<>();
        
        Connection conexionBD = ConexionBD.abrirConexion();   
        if (conexionBD != null) {
            String consulta = "SELECT DISTINCT di.tipo_documento " +
                              "FROM entrega_documento ed " +
                              "JOIN documento_inicial di ON ed.id_documento_inicial = di.id_documento_inicial " +
                              "JOIN expediente exp ON ed.id_expediente = exp.id_expediente " +
                              "WHERE exp.id_periodo_escolar = ? AND ed.tipo_entrega = 'Inicial'";
            try (PreparedStatement sentencia = conexionBD.prepareStatement(consulta)) {
                sentencia.setInt(1, idPeriodoEscolar);
                try (ResultSet resultado = sentencia.executeQuery()) {
                    while (resultado.next()) {
                        tiposUsados.add(resultado.getString("tipo_documento"));
                    }
                }
            } finally {
                conexionBD.close();
            }
        } else {
            throw new SQLException("No hay conexión con la base de datos.");
        }

        List<TipoDocumentoInicial> tiposSinAsignar = tiposExistentes.stream()
                .filter(tipoEnum -> !tiposUsados.contains(tipoEnum.getNombreEnBD()))
                .collect(Collectors.toList());
                
        return tiposSinAsignar;
    }
}
   

