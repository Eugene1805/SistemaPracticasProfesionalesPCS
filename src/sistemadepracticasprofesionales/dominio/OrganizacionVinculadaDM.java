/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemadepracticasprofesionales.dominio;

import java.sql.SQLException;
import sistemadepracticasprofesionales.modelo.dao.OrganizacionVinculadaDAO;
import sistemadepracticasprofesionales.modelo.pojo.ResultadoOperacion;

/**
 *
 * @author Nash
 * Fecha: 10/06/2025
 * Descricpión: Clase para gestionar las reglas de negocio relacionadas a la gestión de las Organizaciones Vinculadas
 */
public class OrganizacionVinculadaDM {
    public static ResultadoOperacion verificarExistenciaResponsableAsignado(int idOrganizacion){
        ResultadoOperacion resultado = new ResultadoOperacion();
        try {
            boolean existe = OrganizacionVinculadaDAO.tieneResponsableAsignado(idOrganizacion);
            resultado.setError(existe);
            if (existe) {
                resultado.setMensaje("La organizacion vinculada seleccionada ya tiene un Responsable del Proyecto asigno, intente con otra");
            }
        } catch (SQLException e) {
            resultado.setError(true);
            resultado.setMensaje("Por el momento no se puede comprobar si la Organizacion Vinculada ya tiene un responsable asignado, inténtelo más tarde");
        }
        return resultado;
    }   
}
