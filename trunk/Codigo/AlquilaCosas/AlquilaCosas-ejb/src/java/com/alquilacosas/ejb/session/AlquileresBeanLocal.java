/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.dto.PedidoCambioDTO;
import java.util.Date;
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

    public AlquilerDTO getAlquiler(int usuarioId, int alquilerId);

    public AlquilerDTO modificarAlquiler(AlquilerDTO dto, Date fechaFin, double monto) throws com.alquilacosas.common.AlquilaCosasException;

    public List<Date> getFechasSinStock(int alquilerId);

    public void solicitarCambioAlquiler(int alquilerId, java.util.Date fechaFin) throws com.alquilacosas.common.AlquilaCosasException;

    public com.alquilacosas.dto.CalificacionDTO getCalificacionOfrece(java.lang.Integer alquilerId) throws com.alquilacosas.common.AlquilaCosasException;

    public com.alquilacosas.dto.CalificacionDTO getCalificacionToma(java.lang.Integer alquilerId) throws com.alquilacosas.common.AlquilaCosasException;

    public java.util.List<com.alquilacosas.ejb.entity.Puntuacion> getPuntuaciones();

    public void registrarCalificacion(Integer puntuacionId, Integer alquilerId, String comentario, Integer usuarioId, boolean tomado);

    public void registrarReplica(int calificacionId, String comentarioReplica, int usuarioId);

    public boolean cancelarAlquiler(int alquilerId);

    public PedidoCambioDTO getPedidoCambio(int pedidoCambioId);

    public AlquilerDTO responderPedidoCambio(int pedidoCambioId, AlquilerDTO alquilerDTO, Date fechaHasta, double monto, boolean aceptado) throws com.alquilacosas.common.AlquilaCosasException;
    
}
