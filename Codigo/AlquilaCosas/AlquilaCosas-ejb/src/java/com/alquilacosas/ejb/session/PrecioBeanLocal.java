/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.PrecioFacade;
import com.alquilacosas.ejb.entity.Publicacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author damiancardozo
 */
@Local
public interface PrecioBeanLocal {
    
    List<PrecioFacade> getPrecios(Publicacion publicacion);
    
}
