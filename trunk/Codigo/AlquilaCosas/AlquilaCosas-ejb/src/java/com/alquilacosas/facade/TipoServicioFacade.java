/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.TipoServicio;
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
public class TipoServicioFacade extends AbstractFacade<TipoServicio> {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoServicioFacade() {
        super(TipoServicio.class);
    }

    public TipoServicio findByNombre(TipoServicio.NombreTipoServicio nombre) {
        Query query = em.createNamedQuery("TipoServicio.findByNombre");
        query.setParameter("nombre", nombre);
        TipoServicio tipoServicio = null;
        try {
            tipoServicio = (TipoServicio) query.getSingleResult();
        } catch (NoResultException e) {
            Logger.getLogger(TipoServicioFacade.class).
                    error("findByNombre(). "
                    + "Excepcion al ejecutar consulta: " + e);
        }
        return tipoServicio;
    }
}
