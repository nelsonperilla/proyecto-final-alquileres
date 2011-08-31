/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.PeriodoDTO;
import com.alquilacosas.ejb.entity.Periodo;
import com.alquilacosas.ejb.entity.Periodo.NombrePeriodo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wilson
 */
@Local
public interface PeriodoAlquilerBeanLocal {

     List<PeriodoDTO> getPeriodos();

     void modificarPeriodo(Periodo periodoNuevo);

     void borrarPeriodo(Periodo periodoBorrar) throws AlquilaCosasException ;

     void registrarPeriodo(String nombre, String descripcion, int horas) throws AlquilaCosasException ;
     
     Periodo getPeriodo(NombrePeriodo nombrePeriodo);
}
