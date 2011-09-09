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
public interface AlquileresOfrecidosBeanLocal {

    public java.util.List<com.alquilacosas.dto.AlquilerDTO> getAlquileres(int usuarioId);

    public java.util.List<com.alquilacosas.ejb.entity.Puntuacion> getPuntuaciones();

    public void registrarCalificacion(int usuarioId, int alquilerId, int puntuacionId, java.lang.String comentario);
    
}
