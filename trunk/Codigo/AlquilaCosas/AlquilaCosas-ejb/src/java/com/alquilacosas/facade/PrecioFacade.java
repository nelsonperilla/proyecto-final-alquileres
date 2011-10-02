/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Precio;
import com.alquilacosas.ejb.entity.Publicacion;
import java.util.ArrayList;
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
public class PrecioFacade extends AbstractFacade<Precio> {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PrecioFacade() {
        super(Precio.class);
    }

    public List<Precio> getUltimoPrecios(Publicacion publicacion) {
        List<Precio> precios = new ArrayList<Precio>();
        Query query = em.createNamedQuery("Precio.findByPublicacion");
        query.setParameter("publicacion", publicacion);
        precios = query.getResultList();
        return precios;
    }

    public List<Precio> findByPublicacion(Publicacion publicacion) {
        Query query = em.createNamedQuery("Precio.findByPublicacion");
        query.setParameter("publicacion", publicacion);
        return query.getResultList();
    }
}
