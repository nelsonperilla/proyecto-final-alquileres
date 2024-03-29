/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Domicilio;
import com.alquilacosas.ejb.entity.EstadoPublicacion;
import com.alquilacosas.ejb.entity.EstadoPublicacion.NombreEstadoPublicacion;
import com.alquilacosas.ejb.entity.Periodo.NombrePeriodo;
import com.alquilacosas.ejb.entity.Precio;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.PublicacionXEstado;
import com.alquilacosas.ejb.entity.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
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

    public List<Publicacion> getPublicacionesInicio(int filas) {
        String q = "SELECT p FROM Publicacion p, PublicacionXEstado pxe, EstadoPublicacion e "
                + "WHERE pxe.publicacion = p "
                + "AND pxe.estadoPublicacion = e "
                + "AND pxe.fechaHasta IS NULL "
                + "AND e.nombre = :estado ORDER BY p.destacada DESC, p.fechaDesde DESC ";
        TypedQuery<Publicacion> query = em.createQuery(q, Publicacion.class);
        query.setParameter("estado", NombreEstadoPublicacion.ACTIVA);
        query.setMaxResults(filas);
        return query.getResultList();
    }
    
    public List<Publicacion> getPublicacionesDeUsuario(int usuarioId) {
        Query query = em.createQuery("SELECT p FROM Publicacion p WHERE "
                + "p.usuarioFk.usuarioId = :id ORDER BY p.fechaHasta DESC");
        query.setParameter("id", usuarioId);
        return query.getResultList();
    }

    /**
     * Devuelve una lista de publicaciones ordenada por destacacion, y fecha de publicacion descendente
     * de las publicaciones que cumplan con los filtros enviados, y que esten dentro del margen de publicaciones
     * fijado
     * @param palabra String contenido en el titulo o descripcion de la publicacion
     * @param categoriaId Id de la categoria a la que debe pertenecer la publicacion
     * @param ubicacion String contenido en la provincia, ciudad, barrio o calle de
     * la direccion del dueño de la publicacion
     * @param periodoId Id del periodo por el cual se va a buscar precio
     * @param precioDesde Precio minimo que debe tener la publicacion para un periodo determinado
     * @param precioHasta Precio maximo que debe tener la publicacion para un periodo determinado
     * @param registros Cantidad de publicaciones a devolver
     * @param desde A partir de qué publicacion mostrar
     * @return La lista de publicaciones deseadas
     */
    public List<Publicacion> findPublicaciones(String palabra, List<Integer> categoriaIds,
            String ubicacion, NombrePeriodo nombrePeriodo, Double precioDesde, Double precioHasta,
            int registros, int desde) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Publicacion> cq = criteriaBuilder.createQuery(Publicacion.class);

        Metamodel m = em.getMetamodel();
        EntityType<Publicacion> Publicacion_ = m.entity(Publicacion.class);

        Root<Publicacion> root = cq.from(Publicacion_);
        List<Predicate> predicates = new ArrayList<Predicate>();

        if (palabra != null && !palabra.equals("")) {
            palabra = "%" + palabra.toUpperCase() + "%";
            Expression<String> exp1 = criteriaBuilder.upper(root.<String>get("titulo"));
            Expression<String> exp2 = criteriaBuilder.upper(root.<String>get("descripcion"));
            Predicate predicate1 = criteriaBuilder.like(exp1, palabra);
            Predicate predicate2 = criteriaBuilder.like(exp2, palabra);
            Predicate predicate = criteriaBuilder.or(predicate1, predicate2);
            predicates.add(predicate);
        }
        if (categoriaIds != null  && !categoriaIds.isEmpty()) {
            Predicate p = root.get("categoriaFk").get("categoriaId").in(categoriaIds);
            predicates.add(p);
        }
        if (ubicacion != null && !ubicacion.equals("")) {
            Join<Publicacion, Usuario> joinUser = root.join("usuarioFk");
            Join<Usuario, Domicilio> joinDom = joinUser.join("domicilioList");
            Path dom = (Path) joinDom.as(Domicilio.class);
            ubicacion = "%" + ubicacion.toUpperCase() + "%";
            Path exp1 = dom.get("barrio");
            Predicate p1 = criteriaBuilder.like(criteriaBuilder.upper(exp1), ubicacion);
            predicates.add(p1);
        }
        if (nombrePeriodo != null && (precioDesde != null || precioHasta != null)) {
            Join<Publicacion, Precio> joinPrecio = root.join("precioList");
            Path expPeriod = ((Path) joinPrecio.as(Precio.class)).get("periodoFk").get("nombre");
            Path expPrecio = ((Path) joinPrecio.as(Precio.class));
            
            Predicate pred0 = criteriaBuilder.equal(expPeriod, nombrePeriodo);
            Predicate pred1 = criteriaBuilder.isNull(expPrecio.get("fechaHasta"));
            Predicate pred2 = null;
            if (precioDesde != null && precioHasta != null) {
                pred2 = criteriaBuilder.between(expPrecio.get("precio"), precioDesde, precioHasta);
            } else if (precioDesde != null && precioHasta == null) {
                pred2 = criteriaBuilder.greaterThanOrEqualTo(expPrecio.get("precio"), precioDesde);
            } else if (precioDesde == null && precioHasta != null) {
                pred2 = criteriaBuilder.lessThanOrEqualTo(expPrecio.get("precio"), precioHasta);
            }
            predicates.add(pred0);
            predicates.add(pred1);
            predicates.add(pred2);
        }
        // Hacer joins para traer publicaciones en estado activo
        Join<Publicacion, PublicacionXEstado> join = root.join("publicacionXEstadoList");
        Path exp1 = ((Path) join.as(PublicacionXEstado.class)).get("fechaHasta");
        Predicate predicate1 = criteriaBuilder.isNull(exp1);
        Join<PublicacionXEstado, EstadoPublicacion> join2 = join.join("estadoPublicacion");
        Path exp2 = ((Path) join2.as(EstadoPublicacion.class)).get("nombre");
        Predicate predicate2 = criteriaBuilder.equal(exp2, NombreEstadoPublicacion.ACTIVA);

        predicates.add(predicate1);
        predicates.add(predicate2);

        Predicate[] predicateArray = new Predicate[predicates.size()];
        predicates.toArray(predicateArray);
        cq.where(predicateArray);

        cq.orderBy(criteriaBuilder.desc(root.get("destacada")));

        Query query = em.createQuery(cq);
        query.setMaxResults(registros);
        query.setFirstResult(desde);
        return query.getResultList();
    }

    /**
     * Devuelve la cantidad total de publicaciones que cumplan con los filtros enviados
     * 
     * @param palabra String contenido en el titulo o descripcion de la publicacion
     * @param categoriaId Id de la categoria a la que debe pertenecer la publicacion
     * @param ubicacion String contenido en la provincia, ciudad, barrio o calle de
     * la direccion del dueño de la publicacion
     * @param periodoId Id del periodo por el cual se va a buscar precio
     * @param precioDesde Precio minimo que debe tener la publicacion para un periodo determinado
     * @param precioHasta Precio maximo que debe tener la publicacion para un periodo determinado
     * @return La cantidad de publicaciones que cumplen los filtros
     */ 
    public Long countBusquedaPublicaciones(String palabra, List<Integer> categoriaIds,
            String ubicacion, NombrePeriodo nombrePeriodo, Double precioDesde, Double precioHasta) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<Publicacion> root = cq.from(Publicacion.class);
        cq.select(criteriaBuilder.count(root));
        List<Predicate> predicates = new ArrayList<Predicate>();

        if (palabra != null && !palabra.equals("")) {
            palabra = "%" + palabra.toUpperCase() + "%";
            Expression<String> exp1 = criteriaBuilder.upper(root.<String>get("titulo"));
            Expression<String> exp2 = criteriaBuilder.upper(root.<String>get("descripcion"));
            Predicate predicate1 = criteriaBuilder.like(exp1, palabra);
            Predicate predicate2 = criteriaBuilder.like(exp2, palabra);
            Predicate predicate = criteriaBuilder.or(predicate1, predicate2);
            predicates.add(predicate);
        }
        if (categoriaIds != null && !categoriaIds.isEmpty()) {
            
            Predicate p = root.get("categoriaFk").get("categoriaId").in(categoriaIds);
            predicates.add(p);
        }
        if (ubicacion != null && !ubicacion.equals("")) {
            Join<Publicacion, Usuario> joinUser = root.join("usuarioFk");
            Join<Usuario, Domicilio> joinDom = joinUser.join("domicilioList");
            Path dom = (Path) joinDom.as(Domicilio.class);
            ubicacion = "%" + ubicacion.toUpperCase() + "%";
            Path exp1 = dom.get("barrio");
            Predicate p1 = criteriaBuilder.like(criteriaBuilder.upper(exp1), ubicacion);
            predicates.add(p1);
        }
        if (nombrePeriodo != null && (precioDesde != null || precioHasta != null)) {
            Join<Publicacion, Precio> joinPrecio = root.join("precioList");
            Path expPeriod = ((Path) joinPrecio.as(Precio.class)).get("periodoFk").get("nombre");
            Path expPrecio = ((Path) joinPrecio.as(Precio.class));

            Predicate pred0 = criteriaBuilder.equal(expPeriod, nombrePeriodo);
            Predicate pred1 = criteriaBuilder.isNull(expPrecio.get("fechaHasta"));
            Predicate pred2 = null;
            if (precioDesde != null && precioHasta != null) {
                pred2 = criteriaBuilder.between(expPrecio.get("precio"), precioDesde, precioHasta);
            } else if (precioDesde != null && precioHasta == null) {
                pred2 = criteriaBuilder.greaterThanOrEqualTo(expPrecio.get("precio"), precioDesde);
            } else if (precioDesde == null && precioHasta != null) {
                pred2 = criteriaBuilder.lessThanOrEqualTo(expPrecio.get("precio"), precioHasta);
            }
            predicates.add(pred0);
            predicates.add(pred1);
            predicates.add(pred2);
        }

        // Hacer joins para traer publicaciones en estado activo
        Join<Publicacion, PublicacionXEstado> join = root.join("publicacionXEstadoList");
        Path exp1 = ((Path) join.as(PublicacionXEstado.class)).get("fechaHasta");
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

    /**
     * Devuelve una lista de publicaciones  que esten en una de las categorias indicadas
     * @param categoriaIds Lista de los ids de las categorias dentro de las cuales deben estar las publicaciones
     * @param registros Cantidad de registros a devolver
     * @param desde A partir de qué registro
     * @return La lista de publicaciones deseadas
     */
    public List<Publicacion> findByCategoria(List<Integer> categoriaIds, int registros, int desde) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Publicacion> cq = criteriaBuilder.createQuery(Publicacion.class);

        Metamodel m = em.getMetamodel();
        EntityType<Publicacion> Publicacion_ = m.entity(Publicacion.class);

        Root<Publicacion> root = cq.from(Publicacion_);
        List<Predicate> predicates = new ArrayList<Predicate>();

        Join<Publicacion, PublicacionXEstado> join = root.join("publicacionXEstadoList");
        Path exp1 = ((Path) join.as(PublicacionXEstado.class)).get("fechaHasta");
        Predicate predicate1 = criteriaBuilder.isNull(exp1);
        predicates.add(predicate1);

        Join<PublicacionXEstado, EstadoPublicacion> join2 = join.join("estadoPublicacion");
        Path exp2 = ((Path) join2.as(EstadoPublicacion.class)).get("nombre");
        Predicate predicate2 = criteriaBuilder.equal(exp2, NombreEstadoPublicacion.ACTIVA);
        predicates.add(predicate2);

        Path expCat = root.get("categoriaFk");
        List<Predicate> orPredicates = new ArrayList<Predicate>();

        for (Integer i : categoriaIds) {
            Predicate p = criteriaBuilder.equal(expCat.get("categoriaId"), i);
            orPredicates.add(p);
        }
        Predicate[] orPredicateArray = new Predicate[orPredicates.size()];
        orPredicates.toArray(orPredicateArray);

        Predicate orPredicate = criteriaBuilder.or(orPredicateArray);
        predicates.add(orPredicate);

        Predicate[] predicateArray = new Predicate[predicates.size()];
        predicates.toArray(predicateArray);
        cq.where(predicateArray);
        cq.orderBy(criteriaBuilder.desc(root.get("destacada")));
        Query query = em.createQuery(cq);
        query.setMaxResults(registros);
        query.setFirstResult(desde);
        return query.getResultList();

    }

    /**
     * Devuelve la cantidad total de las publicaciones que estan en una de las categorias enviadas
     * @param categoriaIds Lista con el id de las categorias
     * @return La cantidad total de publicaciones que cumplen la condicion
     */
    public Long countByCategoria(List<Integer> categoriaIds) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);

        Metamodel m = em.getMetamodel();
        EntityType<Publicacion> Publicacion_ = m.entity(Publicacion.class);
        Root<Publicacion> root = cq.from(Publicacion_);

        List<Predicate> predicates = new ArrayList<Predicate>();

        Join<Publicacion, PublicacionXEstado> join = root.join("publicacionXEstadoList");
        Path exp1 = ((Path) join.as(PublicacionXEstado.class)).get("fechaHasta");
        Predicate predicate1 = criteriaBuilder.isNull(exp1);
        predicates.add(predicate1);

        Join<PublicacionXEstado, EstadoPublicacion> join2 = join.join("estadoPublicacion");
        Path exp2 = ((Path) join2.as(EstadoPublicacion.class)).get("nombre");
        Predicate predicate2 = criteriaBuilder.equal(exp2, NombreEstadoPublicacion.ACTIVA);
        predicates.add(predicate2);

        Path expCat = root.get("categoriaFk");
        List<Predicate> orPredicates = new ArrayList<Predicate>();

        for (Integer i : categoriaIds) {
            Predicate p = criteriaBuilder.equal(expCat.get("categoriaId"), i);
            orPredicates.add(p);
        }
        Predicate[] orPredicateArray = new Predicate[orPredicates.size()];
        orPredicates.toArray(orPredicateArray);

        Predicate orPredicate = criteriaBuilder.or(orPredicateArray);
        predicates.add(orPredicate);

        Predicate[] predicateArray = new Predicate[predicates.size()];
        predicates.toArray(predicateArray);
        cq.where(predicateArray);
        cq.select(criteriaBuilder.count(root));
        return em.createQuery(cq).getSingleResult();
    }
}
