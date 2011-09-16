/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.dto.CalificacionDTO;
import com.alquilacosas.ejb.entity.Puntuacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wilson
 */
@Local
public interface AlquileresTomadosBeanLocal {

     List<AlquilerDTO> getAlquileresSinCalificarPorUsuario(int usuarioId) throws AlquilaCosasException;

     void registrarCalificacion(Integer puntuacion, Integer alquilerId, String comentario, Integer usuarioId);

     List<Puntuacion> getPuntuaciones();

     List<AlquilerDTO> getAlquileresActivosPorUsuario(int usuarioId) throws AlquilaCosasException ;

     List<AlquilerDTO> getAlquileresConCalificarPorUsuario(int usuarioId) throws AlquilaCosasException;
     
     CalificacionDTO getCalificacionOfrece(Integer alquilerId, Integer usuarioId) throws AlquilaCosasException;
     
     CalificacionDTO getCalificacionToma(Integer alquilerId, Integer usuarioId) throws AlquilaCosasException;
}
