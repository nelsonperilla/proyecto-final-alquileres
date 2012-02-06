/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.PagoDTO;
import com.alquilacosas.ejb.entity.TipoPago;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author damiancardozo
 */
@Local
public interface PagosRecibidosBeanLocal {

    public List<PagoDTO> getPagosRecibidos(TipoPago.NombreTipoPago tipoPago, Date fechaDesde, 
            Boolean confirmado, int registros, int desde);

    public java.lang.Long getCantidadPagos(TipoPago.NombreTipoPago tipoPago, 
            Date fechaDesde, Boolean confirmado);

    @javax.annotation.security.RolesAllowed(value = {"USUARIO", "ADMIN"})
    public void confirmarPago(java.lang.Integer pagoId);

    public void eliminarPago(int pagoId);
    
}
