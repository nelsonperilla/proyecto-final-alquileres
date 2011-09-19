/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.EstadoPedidoCambio;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class EstadoPedidoCambioFacade extends AbstractFacade<EstadoPedidoCambio> {
    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstadoPedidoCambioFacade() {
        super(EstadoPedidoCambio.class);
    }
    
    public EstadoPedidoCambio findByNombre( EstadoPedidoCambio.NombreEstadoPedidoCambio nombre ){
        EstadoPedidoCambio estadoPedido = null;
        Query query = em.createQuery("SELECT e FROM EstadoPedidoCambio e WHERE e.nombre = :nombre");
        query.setParameter("nombre", nombre);
        estadoPedido = (EstadoPedidoCambio) query.getSingleResult();
        return estadoPedido;
    }
    
}
