/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.PrecioDTO;
import com.alquilacosas.ejb.entity.Publicacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author damiancardozo
 */
@Local
public interface PrecioBeanLocal {
    
    List<PrecioDTO> getPrecios(Publicacion publicacion);
    
}
