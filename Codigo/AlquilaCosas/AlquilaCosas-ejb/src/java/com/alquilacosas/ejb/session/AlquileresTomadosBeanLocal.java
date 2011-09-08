/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.ejb.entity.Alquiler;
import com.alquilacosas.ejb.entity.Calificacion;
import com.alquilacosas.ejb.entity.Puntuacion;
import com.alquilacosas.ejb.entity.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wilson
 */
@Local
public interface AlquileresTomadosBeanLocal {

     List<AlquilerDTO> getAlquileresSinCalificarPorUsuario(int usuarioId) throws AlquilaCosasException;

     Alquiler getAlquilerPorId(int alquilerId);

     Usuario getUsuarioPorId(int usuarioId);

     void registrarCalificacion(Calificacion nuevaCalificacion);

     List<Puntuacion> getPuntuaciones();

     Puntuacion getPuntuacionesPorId(int puntuacionId);
     
}
