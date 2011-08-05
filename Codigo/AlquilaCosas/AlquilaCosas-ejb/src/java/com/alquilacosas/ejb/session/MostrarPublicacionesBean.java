/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.ejb.entity.Categoria;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Publicacion;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

/**
 *
 * @author jorge
 */
@Stateless
public class MostrarPublicacionesBean implements MostrarPublicacionesBeanLocal {
    @PersistenceContext(unitName="AlquilaCosas-ejbPU") 
    private EntityManager entityManager;
    private Publicacion activePublication;
    
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
    
    @Override
    public List<byte[]> getImage(int publicacion)
    {
        List<ImagenPublicacion> images;//=new ArrayList<ImagenPublicacion>();
        Publicacion filter=entityManager.find(Publicacion.class,publicacion);
        Query query = entityManager.createNamedQuery("ImagenPublicacion.findByPublicacionId");
        query.setParameter("publicacion", filter);
        images = query.getResultList();
        List<byte[]> result=new ArrayList<byte[]>();
        for(ImagenPublicacion image : images)
            result.add(image.getImagen());
        return result;
    }

//    @Override
//    public void setSelectedPublication(int id) {
//        activePublication = entityManager.find(Publicacion.class, id);
//    }
//
//    @Override
//    public Publicacion getSelectedPublication() {
//        return activePublication;
//    }
    
    
}