/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.UsuarioXEstado;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class UsuarioXEstadoFacade extends AbstractFacade<UsuarioXEstado> {
    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
}

    public UsuarioXEstadoFacade() {
        super(UsuarioXEstado.class);
    }
    
    public UsuarioXEstado findCurrent(int usuarioId) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<UsuarioXEstado> cq = criteriaBuilder.createQuery(UsuarioXEstado.class);
        Root<UsuarioXEstado> usuarioXEstadoRoot = cq.from(UsuarioXEstado.class);
        Predicate predicate1 = criteriaBuilder.equal(usuarioXEstadoRoot.get("usuario").get("usuarioId"), usuarioId);
        Predicate predicate2 = criteriaBuilder.isNull(usuarioXEstadoRoot.get("fechaHasta"));
        cq.where(criteriaBuilder.and(predicate1, predicate2));
        UsuarioXEstado uxe = null;
        try {
            uxe = em.createQuery(cq).getSingleResult();
        } catch(NoResultException e) {
            
        }
        return uxe;
    }
    
}
