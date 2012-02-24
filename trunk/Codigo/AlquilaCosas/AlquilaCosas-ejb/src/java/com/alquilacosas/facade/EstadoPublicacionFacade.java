/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.EstadoPublicacion;
import com.alquilacosas.ejb.entity.EstadoPublicacion.NombreEstadoPublicacion;
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
public class EstadoPublicacionFacade extends AbstractFacade<EstadoPublicacion> {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstadoPublicacionFacade() {
        super(EstadoPublicacion.class);
    }

    public EstadoPublicacion findByNombre(NombreEstadoPublicacion nombre) {
        EstadoPublicacion estadoPublicacion = null;
        Query query = em.createQuery("SELECT e FROM EstadoPublicacion e WHERE e.nombre = :nombre");
        query.setParameter("nombre", nombre);
        estadoPublicacion = null;
        try {
            estadoPublicacion = (EstadoPublicacion) query.getSingleResult();
        } catch (Exception e) {
        }
        return estadoPublicacion;
    }
}
