/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Alquiler;
import com.alquilacosas.ejb.entity.AlquilerXEstado;
import com.alquilacosas.ejb.entity.Alquiler_;
import com.alquilacosas.ejb.entity.EstadoAlquiler;
import com.alquilacosas.ejb.entity.EstadoAlquiler.NombreEstadoAlquiler;
import com.alquilacosas.ejb.entity.Publicacion;
import java.util.ArrayList;
import com.alquilacosas.ejb.entity.Usuario;
import java.util.Calendar;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
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
public class AlquilerFacade extends AbstractFacade<Alquiler> {
    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public AlquilerFacade() {
        super(Alquiler.class);
    }
    

    /** Trae todos los alquileres que han sido confirmados o estan activos
     * para una publicacion en paricular, eliminando de la lista aquellos que
     * han finalizado antes de la fecha actual.
     * @param publicacionId
     * @return 
     */
    public List<Alquiler> getAlquileresByPublicacionFromToday(int publicationId)
    {
        //List<Alquiler> respuesta = new ArrayList<Alquiler>();
//        Publicacion filter = em.find(Publicacion.class, publicationId);
//        Query query = em.createNamedQuery("Alquiler.findAlquileresByPublicacionFromToday");
//        query.setParameter("publicacion", filter);
//        query.setParameter("estadoConfirmado", NombreEstadoAlquiler.CONFIRMADO);
//        query.setParameter("estadoActivo", NombreEstadoAlquiler.ACTIVO);
//        respuesta = query.getResultList();        
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Alquiler> queryBuilder =  criteriaBuilder.createQuery(Alquiler.class);
        
        Metamodel m = em.getMetamodel();
        EntityType<Alquiler> alquiler = m.entity(Alquiler.class);
        
        Root<Alquiler> root = queryBuilder.from(alquiler);
        
        Join<Alquiler, AlquilerXEstado> join = root.join("alquilerXEstadoList");
        Path endDate = ((Path)join.as(AlquilerXEstado.class)).get("fechaHasta");
        Predicate lastState = criteriaBuilder.isNull(endDate);
        
        Join<AlquilerXEstado, EstadoAlquiler> join2 = join.join("estadoAlquilerFk");
        Path stateName = ((Path) join2.as(EstadoAlquiler.class)).get("nombre");
        Predicate confirmState = criteriaBuilder.equal(stateName, NombreEstadoAlquiler.CONFIRMADO);
        Predicate activeState = criteriaBuilder.equal(stateName, NombreEstadoAlquiler.ACTIVO);
        Predicate orStates = criteriaBuilder.or(confirmState,activeState);
        
        Calendar date = Calendar.getInstance();
        date.setTime(new Date());
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);

        Predicate endAlquiler = criteriaBuilder.greaterThanOrEqualTo(root.get(Alquiler_.fechaFin),date.getTime()); 
        
        queryBuilder.where(endAlquiler, orStates);
        Query query = em.createQuery(queryBuilder);

        
        return query.getResultList();        
        
    }
    public List<Alquiler> getAlquilerPorPeriodo( Date fechaInicio, Date fechaFin, Integer idAlqConfirmado ){
        
        List<Alquiler> alquileres = null;
        String id = String.valueOf(idAlqConfirmado);
        java.sql.Date fechaDesde = new java.sql.Date(fechaInicio.getTime());
        java.sql.Date fechaHasta = new java.sql.Date(fechaFin.getTime());
        
        Query query = em.createNativeQuery(" SELECT * FROM Alquiler a, "
                + "Alquiler_X_Estado axe, Estado_Alquiler ea "
                + "WHERE ((a.fecha_Inicio <= '"+ fechaDesde +"' AND a.fecha_Fin >= '"+ fechaHasta +"') "
                + "OR ( a.fecha_Inicio <=  '"+ fechaHasta +"'  AND a.fecha_Fin >= '"+ fechaHasta +"' ) "
                + "OR ( a.fecha_Inicio >= '"+ fechaDesde +"' AND a.fecha_Fin <= '"+ fechaHasta +"' )) "
                + "AND a.alquiler_Id = axe.alquiler_FK "
                + "AND axe.estado_Alquiler_FK = ea.estado_Alquiler_Id " 
                + "AND axe.fecha_hasta IS NULL AND ea.nombre = 'PEDIDO' "
                + "AND a.alquiler_id <> " + id +" ", Alquiler.class);

        alquileres = query.getResultList();
        
        return alquileres;
    }
    
    public List<Alquiler> getAlquileresPorPublicacion( Publicacion p, EstadoAlquiler.NombreEstadoAlquiler estado ){
        
            Query query = em.createQuery("SELECT a FROM Alquiler a, AlquilerXEstado axe, EstadoAlquiler ea "
                + "where a.alquilerId = axe.alquilerFk.alquilerId "
                + "AND axe.estadoAlquilerFk.estadoAlquilerId = ea.estadoAlquilerId "
                + "AND ea.nombre = :estado "
                + "AND a.publicacionFk = :publicacion "
                + "AND axe.fechaHasta IS NULL");
        query.setParameter("estado", estado);
        query.setParameter("publicacion", p);
        
        return query.getResultList();
    }
    
    public List<Alquiler> getAlquileresConfirmadosActivos(){
        List<Alquiler> alquileres = null;
        Query query = em.createQuery("SELECT a FROM Alquiler a, AlquilerXEstado axe, EstadoAlquiler ea "
                + "WHERE a.alquilerId = axe.alquilerFk.alquilerId "
                + "AND axe.estadoAlquilerFk.estadoAlquilerId = ea.estadoAlquilerId "
                + "AND ( ea.nombre = :estado1 OR ea.nombre = :estado2 ) ");
        query.setParameter("estado1", EstadoAlquiler.NombreEstadoAlquiler.CONFIRMADO);
        query.setParameter("estado2", EstadoAlquiler.NombreEstadoAlquiler.ACTIVO);
        alquileres = query.getResultList();
        return alquileres;
    }
    
    public List<Alquiler> getAlquileresPorUsuario( Usuario usuario ){
        List<Alquiler> alquileres = null;
        Query query = em.createQuery("SELECT a FROM Alquiler a, AlquilerXEstado axe, EstadoAlquiler ea "
                + "WHERE a.alquilerId = axe.alquilerFk.alquilerId "
                + "AND axe.estadoAlquilerFk.estadoAlquilerId = ea.estadoAlquilerId "
                + "AND axe.fechaHasta IS NULL "
                + "AND ea.nombre = :estado "
                + "AND a.usuarioFk = :usuario");
        query.setParameter("usuario", usuario);
        query.setParameter("estado", EstadoAlquiler.NombreEstadoAlquiler.PEDIDO);
        alquileres = query.getResultList();
        return alquileres;
    }
}
