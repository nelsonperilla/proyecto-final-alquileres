/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.PrecioTipoServicio;
import com.alquilacosas.ejb.entity.TipoDestacacion.NombreTipoDestacacion;
import com.alquilacosas.ejb.entity.TipoPublicidad;
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
public class PrecioTipoServicioFacade extends AbstractFacade<PrecioTipoServicio> {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PrecioTipoServicioFacade() {
        super(PrecioTipoServicio.class);
    }

    public PrecioTipoServicio getPrecioDestacacion(NombreTipoDestacacion tipo) {
        Query query = em.createQuery("SELECT p FROM PrecioTipoServicio p "
                + "WHERE p.tipoDestacacionFk.nombre = :tipo AND p.fechaHasta IS NULL");
        query.setParameter("tipo", tipo);
        PrecioTipoServicio precio = null;
        try {
            precio = (PrecioTipoServicio) query.getSingleResult();
        } catch (NoResultException e) {
            Logger.getLogger(PrecioTipoServicioFacade.class).
                    error("getPrecioDestacacion(). "
                    + "Excepcion al ejecutar consulta: " + e);
        }
        return precio;
    }
    
    public PrecioTipoServicio getPrecioPublicidad(TipoPublicidad tipo) {
        Query query = em.createQuery("SELECT p FROM PrecioTipoServicio p "
                + "WHERE p.tipoPublicidadFk = :tipo AND p.fechaHasta IS NULL");
        query.setParameter("tipo", tipo);
        PrecioTipoServicio precio = null;
        try {
            precio = (PrecioTipoServicio) query.getSingleResult();
        } catch (NoResultException e) {
            Logger.getLogger(PrecioTipoServicioFacade.class).
                    error("getPrecioPublicidad(). "
                    + "Excepcion al ejecutar consulta: " + e);
        }
        return precio;
    }
}
