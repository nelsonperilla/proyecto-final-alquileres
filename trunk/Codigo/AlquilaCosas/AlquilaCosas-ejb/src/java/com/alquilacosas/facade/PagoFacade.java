/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Pago;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class PagoFacade extends AbstractFacade<Pago> {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PagoFacade() {
        super(Pago.class);
    }
    
    /**
     * Devuelve todos los pagos efectuados por un usuario
     * @param usuarioId
     * @return 
     */
    public List<Pago> getPagosPorUsuario(int usuarioId) {
        Query query = em.createQuery("SELECT p FROM Pago p WHERE "
                + "p.servicioFk.usuarioFk.usuarioId = :id");
        query.setParameter("id", usuarioId);
        return query.getResultList();
    }
    
}
