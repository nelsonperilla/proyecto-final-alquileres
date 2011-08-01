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
import com.alquilacosas.ejb.entity.UsuarioXEstadoPK;
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
                uxeQuery.setParameter("usuarioFk", usuario.getUsuarioId());
                ultimoUxe = (UsuarioXEstado) uxeQuery.getSingleResult();
            } catch(NoResultException e) {
                 System.out.println("error!");
            }
            if(ultimoUxe == null || ultimoUxe.getEstadoUsuario().getNombre()
                    != NombreEstado.REGISTRADO) {
                throw new AlquilaCosasException("El estado del usuario no requiere activación.");
            }
            
            
            Query estadoActivoQuery = entityManager.createNamedQuery("EstadoUsuario.findByNombre");
            estadoActivoQuery.setParameter("nombre", NombreEstado.ACTIVO);
            EstadoUsuario estado = (EstadoUsuario) estadoActivoQuery.getSingleResult();

            
            Query query2 = entityManager.createNamedQuery("UsuarioXEstado.findByUsuarioFk");
            query2.setParameter("usuarioFk", usuario.getUsuarioId());
            UsuarioXEstado uxe = (UsuarioXEstado) query2.getSingleResult();
            uxe.setFechaHasta(new Date());
            entityManager.persist(uxe);            
            
            UsuarioXEstado nuevoUxE = new UsuarioXEstado();
            nuevoUxE.setEstadoUsuario(estado);
            nuevoUxE.setUsuario(usuario);
            nuevoUxE.setFechaDesde(new Date());
            UsuarioXEstadoPK nuevoUxEPk = new UsuarioXEstadoPK(usuario.getUsuarioId(), estado.getEstadoUsuarioId());
            nuevoUxE.setUsuarioXEstadoPK(nuevoUxEPk);
            entityManager.persist(nuevoUxE);
            return true;
            
        }
        else {
            return false;
        }
        
    }

    
    
}
