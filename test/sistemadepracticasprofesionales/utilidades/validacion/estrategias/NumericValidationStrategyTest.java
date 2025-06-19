package sistemadepracticasprofesionales.utilidades.validacion.estrategias;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import sistemadepracticasprofesionales.utilidades.validacion.ResultadoValidacion;

/**
 *
 * @author eugen
 * Fecha: 19/06/25
 * Descripcion: Test para la clase NumericValidationStrategy
 */
public class NumericValidationStrategyTest {
    @BeforeClass
    public static void initJavaFX() {
        System.setProperty("java.awt.headless", "true"); // Para entornos CI
        new JFXPanel(); // Inicializa el toolkit de JavaFX
    }

    @Test
    public void testValidarCampoObligatorioVacio() {
        TextField textField = new TextField();
        NumericValidationStrategy strategy = new NumericValidationStrategy(true);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertFalse(resultado.isValido());
        assertEquals("Campo obligatorio", resultado.getMensaje());
    }

    @Test
    public void testValidarCampoNoObligatorioVacio() {
        TextField textField = new TextField();
        NumericValidationStrategy strategy = new NumericValidationStrategy(false);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertTrue(resultado.isValido());
        assertEquals("", resultado.getMensaje());
    }

    @Test
    public void testValidarFormatoInvalido() {
        TextField textField = new TextField("abc");
        NumericValidationStrategy strategy = new NumericValidationStrategy(true);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertFalse(resultado.isValido());
        assertEquals("Debe ser un número válido", resultado.getMensaje());
    }

    @Test
    public void testValidarFormatoValido() {
        TextField textField = new TextField("123");
        NumericValidationStrategy strategy = new NumericValidationStrategy(true);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertTrue(resultado.isValido());
        assertEquals("", resultado.getMensaje());
    }

    @Test
    public void testGetMensajeError() {
        NumericValidationStrategy strategy = new NumericValidationStrategy(true);
        assertEquals("Error en campo numerico", strategy.getMensajeError());
    }
}
