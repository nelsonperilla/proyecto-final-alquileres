/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.AlquilerDTO;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ignaciogiagante
 */
@Local
public interface PedidosBeanLocal {
    
    public void confirmarPedidoDeAlquiler( int alquilerId )throws AlquilaCosasException;
    
    public void cancelarPedidoDeAlquiler( Integer alquilerId )throws AlquilaCosasException;
    
    public List<AlquilerDTO> getPedidosRecibidos( int usuarioDuenioId );

    public void rechazarPedidoDeAlquiler(Integer alquilerId)throws AlquilaCosasException;

    public List<AlquilerDTO> getPedidosRealizados(Integer usuarioId);
}
