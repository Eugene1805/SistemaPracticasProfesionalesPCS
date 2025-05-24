package sistemadepracticasprofesionales.utilidades.validacion.estrategias;

import javafx.scene.control.ComboBox;
import sistemadepracticasprofesionales.utilidades.validacion.ResultadoValidacion;

/**
 *
 * @author eugen
 * Fecha: 24/05/25
 * Descripcion:  Estrategia de validacion utilizada para validar ComboBoxes
 */
public class ComboValidationStrategy implements IEstrategiaValidacion<ComboBox>{
    private final boolean obligatorio;

    public ComboValidationStrategy(boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    @Override
    public ResultadoValidacion validar(ComboBox comboBox) {
        if (obligatorio && (comboBox.getValue() == null || comboBox.getValue().toString().isEmpty())) {
            return new ResultadoValidacion(false, "Selección obligatoria");
        }
        return new ResultadoValidacion(true, "");

    }

    @Override
    public String getMensajeError() {
        return "Error en seleccion";
    }
}
