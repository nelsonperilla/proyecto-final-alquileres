/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.EstadoDenuncia;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class EstadoDenunciaFacade extends AbstractFacade<EstadoDenuncia> {
    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstadoDenunciaFacade() {
        super(EstadoDenuncia.class);
    }
    
}
