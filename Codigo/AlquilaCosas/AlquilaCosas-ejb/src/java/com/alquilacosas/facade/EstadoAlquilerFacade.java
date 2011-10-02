/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Alquiler;
import com.alquilacosas.ejb.entity.EstadoAlquiler;
import javax.ejb.Stateless;
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

    public EstadoAlquiler getEstadoAlquiler(Alquiler alquiler) {
        EstadoAlquiler estado = null;
        Query query = em.createQuery("SELECT ea FROM EstadoAlquiler ea, AlquilerXEstado axe, Alquiler a "
                + "WHERE a = axe.alquilerFk "
                + "AND axe.estadoAlquilerFk = ea "
                + "AND axe.fechaHasta IS NULL AND a = :alquiler");
        query.setParameter("alquiler", alquiler);
        try {
            estado = (EstadoAlquiler) query.getSingleResult();
        } catch (NoResultException e) {
            Logger.getLogger(EstadoAlquilerFacade.class).
                    error("getEstadoAlquiler(). "
                    + "Excepcion al ejecutar consulta: " + e);
        }
        return estado;
    }

    public EstadoAlquiler findByNombre(EstadoAlquiler.NombreEstadoAlquiler nombre) {
        EstadoAlquiler estadoPublicacion = null;
        Query query = em.createNamedQuery("EstadoAlquiler.findByNombre");
        query.setParameter("nombre", nombre);
        estadoPublicacion = (EstadoAlquiler) query.getSingleResult();
        return estadoPublicacion;
    }
}
