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
public interface MisPagosBeanLocal {

    public java.util.List<com.alquilacosas.dto.PagoDTO> getMisPagos(int usuarioId);
    
}
