/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.UsuarioDTO;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author damiancardozo
 */
@Local
public interface AdministrarUsuariosBeanLocal {

    List<UsuarioDTO> getUsuariosList();

    void setRoles(UsuarioDTO usuarioFacade, List<Integer> roles);

    public void registrarPublicitante(int usuarioId);
    
}
