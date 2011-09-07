/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.AlquilerXEstado;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class AlquilerXEstadoFacade extends AbstractFacade<AlquilerXEstado> {
    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public AlquilerXEstadoFacade() {
        super(AlquilerXEstado.class);
    }
    
    public AlquilerXEstado findByAlquiler( int alquilerId ){
        AlquilerXEstado axe = new AlquilerXEstado();
        Query query = em.createNamedQuery("AlquilerXEstado.findByAlquiler");
        query.setParameter("alquilerId", alquilerId);
        axe = (AlquilerXEstado) query.getSingleResult();
        return axe;
    }
}
