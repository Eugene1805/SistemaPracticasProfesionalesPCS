package sistemadepracticasprofesionales.utilidades.validacion.estrategias;

import javafx.scene.control.TextField;
import sistemadepracticasprofesionales.utilidades.validacion.ResultadoValidacion;

/**
 * Estrategia de validación para validar números enteros dentro de un rango definido
 * @author eugen
 * Fecha: 18/06/25
 */
public class IntegerValidationStrategy implements IEstrategiaValidacion<TextField> {
    private final boolean obligatorio;
    private final int limiteSuperior;
    private final int limiteInferior;

    public IntegerValidationStrategy(boolean obligatorio, int limiteSuperior, int limiteInferior) {
        if (limiteSuperior < limiteInferior) {
            throw new IllegalArgumentException("El límite superior debe ser mayor al inferior");
        }
        this.obligatorio = obligatorio;
        this.limiteSuperior = limiteSuperior;
        this.limiteInferior = limiteInferior;
    }

    @Override
    public ResultadoValidacion validar(TextField textField) {
        String texto = textField.getText() == null ? "" : textField.getText().trim();
        
        // Validación de campo obligatorio vacío
        if (obligatorio && texto.isEmpty()) {
            return new ResultadoValidacion(false, "Campo obligatorio");
        }
        
        // Si no es obligatorio y está vacío, es válido
        if (texto.isEmpty()) {
            return new ResultadoValidacion(true, "");
        }
        
        // Validación de formato numérico
        long valorNumerico;
        try {
            valorNumerico = Long.parseLong(texto);
        } catch (NumberFormatException e) {
            return new ResultadoValidacion(false, "Debe ser un número entero válido");
        }
        
        // Validación adicional: que no exceda los límites de un int
        if (valorNumerico > Integer.MAX_VALUE || valorNumerico < Integer.MIN_VALUE) {
            return new ResultadoValidacion(false, 
                "El número excede los límites de un entero (debe estar entre " + 
                Integer.MIN_VALUE + " y " + Integer.MAX_VALUE + ")");
        }
        
        // Validación de rango permitido
        if (valorNumerico < limiteInferior || valorNumerico > limiteSuperior) {
            return new ResultadoValidacion(
                false, 
                String.format("El número debe estar entre %d y %d", limiteInferior, limiteSuperior)
            );
        }
        
        return new ResultadoValidacion(true, "");
    }

    @Override
    public String getMensajeError() {
        return String.format("Error en campo numérico");
    }
}