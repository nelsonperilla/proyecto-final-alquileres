/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Publicacion;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class PublicacionFacade extends AbstractFacade<Publicacion> {
    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PublicacionFacade() {
        super(Publicacion.class);
    }
    
    public List<Publicacion> findPublicaciones(String palabra, Integer categoriaId,
            String ubicacion, Integer periodoId, Double precioDesde, Double precioHasta,
            int registros, int desde) {
        
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Publicacion> cq =  criteriaBuilder.createQuery(Publicacion.class);
        Root<Publicacion> root = cq.from(Publicacion.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        
        if(palabra != null && !palabra.equals("")) {
            palabra = "%" + palabra.toUpperCase() + "%";
            Expression<String> exp1 = criteriaBuilder.upper(root.<String>get("titulo"));
            Expression<String> exp2 = criteriaBuilder.upper(root.<String>get("descripcion"));
            Predicate predicate1 = criteriaBuilder.like(exp1, palabra);
            Predicate predicate2 = criteriaBuilder.like(exp2, palabra);
            Predicate predicate = criteriaBuilder.or(predicate1, predicate2);
            predicates.add(predicate);
        }
        if(categoriaId != null && categoriaId != 0) {
            Predicate predicate = criteriaBuilder.equal(root.get("categoriaFk").get("categoriaId"), categoriaId);
            predicates.add(predicate);
        }
        if(periodoId != null && periodoId != 0 && (precioDesde != null || precioHasta != null)) {
            if(precioDesde != null && precioHasta != null) {
                
            }
            else if(precioDesde != null && precioHasta == null) {
                
            }
            else if(precioDesde == null && precioHasta != null) {
                
            }
        }
        
        Predicate[] predicateArray = new Predicate[predicates.size()];
        predicates.toArray(predicateArray);
        cq.where(predicateArray);
        
        Query query = em.createQuery(cq);
        query.setMaxResults(registros);
        query.setFirstResult(desde);
        return query.getResultList();
    }
    
    public Long countBusquedaPublicaciones(String palabra, Integer categoriaId,
            String ubicacion, Integer periodoId, Double precioDesde, Double precioHasta) {
        
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq =  criteriaBuilder.createQuery(Long.class);
        Root<Publicacion> root = cq.from(Publicacion.class);
        cq.select(criteriaBuilder.count(root));
        List<Predicate> predicates = new ArrayList<Predicate>();
        
        if(palabra != null && !palabra.equals("")) {
            palabra = "%" + palabra.toUpperCase() + "%";
            Expression<String> exp1 = criteriaBuilder.upper(root.<String>get("titulo"));
            Expression<String> exp2 = criteriaBuilder.upper(root.<String>get("descripcion"));
            Predicate predicate1 = criteriaBuilder.like(exp1, palabra);
            Predicate predicate2 = criteriaBuilder.like(exp2, palabra);
            Predicate predicate = criteriaBuilder.or(predicate1, predicate2);
            predicates.add(predicate);
        }
        if(categoriaId != null && categoriaId != 0) {
            Predicate predicate = criteriaBuilder.equal(root.get("categoriaFk").get("categoriaId"), categoriaId);
            predicates.add(predicate);
        }
        if(periodoId != null && periodoId != 0 && (precioDesde != null || precioHasta != null)) {
            if(precioDesde != null && precioHasta != null) {
                
            }
            else if(precioDesde != null && precioHasta == null) {
                
            }
            else if(precioDesde == null && precioHasta != null) {
                
            }
        }
        Predicate[] predicateArray = new Predicate[predicates.size()];
        predicates.toArray(predicateArray);
        cq.where(predicateArray);
        return em.createQuery(cq).getSingleResult();
    }
    
    public List<Publicacion> findByCategoria(int categoriaId, int registros, int desde) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Publicacion> cq =  criteriaBuilder.createQuery(Publicacion.class);
        Root<Publicacion> root = cq.from(Publicacion.class);
        cq.where(criteriaBuilder.equal(root.get("categoriaFk").get("categoriaId"), categoriaId));
        Query query = em.createQuery(cq);
        query.setMaxResults(registros);
        query.setFirstResult(desde);
        return query.getResultList();
    }
    
    public Long countByCategoria(int categoriaId) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq =  criteriaBuilder.createQuery(Long.class);
        Root<Publicacion> root = cq.from(Publicacion.class);
        cq.select(criteriaBuilder.count(root));
        cq.where(criteriaBuilder.equal(root.get("categoriaFk").get("categoriaId"), categoriaId));
        return em.createQuery(cq).getSingleResult();
    }
    
}
