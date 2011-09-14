/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.ejb.entity.Alquiler;
import com.alquilacosas.ejb.entity.AlquilerXEstado;
import com.alquilacosas.ejb.entity.EstadoAlquiler;
import com.alquilacosas.ejb.entity.EstadoAlquiler.NombreEstadoAlquiler;
import java.util.Date;
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

    public void saveState(Alquiler alquiler, NombreEstadoAlquiler nombreEstadoAlquiler)
        throws AlquilaCosasException{
        AlquilerXEstado estadoActual = new AlquilerXEstado();
        estadoActual.setAlquilerFk(alquiler);
        
        Query query = em.createNamedQuery("EstadoAlquiler.findByNombre");
        query.setParameter("nombre", nombreEstadoAlquiler);
        EstadoAlquiler estado = (EstadoAlquiler) query.getSingleResult();
        estadoActual.setEstadoAlquilerFk(estado);
        estadoActual.setFechaDesde(new Date());
        
        alquiler.agregarAlquilerXEstado(estadoActual);
        try {
            em.persist(estadoActual);
        }catch (Exception e){
            throw new AlquilaCosasException(e.getMessage());
        }
    }
}
