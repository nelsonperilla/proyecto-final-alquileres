/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.PublicacionXEstado;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author damiancardozo
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
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
            Logger.getLogger(PublicacionXEstadoFacade.class).
                    error("getPublicacionXEstado(). "
                    + "Excepcion al ejecutar consulta: " + e);
        }

        return pxe;
    }
}
