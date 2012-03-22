/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.ImagenUsuario;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author ignaciogiagante
 */
@Stateless
public class ImagenUsuarioFacade extends AbstractFacade<ImagenUsuario> {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ImagenUsuarioFacade() {
        super(ImagenUsuario.class);
    }

    public ImagenUsuario getImagenUsuario(Integer imagenId) {
        Query query = em.createQuery("SELECT iu FROM ImagenUsuario iu "
                + "WHERE iu.imagenUsuarioId = :imagenId ");
        query.setParameter("imagenId", imagenId);
        ImagenUsuario imagen = null;
        try {
            imagen = (ImagenUsuario) query.getSingleResult();
        } catch (NoResultException e) {
            Logger.getLogger(PublicidadFacade.class).error("Exception en getImagenUsuario() para id = " + imagenId + " - " + e);
        }
        return imagen;
    }
}
