/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.AlquilerDTO;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author damiancardozo
 */
@Local
public interface AlquileresBeanLocal {

    public List<AlquilerDTO> getAlquileres(int usuarioId);

    public List<AlquilerDTO> getPedidos(int usuarioId);
    
}
