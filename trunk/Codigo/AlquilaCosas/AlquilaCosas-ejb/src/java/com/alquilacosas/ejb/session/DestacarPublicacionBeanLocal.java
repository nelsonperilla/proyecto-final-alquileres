/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.PublicacionDTO;
import javax.ejb.Local;

/**
 *
 * @author damiancardozo
 */
@Local
public interface DestacarPublicacionBeanLocal {

    public void destacarPublicacion(java.lang.Integer publicacionId);
    
    public PublicacionDTO getPublicacion(Integer publicacionId, Integer usuarioId) 
            throws AlquilaCosasException;
    
    public Double getPrecioDestacacion();
    
}
