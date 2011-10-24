/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.PagoDTO;
import com.alquilacosas.ejb.entity.Destacacion;
import com.alquilacosas.ejb.entity.Pago;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.facade.UsuarioFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class MisPagosBean implements MisPagosBeanLocal {

    @EJB
    private UsuarioFacade usuarioFacade;
    
    @Override
    public List<PagoDTO> getMisPagos(int usuarioId) {
        
        Usuario usuario = usuarioFacade.find(usuarioId);
        List<Pago> pagos = usuario.getPagoList();
        List<PagoDTO> dtos = new ArrayList<PagoDTO>();
        for(Pago p: pagos) {
            PagoDTO.EstadoPago estado = (p.getFechaConfirmado() == null) ? 
                    PagoDTO.EstadoPago.INICIADO : PagoDTO.EstadoPago.CONFIRMADO;
            PagoDTO.TipoServicio tipo = null;
            if(p.getServicioFk() instanceof Destacacion) {
                tipo = PagoDTO.TipoServicio.DESTACACION;
            } else {
                tipo = PagoDTO.TipoServicio.PUBLICIDAD;
            }
            PagoDTO dto = new PagoDTO(p.getPagoId(), p.getFechaInicio(), 
                    p.getFechaConfirmado(), p.getMonto(), p.getTipoPagoFk().getNombre(), 
                    estado, tipo);
            dtos.add(dto);
        }
        return dtos;
    }
    
}
