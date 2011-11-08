/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.CalificacionDTO;
import com.alquilacosas.dto.DomicilioDTO;
import com.alquilacosas.dto.UsuarioDTO;
import com.alquilacosas.ejb.entity.Calificacion;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.facade.CalificacionFacade;
import com.alquilacosas.facade.UsuarioFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author wilson
 */
@Stateless
public class VerReputacionBean implements VerReputacionBeanLocal {

     // Add business logic below. (Right-click in editor and choose
     // "Insert Code > Add Business Method")
     
     @EJB
     UsuarioFacade usuarioFacade;
     @EJB
     CalificacionFacade calificacionFacade;

     @Override
     public UsuarioDTO getUsuarioReputacionPorId(int usuarioId) throws AlquilaCosasException {
          Usuario usuario = usuarioFacade.find(usuarioId);
          if(usuario == null) {
              throw new AlquilaCosasException("Usuario no encontrado");
          }
          UsuarioDTO usuarioDTO = new UsuarioDTO();
          usuarioDTO.setUsername(usuario.getLoginList().get(0).getUsername());
          usuarioDTO.setFechaDeRegistro(usuario.getLoginList().get(0).getFechaCreacion());
          DomicilioDTO domicilioDTO = new DomicilioDTO();
          domicilioDTO.setCiudad(usuario.getDomicilioList().get(0).getCiudad());
          domicilioDTO.setProvincia(usuario.getDomicilioList().get(0).getProvinciaFk().getNombre());
          domicilioDTO.setPais(usuario.getDomicilioList().get(0).getProvinciaFk().getPaisFk().getNombre());
          usuarioDTO.setDomicilio(domicilioDTO);
          usuarioDTO.setCantidadAdvertencia(usuario.getAdvertenciaList().size());
          usuarioDTO.setCantidadSuspencion(usuario.getSuspensionList().size());
          double rating =  calificacionFacade.getCalificacionByUsuario(usuarioId);
          rating*=0.5;
          rating+=5;
          usuarioDTO.setUserRating(rating);
          Integer cantOfrece = 0;
          Integer cantOfreceNegativas = 0;
          Integer cantOfreceNeutrales = 0;
          Integer cantOfrecePositivas = 0;
          for (Calificacion c : calificacionFacade.getCalificacionAlquilerOfrecidoPorUsuario(usuario)) {
               if ("POSITIVO".equals(c.getPuntuacionFk().getNombre())) 
                    cantOfrecePositivas ++;
               else 
                    if ("NEUTRAL".equals(c.getPuntuacionFk().getNombre()))
                         cantOfreceNeutrales ++;
                    else
                         cantOfreceNegativas ++;
               cantOfrece ++;
          }
          usuarioDTO.setUserOfrecePositivas(cantOfrecePositivas);
          usuarioDTO.setUserOfreceNegativas(cantOfreceNegativas);
          usuarioDTO.setUserOfreceNeutrales(cantOfreceNeutrales);
          if (cantOfrece > 0) {               
               usuarioDTO.setUserOfrecePositivasPorcentaje(redondear((cantOfrecePositivas.doubleValue() / cantOfrece.doubleValue()) * 100));
               usuarioDTO.setUserOfreceNegativasPorcentaje(redondear((cantOfreceNegativas.doubleValue() / cantOfrece.doubleValue()) * 100));
               usuarioDTO.setUserOfreceNeutralesPorcentaje(redondear((cantOfreceNeutrales.doubleValue() / cantOfrece.doubleValue()) * 100));
          }
          else {
               usuarioDTO.setUserOfrecePositivasPorcentaje(0);
               usuarioDTO.setUserOfreceNegativasPorcentaje(0);
               usuarioDTO.setUserOfreceNeutralesPorcentaje(0);
          }
          Integer cantToma = 0;
          Integer cantTomaNegativas = 0;
          Integer cantTomaNeutrales = 0;
          Integer cantTomaPositivas = 0;
          for (Calificacion c : calificacionFacade.getCalificacionAlquilerTomadoPorUsuario(usuario)) {
               if ("POSITIVO".equals(c.getPuntuacionFk().getNombre())) 
                    cantTomaPositivas ++;
               else 
                    if ("NEUTRAL".equals(c.getPuntuacionFk().getNombre()))
                         cantTomaNeutrales ++;
                    else
                         cantTomaNegativas ++;
               cantToma ++;
          }
          usuarioDTO.setUserTomaPositivas(cantTomaPositivas);
          usuarioDTO.setUserTomaNegativas(cantTomaNegativas);
          usuarioDTO.setUserTomaNeutrales(cantTomaNeutrales);
          if (cantToma > 0) {               
               usuarioDTO.setUserTomaPositivasPorcentaje(redondear((cantTomaPositivas.doubleValue() / cantToma.doubleValue()) * 100));
               usuarioDTO.setUserTomaNegativasPorcentaje(redondear((cantTomaNegativas.doubleValue() / cantToma.doubleValue()) * 100));
               usuarioDTO.setUserTomaNeutralesPorcentaje(redondear((cantTomaNeutrales.doubleValue() / cantToma.doubleValue()) * 100));
          }
          else {
               usuarioDTO.setUserTomaPositivasPorcentaje(0);
               usuarioDTO.setUserTomaNegativasPorcentaje(0);
               usuarioDTO.setUserTomaNeutralesPorcentaje(0);
          }
          return usuarioDTO;
     }

     @Override
     public List<CalificacionDTO> getCalificacionOfrece(int usuarioId) {
          List<CalificacionDTO> calificacionDTO = new ArrayList<CalificacionDTO>();
          Usuario usuario = usuarioFacade.find(usuarioId);
          List<Calificacion> calificaciones = calificacionFacade.getCalificacionAlquilerOfrecidoPorUsuario(usuario);
          for (Calificacion c : calificaciones) {
               CalificacionDTO calificacion = new CalificacionDTO();
               calificacion.setComentarioCalificacion(c.getComentarioCalificador());
               calificacion.setComentarioReplica(c.getComentarioReplica());
               calificacion.setFechaCalificacion(c.getFechaCalificacion());
               calificacion.setFechaReplica(c.getFechaReplica());
               calificacion.setIdUsuarioCalidicador(c.getUsuarioCalificadorFk().getUsuarioId());
               calificacion.setNombreUsuarioCalificador(c.getUsuarioCalificadorFk().getLoginList().get(0).getUsername());
               calificacion.setNombrePuntuacion(c.getPuntuacionFk().getNombre());
               calificacion.setYaReplico(c.getFechaReplica() == null ? Boolean.FALSE : Boolean.TRUE);
               calificacionDTO.add(calificacion);
          }
          return calificacionDTO;
     }

     @Override
     public List<CalificacionDTO> getCalificacionToma(int usuarioId) {
          List<CalificacionDTO> calificacionDTO = new ArrayList<CalificacionDTO>();
          Usuario usuario = usuarioFacade.find(usuarioId);
          List<Calificacion> calificaciones = calificacionFacade.getCalificacionAlquilerTomadoPorUsuario(usuario);
          for (Calificacion c : calificaciones) {
               CalificacionDTO calificacion = new CalificacionDTO();
               calificacion.setComentarioCalificacion(c.getComentarioCalificador());
               calificacion.setComentarioReplica(c.getComentarioReplica());
               calificacion.setFechaCalificacion(c.getFechaCalificacion());
               calificacion.setFechaReplica(c.getFechaReplica());
               calificacion.setIdUsuarioCalidicador(c.getUsuarioCalificadorFk().getUsuarioId());
               calificacion.setNombreUsuarioCalificador(c.getUsuarioCalificadorFk().getLoginList().get(0).getUsername());
               calificacion.setNombrePuntuacion(c.getPuntuacionFk().getNombre());
               calificacion.setYaReplico(c.getFechaReplica() == null ? Boolean.FALSE : Boolean.TRUE);
               calificacionDTO.add(calificacion);
          }
          return calificacionDTO;
     }
     
     @Override
     public double redondear(double numero) {
          return Math.rint(numero * 100) / 100;
     }
}
