/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.ejb.entity.Periodo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wilson
 */
@Stateless
public class PeriodoAlquilerBean implements PeriodoAlquilerBeanLocal {
     @PersistenceContext(unitName="AlquilaCosas-ejbPU") 
     private EntityManager entityManager;

     @Override
     public List<Periodo> getPeriodosAlquiler() {
          List<Periodo> periodos;
          Query query = entityManager.createNamedQuery("Periodo.findAll");
          periodos = query.getResultList();
          return periodos;
     }

     // Add business logic below. (Right-click in editor and choose
     // "Insert Code > Add Business Method")

     @Override
     public void modificarPeriodo(Periodo periodoNuevo) {
          Periodo modifPeriodo = entityManager.find(Periodo.class, periodoNuevo.getPeriodoId());
          modifPeriodo.setNombre(periodoNuevo.getNombre());
          modifPeriodo.setDescripcion(periodoNuevo.getDescripcion());
          modifPeriodo.setHoras(periodoNuevo.getHoras());
          entityManager.merge(modifPeriodo);
     }

     @Override
     public void borrarPeriodo(Periodo periodo) throws AlquilaCosasException {
          Periodo borrarPeriodo = entityManager.find(Periodo.class, periodo.getPeriodoId());
          if (borrarPeriodo.getPrecioList().isEmpty())
               entityManager.remove(borrarPeriodo);               
          else
               throw new AlquilaCosasException("El Periodo tiene Precios Asociados");
     }

     @Override
     public void registrarPeriodo(Periodo nuevoPeriodo) throws AlquilaCosasException {
          try{
               entityManager.persist(nuevoPeriodo);
          }
          catch(Exception e){
               throw new AlquilaCosasException("Error al insertar el Periodo - " + e.getMessage());
          }
     }
}
