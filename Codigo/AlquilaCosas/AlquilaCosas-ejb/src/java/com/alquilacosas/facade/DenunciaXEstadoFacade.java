/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Denuncia;
import com.alquilacosas.ejb.entity.DenunciaXEstado;
import com.alquilacosas.ejb.entity.EstadoDenuncia;
import com.alquilacosas.ejb.entity.EstadoDenuncia.NombreEstadoDenuncia;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class DenunciaXEstadoFacade extends AbstractFacade<DenunciaXEstado> {
    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DenunciaXEstadoFacade() {
        super(DenunciaXEstado.class);
    }

    public void saveState(Denuncia denuncia, NombreEstadoDenuncia nombreEstadoDenuncia) {
        DenunciaXEstado estadoActual = new DenunciaXEstado();
        estadoActual.setDenunciaFk(denuncia);

        Query query = em.createNamedQuery("EstadoDenuncia.findByNombre");
        query.setParameter("nombre", nombreEstadoDenuncia);
        EstadoDenuncia estado = (EstadoDenuncia) query.getSingleResult();
        estadoActual.setEstadoDenunciaFk(estado);
        estadoActual.setFechaDesde(new Date());

        denuncia.agregarDenunciaXEstado(estadoActual);
        em.persist(estadoActual);
    }    
}
