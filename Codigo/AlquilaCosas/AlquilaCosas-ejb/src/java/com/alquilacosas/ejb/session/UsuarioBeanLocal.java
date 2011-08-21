/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.common.DomicilioFacade;
import com.alquilacosas.common.UsuarioFacade;
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
public interface UsuarioBeanLocal {
    
    UsuarioFacade getDatosUsuario(Integer id);
    
    public void registrarUsuario(String username, String password, String nombre,
            String apellido, DomicilioFacade domicilio, int provincia, 
            Date fechaNacimiento, String dni, String telefono, String email)
            throws AlquilaCosasException;

    List<Provincia> getProvincias(int pais);

    List<Pais> getPaises();

    boolean usernameExistente(String username);

    UsuarioFacade actualizarUsuario(int idUsuario, String telefono, Date fechaNacimiento, 
            DomicilioFacade dom) throws AlquilaCosasException;

    List<UsuarioFacade> getUsuariosList();

    void setRoles(UsuarioFacade usuarioFacade, List<Integer> roles);
    
}
