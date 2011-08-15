/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.ejb.entity.Periodo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wilson
 */
@Local
public interface PeriodoAlquilerBeanLocal {

     public List<Periodo> getPeriodosAlquiler();

     void modificarPeriodo(Periodo periodoNuevo);

     void borrarPeriodo(Periodo periodoBorrar) throws AlquilaCosasException ;

     void registrarPeriodo(Periodo nuevoPeriodo) throws AlquilaCosasException ;
     
}
