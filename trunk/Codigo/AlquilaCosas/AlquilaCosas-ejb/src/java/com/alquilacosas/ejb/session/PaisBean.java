/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.ejb.entity.Pais;
import com.alquilacosas.facade.PaisFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author wilson
 */
@Stateless
public class PaisBean implements PaisBeanLocal {
     
     @PersistenceContext(unitName="AlquilaCosas-ejbPU") 
     private EntityManager entityManager;
     
     @EJB
     private PaisFacade paisFacade;

     @Override
     public List<Pais> getPaises() {
          return paisFacade.findAll();
     }
     
     @Override
     public void modificarPais(Pais paisNuevo) {
          Pais modifPais = paisFacade.find(paisNuevo.getPaisId());
          modifPais.setNombre(paisNuevo.getNombre());
          paisFacade.edit(modifPais);
     }

     @Override
     public void borrarPais(Pais Pais) throws AlquilaCosasException {
          Pais borrarPais = entityManager.find(Pais.class, Pais.getPaisId());
          if (borrarPais.getProvinciaList().isEmpty())
              paisFacade.remove(borrarPais); 
          else
               throw new AlquilaCosasException("El Pais tiene Precios Asociados");
     }

     @Override
     public void registrarPais(Pais nuevoPais) throws AlquilaCosasException {
          try{
              paisFacade.create(nuevoPais); 
          }
          catch(Exception e){
               throw new AlquilaCosasException("Error al insertar el Pais - " + e.getMessage());
          }
     }
}
