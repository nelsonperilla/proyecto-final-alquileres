/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.ejb.entity.EstadoPublicacion;
import com.alquilacosas.ejb.entity.Publicacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ignaciogiagante
 */
@Local
public interface MisPublicacionesBeanLocal {
    public List<Publicacion> getPublicaciones( int usuarioId );
    
        public List<EstadoPublicacion> getEstados();
}
