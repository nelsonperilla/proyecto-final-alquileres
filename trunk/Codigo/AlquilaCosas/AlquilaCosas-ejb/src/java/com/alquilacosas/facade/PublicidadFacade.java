/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Publicidad;
import com.alquilacosas.ejb.entity.TipoPublicidad;
import com.alquilacosas.ejb.entity.TipoPublicidad.UbicacionPublicidad;
import java.util.Date;
import java.util.List;
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
public class PublicidadFacade extends AbstractFacade<Publicidad> {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PublicidadFacade() {
        super(Publicidad.class);
    }
    
    /**
     * Devuelve todas las publicidades que se deben mostrar el dia de la fecha 
     * en una ubicacion determinada.
     * @param ubicacion
     * @return 
     */
    public List<Publicidad> getPublicidadesPorUbicacion(UbicacionPublicidad ubicacion) {
        Query query = em.createQuery("SELECT p FROM Publicidad p, Pago pa "
                + " WHERE pa.servicioFk.servicioId = p.servicioId "
                + "AND p.tipoPublicidadFk.ubicacion = :ubicacion "
                + "AND (:fecha BETWEEN p.fechaDesde AND p.fechaHasta) "
                + "AND pa.fechaConfirmado IS NOT NULL");
        query.setParameter("ubicacion", ubicacion);
        query.setParameter("fecha", new Date());
        return query.getResultList();
    }
    
    public byte[] getImagenPublicidad(int id) {
        Query query = em.createNativeQuery("SELECT imagen FROM PUBLICIDAD "
                + "WHERE servicio_id = " + id);
        byte[] imagen = null;
        try {
            imagen = (byte[]) query.getSingleResult();
        } catch (NoResultException e) {
            Logger.getLogger(PublicidadFacade.class).error("Exception en getImagenPublicidad() para id = " + id + " - " + e);
        }
        return imagen;
    }
    
     public List<Publicidad> getPublicidadPorUsuario( Integer usuarioId ){
        List<Publicidad> publicidades = null;
        Query query = em.createQuery("SELECT p from Publicidad p "
                + "WHERE p.usuarioFk.usuarioId = :usuarioId");
        query.setParameter("usuarioId", usuarioId);
        publicidades = query.getResultList();
        return publicidades;
    }
     
    public List<Publicidad> getPublicidadesPorSector( UbicacionPublicidad ubicacion ){
        List<Publicidad> publicidades = null;
        Query query = em.createQuery("SELECT p from Publicidad p "
                + "WHERE p.tipoPublicidadFk.ubicacion = :ubicacion");
        query.setParameter("ubicacion", ubicacion);
        publicidades = query.getResultList();
        return publicidades;
    }
    
    public boolean eliminarPublicidad(Integer publicidadId) {
        Query query = em.createQuery("DELETE FROM Publicidad p WHERE p.servicioId = :id");
        query.setParameter("id", publicidadId);
        int filas = query.executeUpdate();
        return filas > 0;
    }
}
