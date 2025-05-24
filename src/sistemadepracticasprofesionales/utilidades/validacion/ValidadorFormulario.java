package sistemadepracticasprofesionales.utilidades.validacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextInputControl;
import sistemadepracticasprofesionales.utilidades.validacion.estrategias.IEstrategiaValidacion;

/**
 *
 * @author eugen
 * Fecha: 24/05/25
 * Descripcion:Clase usada para validar cualquier tipo de formulario
 */
public class ValidadorFormulario {
    private final Map<Control, IEstrategiaValidacion<?>> validations = new HashMap<>();
    private final List<Runnable> cleanupActions = new ArrayList<>();
    
    public <T extends Control> void addValidation(T control, IEstrategiaValidacion<T> strategy) {
        validations.put(control, strategy);
    }
    
    public void addCleanupAction(Runnable action) {
        cleanupActions.add(action);
    }
    
    public boolean validate() {
        boolean allValid = true;
        
        for (Map.Entry<Control, IEstrategiaValidacion<?>> entry : validations.entrySet()) {
            Control control = entry.getKey();
            IEstrategiaValidacion<?> strategy = entry.getValue();
            
            ResultadoValidacion result = validateControl(control, strategy);
            
            if (!result.isValido()) {
                allValid = false;
                highlightError(control);
                if (shouldClean(control, result)) {
                    cleanControl(control);
                }
            } else {
                clearError(control);
            }
        }
        
        if (!allValid) {
            cleanupActions.forEach(Runnable::run);
        }
        
        return allValid;
    }
    
    private <T extends Control> ResultadoValidacion validateControl(T control, IEstrategiaValidacion<?> strategy) {
        @SuppressWarnings("unchecked")
        IEstrategiaValidacion<T> typedStrategy = (IEstrategiaValidacion<T>) strategy;
        return typedStrategy.validar(control);
    }
    
    private boolean shouldClean(Control control, ResultadoValidacion result) {
        return control instanceof TextInputControl && 
               result.getMensaje().contains("numero") || 
               result.getMensaje().contains("formato");
    }
    
    private void cleanControl(Control control) {
        if (control instanceof TextInputControl) {
            ((TextInputControl) control).clear();
        } else if (control instanceof ComboBox) {
            ((ComboBox<?>) control).getSelectionModel().clearSelection();
        }
    }
    
    private void highlightError(Control control) {
        control.setStyle("-fx-border-color: red; -fx-background-color: #FFEEEE;");
    }
    
    private void clearError(Control control) {
        control.setStyle("");
    }
}
