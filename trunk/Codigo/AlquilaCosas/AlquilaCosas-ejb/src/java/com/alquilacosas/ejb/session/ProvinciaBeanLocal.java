/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.ejb.entity.Pais;
import com.alquilacosas.ejb.entity.Provincia;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wilson
 */
@Local
public interface ProvinciaBeanLocal {
     List<Provincia> getProvincias();
     
     void modificarProvincia(Provincia provinciaNuevo);

     void borrarProvincia(Provincia provinciaBorrar) throws AlquilaCosasException ;

     void registrarProvincia(String nombre, Pais pais) throws AlquilaCosasException ;

     List<Pais> getPaises();

     List<Provincia> getProvinciaByPais(int idPais);

    public List<String> getCiudades();
}
