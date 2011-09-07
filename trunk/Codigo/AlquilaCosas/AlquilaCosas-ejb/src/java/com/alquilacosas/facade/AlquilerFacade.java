/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Alquiler;
import com.alquilacosas.ejb.entity.EstadoAlquiler;
import com.alquilacosas.ejb.entity.Publicacion;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
