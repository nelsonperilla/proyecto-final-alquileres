/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Alquiler;
import com.alquilacosas.ejb.entity.Calificacion;
import com.alquilacosas.ejb.entity.Usuario;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class CalificacionFacade extends AbstractFacade<Calificacion> {

     @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
     private EntityManager em;

     @Override
     protected EntityManager getEntityManager() {
          return em;
     }

     public CalificacionFacade() {
          super(Calificacion.class);
     }

     public boolean isCalificacionExistente(Usuario usuario, Alquiler alquiler) {
          Query query = em.createQuery("SELECT count(c.calificacionId) FROM "
                  + "Calificacion c WHERE c.alquilerFk = :alquiler AND "
                  + "c.usuarioCalificadorFk = :usuario");
          query.setParameter("usuario", usuario);
          query.setParameter("alquiler", alquiler);
          Long resultado = -1L;
          try {
               resultado = (Long) query.getSingleResult();
          } catch (NoResultException e) {
          }
          if (resultado > 0) {
               return true;
          } else {
               return false;
          }

     }

     public Calificacion getCalificacionPorAlquilerUsuarioToma(Alquiler alquiler) {
          Query query = em.createQuery("SELECT c "
                  + "FROM Alquiler a, Calificacion c "
                  + "WHERE c.alquilerFk = a "
                  + "AND a = :alquiler "
                  + "AND a.usuarioFk = c.usuarioCalificadorFk");
          query.setParameter("alquiler", alquiler);
          Calificacion calificacion;
          try {
               calificacion = (Calificacion)query.getSingleResult();
          }
          catch (javax.persistence.NoResultException e) {
               calificacion = null;
          }
          return calificacion;
     }
     
     public Calificacion getCalificacionPorAlquilerUsuarioOfrece(Alquiler alquiler) {
          Query query = em.createQuery("SELECT c "
                  + "FROM Alquiler a, Calificacion c, Publicacion p "
                  + "WHERE a.publicacionFk = p "
                  + "AND c.alquilerFk = a "
                  + "AND a = :alquiler "
                  + "AND p.usuarioFk = c.usuarioCalificadorFk");
          query.setParameter("alquiler", alquiler);
          Calificacion calificacion;
          try {
               calificacion = (Calificacion)query.getSingleResult();
          }
          catch (javax.persistence.NoResultException e) {
               calificacion = null;
          }
          return calificacion;
     }
}
