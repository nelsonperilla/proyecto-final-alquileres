/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.PublicacionXEstado;
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
public class PublicacionXEstadoFacade extends AbstractFacade<PublicacionXEstado> {
    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PublicacionXEstadoFacade() {
        super(PublicacionXEstado.class);
    }
    
    public PublicacionXEstado getPublicacionXEstado(Publicacion p) {

        Query query = em.createQuery(
                "SELECT pxe FROM PublicacionXEstado pxe "
                + "WHERE pxe.publicacion = :publicacion "
                + "AND pxe.fechaHasta IS NULL");
        query.setParameter("publicacion", p);

        PublicacionXEstado pxe = null;
        try {
            pxe = (PublicacionXEstado) query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("PublicacionXEstado no encontrada");
        }

        return pxe;
    }
}
