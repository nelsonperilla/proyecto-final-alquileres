/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.TipoDestacacion;
import com.alquilacosas.ejb.entity.TipoDestacacion.NombreTipoDestacacion;
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
public class TipoDestacacionFacade extends AbstractFacade<TipoDestacacion> {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoDestacacionFacade() {
        super(TipoDestacacion.class);
    }

    public TipoDestacacion findByNombre(NombreTipoDestacacion nombre) {
        Query query = em.createNamedQuery("TipoServicio.findByNombre");
        query.setParameter("nombre", nombre);
        TipoDestacacion tipoServicio = null;
        try {
            tipoServicio = (TipoDestacacion) query.getSingleResult();
        } catch (NoResultException e) {
            Logger.getLogger(TipoDestacacionFacade.class).
                    error("findByNombre(). "
                    + "Excepcion al ejecutar consulta: " + e);
        }
        return tipoServicio;
    }
}
