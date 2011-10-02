/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.TipoPago;
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
public class TipoPagoFacade extends AbstractFacade<TipoPago> {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoPagoFacade() {
        super(TipoPago.class);
    }

    public TipoPago findByNombre(TipoPago.NombreTipoPago nombre) {
        Query query = em.createNamedQuery("TipoPago.findByNombre");
        query.setParameter("nombre", nombre);
        TipoPago tipoPago = null;
        try {
            tipoPago = (TipoPago) query.getSingleResult();
        } catch (NoResultException e) {
            Logger.getLogger(TipoPagoFacade.class).
                    error("findByNombre(). "
                    + "Excepcion al ejecutar consulta: " + e);
        }
        return tipoPago;
    }
}
