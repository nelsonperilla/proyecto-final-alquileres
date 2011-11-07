/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.PagoDTO;
import com.alquilacosas.ejb.entity.Destacacion;
import com.alquilacosas.ejb.entity.Pago;
import com.alquilacosas.ejb.entity.TipoPago;
import com.alquilacosas.ejb.entity.TipoPago.NombreTipoPago;
import com.alquilacosas.facade.PagoFacade;
import com.alquilacosas.facade.TipoPagoFacade;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class PagosRecibidosBean implements PagosRecibidosBeanLocal {

    @EJB
    private PagoFacade pagoFacade;
    @EJB
    private TipoPagoFacade tipoPagoFacade;
    
    @Override
    public List<PagoDTO> getPagosRecibidos(NombreTipoPago tipoPago, Date fechaDesde, 
            Boolean confirmado, int registros, int desde) {
        TipoPago tipo = null;
        if(tipoPago != null) {
            tipo = tipoPagoFacade.findByNombre(tipoPago);
        }
        List<Pago> pagos = pagoFacade.getPagosRecibidos(tipo, fechaDesde, confirmado, registros, desde);
        List<PagoDTO> dtos = new ArrayList<PagoDTO>();
        for(Pago p: pagos) {
            PagoDTO.EstadoPago estado = (p.getFechaConfirmado() == null) ? 
                    PagoDTO.EstadoPago.INICIADO : PagoDTO.EstadoPago.CONFIRMADO;
            PagoDTO.TipoServicio t = null;
            if(p.getServicioFk() instanceof Destacacion) {
                t = PagoDTO.TipoServicio.DESTACACION;
            } else {
                t = PagoDTO.TipoServicio.PUBLICIDAD;
            }
            String usuario = p.getUsuarioFk().getNombre() + " " + p.getUsuarioFk().getApellido();
            PagoDTO dto = new PagoDTO(p.getPagoId(), usuario, p.getFechaInicio(), 
                    p.getFechaConfirmado(), p.getMonto(), p.getTipoPagoFk().getNombre(), 
                    estado, t);
            dtos.add(dto);
        }
        return dtos;
    }
    
    @Override
    public Long getCantidadPagos(NombreTipoPago tipoPago, Date fechaDesde, Boolean confirmado) {
        
        TipoPago tipo = null;
        if(tipoPago != null) {
            tipo = tipoPagoFacade.findByNombre(tipoPago);
        }
        return pagoFacade.countPagosRecibidos(null, fechaDesde, confirmado);
    }
    
}
