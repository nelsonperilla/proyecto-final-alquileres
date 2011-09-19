/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.entity.EstadoPublicacion;
import com.alquilacosas.ejb.entity.Pago;
import com.alquilacosas.ejb.entity.PrecioServicio;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.PublicacionXEstado;
import com.alquilacosas.ejb.entity.ServicioDestacacion;
import com.alquilacosas.ejb.entity.TipoPago;
import com.alquilacosas.ejb.entity.TipoServicio;
import com.alquilacosas.ejb.entity.TipoServicio.NombreTipoServicio;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.facade.PrecioServicioFacade;
import com.alquilacosas.facade.PublicacionFacade;
import com.alquilacosas.facade.PublicacionXEstadoFacade;
import com.alquilacosas.facade.ServicioDestacacionFacade;
import com.alquilacosas.facade.TipoPagoFacade;
import com.alquilacosas.facade.TipoServicioFacade;
import java.util.Date;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class DestacarPublicacionBean implements DestacarPublicacionBeanLocal {

    @EJB
    private PublicacionFacade publicacionFacade;
    @EJB
    private PublicacionXEstadoFacade estadoFacade;
    @EJB
    private PrecioServicioFacade precioFacade;
    @EJB
    private TipoServicioFacade tipoServicioFacade;
    @EJB
    private ServicioDestacacionFacade servicioFacade;
    @EJB
    private TipoPagoFacade tipoPagoFacade;
    
    @Override
    @RolesAllowed({"USUARIO", "ADMIN"})
    public void destacarPublicacion(Integer publicacionId) {

        Double monto = getPrecioDestacacion();
        Publicacion publicacion = publicacionFacade.find(publicacionId);
        Usuario usuario = publicacion.getUsuarioFk();
        
        TipoServicio tipoServicio = tipoServicioFacade.findByNombre(NombreTipoServicio.DESTACACION);
        
        ServicioDestacacion servicio = new ServicioDestacacion();
        servicio.setFechaDesde(publicacion.getFechaDesde());
        servicio.setFechaHasta(publicacion.getFechaHasta());
        servicio.setPublicacionFk(publicacion);
        servicio.setTipoServicioFk(tipoServicio);
        servicio.setUsuarioFk(usuario);
        
        TipoPago tipoPago = tipoPagoFacade.findByNombre(TipoPago.NombreTipoPago.PAYPAL);
        
        Pago pago = new Pago();
        pago.setMonto(monto);
        pago.setFechaPago(new Date());
        pago.setTipoPagoFk(tipoPago);
        
        servicio.agregarPago(pago);
        
        publicacion.setDestacada(true);
        
        publicacionFacade.edit(publicacion);
        servicioFacade.edit(servicio);
    }
    
    @Override
    @RolesAllowed({"USUARIO", "ADMIN"})
    public PublicacionDTO getPublicacion(Integer publicacionId, Integer usuarioId) throws AlquilaCosasException {
        Publicacion publicacion = publicacionFacade.find(publicacionId);
        if(publicacion == null) {
            throw new AlquilaCosasException("La publicacion no existe!");
        }
        Usuario u = publicacion.getUsuarioFk();
        if(u.getUsuarioId() != usuarioId) {
            throw new AlquilaCosasException("La publicacion indicada es invalida!");
        }
        PublicacionXEstado pxe = estadoFacade.getPublicacionXEstado(publicacion);
        if(pxe == null) {
            throw new AlquilaCosasException("Imposible determinar el estado de la publicacion!");
        }
        EstadoPublicacion estado = pxe.getEstadoPublicacion();
        if(estado.getNombre() != EstadoPublicacion.NombreEstadoPublicacion.ACTIVA)
            throw new AlquilaCosasException("La publicacion no esta activa");
        if(publicacion.getDestacada()) {
            throw new AlquilaCosasException("La publicacion ya esta destacada.");
        }
        PublicacionDTO dto = new PublicacionDTO(publicacion.getPublicacionId(), publicacion.getTitulo(), "",
                publicacion.getFechaDesde(), publicacion.getFechaHasta(), publicacion.getDestacada(),
                publicacion.getCantidad());
        return dto;
    }
    
    @Override
    public Double getPrecioDestacacion() {
        PrecioServicio precio = precioFacade.getPrecioServicio(NombreTipoServicio.DESTACACION);
        if(precio != null)
            return precio.getPrecio();
        else
            return null;
    }

}

