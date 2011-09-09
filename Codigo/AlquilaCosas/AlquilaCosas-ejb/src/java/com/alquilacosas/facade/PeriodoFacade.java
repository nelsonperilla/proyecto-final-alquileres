/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Periodo;
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
public class PeriodoFacade extends AbstractFacade<Periodo> {
    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PeriodoFacade() {
        super(Periodo.class);
        
    }
    
    public List<Periodo> getPeriodos() {
        List<Periodo> periodos = null;
        Query query = em.createNamedQuery("Periodo.findAll");
        periodos = query.getResultList();
        return periodos;
    }
    
    public Periodo getPeriodo( int periodoId ) {
        
        Periodo p = em.find(Periodo.class, periodoId);
        return p;
        
    }
    
    public List<Periodo> getPeriodosOrderByHoras() {
        Query query = em.createNamedQuery("Periodo.findAll");
        return query.getResultList();   
    }
    
}
