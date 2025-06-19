package sistemadepracticasprofesionales.utilidades.validacion.estrategias;

import java.time.LocalDate;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.DatePicker;
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
 * Descripcion: Tests para la clase DateValidationStrategy
 */
public class DateValidationStrategyTest {
    @BeforeClass
    public static void initJavaFX() {
        System.setProperty("java.awt.headless", "true"); // Para entornos CI
        new JFXPanel(); // Inicializa el toolkit de JavaFX
    }
    @Test
    public void testValidarDateObligatorioVacio() {
        DatePicker datePicker = new DatePicker();
        DateValidationStrategy strategy = new DateValidationStrategy(true);
        
        ResultadoValidacion resultado = strategy.validar(datePicker);
        
        assertFalse(resultado.isValido());
        assertEquals("Selección obligatoria", resultado.getMensaje());
    }

    @Test
    public void testValidarDateObligatorioConValor() {
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        DateValidationStrategy strategy = new DateValidationStrategy(true);
        
        ResultadoValidacion resultado = strategy.validar(datePicker);
        
        assertTrue(resultado.isValido());
        assertEquals("", resultado.getMensaje());
    }

    @Test
    public void testValidarDateNoObligatorioVacio() {
        DatePicker datePicker = new DatePicker();
        DateValidationStrategy strategy = new DateValidationStrategy(false);
        
        ResultadoValidacion resultado = strategy.validar(datePicker);
        
        assertTrue(resultado.isValido());
        assertEquals("", resultado.getMensaje());
    }

    @Test
    public void testGetMensajeError() {
        DateValidationStrategy strategy = new DateValidationStrategy(true);
        assertEquals("Error en seleccion", strategy.getMensajeError());
    }
}
