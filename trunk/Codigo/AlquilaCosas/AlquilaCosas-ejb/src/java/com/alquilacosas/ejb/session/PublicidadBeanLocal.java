/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.ejb.entity.TipoPago.NombreTipoPago;
import com.alquilacosas.ejb.entity.TipoPublicidad;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author damiancardozo
 */
@Local
public interface PublicidadBeanLocal {

    public Integer registrarPublicidad(int usuarioId, String titulo, 
            String url, String caption, 
            TipoPublicidad.UbicacionPublicidad ubicacion, 
            TipoPublicidad.DuracionPublicidad duracion, 
            byte[] imagen, Date fechaDesde,  Double precio, NombreTipoPago nombreTipoPago) 
            throws com.alquilacosas.common.AlquilaCosasException;

    public Double getPrecio(TipoPublicidad.DuracionPublicidad duracion, 
            TipoPublicidad.UbicacionPublicidad ubicacion);
    
}
