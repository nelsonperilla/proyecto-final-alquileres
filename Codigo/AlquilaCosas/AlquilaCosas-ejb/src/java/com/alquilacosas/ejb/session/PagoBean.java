/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.ejb.entity.Destacacion;
import com.alquilacosas.ejb.entity.Pago;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.Publicidad;
import com.alquilacosas.ejb.entity.Servicio;
import com.alquilacosas.ejb.entity.TipoDestacacion.NombreTipoDestacacion;
import com.alquilacosas.ejb.entity.TipoPublicidad.DuracionPublicidad;
import com.alquilacosas.facade.DestacacionFacade;
import com.alquilacosas.facade.PagoFacade;
import com.alquilacosas.facade.PrecioFacade;
import com.alquilacosas.facade.PublicacionFacade;
import com.alquilacosas.facade.PublicidadFacade;
import com.alquilacosas.facade.TipoPagoFacade;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class PagoBean implements PagoBeanLocal {

    @EJB
    private PublicacionFacade publicacionFacade;
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
    
    @RolesAllowed({"USUARIO", "ADMIN"})
    @Override
    public void efectuarServicio(Integer pagoId) {

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
    
}
