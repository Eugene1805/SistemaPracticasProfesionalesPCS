package sistemadepracticasprofesionales.utilidades.validacion.estrategias;

import javafx.scene.control.TextInputControl;
import sistemadepracticasprofesionales.utilidades.validacion.ResultadoValidacion;

/**
 *
 * @author eugen
 * Fecha: 24/05/25
 * Descripcion: Estrategia de validacion utilizada para validar campos de texto util, para campos que no deseo
 * sean vacios y con una longitud maxima definida en la base de datos
 */
public class TextValidationStrategy implements IEstrategiaValidacion<TextInputControl>{ //Se usa TextInputControl
    private final int longitudMaxima;                                                   // porque es padre de los
    private final boolean obligatorio;                                                  //controles que reciben texto

    public TextValidationStrategy(int longitudMaxima, boolean obligatorio) {
        this.longitudMaxima = longitudMaxima;
        this.obligatorio = obligatorio;
    }

    @Override
    public ResultadoValidacion validar(TextInputControl textField) {
        String texto = textField.getText() == null ? "" : textField.getText().trim();
        
        if(obligatorio && texto.isEmpty()){
            return new ResultadoValidacion(false, "Campo oblogatorio");
        }
        
        if(texto.length() > longitudMaxima){
            return new ResultadoValidacion(false, "Máximo " + longitudMaxima + " caracteres");
        }
        
        return new ResultadoValidacion(true, "");
    }

    @Override
    public String getMensajeError() {
        return "Error en campo de texto";
    } 
}
