/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.CalificacionDTO;
import com.alquilacosas.dto.UsuarioDTO;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wilson
 */
@Local
public interface VerReputacionBeanLocal {

     public UsuarioDTO getUsuarioReputacionPorId(int usuarioId) throws AlquilaCosasException;

     public List<CalificacionDTO> getCalificacionOfrece(int usuarioId);

     public List<CalificacionDTO> getCalificacionToma(int usuarioId);

     public double redondear(double numero);     
}
