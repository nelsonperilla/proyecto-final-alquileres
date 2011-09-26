/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.EstadoUsuario;
import com.alquilacosas.ejb.entity.EstadoUsuario.NombreEstadoUsuario;
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
public class EstadoUsuarioFacade extends AbstractFacade<EstadoUsuario> {
    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstadoUsuarioFacade() {
        super(EstadoUsuario.class);
    }
    
    public EstadoUsuario findByNombre(NombreEstadoUsuario nombre) {
        EstadoUsuario estado = null;
        Query getEstadoQuery = em.createNamedQuery("EstadoUsuario.findByNombre");
        getEstadoQuery.setParameter("nombre", nombre);
        try {
            estado = (EstadoUsuario) getEstadoQuery.getSingleResult();
        } catch(NoResultException e) {
            
        }
        return estado;
    }
    
}
