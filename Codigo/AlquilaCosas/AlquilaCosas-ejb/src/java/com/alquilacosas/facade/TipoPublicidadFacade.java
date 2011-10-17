/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.TipoPublicidad;
import com.alquilacosas.ejb.entity.TipoPublicidad.DuracionPublicidad;
import com.alquilacosas.ejb.entity.TipoPublicidad.UbicacionPublicidad;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class TipoPublicidadFacade extends AbstractFacade<TipoPublicidad> {
    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoPublicidadFacade() {
        super(TipoPublicidad.class);
    }
    
    public TipoPublicidad findByUbicacionYDuracion(UbicacionPublicidad ubicacion, 
            DuracionPublicidad duracion) {
        Query query = em.createQuery("SELECT tp FROM TipoPublicidad WHERE "
                + "tp.ubicacion = :ubicacion AND tp.duracion = :duracion");
        query.setParameter("ubicacion", ubicacion);
        query.setParameter("duracion", duracion);
        TipoPublicidad tipo = null;
        try {
            tipo = (TipoPublicidad) query.getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(TipoPublicidadFacade.class).
                    error("findByUbicacionYDuracion(). "
                    + "Excepcion al ejecutar consulta: " + e);
        }
        return tipo;
    }
    
}
