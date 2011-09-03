/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.ejb.entity.Pais;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wilson
 */
@Local
public interface PaisBeanLocal {

     List<Pais> getPaises();
     
     void modificarPais(Pais paisNuevo);

     void borrarPais(Pais paisBorrar) throws AlquilaCosasException ;

     void registrarPais(Pais nuevoPais) throws AlquilaCosasException ;

     Pais getPaisById(int paisId);
     
}
