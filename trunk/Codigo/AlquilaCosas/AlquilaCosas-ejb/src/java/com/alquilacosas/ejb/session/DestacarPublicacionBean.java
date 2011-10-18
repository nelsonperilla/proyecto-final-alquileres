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
import com.alquilacosas.ejb.entity.Publicidad;
import com.alquilacosas.ejb.entity.Servicio;
import com.alquilacosas.ejb.entity.TipoPago;
import com.alquilacosas.ejb.entity.TipoDestacacion;
import com.alquilacosas.ejb.entity.TipoDestacacion.NombreTipoDestacacion;
import com.alquilacosas.ejb.entity.TipoPago.NombreTipoPago;
import com.alquilacosas.ejb.entity.TipoPublicidad.DuracionPublicidad;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.facade.PrecioFacade;
import com.alquilacosas.facade.PrecioTipoServicioFacade;
import com.alquilacosas.facade.PublicacionFacade;
import com.alquilacosas.facade.PublicacionXEstadoFacade;
import com.alquilacosas.facade.DestacacionFacade;
import com.alquilacosas.facade.PagoFacade;
import com.alquilacosas.facade.PublicidadFacade;
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
    private PrecioTipoServicioFacade precioFacade;
    @EJB
    private TipoDestacacionFacade tipoDestacacionFacade;
    @EJB
    private DestacacionFacade destacacionFacade;
    @EJB
    private TipoPagoFacade tipoPagoFacade;
    @EJB
    private PrecioFacade precioPublicacionFacade;
    @EJB
    private PagoFacade pagoFacade;
    @EJB
    private PublicidadFacade publicidadFacade;
    
    @Override
    public Integer iniciarCobroDestacacion(Integer publicacionId, NombreTipoDestacacion nombreTipo, 
            Double precio, NombreTipoPago nombreTipoPago) {
        
        TipoPago tipoPago = tipoPagoFacade.findByNombre(nombreTipoPago);
        Pago pago = new Pago();
        pago.setFechaInicio(new Date());
        pago.setMonto(precio);
        pago.setTipoPagoFk(tipoPago);
        
        Publicacion publicacion = publicacionFacade.find(publicacionId);
        TipoDestacacion tipo = tipoDestacacionFacade.findByNombre(nombreTipo);
        Destacacion destacacion = new Destacacion();
        destacacion.setPublicacionFk(publicacion);
        destacacion.setTipoDestacacionFk(tipo);
        destacacion.agregarPago(pago);
        
        destacacionFacade.create(destacacion);
        return pago.getPagoId();
    }
    
    @Override
    @RolesAllowed({"USUARIO", "ADMIN"})
    public void efectuarServicio(Integer pagoId) {

        Pago pago = pagoFacade.find(pagoId);
        pago.setFechaConfirmado(new Date());
        pagoFacade.edit(pago);
        
        Servicio servicio = (Destacacion) pago.getServicioFk();
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        if(servicio instanceof Destacacion) {
            Destacacion destacacion = (Destacacion) servicio;
            destacacion.setFechaDesde(new Date());
            NombreTipoDestacacion tipo = destacacion.getTipoDestacacionFk().getNombre();
            if(tipo == NombreTipoDestacacion.SEMANAL) {
                cal.add(Calendar.DAY_OF_YEAR, 7);
                destacacion.setFechaHasta(cal.getTime());
            }
            else if(tipo == NombreTipoDestacacion.BISEMANAL) {
                cal.add(Calendar.DAY_OF_YEAR, 14);
                destacacion.setFechaHasta(cal.getTime());
            } else if(tipo == NombreTipoDestacacion.MENSUAL) {
                cal.add(Calendar.MONTH, 1);
                destacacion.setFechaHasta(cal.getTime());
            }
            Publicacion publicacion = destacacion.getPublicacionFk();
            publicacion.setDestacada(true);
            if(publicacion.getFechaHasta().before(destacacion.getFechaHasta())) {
                publicacion.setFechaHasta(destacacion.getFechaHasta());
            }
            publicacionFacade.edit(publicacion);
            destacacionFacade.edit(destacacion);
        } else {
            Publicidad publicidad = (Publicidad) servicio;
            publicidad.setFechaDesde(new Date());
            DuracionPublicidad duracion = publicidad.getTipoPublicidadFk().getDuracion();
            if(duracion == DuracionPublicidad.SEMANAL) {
                cal.add(Calendar.DAY_OF_YEAR, 7);
                publicidad.setFechaHasta(cal.getTime());
            } else if(duracion == DuracionPublicidad.BISEMANAL) {
                cal.add(Calendar.DAY_OF_YEAR, 14);
                publicidad.setFechaHasta(cal.getTime());
            } else if(duracion == DuracionPublicidad.MENSUAL) {
                cal.add(Calendar.MONTH, 1);
                publicidad.setFechaHasta(cal.getTime());
            } else if(duracion == DuracionPublicidad.BIMENSUAL) {
                cal.add(Calendar.MONTH, 2);
                publicidad.setFechaHasta(cal.getTime());
            }
            publicidadFacade.edit(publicidad);
        }        
        
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
    public Double getPrecioDestacacion(NombreTipoDestacacion tipo) {
        PrecioTipoServicio precio = precioFacade.getPrecioDestacacion(tipo);
        if(precio != null)
            return precio.getPrecio();
        else
            return null;
    }

}

