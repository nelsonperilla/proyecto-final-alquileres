/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.MotivoDenuncia;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class MotivoDenunciaFacade extends AbstractFacade<MotivoDenuncia> {
    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MotivoDenunciaFacade() {
        super(MotivoDenuncia.class);
    }
    
}
