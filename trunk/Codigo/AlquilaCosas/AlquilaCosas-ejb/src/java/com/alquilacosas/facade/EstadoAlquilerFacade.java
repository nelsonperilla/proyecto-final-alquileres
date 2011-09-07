/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.EstadoAlquiler;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class EstadoAlquilerFacade extends AbstractFacade<EstadoAlquiler> {
    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstadoAlquilerFacade() {
        super(EstadoAlquiler.class);
    }
    
    public EstadoAlquiler getEstadoAlquiler( int alquilerId ){
        EstadoAlquiler ae = new EstadoAlquiler();
        Query query = em.createNamedQuery("EstadoAlquiler.findByAlquiler");
        query.setParameter("alquilerId", alquilerId);
        return ae;
    }
    
    public EstadoAlquiler findByNombre( EstadoAlquiler.NombreEstadoAlquiler nombre ){
        EstadoAlquiler estadoPublicacion = null;
        Query query = em.createNamedQuery("EstadoAlquiler.findByNombre");
        query.setParameter("nombre", nombre);
        estadoPublicacion = (EstadoAlquiler) query.getSingleResult();
        return estadoPublicacion;
    }
}
