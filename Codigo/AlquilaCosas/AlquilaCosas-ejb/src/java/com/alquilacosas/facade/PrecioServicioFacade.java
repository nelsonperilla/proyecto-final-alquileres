/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.PrecioServicio;
import com.alquilacosas.ejb.entity.TipoServicio.NombreTipoServicio;
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
public class PrecioServicioFacade extends AbstractFacade<PrecioServicio> {
    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PrecioServicioFacade() {
        super(PrecioServicio.class);
    }
    
    public PrecioServicio getPrecioServicio(NombreTipoServicio servicio) {
        Query query = em.createQuery("SELECT p FROM PrecioServicio p WHERE p.tipoServicioFk.nombre = :servicio");
        query.setParameter("servicio", servicio);
        PrecioServicio precio = null;
        try {
            precio = (PrecioServicio) query.getSingleResult();
        } catch (NoResultException e) {
            
        }
        return precio;
    }
    
}
