/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.ejb.entity.Periodo.NombrePeriodo;
import com.alquilacosas.ejb.entity.Puntuacion;
import javax.ejb.Local;

/**
 *
 * @author damiancardozo
 */
@Local
public interface AlquileresOfrecidosBeanLocal {

    public java.util.List<AlquilerDTO> getAlquileresVigentes(int usuarioId);

    public java.util.List<Puntuacion> getPuntuaciones();

    public void registrarCalificacion(int usuarioId, int alquilerId, int puntuacionId, java.lang.String comentario);

    public java.util.List<AlquilerDTO> getAlquileresSinCalificar(int usuarioId);

    public java.util.List<AlquilerDTO> getAlquileresCalificados(int usuarioId);

    public AlquilerDTO modificarAlquiler(AlquilerDTO alquilerDTO, NombrePeriodo periodo, 
            int duracion) throws AlquilaCosasException;

    public boolean cancelarAlquiler(int alquilerId) throws AlquilaCosasException;
    
}
