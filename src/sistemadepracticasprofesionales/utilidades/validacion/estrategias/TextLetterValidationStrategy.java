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
 * Descripción: Estrategia de validacion utilizada para validar campos de texto util, para campos que no deseo
 * sean vacios, con una longitud maxima definida en la base de datos y que solo puedan ingresarse caracteres alfabéticos
 */
public class TextLetterValidationStrategy implements IEstrategiaValidacion<TextInputControl>{
    private final int longitudMaxima;                                                   
    private final boolean obligatorio;                                                  

    public TextLetterValidationStrategy(int longitudMaxima, boolean obligatorio) {
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
        
        if(!texto.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")){
            return new ResultadoValidacion(false, "Solo deben ingresarse letras");
        }
        
        return new ResultadoValidacion(true, "");
    }

    @Override
    public String getMensajeError() {
        return "Error en campo de texto";
    } 
}
