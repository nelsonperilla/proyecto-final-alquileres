/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.common.SeguridadException;
import com.alquilacosas.common.UsuarioLogueado;
import com.alquilacosas.ejb.entity.EstadoUsuario;
import com.alquilacosas.ejb.entity.EstadoUsuario.NombreEstadoUsuario;
import com.alquilacosas.ejb.entity.Login;
import com.alquilacosas.ejb.entity.Rol;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.ejb.entity.UsuarioXEstado;
import com.alquilacosas.facade.EstadoUsuarioFacade;
import com.alquilacosas.facade.LoginFacade;
import com.alquilacosas.facade.UsuarioFacade;
import com.alquilacosas.facade.UsuarioXEstadoFacade;
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
public class LoginBean implements LoginBeanLocal {
    
    @EJB
    private LoginFacade loginFacade;
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private UsuarioXEstadoFacade uxeFacade;
    @EJB
    private EstadoUsuarioFacade estadoUsuarioFacade;
    
    @Override
    public boolean activarCuenta(String username, String codigo) throws AlquilaCosasException {
        
        Login login = loginFacade.findByUsername(username);
        
        if(login == null) {
            throw new AlquilaCosasException("Nombre de usuario inexistente.");
        }
        
        String codigoDB = login.getCodigoActivacion();
        
        if(codigo.equals(codigoDB)) {
            
            Usuario usuario = login.getUsuarioFk();
            
            UsuarioXEstado ultimoUxe = uxeFacade.findCurrent(usuario.getUsuarioId());
            if(ultimoUxe == null || ultimoUxe.getEstadoUsuario().getNombre()
                    != NombreEstadoUsuario.REGISTRADO) {
                throw new AlquilaCosasException("El estado del usuario no requiere activación.");
            }
            
            EstadoUsuario estado = estadoUsuarioFacade.findByNombre(NombreEstadoUsuario.ACTIVO);
            if(estado == null) {
                throw new AlquilaCosasException("No se encontro el estado " + 
                        NombreEstadoUsuario.ACTIVO.toString() + " en la base de datos.");
            }

            ultimoUxe.setFechaHasta(new Date());          
            
            UsuarioXEstado nuevoUxE = new UsuarioXEstado(usuario, estado);
            nuevoUxE.setFechaDesde(new Date());
            usuario.agregarUsuarioXEstado(nuevoUxE);
            usuarioFacade.edit(usuario);
            return true;
            
        }
        else {
            return false;
        }
    }
    
    @Override
    public UsuarioLogueado login(String username, String password) throws SeguridadException {
        Login login = loginFacade.login(username, password);
        if(login == null) {
            throw new SeguridadException("Credenciales incorrectas.");
        } else {
            Usuario usuario = login.getUsuarioFk();
            String ciudad = null;
            if(!usuario.getDomicilioList().isEmpty()) {
                ciudad = usuario.getDomicilioList().get(0).getCiudad();
            }
            List<Rol.NombreRol> roles = new ArrayList<Rol.NombreRol>();
            for(Rol r: login.getRolList()) {
                roles.add(r.getNombre());
            }
            return new UsuarioLogueado(usuario.getUsuarioId(), usuario.getNombre(), usuario.getApellido(), ciudad, null, roles);
        }
    }
    
    @Override
    public Integer loginUsuario(String username) throws AlquilaCosasException {
        
        Login login = loginFacade.findByUsername(username);
        if(login == null) {
            throw new AlquilaCosasException("Nombre de usuario inexistente");
        }
        login.setFechaUltimoIngreso(new Date());
        loginFacade.edit(login);
        Usuario usuario = login.getUsuarioFk();
        if(usuario == null)
            throw new AlquilaCosasException("Usuario no asociado a la cuenta.");
        
        return usuario.getUsuarioId();
    }

    @Override
    public void cambiarPassword(int usuarioId, String password, String passwordNuevo) throws AlquilaCosasException {
        Login login = loginFacade.findByUsuarioId(usuarioId);
        if(!login.getPassword().equals(password)) {
            throw new AlquilaCosasException("La contraseña no coincide.");
        }
        
        login.setPassword(passwordNuevo);
        loginFacade.edit(login);
    }
    
}
