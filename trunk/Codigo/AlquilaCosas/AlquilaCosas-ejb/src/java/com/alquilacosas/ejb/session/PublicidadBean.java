/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.ejb.entity.Pago;
import com.alquilacosas.ejb.entity.PrecioTipoServicio;
import com.alquilacosas.ejb.entity.Publicidad;
import com.alquilacosas.ejb.entity.TipoPago;
import com.alquilacosas.ejb.entity.TipoPago.NombreTipoPago;
import com.alquilacosas.ejb.entity.TipoPublicidad;
import com.alquilacosas.ejb.entity.TipoPublicidad.DuracionPublicidad;
import com.alquilacosas.ejb.entity.TipoPublicidad.UbicacionPublicidad;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.facade.PrecioTipoServicioFacade;
import com.alquilacosas.facade.PublicidadFacade;
import com.alquilacosas.facade.TipoPagoFacade;
import com.alquilacosas.facade.TipoPublicidadFacade;
import com.alquilacosas.facade.UsuarioFacade;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class PublicidadBean implements PublicidadBeanLocal {

    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private PublicidadFacade publicidadFacade;
    @EJB
    private TipoPublicidadFacade tipoPubFacade;
    @EJB
    private PrecioTipoServicioFacade precioFacade;
    @EJB
    private TipoPagoFacade tipoPagoFacade;
    
    @Override
    public Integer registrarPublicidad(int usuarioId, String titulo, String url, String caption,
            UbicacionPublicidad ubicacion, DuracionPublicidad duracion, byte[] imagen,
            Double precio, NombreTipoPago nombreTipoPago)
            throws AlquilaCosasException {
        
        TipoPago tipoPago = tipoPagoFacade.findByNombre(nombreTipoPago);
        Pago pago = new Pago();
        pago.setFechaInicio(new Date());
        pago.setMonto(precio);
        pago.setTipoPagoFk(tipoPago);
        
        Usuario usuario = usuarioFacade.find(usuarioId);
        TipoPublicidad tipo = tipoPubFacade.findByUbicacionYDuracion(ubicacion, duracion);
        if(tipo == null) {
            throw new AlquilaCosasException("No se encontro el tipo de publicidad correspondiente en la base de datos.");
        }
        Publicidad publicidad = new Publicidad(titulo, url, caption, imagen);
        publicidad.setTipoPublicidadFk(tipo);
        publicidad.setFechaDesde(new Date());
        publicidad.setFechaHasta(new Date());
        publicidad.setUsuarioFk(usuario);
        publicidad.agregarPago(pago);
        
        publicidadFacade.create(publicidad);
        return pago.getPagoId();
    }
    
    @Override
    public Double getPrecio(DuracionPublicidad duracion, UbicacionPublicidad ubicacion) {
        TipoPublicidad tipo = tipoPubFacade.findByUbicacionYDuracion(ubicacion, duracion);
        if(tipo != null) {
            PrecioTipoServicio precio = precioFacade.getPrecioPublicidad(tipo);
            if(precio != null)
                return precio.getPrecio();
            else
                return null;
        } else {
            return null;
        }
    }
    
}
