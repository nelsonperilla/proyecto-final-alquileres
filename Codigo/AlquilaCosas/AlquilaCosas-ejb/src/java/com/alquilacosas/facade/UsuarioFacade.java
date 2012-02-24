/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Usuario;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
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
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }

    public Usuario findByEmail(String email) {
        Usuario usuario = null;
        Query q = em.createNamedQuery("Usuario.findByEmail");
        q.setParameter("email", email);
        try {
            usuario = (Usuario) q.getSingleResult();
        } catch (NoResultException e) {
            Logger.getLogger(UsuarioFacade.class).
                    error("findByEmail(). "
                    + "Excepcion al ejecutar consulta: " + e);
        }
        return usuario;
    }
}
