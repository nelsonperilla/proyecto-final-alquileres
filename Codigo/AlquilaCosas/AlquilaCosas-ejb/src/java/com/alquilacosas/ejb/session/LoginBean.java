/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.ejb.entity.EstadoUsuario;
import com.alquilacosas.ejb.entity.EstadoUsuario.NombreEstado;
import com.alquilacosas.ejb.entity.Login;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.ejb.entity.UsuarioXEstado;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class LoginBean implements LoginBeanLocal {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager entityManager;
    
    @Override
    public boolean activarCuenta(String username, String codigo) throws AlquilaCosasException {

        Query loginQuery = entityManager.createNamedQuery("Login.findByUsername");
        loginQuery.setParameter("username", username);
        Login login = (Login) loginQuery.getSingleResult();
        
        String codigoDB = login.getCodigoActivacion();
        
        if(codigo.equals(codigoDB)) {
            
            Usuario usuario = login.getUsuarioFk();
            
            UsuarioXEstado ultimoUxe = null;
            try {
                Query uxeQuery = entityManager.createNamedQuery("UsuarioXEstado.findCurrentByUsuarioFk");
                uxeQuery.setParameter("usuario", usuario);
                ultimoUxe = (UsuarioXEstado) uxeQuery.getSingleResult();
            } catch(NoResultException e) {
                 throw new AlquilaCosasException("No se encontro el usuario.");
            }
            if(ultimoUxe == null || ultimoUxe.getEstadoUsuario().getNombre()
                    != NombreEstado.REGISTRADO) {
                throw new AlquilaCosasException("El estado del usuario no requiere activación.");
            }
            
            EstadoUsuario estado = null;
            try {
                Query estadoActivoQuery = entityManager.createNamedQuery("EstadoUsuario.findByNombre");
                estadoActivoQuery.setParameter("nombre", NombreEstado.ACTIVO);
                estado = (EstadoUsuario) estadoActivoQuery.getSingleResult();
            } catch(Exception e) {
                throw new AlquilaCosasException("No se encontro el estado " + 
                        NombreEstado.ACTIVO.toString() + " en la base de datos.");
            }

            ultimoUxe.setFechaHasta(new Date());          
            
            UsuarioXEstado nuevoUxE = new UsuarioXEstado(usuario, estado);
            nuevoUxE.setFechaDesde(new Date());
            usuario.agregarUsuarioXEstado(nuevoUxE);
            entityManager.merge(usuario);
            return true;
            
        }
        else {
            return false;
        }
    }
    
    @Override
    public Integer loginUsuario(String username) throws AlquilaCosasException {
        
        Login login = null;
        Query buscarLogin = entityManager.createNamedQuery("Login.findByUsername");
        buscarLogin.setParameter("username", username);
        try {
            login = (Login) buscarLogin.getSingleResult();
        } catch (Exception e) {
            throw new AlquilaCosasException("Username no valido");
        }
        
        Usuario usuario = login.getUsuarioFk();
        if(usuario == null)
            throw new AlquilaCosasException("Usuario no asociado a la cuenta.");
        
        return usuario.getUsuarioId();
    }

    
    
}
