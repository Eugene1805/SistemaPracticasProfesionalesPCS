package sistemadepracticasprofesionales.utilidades.validacion.estrategias;

import javafx.scene.control.DatePicker;
import sistemadepracticasprofesionales.utilidades.validacion.ResultadoValidacion;

/**
 *
 * @author Nash
 * Fecha: 30/05/2025
 * Descripcióm Estrategia de validacion utilizada para validar DatePickers
 */
public class DateValidationStrategy implements IEstrategiaValidacion<DatePicker>{
    private final boolean obligatorio;

    public DateValidationStrategy(boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    @Override
    public ResultadoValidacion validar(DatePicker datePicker) {
        if (obligatorio && datePicker.getValue() == null ) {
            return new ResultadoValidacion(false, "Selección obligatoria");
        }
        return new ResultadoValidacion(true, "");

    }

    @Override
    public String getMensajeError() {
        return "Error en seleccion";
    }
}
