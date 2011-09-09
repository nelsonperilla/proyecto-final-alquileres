/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.entity.EstadoPublicacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ignaciogiagante
 */
@Local
public interface MisPublicacionesBeanLocal {
    
    public List<PublicacionDTO> getPublicaciones( int usuarioId );
    
    public List<EstadoPublicacion> getEstados();

    public void borrarPublicacion(java.lang.Integer publicacionId) throws com.alquilacosas.common.AlquilaCosasException;
}
