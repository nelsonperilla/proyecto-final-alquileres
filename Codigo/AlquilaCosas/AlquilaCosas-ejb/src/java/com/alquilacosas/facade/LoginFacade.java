/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Login;
import com.alquilacosas.ejb.entity.Usuario;
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
public class LoginFacade extends AbstractFacade<Login> {
    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public LoginFacade() {
        super(Login.class);
    }
    
    public Login findByUsername(String username) {
        Login login = null;
        Query query = em.createNamedQuery("Login.findByUsername");
        query.setParameter("username", username);
        try {
            login = (Login) query.getSingleResult();
        } catch(NoResultException e) {
            
        }
        return login;
    }
    
    public Login findByUsuario(Usuario usuario) {
       Query query = em.createNamedQuery("Login.findByUsuarioFk");
       query.setParameter("usuarioFk", usuario);
       return (Login) query.getSingleResult();
    }
    
    public Login findByUsuarioId(int usuarioId) {
        Query query = em.createQuery("SELECT l FROM Login l WHERE l.usuarioFk.usuarioId = :usuario");
        query.setParameter("usuario", usuarioId);
        return (Login) query.getSingleResult();
    }
    
}
