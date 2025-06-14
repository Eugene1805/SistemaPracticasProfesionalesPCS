/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemadepracticasprofesionales.utilidades.validacion.estrategias;

import javafx.scene.control.DatePicker;
import sistemadepracticasprofesionales.utilidades.validacion.ResultadoValidacion;

/**
 *
 * @author Nash
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
