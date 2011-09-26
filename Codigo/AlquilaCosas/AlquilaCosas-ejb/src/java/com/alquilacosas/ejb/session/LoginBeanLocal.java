/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import javax.ejb.Local;

/**
 *
 * @author damiancardozo
 */
@Local
public interface LoginBeanLocal {

    boolean activarCuenta(String usuario, String codigo) throws AlquilaCosasException;
    
    Integer loginUsuario(String username) throws AlquilaCosasException;

    public void cambiarPassword(int usuarioId, java.lang.String password, java.lang.String passwordNuevo) throws com.alquilacosas.common.AlquilaCosasException;
    
}
