/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.DomicilioDTO;
import com.alquilacosas.dto.UsuarioDTO;
import com.alquilacosas.ejb.entity.Pais;
import com.alquilacosas.ejb.entity.Provincia;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author damiancardozo
 */
@Local
public interface ModificarUsuarioBeanLocal {
    
    public UsuarioDTO actualizarUsuario(int idUsuario, String telefono, Date fechaNacimiento, 
             DomicilioDTO dom) throws AlquilaCosasException;
    
    public UsuarioDTO getDatosUsuario(Integer id);
    
    public List<Pais> getPaises();
    
    public List<Provincia> getProvincias(int paisId);

    public void agregarDomicilio(Integer usuarioId, DomicilioDTO dom);
    
}
