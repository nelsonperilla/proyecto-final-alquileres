/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Domicilio;
import com.alquilacosas.ejb.entity.EstadoPublicacion;
import com.alquilacosas.ejb.entity.EstadoPublicacion.NombreEstadoPublicacion;
import com.alquilacosas.ejb.entity.Precio;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.PublicacionXEstado;
import com.alquilacosas.ejb.entity.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
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
        
        Metamodel m = em.getMetamodel();
        EntityType<Publicacion> Publicacion_ = m.entity(Publicacion.class);
        
        Root<Publicacion> root = cq.from(Publicacion_);
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
        if(ubicacion != null && !ubicacion.equals("")) {
            Join<Publicacion, Usuario> joinUser = root.join("usuarioFk");
            Join<Usuario, Domicilio> joinDom = joinUser.join("domicilioList");
            Path dom = (Path) joinDom.as(Domicilio.class);
            ubicacion = "%" + ubicacion.toUpperCase() + "%";
            Path exp0 = dom.get("calle");
            Predicate p0 = criteriaBuilder.like(criteriaBuilder.upper(exp0), ubicacion);
            Path exp1 = dom.get("barrio");
            Predicate p1 = criteriaBuilder.like(criteriaBuilder.upper(exp1), ubicacion);
            Path exp2 = dom.get("ciudad");
            Predicate p2 = criteriaBuilder.like(criteriaBuilder.upper(exp2), ubicacion);
            Path exp3 = dom.get("provinciaFk").get("nombre");
            Predicate p3 = criteriaBuilder.like(criteriaBuilder.upper(exp3), ubicacion);
            
            Predicate pred = criteriaBuilder.or(p0, p1, p2, p3);
            predicates.add(pred);
        }
        if(periodoId != null && periodoId != 0 && (precioDesde != null || precioHasta != null)) {
            Join<Publicacion, Precio> joinPrecio = root.join("precioList");
            Path expPeriod = ((Path)joinPrecio.as(Precio.class)).get("periodoFk").get("periodoId");
            Path expPrecio = ((Path)joinPrecio.as(Precio.class)).get("precio");
            
            Predicate pred0 = criteriaBuilder.equal(expPeriod, periodoId);
            Predicate pred1 = criteriaBuilder.isNull(expPrecio.get("fechaHasta"));
            Predicate pred2 = null;
            if(precioDesde != null && precioHasta != null) {
                pred2 = criteriaBuilder.between(expPrecio, precioDesde, precioHasta);
            }
            else if(precioDesde != null && precioHasta == null) {
                pred2 = criteriaBuilder.greaterThanOrEqualTo(expPrecio, precioDesde);
            }
            else if(precioDesde == null && precioHasta != null) {
                pred2 = criteriaBuilder.lessThanOrEqualTo(expPrecio, precioHasta);
            }
            predicates.add(pred0);
            predicates.add(pred1);
            predicates.add(pred2);
        }
        // Hacer joins para traer publicaciones en estado activo
        Join<Publicacion, PublicacionXEstado> join = root.join("publicacionXEstadoList");
        Path exp1 = ((Path)join.as(PublicacionXEstado.class)).get("fechaHasta");
        Predicate predicate1 = criteriaBuilder.isNull(exp1);
        Join<PublicacionXEstado, EstadoPublicacion> join2 = join.join("estadoPublicacion");
        Path exp2 = ((Path) join2.as(EstadoPublicacion.class)).get("nombre");
        Predicate predicate2 = criteriaBuilder.equal(exp2, NombreEstadoPublicacion.ACTIVA);
        
        predicates.add(predicate1);
        predicates.add(predicate2);
        
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
        if(ubicacion != null && !ubicacion.equals("")) {
            Join<Publicacion, Usuario> joinUser = root.join("usuarioFk");
            Join<Usuario, Domicilio> joinDom = joinUser.join("domicilioList");
            Path dom = (Path) joinDom.as(Domicilio.class);
            ubicacion = "%" + ubicacion.toUpperCase() + "%";
            Path exp0 = dom.get("calle");
            Predicate p0 = criteriaBuilder.like(criteriaBuilder.upper(exp0), ubicacion);
            Path exp1 = dom.get("barrio");
            Predicate p1 = criteriaBuilder.like(criteriaBuilder.upper(exp1), ubicacion);
            Path exp2 = dom.get("ciudad");
            Predicate p2 = criteriaBuilder.like(criteriaBuilder.upper(exp2), ubicacion);
            Path exp3 = dom.get("provinciaFk").get("nombre");
            Predicate p3 = criteriaBuilder.like(criteriaBuilder.upper(exp3), ubicacion);
            
            Predicate pred = criteriaBuilder.or(p0, p1, p2, p3);
            predicates.add(pred);
        }
        if(periodoId != null && periodoId != 0 && (precioDesde != null || precioHasta != null)) {
            Join<Publicacion, Precio> joinPrecio = root.join("precioList");
            Path expPeriod = ((Path)joinPrecio.as(Precio.class)).get("periodoFk").get("periodoId");
            Path expPrecio = ((Path)joinPrecio.as(Precio.class)).get("precio");
            
            Predicate pred0 = criteriaBuilder.equal(expPeriod, periodoId);
            Predicate pred1 = criteriaBuilder.isNull(expPrecio.get("fechaHasta"));
            Predicate pred2 = null;
            if(precioDesde != null && precioHasta != null) {
                pred2 = criteriaBuilder.between(expPrecio, precioDesde, precioHasta);
            }
            else if(precioDesde != null && precioHasta == null) {
                pred2 = criteriaBuilder.greaterThanOrEqualTo(expPrecio, precioDesde);
            }
            else if(precioDesde == null && precioHasta != null) {
                pred2 = criteriaBuilder.lessThanOrEqualTo(expPrecio, precioHasta);
            }
            predicates.add(pred0);
            predicates.add(pred1);
            predicates.add(pred2);
        }
        
        // Hacer joins para traer publicaciones en estado activo
        Join<Publicacion, PublicacionXEstado> join = root.join("publicacionXEstadoList");
        Path exp1 = ((Path)join.as(PublicacionXEstado.class)).get("fechaHasta");
        Predicate predicate1 = criteriaBuilder.isNull(exp1);
        Join<PublicacionXEstado, EstadoPublicacion> join2 = join.join("estadoPublicacion");
        Path exp2 = ((Path) join2.as(EstadoPublicacion.class)).get("nombre");
        Predicate predicate2 = criteriaBuilder.equal(exp2, NombreEstadoPublicacion.ACTIVA);
        
        predicates.add(predicate1);
        predicates.add(predicate2);
        
        Predicate[] predicateArray = new Predicate[predicates.size()];
        predicates.toArray(predicateArray);
        cq.where(predicateArray);
        return em.createQuery(cq).getSingleResult();
    }
    
    public List<Publicacion> findByCategoria(int categoriaId, int registros, int desde) {
        
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Publicacion> cq =  criteriaBuilder.createQuery(Publicacion.class);
        
        Metamodel m = em.getMetamodel();
        EntityType<Publicacion> Publicacion_ = m.entity(Publicacion.class);
        
        Root<Publicacion> root = cq.from(Publicacion_);
        
        Join<Publicacion, PublicacionXEstado> join = root.join("publicacionXEstadoList");
        Path exp1 = ((Path)join.as(PublicacionXEstado.class)).get("fechaHasta");
        Predicate predicate1 = criteriaBuilder.isNull(exp1);
        
        Join<PublicacionXEstado, EstadoPublicacion> join2 = join.join("estadoPublicacion");
        Path exp2 = ((Path) join2.as(EstadoPublicacion.class)).get("nombre");
        Predicate predicate2 = criteriaBuilder.equal(exp2, NombreEstadoPublicacion.ACTIVA);
        
//        Predicate predicate2 = criteriaBuilder.equal(((Path)join.as(EstadoPublicacion.class)).
//                get("estadoPublicacion").get("nombre"), NombreEstadoPublicacion.ACTIVA);
        
        Predicate predicate3 = criteriaBuilder.equal(root.get("categoriaFk").get("categoriaId"), categoriaId);
        
        cq.where(predicate1, predicate2, predicate3);
        Query query = em.createQuery(cq);
        query.setMaxResults(registros);
        query.setFirstResult(desde);
        return query.getResultList();
    }
    
    public Long countByCategoria(int categoriaId) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq =  criteriaBuilder.createQuery(Long.class);
        Root<Publicacion> root = cq.from(Publicacion.class);
        
        Join<Publicacion, PublicacionXEstado> join = root.join("publicacionXEstadoList");
        Path exp1 = ((Path)join.as(PublicacionXEstado.class)).get("fechaHasta");
        Predicate predicate1 = criteriaBuilder.isNull(exp1);
        
        Join<PublicacionXEstado, EstadoPublicacion> join2 = join.join("estadoPublicacion");
        Path exp2 = ((Path) join2.as(EstadoPublicacion.class)).get("nombre");
        Predicate predicate2 = criteriaBuilder.equal(exp2, NombreEstadoPublicacion.ACTIVA);
        
//        Predicate predicate2 = criteriaBuilder.equal(((Path)join.as(EstadoPublicacion.class)).
//                get("estadoPublicacion").get("nombre"), NombreEstadoPublicacion.ACTIVA);        
        Predicate predicate3 = criteriaBuilder.equal(root.get("categoriaFk").get("categoriaId"), categoriaId);
        
        cq.select(criteriaBuilder.count(root));
        cq.where(predicate1, predicate2, predicate3);
        return em.createQuery(cq).getSingleResult();
    }
    
    
}
