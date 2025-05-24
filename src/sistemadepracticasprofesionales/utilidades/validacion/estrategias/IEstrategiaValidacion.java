package sistemadepracticasprofesionales.utilidades.validacion.estrategias;

import javafx.scene.control.Control;
import sistemadepracticasprofesionales.utilidades.validacion.ResultadoValidacion;

/**
 *
 * @author eugen
 * Fecha: 24/05/25
 * Descripcion: Interfaz para aplicar el patron Chain of Responsibility a diferentes controls de Java FX
 * como TextFields o Combobox
 */
public interface IEstrategiaValidacion<T extends Control> {
    ResultadoValidacion validar(T control);
    String getMensajeError();
}
