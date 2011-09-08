/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.ejb.entity.Alquiler;
import com.alquilacosas.ejb.entity.AlquilerXEstado;
import com.alquilacosas.ejb.entity.Calificacion;
import com.alquilacosas.ejb.entity.EstadoAlquiler.NombreEstadoAlquiler;
import com.alquilacosas.ejb.entity.Puntuacion;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.facade.CalificacionFacade;
import java.util.ArrayList;
import java.util.List;
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
     
     @Override
     public List<AlquilerDTO> getAlquileresSinCalificarPorUsuario(int usuarioId) throws AlquilaCosasException {
          
          /*
           * SELECT * 
           * FROM ALQUILER A, ALQUILER_X_ESTADO AX, ESTADO_ALQUILER E 
           * WHERE AX.ALQUILER_FK=A.ALQUILER_ID AND AX.ESTADO_ALQUILER_FK=E.ESTADO_ALQUILER_ID 
           * AND E.NOMBRE = 'FINALIZADO' AND AX.FECHA_HASTA IS NULL AND A.USUARIO_FK = 4 
           * AND NOT EXISTS (SELECT * FROM CALIFICACION C 
           *                 WHERE C.ALQUILER_FK = A.ALQUILER_ID 
           *                   AND C.USUARIO_CALIFICADOR_FK = 4)
           */
          
          Usuario usuario = entityManager.find(Usuario.class, usuarioId);
          List<Alquiler> alquileres = usuario.getAlquilerList();
          List<AlquilerDTO> alquileresDTO = new ArrayList<AlquilerDTO>();
          boolean califico = false;
          for (Alquiler a : alquileres) 
               for (AlquilerXEstado ae : a.getAlquilerXEstadoList())
                    if (ae.getFechaHasta() == null && ae.getEstadoAlquilerFk().getNombre() == NombreEstadoAlquiler.FINALIZADO) {
                         for (Calificacion c : a.getCalificacionList())
                              if (c.getUsuarioCalificadorFk().equals(usuario))
                                   califico = true;
                         if (!califico)
                              alquileresDTO.add(new AlquilerDTO(a.getPublicacionFk().getPublicacionId(), a.getUsuarioFk().getUsuarioId(), a.getAlquilerId(), a.getFechaInicio(), a.getFechaFin(), a.getCantidad(), false));
                    }
          return alquileresDTO;
     }

     // Add business logic below. (Right-click in editor and choose
     // "Insert Code > Add Business Method")

     @Override
     public Alquiler getAlquilerPorId(int alquilerId) {
          Alquiler alquiler = entityManager.find(Alquiler.class, alquilerId);
          return alquiler;
     }

     @Override
     public Usuario getUsuarioPorId(int usuarioId) {
          Usuario usuario = entityManager.find(Usuario.class, usuarioId);
          return usuario;
     }

     @Override
     public void registrarCalificacion(Calificacion nuevaCalificacion) {
          CalificacionFacade calificacion = new CalificacionFacade();
          calificacion.create(nuevaCalificacion);
     }

     @Override
     public List<Puntuacion> getPuntuaciones() {
        Query query = entityManager.createNamedQuery("Puntuacion.findAll");
        List<Puntuacion> puntuaciones = query.getResultList();
        return puntuaciones;
     }

     @Override
     public Puntuacion getPuntuacionesPorId(int puntuacionId) {
          Puntuacion puntuacion = entityManager.find(Puntuacion.class, puntuacionId);
          return puntuacion;
     }
     
}
