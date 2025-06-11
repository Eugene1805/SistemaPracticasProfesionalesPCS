package sistemadepracticasprofesionales.utilidades.validacion.estrategias;

import javafx.scene.control.TextField;
import sistemadepracticasprofesionales.utilidades.validacion.ResultadoValidacion;

/**
 *
 * @author eugen
 * Fecha: 24/05/25
 * Descripcion:  Estrategia de validacion utilizada para validar campos de texto, especificamente aquellos que
 * seran convertidos a enteros
 */
public class NumericValidationStrategy implements IEstrategiaValidacion<TextField>{ //Se pusa TextField por la 
    private final boolean obligatorio;                                              //ausencia de Spinners en Java 8

    public NumericValidationStrategy(boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    @Override
    public ResultadoValidacion validar(TextField textField) {
        String texto = textField.getText() == null ? "" : textField.getText().trim();
        
        if(obligatorio && texto.isEmpty()){
            return new ResultadoValidacion(false, "Campo oblogatorio");
        }
        
        if (!texto.isEmpty()) {
            try {
                Long.valueOf(texto);
            } catch (NumberFormatException e) {
                return new ResultadoValidacion(false, "Debe ser un número válido");
            }
        }
        return new ResultadoValidacion(true, "");
    }

    @Override
    public String getMensajeError() {
        return "Error en campo numerico";
    }
    
    
    
}
