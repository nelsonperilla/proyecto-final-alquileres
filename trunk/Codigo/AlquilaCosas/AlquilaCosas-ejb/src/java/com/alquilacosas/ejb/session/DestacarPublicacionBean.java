/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.PrecioDTO;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.entity.Domicilio;
import com.alquilacosas.ejb.entity.EstadoPublicacion;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Pago;
import com.alquilacosas.ejb.entity.Precio;
import com.alquilacosas.ejb.entity.PrecioTipoServicio;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.PublicacionXEstado;
import com.alquilacosas.ejb.entity.Destacacion;
import com.alquilacosas.ejb.entity.TipoPago;
import com.alquilacosas.ejb.entity.TipoDestacacion;
import com.alquilacosas.ejb.entity.TipoDestacacion.NombreTipoDestacacion;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.facade.PrecioFacade;
import com.alquilacosas.facade.PrecioServicioFacade;
import com.alquilacosas.facade.PublicacionFacade;
import com.alquilacosas.facade.PublicacionXEstadoFacade;
import com.alquilacosas.facade.DestacacionFacade;
import com.alquilacosas.facade.TipoPagoFacade;
import com.alquilacosas.facade.TipoDestacacionFacade;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    private TipoDestacacionFacade tipoServicioFacade;
    @EJB
    private DestacacionFacade servicioFacade;
    @EJB
    private TipoPagoFacade tipoPagoFacade;
    @EJB
    private PrecioFacade precioPublicacionFacade;
    
    @Override
    @RolesAllowed({"USUARIO", "ADMIN"})
    public void destacarPublicacion(Integer publicacionId) {

        Double monto = getPrecioDestacacion();
        Publicacion publicacion = publicacionFacade.find(publicacionId);
        Usuario usuario = publicacion.getUsuarioFk();
        
        TipoDestacacion tipoServicio = tipoServicioFacade.findByNombre(NombreTipoDestacacion.MENSUAL);
        
        Destacacion servicio = new Destacacion();
        servicio.setFechaDesde(publicacion.getFechaDesde());
        servicio.setFechaHasta(publicacion.getFechaHasta());
        servicio.setPublicacionFk(publicacion);
        servicio.setTipoDestacacionFk(null);
        
        TipoPago tipoPago = tipoPagoFacade.findByNombre(TipoPago.NombreTipoPago.PAYPAL);
        
        Pago pago = new Pago();
        pago.setMonto(monto);
        pago.setFechaPago(new Date());
        pago.setTipoPagoFk(tipoPago);
        
        servicio.agregarPago(pago);
        
        publicacion.setDestacada(true);
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, 2);
        publicacion.setFechaHasta(cal.getTime());
        
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
        List<Integer> imagenes = new ArrayList<Integer>();
        for (ImagenPublicacion ip : publicacion.getImagenPublicacionList()) {
            imagenes.add(ip.getImagenPublicacionId());
        }
        dto.setImagenIds(imagenes);

        Domicilio d = publicacion.getUsuarioFk().getDomicilioList().get(0);
        dto.setProvincia(d.getProvinciaFk().getNombre());
        dto.setCiudad(d.getCiudad());
        
        List<PrecioDTO> precios = new ArrayList<PrecioDTO>();
        for (Precio precio : precioPublicacionFacade.getUltimoPrecios(publicacion)) {
            precios.add(new PrecioDTO(precio.getPrecio(), precio.getPeriodoFk().getNombre()));
        }
        dto.setPrecios(precios);
        
        return dto;
    }
    
    @Override
    public Double getPrecioDestacacion() {
        PrecioTipoServicio precio = precioFacade.getPrecioServicio(NombreTipoDestacacion.MENSUAL);
        if(precio != null)
            return precio.getPrecio();
        else
            return null;
    }

}

