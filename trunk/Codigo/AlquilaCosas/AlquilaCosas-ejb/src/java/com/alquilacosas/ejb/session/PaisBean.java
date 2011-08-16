/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.ejb.entity.Pais;
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
public class PaisBean implements PaisBeanLocal {
     
     @PersistenceContext(unitName="AlquilaCosas-ejbPU") 
     private EntityManager entityManager;

     @Override
     public List<Pais> getPaises() {
          List<Pais> paises;
          Query query = entityManager.createNamedQuery("Pais.findAll");
          paises = query.getResultList();
          return paises;
     }

     // Add business logic below. (Right-click in editor and choose
     // "Insert Code > Add Business Method")
     
     @Override
     public void modificarPais(Pais PaisNuevo) {
          Pais modifPais = entityManager.find(Pais.class, PaisNuevo.getPaisId());
          modifPais.setNombre(PaisNuevo.getNombre());
          entityManager.merge(modifPais);
     }

     @Override
     public void borrarPais(Pais Pais) throws AlquilaCosasException {
          Pais borrarPais = entityManager.find(Pais.class, Pais.getPaisId());
          if (borrarPais.getProvinciaList().isEmpty())
               entityManager.remove(borrarPais);               
          else
               throw new AlquilaCosasException("El Pais tiene Precios Asociados");
     }

     @Override
     public void registrarPais(Pais nuevoPais) throws AlquilaCosasException {
          try{
               entityManager.persist(nuevoPais);
          }
          catch(Exception e){
               throw new AlquilaCosasException("Error al insertar el Pais - " + e.getMessage());
          }
     }
}
