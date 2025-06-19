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
 * Descripcion: Tests para la clase TestValidationStrategy
 */
public class TextValidationStrategyTest {
    @BeforeClass
    public static void initJavaFX() {
        System.setProperty("java.awt.headless", "true"); // Para entornos CI
        new JFXPanel(); // Inicializa el toolkit de JavaFX
    }
    @Test
    public void testValidarCampoObligatorioVacio() {
        TextField textField = new TextField();
        TextLetterValidationStrategy strategy = new TextLetterValidationStrategy(100, true);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertFalse(resultado.isValido());
        assertEquals("Campo obligatorio", resultado.getMensaje());
    }

    @Test
    public void testValidarCampoNoObligatorioVacio() {
        TextField textField = new TextField();
        TextLetterValidationStrategy strategy = new TextLetterValidationStrategy(100, false);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertTrue(resultado.isValido());
        assertEquals("", resultado.getMensaje());
    }

    @Test
    public void testValidarLongitudExcedida() {
        String texto = "a";
        int veces = 101;
        StringBuilder resultadoFinal = new StringBuilder();
        for (int i = 0; i < veces; i++) {
            resultadoFinal.append(texto);
        }
        String textoRepetido = resultadoFinal.toString();
        TextField textField = new TextField(textoRepetido);
        TextLetterValidationStrategy strategy = new TextLetterValidationStrategy(100, true);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertFalse(resultado.isValido());
        assertEquals("Máximo 100 caracteres", resultado.getMensaje());
    }

    @Test
    public void testValidarCaracteresInvalidos() {
        TextField textField = new TextField("Nombre123");
        TextLetterValidationStrategy strategy = new TextLetterValidationStrategy(100, true);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertFalse(resultado.isValido());
        assertEquals("Solo deben ingresarse letras", resultado.getMensaje());
    }

    @Test
    public void testValidarCaracteresValidos() {
        TextField textField = new TextField("José María");
        TextLetterValidationStrategy strategy = new TextLetterValidationStrategy(100, true);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertTrue(resultado.isValido());
        assertEquals("", resultado.getMensaje());
    }

    @Test
    public void testGetMensajeError() {
        TextLetterValidationStrategy strategy = new TextLetterValidationStrategy(100, true);
        assertEquals("Error en campo de texto", strategy.getMensajeError());
    }
}
