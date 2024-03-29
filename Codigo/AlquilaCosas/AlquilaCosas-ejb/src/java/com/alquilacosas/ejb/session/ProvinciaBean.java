/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.DomicilioDTO;
import com.alquilacosas.ejb.entity.Domicilio;
import com.alquilacosas.ejb.entity.Pais;
import com.alquilacosas.ejb.entity.Provincia;
import com.alquilacosas.facade.PaisFacade;
import com.alquilacosas.facade.ProvinciaFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author wilson
 */
@Stateless
public class ProvinciaBean implements ProvinciaBeanLocal {
     
     @EJB
     private ProvinciaFacade provinciaFacade;
     
     @EJB
     private PaisFacade paisFacade;

     @Override
     public List<Provincia> getProvincias() {
          return provinciaFacade.findAll();
     }
     
     @Override
     public void modificarProvincia(Provincia provinciaNuevo) {
          Provincia modifProvincia = provinciaFacade.find(provinciaNuevo.getProvinciaId());
          modifProvincia.setNombre(provinciaNuevo.getNombre());
          modifProvincia.setPaisFk(provinciaNuevo.getPaisFk());
          provinciaFacade.edit(modifProvincia);
     }

     @Override
     public void borrarProvincia(Provincia provincia) throws AlquilaCosasException {
          Provincia borrarProvincia = provinciaFacade.find(provincia.getProvinciaId());
          provinciaFacade.remove(borrarProvincia);               
     }

     @Override
     public void registrarProvincia(String nombre, Pais pais) throws AlquilaCosasException {
          Provincia nuevaProvincia = new  Provincia();
          nuevaProvincia.setNombre(nombre);
          nuevaProvincia.setPaisFk(pais);
          try{
              provinciaFacade.create(nuevaProvincia); 
          }
          catch(Exception e){
               throw new AlquilaCosasException("Error al insertar la Provincia - " + e.getMessage());
          }
     }
     
    @Override
     public List<String> getCiudades() {
         List<String> ciudades = provinciaFacade.getListaCiudades();
         return ciudades;
     }

     @Override
     public List<Pais> getPaises() {
          return paisFacade.findAll();
     }

     @Override
     public List<Provincia> getProvinciaByPais(int idPais) {
          return provinciaFacade.findByPais(idPais);
     }     
}
