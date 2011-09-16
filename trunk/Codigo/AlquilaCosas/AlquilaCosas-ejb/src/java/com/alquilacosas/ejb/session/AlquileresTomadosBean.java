/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.dto.CalificacionDTO;
import com.alquilacosas.ejb.entity.Alquiler;
import com.alquilacosas.ejb.entity.Calificacion;
import com.alquilacosas.ejb.entity.EstadoAlquiler;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.Puntuacion;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.facade.AlquilerFacade;
import com.alquilacosas.facade.CalificacionFacade;
import com.alquilacosas.facade.EstadoAlquilerFacade;
import com.alquilacosas.facade.PuntuacionFacade;
import com.alquilacosas.facade.UsuarioFacade;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wilson
 */
@Stateless
public class AlquileresTomadosBean implements AlquileresTomadosBeanLocal {

     @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager entityManager;
     
     @EJB
     private AlquilerFacade alquilerFacade;
     @EJB
     private EstadoAlquilerFacade estadoFacade;
     @EJB
     private UsuarioFacade usuarioFacade;
     @EJB
     private PuntuacionFacade puntuacionFacade;
     @EJB
     private CalificacionFacade calificacionFacade;
     
     @Override
     public List<AlquilerDTO> getAlquileresSinCalificarPorUsuario(int usuarioId) throws AlquilaCosasException {
          Usuario usuario = usuarioFacade.find(usuarioId);
          List<Alquiler> alquileres = alquilerFacade.getAlquileresTomadosFinalizadosSinClasificar(usuario);
          List<AlquilerDTO> alquileresDTO = new ArrayList<AlquilerDTO>();
          for (Alquiler a : alquileres) {
               boolean calificado = false;
               Publicacion pub = a.getPublicacionFk();
               Usuario dueno = pub.getUsuarioFk();
               EstadoAlquiler estado = estadoFacade.getEstadoAlquiler(a);
               List<ImagenPublicacion> imagenes = pub.getImagenPublicacionList();
               int imagenId = -1;
               if (!imagenes.isEmpty()) {
                    imagenId = imagenes.get(0).getImagenPublicacionId();
               }
               alquileresDTO.add(new AlquilerDTO(pub.getPublicacionId(), usuarioId,
                       a.getAlquilerId(), imagenId, a.getFechaInicio(), a.getFechaFin(),
                       estado.getNombre(), pub.getTitulo(),
                       dueno.getNombre(), a.getCantidad(), a.getMonto(), calificado));
          }
          return alquileresDTO;
     }

     @Override
     public void registrarCalificacion(Integer puntuacion, Integer alquilerId, String comentario, Integer usuarioId) {
          Alquiler alquiler = alquilerFacade.find(alquilerId);
          Usuario usuario = usuarioFacade.find(usuarioId);
          Puntuacion nuevaPuntuacion = puntuacionFacade.find(puntuacion);
          
          Calificacion nuevaCalificacion = new Calificacion();
          nuevaCalificacion.setFechaCalificacion(new Date());
          nuevaCalificacion.setPuntuacionFk(nuevaPuntuacion);
          nuevaCalificacion.setComentarioCalificador(comentario);          
          nuevaCalificacion.setUsuarioCalificadorFk(usuario);
          
          alquiler.agregarCalificacion(nuevaCalificacion);
          alquilerFacade.edit(alquiler);
     }

     @Override
     public List<Puntuacion> getPuntuaciones() {
        Query query = entityManager.createNamedQuery("Puntuacion.findAll");
        List<Puntuacion> puntuaciones = query.getResultList();
        return puntuaciones;
     }

     @Override
     public List<AlquilerDTO> getAlquileresActivosPorUsuario(int usuarioId) throws AlquilaCosasException {
          Usuario usuario = entityManager.find(Usuario.class, usuarioId);
          List<Alquiler> alquileres = alquilerFacade.getAlquileresTomadosActivos(usuario);
          List<AlquilerDTO> alquileresDTO = new ArrayList<AlquilerDTO>();
          for (Alquiler a : alquileres) 
               alquileresDTO.add(new AlquilerDTO(a.getPublicacionFk().getPublicacionId(), a.getUsuarioFk().getUsuarioId(), a.getAlquilerId(), a.getFechaInicio(), a.getFechaFin(), a.getCantidad(), false));
          return alquileresDTO;
     }

     @Override
     public List<AlquilerDTO> getAlquileresConCalificarPorUsuario(int usuarioId) throws AlquilaCosasException {
          Usuario usuario = entityManager.find(Usuario.class, usuarioId);
          List<Alquiler> alquileres = alquilerFacade.getAlquileresTomadosFinalizadosConCalificacion(usuario);
          List<AlquilerDTO> alquileresDTO = new ArrayList<AlquilerDTO>();
          for (Alquiler a : alquileres) 
               alquileresDTO.add(new AlquilerDTO(a.getPublicacionFk().getPublicacionId(), a.getUsuarioFk().getUsuarioId(), a.getAlquilerId(), a.getFechaInicio(), a.getFechaFin(), a.getCantidad(), true));
          return alquileresDTO;
     }
     
     @Override
     public CalificacionDTO getCalificacionOfrece(Integer alquilerId, Integer usuarioId) throws AlquilaCosasException{
          CalificacionDTO calificacionDTO = new CalificacionDTO();
          Alquiler alquiler = alquilerFacade.find(alquilerId);
          Usuario usuario = usuarioFacade.find(usuarioId);
          Calificacion calificacion = calificacionFacade.getCalificacionPorAlquilerUsuarioOfrece(alquiler, usuario);
          if (calificacion != null) {
               calificacionDTO.setComentarioCalificacion(calificacion.getComentarioCalificador());
               calificacionDTO.setComentarioReplica(calificacion.getComentarioReplica() != null ? calificacion.getComentarioReplica() : null);
               calificacionDTO.setFechaCalificacion(calificacion.getFechaCalificacion());
               calificacionDTO.setFechaReplica(calificacion.getFechaReplica() != null ? calificacion.getFechaReplica() : null);
               calificacionDTO.setIdCalificacion(calificacion.getCalificacionId());
               calificacionDTO.setNombrePuntuacion(calificacion.getPuntuacionFk().getNombre());
               calificacionDTO.setNombreUsuarioCalificador(calificacion.getUsuarioCalificadorFk().getNombre() + " " + calificacion.getUsuarioCalificadorFk().getApellido());
               calificacionDTO.setIdUsuarioCalidicador(calificacion.getUsuarioCalificadorFk().getUsuarioId());
               calificacionDTO.setNombreUsuarioReplica(calificacion.getUsuarioReplicadorFk() != null ? calificacion.getUsuarioReplicadorFk().getNombre() + " " + calificacion.getUsuarioReplicadorFk().getApellido() : null);
               calificacionDTO.setIdUsuarioReplicador(calificacion.getUsuarioReplicadorFk() != null ? calificacion.getUsuarioReplicadorFk().getUsuarioId() : null);
               calificacionDTO.setYaReplico(calificacion.getFechaReplica() == null);
          }
          return calificacionDTO;
     }
     
     @Override
     public CalificacionDTO getCalificacionToma(Integer alquilerId, Integer usuarioId) throws AlquilaCosasException {
          CalificacionDTO calificacionDTO = new CalificacionDTO();
          Alquiler alquiler = alquilerFacade.find(alquilerId);
          Usuario usuario = usuarioFacade.find(usuarioId);
          Calificacion calificacion = calificacionFacade.getCalificacionPorAlquilerUsuarioToma(alquiler, usuario);
          if (calificacion != null) {
               calificacionDTO.setComentarioCalificacion(calificacion.getComentarioCalificador());
               calificacionDTO.setComentarioReplica(calificacion.getComentarioReplica() != null ? calificacion.getComentarioReplica() : null);
               calificacionDTO.setFechaCalificacion(calificacion.getFechaCalificacion());
               calificacionDTO.setFechaReplica(calificacion.getFechaReplica() != null ? calificacion.getFechaReplica() : null);
               calificacionDTO.setIdCalificacion(calificacion.getCalificacionId());
               calificacionDTO.setNombrePuntuacion(calificacion.getPuntuacionFk().getNombre());
               calificacionDTO.setNombreUsuarioCalificador(calificacion.getUsuarioCalificadorFk().getNombre() + " " + calificacion.getUsuarioCalificadorFk().getApellido());
               calificacionDTO.setIdUsuarioCalidicador(calificacion.getUsuarioCalificadorFk().getUsuarioId());
               calificacionDTO.setNombreUsuarioReplica(calificacion.getUsuarioReplicadorFk() != null ? calificacion.getUsuarioReplicadorFk().getNombre() + " " + calificacion.getUsuarioReplicadorFk().getApellido() : null);
               calificacionDTO.setIdUsuarioReplicador(calificacion.getUsuarioReplicadorFk() != null ? calificacion.getUsuarioReplicadorFk().getUsuarioId() : null);
               calificacionDTO.setYaReplico(calificacion.getFechaReplica() == null);
          }
          return calificacionDTO;
     }
}
