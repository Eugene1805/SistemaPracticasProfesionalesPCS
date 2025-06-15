package sistemadepracticasprofesionales.dominio;

import java.sql.SQLException;
import java.time.LocalDate;
import sistemadepracticasprofesionales.modelo.pojo.PeriodoEscolar;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;

/**
 *
 * @author Nash
 */
public class EntregaDM {
    public static ResultadoOperacion verificarFechasEntrega(LocalDate fechaInicio, LocalDate fechaFin, PeriodoEscolar periodo){
        ResultadoOperacion resultado = new ResultadoOperacion();
        resultado.setError(false);
        
        if (fechaInicio.isAfter(fechaFin)) {
            resultado.setError(true);
            resultado.setMensaje("La fecha de inicio de la entrega no puede ser posterior a la fecha de fin");
            return resultado;
        }
        
        if (fechaInicio.isBefore(LocalDate.parse(periodo.getFechaInicio())) || 
                fechaFin.isAfter(LocalDate.parse(periodo.getFechaFin()))) {
            resultado.setError(true);
            resultado.setMensaje("Las fechas de entrega deben de estar dentro del rango del perido escolar actual");
            return resultado;
        } 
            
       return resultado;
    }    
}
