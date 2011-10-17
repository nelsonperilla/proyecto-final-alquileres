/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.PrecioTipoServicio;
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
public class PrecioServicioFacade extends AbstractFacade<PrecioTipoServicio> {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PrecioServicioFacade() {
        super(PrecioTipoServicio.class);
    }

    public PrecioTipoServicio getPrecioServicio(NombreTipoDestacacion servicio) {
        Query query = em.createQuery("SELECT p FROM PrecioServicio p WHERE p.tipoServicioFk.nombre = :servicio");
        query.setParameter("servicio", servicio);
        PrecioTipoServicio precio = null;
        try {
            precio = (PrecioTipoServicio) query.getSingleResult();
        } catch (NoResultException e) {
            Logger.getLogger(PrecioServicioFacade.class).
                    error("getPrecioServicio(). "
                    + "Excepcion al ejecutar consulta: " + e);
        }
        return precio;
    }
}
