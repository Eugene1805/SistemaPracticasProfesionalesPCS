package sistemadepracticasprofesionales.utilidades.validacion.estrategias;

import javafx.scene.control.TextField;
import sistemadepracticasprofesionales.utilidades.validacion.ResultadoValidacion;

/**
 *
 * @author eugen
 * Fecha: 18/06/25
 * Descripcion: Estrategia de validacion para validar numeros positivos con un rango definido
 */
public class IntegerValidationStrategy implements IEstrategiaValidacion<TextField>{
    private final boolean obligatorio;
    private final int limiteSuperior;
    private final int limiteInferior;

    public IntegerValidationStrategy(boolean obligatorio, int limiteSuperior, int limiteInferior) {
        this.obligatorio = obligatorio;
        this.limiteSuperior = limiteSuperior;
        this.limiteInferior = limiteInferior;
        
    }

    @Override
    public ResultadoValidacion validar(TextField textField) {
        String texto = textField.getText() == null ? "" : textField.getText().trim();
        
        if(obligatorio && texto.isEmpty()){
            return new ResultadoValidacion(false, "Campo obligatorio");
        }
        Long valorNumerico = null;
        if (!texto.isEmpty()) {
            try {
                valorNumerico = Long.valueOf(texto);
            } catch (NumberFormatException e) {
                return new ResultadoValidacion(false, "Debe ser un número válido");
            }
        }
        
        if(valorNumerico < limiteInferior || valorNumerico > limiteSuperior){
            return new ResultadoValidacion(false, "El numero no se encuentra dentro de los limites permitidos");
        }
        return new ResultadoValidacion(true, "");
    }

    @Override
    public String getMensajeError() {
        return "Error en campo numerico";
    }
}
