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
 * Descripcion: Tests para la clase PhoneValidationStrategy
 */
public class PhoneValidationStrategyTest {
    @BeforeClass
    public static void initJavaFX() {
        System.setProperty("java.awt.headless", "true"); // Para entornos CI
        new JFXPanel(); // Inicializa el toolkit de JavaFX
    }
    @Test
    public void testValidarCampoObligatorioVacio() {
        TextField textField = new TextField();
        PhoneValidationStrategy strategy = new PhoneValidationStrategy(10, true);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertFalse(resultado.isValido());
        assertEquals("Campo obligatorio", resultado.getMensaje());
    }

    @Test
    public void testValidarLongitudExcedida() {
        TextField textField = new TextField("12345678901");
        PhoneValidationStrategy strategy = new PhoneValidationStrategy(10, true);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertFalse(resultado.isValido());
        assertEquals("Máximo 10 caracteres", resultado.getMensaje());
    }

    @Test
    public void testValidarFormatoInvalido() {
        TextField textField = new TextField("123abc4567");
        PhoneValidationStrategy strategy = new PhoneValidationStrategy(10, true);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertFalse(resultado.isValido());
        assertEquals("El número solo debe contener numeros enteros", resultado.getMensaje());
    }

    @Test
    public void testValidarFormatoValido() {
        TextField textField = new TextField("1234567890");
        PhoneValidationStrategy strategy = new PhoneValidationStrategy(10, true);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertTrue(resultado.isValido());
        assertEquals("", resultado.getMensaje());
    }

    @Test
    public void testGetMensajeError() {
        PhoneValidationStrategy strategy = new PhoneValidationStrategy(10, true);
        assertEquals("Error en campo de texto", strategy.getMensajeError());
    }
}
