/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Pago;
import com.alquilacosas.ejb.entity.TipoPago;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class PagoFacade extends AbstractFacade<Pago> {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PagoFacade() {
        super(Pago.class);
    }
    
    /**
     * Devuelve todos los pagos efectuados por un usuario
     * @param usuarioId
     * @return 
     */
    public List<Pago> getPagosPorUsuario(int usuarioId) {
        Query query = em.createQuery("SELECT p FROM Pago p WHERE "
                + "p.servicioFk.usuarioFk.usuarioId = :id");
        query.setParameter("id", usuarioId);
        return query.getResultList();
    }
    
    public List<Pago> getPagosRecibidos(TipoPago pago, Date fechaDesde, Boolean confirmado, 
            int registros, int desde) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Pago> cq = criteriaBuilder.createQuery(Pago.class);
        
        Metamodel m = em.getMetamodel();
        EntityType<Pago> Pago_ = m.entity(Pago.class);
        
        Root<Pago> root = cq.from(Pago_);
        List<Predicate> predicates = new ArrayList<Predicate>();
        
        if(pago != null) {
            Join<Pago, TipoPago> join = root.join("tipoPagoFk");
            Path exp = (Path) join.as(TipoPago.class);
            Predicate p1 = criteriaBuilder.equal(exp, pago);
            predicates.add(p1);
        }
        if(fechaDesde != null) {
            Path exp = root.get("fechaInicio");
            Predicate p1 = criteriaBuilder.greaterThanOrEqualTo(exp, fechaDesde);
            predicates.add(p1);
        }
        if(confirmado != null) {
            Path exp = root.get("fechaConfirmado");
            Predicate p1 = null;
            if(confirmado) {
                p1 = criteriaBuilder.isNotNull(exp);
            } else {
                p1 = criteriaBuilder.isNull(exp);
            }
            predicates.add(p1);
        }
        
        Predicate[] predicateArray = new Predicate[predicates.size()];
        predicates.toArray(predicateArray);
        cq.where(predicateArray);
        
        cq.orderBy(criteriaBuilder.desc(root.get("fechaInicio")));
        
        Query query = em.createQuery(cq);
        query.setMaxResults(registros);
        query.setFirstResult(desde);
        return query.getResultList();        
    }
    
    public Long countPagosRecibidos(TipoPago pago, Date fechaDesde, Boolean confirmado) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        
        Metamodel m = em.getMetamodel();
        EntityType<Pago> Pago_ = m.entity(Pago.class);
        
        Root<Pago> root = cq.from(Pago_);
        cq.select(criteriaBuilder.count(root));
        
        List<Predicate> predicates = new ArrayList<Predicate>();
        
        if(pago != null) {
            Join<Pago, TipoPago> join = root.join("tipoPagoFk");
            Path exp = (Path) join.as(TipoPago.class);
            Predicate p1 = criteriaBuilder.equal(exp, pago);
            predicates.add(p1);
        }
        if(fechaDesde != null) {
            Path exp = root.get("fechaInicio");
            Predicate p1 = criteriaBuilder.greaterThanOrEqualTo(exp, fechaDesde);
            predicates.add(p1);
        }
        if(confirmado != null) {
            Path exp = root.get("fechaConfirmado");
            Predicate p1 = null;
            if(confirmado) {
                p1 = criteriaBuilder.isNotNull(exp);
            } else {
                p1 = criteriaBuilder.isNull(exp);
            }
            predicates.add(p1);
        }
        
        Predicate[] predicateArray = new Predicate[predicates.size()];
        predicates.toArray(predicateArray);
        cq.where(predicateArray);
        
        cq.orderBy(criteriaBuilder.desc(root.get("fechaInicio")));
        
        return em.createQuery(cq).getSingleResult();        
    }
    
}
