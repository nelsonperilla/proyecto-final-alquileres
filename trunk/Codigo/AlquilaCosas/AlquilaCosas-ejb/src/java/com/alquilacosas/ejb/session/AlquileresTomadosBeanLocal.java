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

    public List<AlquilerDTO> getAlquileresSinCalificarPorUsuario(int usuarioId);

    public void registrarCalificacion(Integer puntuacion, Integer alquilerId, String comentario, Integer usuarioId);

    public List<Puntuacion> getPuntuaciones();

    public List<AlquilerDTO> getAlquileresActivosPorUsuario(int usuarioId);

    public List<AlquilerDTO> getAlquileresConCalificarPorUsuario(int usuarioId);

    public CalificacionDTO getCalificacionOfrece(Integer alquilerId) throws AlquilaCosasException;

    public CalificacionDTO getCalificacionToma(Integer alquilerId) throws AlquilaCosasException;

    public void registrarReplica(int calificacionId, String comentarioReplica, int usuarioId) throws AlquilaCosasException;

    public boolean cancelarAlquiler(int alquilerId) throws AlquilaCosasException;

    public java.util.List<java.util.Date> getFechasSinStock(int alquilerId);

    public void solicitarCambioAlquiler(int alquilerId, com.alquilacosas.ejb.entity.Periodo.NombrePeriodo periodo, int duracion) throws com.alquilacosas.common.AlquilaCosasException;

    public void cancelarPedidoCambio(int pedidoCambioId);

    public com.alquilacosas.dto.PedidoCambioDTO getPedidoCambio(int pedidoCambioId);
}
