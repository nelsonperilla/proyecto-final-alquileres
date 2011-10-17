/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import javax.ejb.Local;

/**
 *
 * @author damiancardozo
 */
@Local
public interface PublicidadBeanLocal {

    public void registrarPublicidad(int usuarioId, java.lang.String titulo, 
            java.lang.String url, java.lang.String caption, 
            com.alquilacosas.ejb.entity.TipoPublicidad.UbicacionPublicidad ubicacion, 
            com.alquilacosas.ejb.entity.TipoPublicidad.DuracionPublicidad duracion, 
            byte[] imagen) throws com.alquilacosas.common.AlquilaCosasException;
    
}
