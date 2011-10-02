/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.PedidoCambio;
import com.alquilacosas.ejb.entity.PedidoCambioXEstado;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class PedidoCambioXEstadoFacade extends AbstractFacade<PedidoCambioXEstado> {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PedidoCambioXEstadoFacade() {
        super(PedidoCambioXEstado.class);
    }

    public PedidoCambioXEstado getPedidoCambioXEstadoActual(PedidoCambio pedido) {
        Query query = em.createQuery("SELECT pcxe FROM PedidoCambioXEstado pcxe "
                + "WHERE pcxe.pedidoCambioFk = :pedido AND pcxe.fechaHasta IS NULL");
        query.setParameter("pedido", pedido);
        PedidoCambioXEstado pcxe = null;
        try {
            pcxe = (PedidoCambioXEstado) query.getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(PedidoCambioXEstadoFacade.class).
                    error("getPedidoCambioXEstadoActual(). "
                    + "Excepcion al ejecutar consulta: " + e);
        }
        return pcxe;
    }
}
