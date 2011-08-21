/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.PrecioFacade;
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
public class PrecioBean implements PrecioBeanLocal {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager entityManager;
    
    @Override
    public List<PrecioFacade> getPrecios(Publicacion publicacion) {
        List<PrecioFacade> resultado = new ArrayList<PrecioFacade>();
        List<Precio> precios;
        Query query = entityManager.createNamedQuery("Precio.findByPublicacion");
        query.setParameter("publicacion", publicacion);
        precios = query.getResultList();

        for (Precio precio : precios) {
            resultado.add(new PrecioFacade(precio.getPrecio(), precio.getPeriodoFk().getNombre()));
        }

        return resultado;
    }
    
}
