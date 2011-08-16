/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.ejb.entity.Pais;
import com.alquilacosas.ejb.entity.Provincia;
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
public class ProvinciaBean implements ProvinciaBeanLocal {

     @PersistenceContext(unitName="AlquilaCosas-ejbPU") 
     private EntityManager entityManager;

     @Override
     public List<Provincia> getProvincias() {
          List<Provincia> provincias;
          Query query = entityManager.createNamedQuery("Provincia.findAll");
          provincias = query.getResultList();
          return provincias;
     }
     
     // Add business logic below. (Right-click in editor and choose
     // "Insert Code > Add Business Method")
     
     @Override
     public void modificarProvincia(Provincia provinciaNuevo) {
          Provincia modifProvincia = entityManager.find(Provincia.class, provinciaNuevo.getProvinciaId());
          modifProvincia.setNombre(provinciaNuevo.getNombre());
          modifProvincia.setPaisFk(provinciaNuevo.getPaisFk());
          entityManager.merge(modifProvincia);
     }

     @Override
     public void borrarProvincia(Provincia provincia) throws AlquilaCosasException {
          Provincia borrarProvincia = entityManager.find(Provincia.class, provincia.getProvinciaId());
          if (borrarProvincia.getDomicilioList().isEmpty())
               entityManager.remove(borrarProvincia);               
          else
               throw new AlquilaCosasException("El Provincia tiene Domicilios Asociados");
     }

     @Override
     public void registrarProvincia(Provincia nuevoProvincia) throws AlquilaCosasException {
          try{
               entityManager.persist(nuevoProvincia);
          }
          catch(Exception e){
               throw new AlquilaCosasException("Error al insertar la Provincia - " + e.getMessage());
          }
     }

     @Override
     public List<Pais> getPaises() {
          List<Pais> paises;
          Query query = entityManager.createNamedQuery("Pais.findAll");
          paises = query.getResultList();
          return paises;
     }
     
}
