package sistemadepracticasprofesionales.utilidades.validacion.estrategias;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ComboBox;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import sistemadepracticasprofesionales.utilidades.validacion.ResultadoValidacion;

/**
 *
 * @author eugen
 * Fecha:19/06/25
 * Descripcion: Tests para la clase ComboValidacionStrategy
 */
public class ComboValidationStrategyTest {
    
    @BeforeClass
    public static void initJavaFX() {
        System.setProperty("java.awt.headless", "true"); // Para entornos CI
        new JFXPanel(); // Inicializa el toolkit de JavaFX
    }
    @Test
    public void testValidarComboObligatorioVacio() {
        ComboBox<String> comboBox = new ComboBox<>();
        ComboValidationStrategy strategy = new ComboValidationStrategy(true);
        
        ResultadoValidacion resultado = strategy.validar(comboBox);
        
        assertFalse(resultado.isValido());
        assertEquals("Selección obligatoria", resultado.getMensaje());
    }

    @Test
    public void testValidarComboObligatorioConValor() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setValue("Valor seleccionado");
        ComboValidationStrategy strategy = new ComboValidationStrategy(true);
        
        ResultadoValidacion resultado = strategy.validar(comboBox);
        
        assertTrue(resultado.isValido());
        assertEquals("", resultado.getMensaje());
    }

    @Test
    public void testValidarComboNoObligatorioVacio() {
        ComboBox<String> comboBox = new ComboBox<>();
        ComboValidationStrategy strategy = new ComboValidationStrategy(false);
        
        ResultadoValidacion resultado = strategy.validar(comboBox);
        
        assertTrue(resultado.isValido());
        assertEquals("", resultado.getMensaje());
    }

    @Test
    public void testGetMensajeError() {
        ComboValidationStrategy strategy = new ComboValidationStrategy(true);
        assertEquals("Error en seleccion", strategy.getMensajeError());
    }
}
