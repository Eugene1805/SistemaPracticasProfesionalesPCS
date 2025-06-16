/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemadepracticasprofesionales.modelo.pojo;

/**
 *
 * @author Nash
 */
public enum TipoDocumentoInicial {
    OFICIO_DE_ASIGNACION("Oficio De Asignacion"),
    CARTA_DE_ACEPTACION("Carta De Aceptacion"),
    CONSTANCIA_DE_SEGURO("Constancia De Seguro"),
    CRONOGRAMA_DE_ACTIVIDADES("Cronograma de Actividades"),
    HORARIO("Horario");

    private final String nombre;

    TipoDocumentoInicial(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreEnBD() {
        return nombre;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
    
    public static TipoDocumentoInicial fromString(String text) {
        for (TipoDocumentoInicial b : TipoDocumentoInicial.values()) {
            if (b.nombre.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
    
}  

