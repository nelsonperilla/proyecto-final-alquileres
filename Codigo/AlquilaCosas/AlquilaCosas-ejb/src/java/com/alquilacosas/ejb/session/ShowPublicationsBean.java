/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.ejb.entity.Categoria;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Publicacion;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Blob;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jorge
 */
@Stateful
public class ShowPublicationsBean implements ShowPublicationsBeanLocal {

    @PersistenceContext(unitName="AlquilaCosas-ejbPU") 
    private EntityManager entityManager;
    
    @Override
    public List<Publicacion> getPublicacionesRandom(int pagina) {
       Query query = entityManager.createNamedQuery("Publicacion.findAll");
       List<Publicacion> publicaciones = query.getResultList();
       return publicaciones;
    }

    @Override
    public List<Publicacion> getPublicacionesPoCategoria(int pagina, int categoria) {
        Categoria filter = entityManager.find(Categoria.class, categoria);
        Query query = entityManager.createNamedQuery("Publicacion.findByCategoria");
        query.setParameter("categoria", filter);
        List<Publicacion> publicaciones = query.getResultList();
        return publicaciones;
    }
    
    public byte[] getImage(int publicacion)
    {
        Publicacion filter=entityManager.find(Publicacion.class,publicacion);
        Query query = entityManager.createNamedQuery("ImagenPublicacion.findAll");
        //query.setParameter("publicacion", filter);
        List<ImagenPublicacion> imagenes = query.getResultList();
        return imagenes.get(0).getImagen();
    }
    
    
    
}