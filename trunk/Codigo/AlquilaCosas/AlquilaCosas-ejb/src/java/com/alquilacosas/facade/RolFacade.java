/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Rol;
import com.alquilacosas.ejb.entity.Rol.NombreRol;
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
public class RolFacade extends AbstractFacade<Rol> {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RolFacade() {
        super(Rol.class);
    }

    public Rol findByNombre(NombreRol nombre) {
        Rol rol = null;
        Query getRolQuery = em.createNamedQuery("Rol.findByNombre");
        getRolQuery.setParameter("nombre", nombre);
        try {
            rol = (Rol) getRolQuery.getSingleResult();
        } catch (NoResultException e) {
            Logger.getLogger(RolFacade.class).
                    error("findByNombre(). "
                    + "Excepcion al ejecutar consulta: " + e);
        }
        return rol;
    }
}
