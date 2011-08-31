/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.dto.PeriodoDTO;
import com.alquilacosas.ejb.entity.Periodo;
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
public class PeriodoFacade extends AbstractFacade<Periodo> {
    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public PeriodoFacade() {
        super(Periodo.class);
        
    }
    
    public List<PeriodoDTO> getPeriodos() {
        List<Periodo> periodos = null;
        List<PeriodoDTO> periodosDTO = new ArrayList<PeriodoDTO>();
        Query query = em.createNamedQuery("Periodo.findAll");
        periodos = query.getResultList();
        
        for( Periodo p : periodos ){
            PeriodoDTO periodoDto = new PeriodoDTO(p.getPeriodoId(), 
                    p.getNombre());
            periodosDTO.add(periodoDto);
        }
        
        return periodosDTO;
    }
    
    public PeriodoDTO getPeriodo( int periodoId ) {
        
        Periodo p = em.find(Periodo.class, periodoId);
        PeriodoDTO periodoDto = new PeriodoDTO(p.getPeriodoId(), p.getNombre());
        return periodoDto;
        
    }
    
}
