/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.common.SeguridadException;
import com.alquilacosas.common.UsuarioLogueado;
import javax.ejb.Local;

/**
 *
 * @author damiancardozo
 */
@Local
public interface LoginBeanLocal {

    boolean activarCuenta(String usuario, String codigo) throws AlquilaCosasException;
    
    public Integer loginUsuario(String username) throws AlquilaCosasException;

    public void cambiarPassword(int usuarioId, java.lang.String password, java.lang.String passwordNuevo) throws com.alquilacosas.common.AlquilaCosasException;

    public UsuarioLogueado login(java.lang.String username, java.lang.String password) throws SeguridadException;

    public UsuarioLogueado facebookLogin(java.lang.String email) throws com.alquilacosas.common.SeguridadException;
    
}
