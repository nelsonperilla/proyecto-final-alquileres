/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.PublicacionFacade;
import com.alquilacosas.ejb.entity.EstadoPublicacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ignaciogiagante
 */
@Local
public interface MisPublicacionesBeanLocal {
    
    List<PublicacionFacade> getPublicaciones( int usuarioId );
    
    List<EstadoPublicacion> getEstados();
}
