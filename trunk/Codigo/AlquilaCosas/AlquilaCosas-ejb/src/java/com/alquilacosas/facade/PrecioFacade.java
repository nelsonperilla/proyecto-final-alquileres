/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Precio;
import com.alquilacosas.ejb.entity.Publicacion;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author damiancardozo
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PrecioFacade extends AbstractFacade<Precio> {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PrecioFacade() {
        super(Precio.class);
    }

    public List<Precio> buscarActualesPorPublicacion(Publicacion publicacion) {
        Query query = em.createQuery("SELECT p FROM Precio p WHERE "
        + "p.publicacionFk = :publicacion AND p.fechaHasta IS NULL ORDER BY p.precio ASC");
        query.setParameter("publicacion", publicacion);
        return query.getResultList();
    }
}
