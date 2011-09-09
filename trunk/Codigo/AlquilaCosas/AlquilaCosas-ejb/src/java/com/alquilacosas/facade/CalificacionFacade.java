/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Alquiler;
import com.alquilacosas.ejb.entity.Calificacion;
import com.alquilacosas.ejb.entity.Usuario;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class CalificacionFacade extends AbstractFacade<Calificacion> {
    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CalificacionFacade() {
        super(Calificacion.class);
    }
    
    public boolean isCalificacionExistente(Usuario usuario, Alquiler alquiler) {
        Query query = em.createQuery("SELECT count(c.calificacionId) FROM "
                + "Calificacion c WHERE c.alquilerFk = :alquiler AND "
                + "c.usuarioCalificadorFk = :usuario");
        query.setParameter("usuario", usuario);
        query.setParameter("alquiler", alquiler);
        Integer resultado = 0;
        try {
            resultado = (Integer) query.getSingleResult();
        } catch (Exception e) {
        }
        if(resultado > 0)
            return true;
        else
            return false;
       
    }
    
}
