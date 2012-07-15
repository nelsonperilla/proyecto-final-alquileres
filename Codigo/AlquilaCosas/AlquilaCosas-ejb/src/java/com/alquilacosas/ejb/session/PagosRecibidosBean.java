/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.PagoDTO;
import com.alquilacosas.ejb.entity.Destacacion;
import com.alquilacosas.ejb.entity.Pago;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.Publicidad;
import com.alquilacosas.ejb.entity.Servicio;
import com.alquilacosas.ejb.entity.TipoDestacacion.NombreTipoDestacacion;
import com.alquilacosas.ejb.entity.TipoPago;
import com.alquilacosas.ejb.entity.TipoPago.NombreTipoPago;
import com.alquilacosas.ejb.entity.TipoPublicidad.DuracionPublicidad;
import com.alquilacosas.facade.DestacacionFacade;
import com.alquilacosas.facade.PagoFacade;
import com.alquilacosas.facade.PublicacionFacade;
import com.alquilacosas.facade.PublicidadFacade;
import com.alquilacosas.facade.ServicioFacade;
import com.alquilacosas.facade.TipoPagoFacade;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class PagosRecibidosBean implements PagosRecibidosBeanLocal {

    @EJB
    private PagoFacade pagoFacade;
    @EJB
    private TipoPagoFacade tipoPagoFacade;
    @EJB
    private PublicidadFacade publicidadFacade;
    @EJB
    private PublicacionFacade publicacionFacade;
    @EJB
    private DestacacionFacade destacacionFacade;
    @EJB
    private ServicioFacade servicioFacade;
    
    @Override
    public List<PagoDTO> getPagosRecibidos(NombreTipoPago tipoPago, Date fechaDesde, 
            Boolean confirmado, int registros, int desde) {
        TipoPago tipo = null;
        if(tipoPago != null) {
            tipo = tipoPagoFacade.findByNombre(tipoPago);
        }
        List<Pago> pagos = pagoFacade.getPagosRecibidos(tipo, fechaDesde, confirmado, registros, desde);
        List<PagoDTO> dtos = new ArrayList<PagoDTO>();
        for(Pago p: pagos) {
            PagoDTO.EstadoPago estado = (p.getFechaConfirmado() == null) ? 
                    PagoDTO.EstadoPago.INICIADO : PagoDTO.EstadoPago.CONFIRMADO;
            PagoDTO.TipoServicio t = null;
            if(p.getServicioFk() instanceof Destacacion) {
                t = PagoDTO.TipoServicio.DESTACACION;
            } else {
                t = PagoDTO.TipoServicio.PUBLICIDAD;
            }
            String usuario = p.getUsuarioFk().getNombre() + " " + p.getUsuarioFk().getApellido();
            PagoDTO dto = new PagoDTO(p.getPagoId(), usuario, p.getFechaInicio(), 
                    p.getFechaConfirmado(), p.getMonto(), p.getTipoPagoFk().getNombre(), 
                    estado, t);
            dtos.add(dto);
        }
        return dtos;
    }
    
    @Override
    public Long getCantidadPagos(NombreTipoPago tipoPago, Date fechaDesde, Boolean confirmado) {
        TipoPago tipo = null;
        if(tipoPago != null) {
            tipo = tipoPagoFacade.findByNombre(tipoPago);
        }
        return pagoFacade.countPagosRecibidos(tipo, fechaDesde, confirmado);
    }
    
    @Override
    public void confirmarPago(Integer pagoId) {

        Pago pago = pagoFacade.find(pagoId);
        pago.setFechaConfirmado(new Date());
        pagoFacade.edit(pago);
        
        Servicio servicio = pago.getServicioFk();
        
        Calendar cal = Calendar.getInstance();
        if(servicio instanceof Destacacion) {
            cal.setTime(new Date());
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
            cal.setTime(publicidad.getFechaDesde());
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
    public void eliminarPago(int pagoId) {
        Pago pago = pagoFacade.find(pagoId);
        Servicio servicio = pago.getServicioFk();
        pagoFacade.remove(pago);
        servicioFacade.remove(servicio);
    }
    
}
