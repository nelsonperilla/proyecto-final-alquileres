/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.timer;

import com.alquilacosas.ejb.entity.Alquiler;
import com.alquilacosas.ejb.entity.AlquilerXEstado;
import com.alquilacosas.ejb.entity.EstadoAlquiler;
import com.alquilacosas.ejb.entity.EstadoPublicacion;
import com.alquilacosas.ejb.entity.EstadoPublicacion.NombreEstadoPublicacion;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.PublicacionXEstado;
import com.alquilacosas.facade.AlquilerFacade;
import com.alquilacosas.facade.AlquilerXEstadoFacade;
import com.alquilacosas.facade.EstadoAlquilerFacade;
import com.alquilacosas.facade.EstadoPublicacionFacade;
import com.alquilacosas.facade.PublicacionFacade;
import com.alquilacosas.facade.PublicacionXEstadoFacade;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.SessionContext;
import javax.ejb.Timer;
import javax.persistence.NoResultException;

/**
 *
 * @author ignaciogiagante
 */
@Stateless
@LocalBean
public class AlquilerTimerBean implements AlquilerTimerBeanLocal{
    
    @EJB
    private AlquilerFacade alquilerFacade;
    @EJB
    private AlquilerXEstadoFacade alquilerXEstadoFacade;
    @EJB
    private EstadoAlquilerFacade estadoAlquilerFacade;
    @EJB
    private PublicacionFacade publicacionFacade;
    @EJB
    private PublicacionXEstadoFacade publicacionXEstadoFacade;
    @EJB
    private EstadoPublicacionFacade estadoPublicacionFacade;
    @Resource
    private SessionContext context;
    
    /**
     * El siguiente método cambia de estado los alquileres confirmados a 
     * activos
     * @param timer
     * @author ignacio Gigante
     */

    @Schedule(second="10", minute="*", hour="*")
    @Override
    public void fromConfirmadoToActivo(Timer timer) {
        
        GregorianCalendar hoy = this.getCalendarWithoutHour(null);
        GregorianCalendar fechaInicio = new GregorianCalendar();
        List<Alquiler> alquileresConfirmados = null;
        try {
            alquileresConfirmados = alquilerFacade.getAlquileresConfirmados();
        } catch (NoResultException e) {
            context.getRollbackOnly();
            System.out.println("No se pudo traer de la BD los alquileres confirmados" + e.getStackTrace());
        }
        
        for( Alquiler a : alquileresConfirmados ){
            fechaInicio.setTime(a.getFechaInicio());
            if( this.compararFecha(hoy, fechaInicio) ){
                AlquilerXEstado axe = alquilerXEstadoFacade.findByAlquiler(a.getAlquilerId());
                axe.setFechaHasta( new Date() );
                //se crea un nuevo estado para el alquiler
                EstadoAlquiler ea = estadoAlquilerFacade.findByNombre(EstadoAlquiler.NombreEstadoAlquiler.ACTIVO);
                AlquilerXEstado axeNuevo = new AlquilerXEstado( a, ea, new Date() );
                a.agregarAlquilerXEstado(axeNuevo);
                alquilerFacade.edit(a);
            }
                
        }
    }
    
    /**
     * El siguiente método cambia de estado los alquileres activos a 
     * finalizados
     * @param timer
     * @author ignacio Gigante
     */
    
    @Schedule(second="0", minute="0", hour="23")
    @Override
    public void fromActivoToFinalizado(Timer timer) {
        
        GregorianCalendar hoy = this.getCalendarWithoutHour(null);
        GregorianCalendar fechaFin = new GregorianCalendar();
        
        List<Alquiler> alquileresActivos = null;
        try {
            alquileresActivos = alquilerFacade.getAlquileresActivos();
        } catch (NoResultException e) {
            context.getRollbackOnly();
            System.out.println("No se pudo traer de la BD los alquileres activos" + e.getStackTrace());
        }
        
        for( Alquiler a : alquileresActivos ){
            
            fechaFin.setTime(a.getFechaInicio());
            
            if( this.compararFecha(hoy, fechaFin) ){
                AlquilerXEstado axe = alquilerXEstadoFacade.findByAlquiler(a.getAlquilerId());
                axe.setFechaHasta( new Date() );
                //se crea un nuevo estado para el alquiler
                EstadoAlquiler ea = estadoAlquilerFacade.findByNombre(EstadoAlquiler.NombreEstadoAlquiler.FINALIZADO);
                AlquilerXEstado axeNuevo = new AlquilerXEstado( a, ea, new Date() );
                a.agregarAlquilerXEstado(axeNuevo);
                alquilerFacade.edit(a);
            }       
        }
    }
    
    /**
     * El siguiente método controla cuales son las publicaciones que superaron
     * los 60 días. Las mismas se pasan a estado INACTIVAS.
     * @param timer
     * @author ignacio Gigante
     */
    
    @Schedule(second="0", minute="0", hour="23")
    @Override
    public void controlarPublicacionesVencidas(Timer timer) {
        
        GregorianCalendar hoy = this.getCalendarWithoutHour(null);
        hoy.add(Calendar.DATE, -60);
        GregorianCalendar fechaFin = new GregorianCalendar();
        
        List<Publicacion> publicacionesVencidas = null;
        
        try {
            publicacionesVencidas = publicacionFacade.getPublicacionesInicio();
        } catch (NoResultException e) {
            System.out.println("No se pudo traer de la BD las publicaciones" + e.getStackTrace());
        }
        
        for(Publicacion p : publicacionesVencidas ){
            
            fechaFin.setTime(p.getFechaHasta());
            
            if( this.compararFecha(hoy, fechaFin) ){
                PublicacionXEstado pxe = publicacionXEstadoFacade.getPublicacionXEstado(p);
                pxe.setFechaHasta( new Date() );
                //Se crea un nuevo estado para la publicacion
                EstadoPublicacion ep = estadoPublicacionFacade.findByNombre(NombreEstadoPublicacion.INACTIVA);
                PublicacionXEstado pxeNuevo = new PublicacionXEstado(p, ep);
                p.agregarPublicacionXEstado(pxeNuevo);
                p.setDestacada(false);
                publicacionFacade.edit(p);
            }       
        }
    }
    
    /**
     * El siguiente método cambia de estado los alquileres confirmados a 
     * activos
     * @param timer
     * @author ignacio Gigante
     */
    
    @Schedule(second="0", minute="0", hour="23")
    @Override
    public void borrarPedidosRechazadosYCancelados(Timer timer) {
        
        List<Alquiler> alquileresABorrar = null;
        try {
            alquileresABorrar = alquilerFacade.getAlquileresRechazadosOCancelados();
        } catch (NoResultException e) {
            context.getRollbackOnly();
            System.out.println("No se pudo traer de la BD los alquileres rechazdos o cancelados" + e.getStackTrace());
        }
        
        for( Alquiler a : alquileresABorrar ){
           alquilerFacade.remove(a);
        }
        
    }
    
    private GregorianCalendar getCalendarWithoutHour( Date date ){
        GregorianCalendar cal = new GregorianCalendar();
        if( date != null )
            cal.setTime(date);
        cal.add(Calendar.MILLISECOND, 0);
        cal.add(Calendar.SECOND, 0);
        cal.add(Calendar.MINUTE, 0);
        cal.add(Calendar.HOUR_OF_DAY, 0);
        return cal;
        
    }
    
     private boolean compararFecha(Calendar cal1, Calendar cal2) {
         
      if (cal1 == null || cal2 == null) {
          throw new IllegalArgumentException("La fecha no debería ser nula");
      }
      return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
              cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
          
      }
}
