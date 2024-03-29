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
import org.apache.log4j.Logger;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class LoginFacade extends AbstractFacade<Login> {
    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LoginFacade() {
        super(Login.class);
    }
    
    public Login login(String username, String password) {
        Query query = em.createQuery("SELECT l FROM Login l WHERE l.username = :username AND "
                + "l.password = :password");
        query.setParameter("username", username);
        query.setParameter("password", password);
        Login login = null;
        try {
            login = (Login) query.getSingleResult(); 
        } catch (Exception e) {
            Logger.getLogger(LoginFacade.class).
                    error("checkCredentials(). "
                    + "Credenciales incorrectas.");
        }
        return login;
    }
    
    public Login findByUsername(String username) {
        Login login = null;
        Query query = em.createNamedQuery("Login.findByUsername");
        query.setParameter("username", username);
        try {
            login = (Login) query.getSingleResult();
        } catch(NoResultException e) {
            Logger.getLogger(LoginFacade.class).
                    error("findByUsername(). "
                    + "Excepcion al ejecutar consulta: " + e);
        }
        return login;
    }
    
    public Login findByEmail(String email) {
        Login login = null;
        Query query = em.createQuery("SELECT l FROM Login l, Usuario u WHERE l.usuarioFk = u "
                + "AND u.email = :email");
        query.setParameter("email", email);
        try {
            login = (Login) query.getSingleResult();
        } catch(NoResultException e) {
            Logger.getLogger(LoginFacade.class).
                    error("findByEmail(). "
                    + "Excepcion al ejecutar consulta: " + e);
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
