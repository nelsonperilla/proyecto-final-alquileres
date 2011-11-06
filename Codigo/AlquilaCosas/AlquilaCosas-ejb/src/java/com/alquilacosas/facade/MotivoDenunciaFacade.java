/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.MotivoDenuncia;
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

    public List<MotivoDenuncia> getAllMotivosDenuncia() {
        Query query = em.createNamedQuery("MotivoDenuncia.findAll");
        return query.getResultList();   
    }

    public List<MotivoDenuncia> getMotivosDenunciaPublicacion() {
        Query query = em.createNamedQuery("MotivoDenuncia.findByTipo");
        query.setParameter("motivoPublicacion", true);
        return query.getResultList();   
    }

    public List<MotivoDenuncia> getMotivosDenunciaComentario() {
        Query query = em.createNamedQuery("MotivoDenuncia.findByTipo");
        query.setParameter("motivoPublicacion", false);
        return query.getResultList();   
    }
    
    
}
