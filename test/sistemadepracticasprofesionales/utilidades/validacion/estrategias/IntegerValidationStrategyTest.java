package sistemadepracticasprofesionales.utilidades.validacion.estrategias;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import sistemadepracticasprofesionales.utilidades.validacion.ResultadoValidacion;

/**
 *
 * @author eugen
 * Fecha: 19/06/25
 * Descripcion: Tests para la clase IntegerValidationStrategy
 */
public class IntegerValidationStrategyTest {
    
    @BeforeClass
    public static void initJavaFX() {
        System.setProperty("java.awt.headless", "true"); // Para entornos CI
        new JFXPanel(); // Inicializa el toolkit de JavaFX
    }
    @Test
    public void testValidarCampoObligatorioVacio() {
        TextField textField = new TextField();
        IntegerValidationStrategy strategy = new IntegerValidationStrategy(true, 100, 0);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertFalse(resultado.isValido());
        assertEquals("Campo obligatorio", resultado.getMensaje());
    }

    @Test
    public void testValidarCampoNoObligatorioVacio() {
        TextField textField = new TextField();
        IntegerValidationStrategy strategy = new IntegerValidationStrategy(false, 100, 0);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertTrue(resultado.isValido());
        assertEquals("", resultado.getMensaje());
    }

    @Test
    public void testValidarFormatoInvalido() {
        TextField textField = new TextField("abc");
        IntegerValidationStrategy strategy = new IntegerValidationStrategy(true, 100, 0);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertFalse(resultado.isValido());
        assertEquals("Debe ser un número entero válido", resultado.getMensaje());
    }

    @Test
    public void testValidarNumeroFueraDeRango() {
        TextField textField = new TextField("150");
        IntegerValidationStrategy strategy = new IntegerValidationStrategy(true, 100, 0);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertFalse(resultado.isValido());
        assertEquals("El número debe estar entre 0 y 100", resultado.getMensaje());
    }

    @Test
    public void testValidarNumeroDentroDeRango() {
        TextField textField = new TextField("50");
        IntegerValidationStrategy strategy = new IntegerValidationStrategy(true, 100, 0);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertTrue(resultado.isValido());
        assertEquals("", resultado.getMensaje());
    }

    @Test
    public void testConstructorConLimitesInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> {
            new IntegerValidationStrategy(true, 0, 100);
        });
    }

    @Test
    public void testGetMensajeError() {
        IntegerValidationStrategy strategy = new IntegerValidationStrategy(true, 100, 0);
        assertEquals("Error en campo numérico", strategy.getMensajeError());
    }
}
