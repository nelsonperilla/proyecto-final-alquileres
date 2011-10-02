/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Alquiler;
import com.alquilacosas.ejb.entity.EstadoPedidoCambio.NombreEstadoPedidoCambio;
import com.alquilacosas.ejb.entity.PedidoCambio;
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
public class PedidoCambioFacade extends AbstractFacade<PedidoCambio> {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PedidoCambioFacade() {
        super(PedidoCambio.class);
    }

    /**
     * Autor: Damian
     * Devuelve el pedido de cambio en estado ENVIADO para un alquiler dado.
     * Si bien podria ser una lista de pedidos, por regla de negocio solo
     * puede haber un solo pedido en estado ENVIADO.
     * @param alquiler
     * @return 
     */
    public PedidoCambio getPedidoEnviado(Alquiler alquiler) {
        Query query = em.createQuery("SELECT p FROM PedidoCambio p, PedidoCambioXEstado pcxe, "
                + "EstadoPedidoCambio e WHERE p = pcxe.pedidoCambioFk AND "
                + "pcxe.estadoPedidoCambioFk = e AND pcxe.fechaHasta IS NULL "
                + "AND e.nombre = :estado AND p.alquilerFk = :alquiler");
        query.setParameter("estado", NombreEstadoPedidoCambio.ENVIADO);
        query.setParameter("alquiler", alquiler);
        PedidoCambio pedido = null;
        try {
            pedido = (PedidoCambio) query.getSingleResult();
        } catch (NoResultException e) {
            Logger.getLogger(PedidoCambioFacade.class).
                    error("getPedidoEnviado(). "
                    + "Excepcion al ejecutar consulta: " + e);
        }
        return pedido;
    }

    public boolean hayPedidoEnviado(Alquiler alquiler) {
        Query query = em.createQuery("SELECT COUNT(p) FROM PedidoCambio p, PedidoCambioXEstado pcxe, "
                + "EstadoPedidoCambio e WHERE p = pcxe.pedidoCambioFk AND "
                + "pcxe.estadoPedidoCambioFk = e AND pcxe.fechaHasta IS NULL "
                + "AND e.nombre = :estado AND p.alquilerFk = :alquiler");
        query.setParameter("estado", NombreEstadoPedidoCambio.ENVIADO);
        query.setParameter("alquiler", alquiler);
        Long count = (Long) query.getSingleResult();
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
}
