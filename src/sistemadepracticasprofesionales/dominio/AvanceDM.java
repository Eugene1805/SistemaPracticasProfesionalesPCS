/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemadepracticasprofesionales.dominio;

import java.sql.SQLException;
import java.util.List;
import sistemadepracticasprofesionales.modelo.dao.AvanceDAO;
import sistemadepracticasprofesionales.modelo.pojo.AvanceEntrega;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;

/**
 *
 * @author Nash
 * Fecha: 12/06/2025
 * Descricpión: Clase para gestionar las reglas de negocio relacionadas a la consulta del avance de los 
 * estudiantes dados
 */
public class AvanceDM {
        public static ResultadoOperacion verificarExistenciaDeEntregas(int idEstudiante, int idPeriodoEscolar) {
        ResultadoOperacion resultado = new ResultadoOperacion();
        resultado.setError(false); 

        try {
            List<AvanceEntrega> iniciales = AvanceDAO.obtenerAvanceDocumentosIniciales(idEstudiante,
                    idPeriodoEscolar);
            if (!iniciales.isEmpty()) return resultado; 

            List<AvanceEntrega> intermedios = AvanceDAO.obtenerAvanceDocumentosIntermedios(idEstudiante,
                    idPeriodoEscolar);
            if (!intermedios.isEmpty()) return resultado;

            List<AvanceEntrega> finales = AvanceDAO.obtenerAvanceDocumentosFinales(idEstudiante,
                    idPeriodoEscolar);
            if (!finales.isEmpty()) return resultado;
            
            List<AvanceEntrega> reportes = AvanceDAO.obtenerAvanceReportes(idEstudiante,
                    idPeriodoEscolar);
            if (!reportes.isEmpty()) return resultado;

            resultado.setError(true);
            resultado.setMensaje("Lo sentimos no hay ninguna entrega registrada en el sistema para el perido escolar actual.");

        } catch (SQLException e) {
            resultado.setError(true);
            resultado.setMensaje("No se pudo verificar la existencia de entregas. Intente más tarde.");
            e.printStackTrace();
        }

        return resultado;
    }
}
