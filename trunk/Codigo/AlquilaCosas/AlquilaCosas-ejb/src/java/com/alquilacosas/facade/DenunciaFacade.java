/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Denuncia;
import com.alquilacosas.ejb.entity.EstadoDenuncia.NombreEstadoDenuncia;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class DenunciaFacade extends AbstractFacade<Denuncia> {

     @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
     private EntityManager em;

     @Override
     protected EntityManager getEntityManager() {
          return em;
     }

     public DenunciaFacade() {
          super(Denuncia.class);
     }

     public List<Denuncia> getAllDenuncias() {
          Query query = em.createQuery("SELECT d "
                  + "FROM Denuncia d, DenunciaXEstado dxe, EstadoDenuncia ed "
                  + "WHERE d = dxe.denunciaFk "
                  + "AND ed = dxe.estadoDenunciaFk "
                  + "AND ed.nombre = :estado "
                  + "AND dxe.fechaHasta IS NULL "
                  + "ORDER BY d.fecha");
          query.setParameter("estado", NombreEstadoDenuncia.PENDIENTE);
          List<Denuncia> denuncias;
          try {
               denuncias = query.getResultList();
          } catch (javax.persistence.NoResultException e) {
               Logger.getLogger(DenunciaFacade.class).
                       error("getAllDenuncias(). "
                       + "Excepcion al ejecutar consulta: " + e);
               denuncias = null;
          }
          return denuncias;
     }
     
     public List<Denuncia> getDenunciasPublicacion() {
          Query query = em.createQuery("SELECT d "
                  + "FROM Denuncia d, DenunciaXEstado dxe, EstadoDenuncia ed "
                  + "WHERE d = dxe.denunciaFk "
                  + "AND ed = dxe.estadoDenunciaFk "
                  + "AND ed.nombre = :estado "
                  + "AND dxe.fechaHasta IS NULL "
                  + "AND d.comentarioFk IS NULL "
                  + "ORDER BY d.fecha");
          query.setParameter("estado", NombreEstadoDenuncia.PENDIENTE);
          List<Denuncia> denuncias;
          try {
               denuncias = query.getResultList();
          } catch (javax.persistence.NoResultException e) {
               Logger.getLogger(DenunciaFacade.class).
                       error("getAllDenuncias(). "
                       + "Excepcion al ejecutar consulta: " + e);
               denuncias = null;
          }
          return denuncias;
     }
     
     public List<Denuncia> getDenunciasComentario() {
          Query query = em.createQuery("SELECT d "
                  + "FROM Denuncia d, DenunciaXEstado dxe, EstadoDenuncia ed "
                  + "WHERE d = dxe.denunciaFk "
                  + "AND ed = dxe.estadoDenunciaFk "
                  + "AND ed.nombre = :estado "
                  + "AND dxe.fechaHasta IS NULL "
                  + "AND d.comentarioFk IS NOT NULL "
                  + "ORDER BY d.fecha");
          query.setParameter("estado", NombreEstadoDenuncia.PENDIENTE);
          List<Denuncia> denuncias;
          try {
               denuncias = query.getResultList();
          } catch (javax.persistence.NoResultException e) {
               Logger.getLogger(DenunciaFacade.class).
                       error("getAllDenuncias(). "
                       + "Excepcion al ejecutar consulta: " + e);
               denuncias = null;
          }
          return denuncias;
     }
}
