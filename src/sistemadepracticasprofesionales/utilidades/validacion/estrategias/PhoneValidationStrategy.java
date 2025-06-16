/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemadepracticasprofesionales.utilidades.validacion.estrategias;

import javafx.scene.control.TextInputControl;
import sistemadepracticasprofesionales.utilidades.validacion.ResultadoValidacion;

/**
 *
 * @author Nash
 * Fecha: 30/05/2025
 * Descripción: Estrategia de validacion utilizada para validar campos que soliciten un teléfono celular
 */
public class PhoneValidationStrategy implements IEstrategiaValidacion<TextInputControl>{
 private final int longitudMaxima;                                                   
    private final boolean obligatorio;                                                  

    public PhoneValidationStrategy(int longitudMaxima, boolean obligatorio) {
        this.longitudMaxima = longitudMaxima;
        this.obligatorio = obligatorio;
    }

    @Override
    public ResultadoValidacion validar(TextInputControl textField) {
        String texto = textField.getText() == null ? "" : textField.getText().trim();
        
        if(obligatorio && texto.isEmpty()){
            return new ResultadoValidacion(false, "Campo obligatorio");
        }
        
        if(texto.length() > longitudMaxima){
            return new ResultadoValidacion(false, "Máximo " + longitudMaxima + " caracteres");
        }
        
        if (!texto.matches("^\\d{10}$")) {
            return new ResultadoValidacion(false,  "El número solo debe contener numeros enteros");   
        }
               
        return new ResultadoValidacion(true, "");
    }

    @Override
    public String getMensajeError() {
        return "Error en campo de texto";
    } 
}
