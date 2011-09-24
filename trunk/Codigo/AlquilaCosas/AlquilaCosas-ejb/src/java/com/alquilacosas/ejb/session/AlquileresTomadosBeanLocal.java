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

     List<AlquilerDTO> getAlquileresSinCalificarPorUsuario(int usuarioId);

     void registrarCalificacion(Integer puntuacion, Integer alquilerId, String comentario, Integer usuarioId);

     List<Puntuacion> getPuntuaciones();

     List<AlquilerDTO> getAlquileresActivosPorUsuario(int usuarioId);

     List<AlquilerDTO> getAlquileresConCalificarPorUsuario(int usuarioId);
     
     CalificacionDTO getCalificacionOfrece(Integer alquilerId) throws AlquilaCosasException;
     
     CalificacionDTO getCalificacionToma(Integer alquilerId) throws AlquilaCosasException;

     void registrarReplica(int calificacionId, String comentarioReplica, int usuarioId) throws AlquilaCosasException;

     boolean cancelarAlquiler(int alquilerId) throws AlquilaCosasException;

    public java.util.List<java.util.Date> getFechasSinStock(int alquilerId);

    public void solicitarCambioAlquiler(int alquilerId, com.alquilacosas.ejb.entity.Periodo.NombrePeriodo periodo, int duracion) throws com.alquilacosas.common.AlquilaCosasException;

    public void cancelarPedidoCambio(int pedidoCambioId);

    public com.alquilacosas.dto.PedidoCambioDTO getPedidoCambio(int pedidoCambioId);
}
