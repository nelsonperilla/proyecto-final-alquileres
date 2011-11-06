/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.entity.TipoDestacacion.NombreTipoDestacacion;
import com.alquilacosas.ejb.entity.TipoPago.NombreTipoPago;
import javax.ejb.Local;

/**
 *
 * @author damiancardozo
 */
@Local
public interface DestacarPublicacionBeanLocal {

    public Integer iniciarCobroDestacacion(Integer usuarioId, Integer publicacionId, 
            NombreTipoDestacacion nombreTipo, Double precio, NombreTipoPago nombreTipoPago);
    
    public void efectuarServicio(Integer pagoId);
    
    public PublicacionDTO getPublicacion(Integer publicacionId, Integer usuarioId) 
            throws AlquilaCosasException;
    
    public Double getPrecioDestacacion(NombreTipoDestacacion tipo);
    
}
