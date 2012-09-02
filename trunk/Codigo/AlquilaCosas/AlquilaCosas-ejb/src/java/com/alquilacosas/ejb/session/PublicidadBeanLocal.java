/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.PublicidadDTO;
import com.alquilacosas.ejb.entity.TipoPago.NombreTipoPago;
import com.alquilacosas.ejb.entity.TipoPublicidad;
import com.alquilacosas.ejb.entity.TipoPublicidad.UbicacionPublicidad;
import java.util.Date;
import java.util.List;
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
            byte[] imagen, Date fechaDesde,Date fechaHasta, Double precio, NombreTipoPago nombreTipoPago) 
            throws AlquilaCosasException;
    
    public void actualizarPublicidad(Integer publicidadId, 
            String titulo, String url, String caption, byte[] imagen)
            throws AlquilaCosasException ;
    
    public void eliminarPublicidad(Integer publicidadId) throws AlquilaCosasException;

    public Double getPrecio(TipoPublicidad.DuracionPublicidad duracion, 
            TipoPublicidad.UbicacionPublicidad ubicacion);
    
    public List<PublicidadDTO> getPublicidades(int usuarioId);
    
    public List<Date> getFechasSinDisponibilidad( UbicacionPublicidad ubicacion );

    PublicidadDTO getPublicidad(Integer publicidadId);
    
}
