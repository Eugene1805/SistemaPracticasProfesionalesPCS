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
 * Descripcion: Test para la clase MailValidationStrategy
 */
public class MailValidationStrategyTest {
    @BeforeClass
    public static void initJavaFX() {
        System.setProperty("java.awt.headless", "true"); // Para entornos CI
        new JFXPanel(); // Inicializa el toolkit de JavaFX
    }
    @Test
    public void testValidarCampoObligatorioVacio() {
        TextField textField = new TextField();
        MailValidationStrategy strategy = new MailValidationStrategy(100, true);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertFalse(resultado.isValido());
        assertEquals("Campo obligatorio", resultado.getMensaje());
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
        MailValidationStrategy strategy = new MailValidationStrategy(100, true);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertFalse(resultado.isValido());
        assertEquals("Máximo 100 caracteres", resultado.getMensaje());
    }

    @Test
    public void testValidarFormatoInvalido() {
        TextField textField = new TextField("correo.invalido");
        MailValidationStrategy strategy = new MailValidationStrategy(100, true);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertFalse(resultado.isValido());
        assertEquals("Correo no válido", resultado.getMensaje());
    }

    @Test
    public void testValidarFormatoValido() {
        TextField textField = new TextField("correo@valido.com");
        MailValidationStrategy strategy = new MailValidationStrategy(100, true);
        
        ResultadoValidacion resultado = strategy.validar(textField);
        
        assertTrue(resultado.isValido());
        assertEquals("", resultado.getMensaje());
    }

    @Test
    public void testGetMensajeError() {
        MailValidationStrategy strategy = new MailValidationStrategy(100, true);
        assertEquals("Error en campo de texto", strategy.getMensajeError());
    }
}
