/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.ejb.entity.EstadoPublicacion;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ignaciogiagante
 */
@Stateless
public class MisPublicacionesBean implements MisPublicacionesBeanLocal {
    
    @PersistenceContext(unitName="AlquilaCosas-ejbPU") 
    private EntityManager entityManager;
    
    private Usuario usuario;
    private List<Publicacion> listaPublicaciones;
    private int publicacionId;
    
    @Override
    public List<Publicacion> getPublicaciones( int usuarioId ){
       
        usuario = entityManager.find(Usuario.class, usuarioId);
        
        listaPublicaciones = usuario.getPublicacionList();

        return listaPublicaciones;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Publicacion> getListaPublicaciones() {
        return listaPublicaciones;
    }

    public void setListaPublicaciones(List<Publicacion> listaPublicaciones) {
        this.listaPublicaciones = listaPublicaciones;
    }

    public int getPublicacionId() {
        return publicacionId;
    }

    public void setPublicacionId(int publicacionId) {
        this.publicacionId = publicacionId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
        
    
    @Override
    public List<EstadoPublicacion> getEstados() {
       return entityManager.createNamedQuery("EstadoPublicacion.findAll").getResultList();
    }
}
