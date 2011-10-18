/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import javax.ejb.Local;

/**
 *
 * @author damiancardozo
 */
@Local
public interface PagoBeanLocal {

    @javax.annotation.security.RolesAllowed(value = {"USUARIO", "ADMIN"})
    public void efectuarServicio(java.lang.Integer pagoId);
    
}
