/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.ejb.entity.Pais;
import com.alquilacosas.ejb.entity.Provincia;
import com.alquilacosas.facade.PaisFacade;
import com.alquilacosas.facade.ProvinciaFacade;
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
          if (borrarProvincia.getDomicilioList().isEmpty())
               provinciaFacade.remove(borrarProvincia);               
          else
               throw new AlquilaCosasException("El Provincia tiene Domicilios Asociados");
     }

     @Override
     public void registrarProvincia(Provincia nuevoProvincia) throws AlquilaCosasException {
          try{
              provinciaFacade.create(nuevoProvincia); 
          }
          catch(Exception e){
               throw new AlquilaCosasException("Error al insertar la Provincia - " + e.getMessage());
          }
     }

     @Override
     public List<Pais> getPaises() {
          return paisFacade.findAll();
     }
     
}
