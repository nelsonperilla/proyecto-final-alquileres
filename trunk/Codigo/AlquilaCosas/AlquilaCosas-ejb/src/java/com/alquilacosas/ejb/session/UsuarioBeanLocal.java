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
public interface UsuarioBeanLocal {
    
    public void registrarUsuario(String username, String password, String nombre,
            String apellido, DomicilioDTO dom, int prov,
            Date fechaNacimiento, String dni, String telefono, String email) 
            throws AlquilaCosasException;
    
    public boolean usernameExistente(String username);
    
    public List<Pais> getPaises();
    
    public List<Provincia> getProvincias(int paisId);

    public void registrarUsuarioConFacebook(String email, String nombre, String apellido) throws AlquilaCosasException;

    public UsuarioDTO getDatosUsuario(Integer id);

    public UsuarioDTO actualizarUsuario(int idUsuario, String telefono, Date fechaNacimiento, DomicilioDTO dom) throws com.alquilacosas.common.AlquilaCosasException;

    public void agregarDomicilio(Integer usuarioId, DomicilioDTO dom);

    public void actualizarDomicilio(Integer usuarioId, DomicilioDTO dom);

    public void actualizarInfoBasica(Integer usuarioId, String nombre, String apellido, String dni, String telefono, Date fechaNacimiento);
    
}
