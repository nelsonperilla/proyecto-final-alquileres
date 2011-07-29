/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.ejb.entity.Domicilio;
import com.alquilacosas.ejb.entity.Pais;
import com.alquilacosas.ejb.entity.Provincia;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author damiancardozo
 */
@Local
public interface RegistrarUsuarioBeanLocal {
    
    public void registrarUsuario(String username, String password, String nombre,
            String apellido, List<Domicilio> domicilios, int provincia, 
            Date fechaNacimiento, String dni, String telefono, String email);

    List<Provincia> getProvincias(int pais);

    List<Pais> getPaises();
    
}
