/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.PublicacionFacade;
import com.alquilacosas.ejb.entity.Categoria;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Publicacion;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    public List<PublicacionFacade> getPublicacionesRandom(int pagina) {
//       Query query = entityManager.createNamedQuery("Publicacion.findAll");
//       List<Publicacion> publicaciones = query.getResultList();
//       return publicaciones;
        Query query = entityManager.createNamedQuery("Publicacion.findAll");

        List<Publicacion> publicaciones = query.getResultList();
        List<PublicacionFacade> resultado = new ArrayList<PublicacionFacade>();
        for(Publicacion publicacion: publicaciones) {
            PublicacionFacade tempPublication = new PublicacionFacade(publicacion.getPublicacionId(), publicacion.getTitulo(),
                    publicacion.getDescripcion(), publicacion.getFechaDesde(), publicacion.getFechaHasta(), publicacion.getDestacada(),
                    publicacion.getCantidad());
            List<Integer> imagenes = new ArrayList<Integer>();
            for(ImagenPublicacion imagen: publicacion.getImagenPublicacionList()) {
                imagenes.add(imagen.getImagenPublicacionId());
            }
            tempPublication.setImagenIds(imagenes);
            resultado.add(tempPublication);
        }
        
        return resultado;        
    }

    @Override
    public List<PublicacionFacade> getPublicacionesPoCategoria(int pagina, int categoria) {
        Categoria filter = entityManager.find(Categoria.class, categoria);
        Query query = entityManager.createNamedQuery("Publicacion.findByCategoria");
        query.setParameter("categoria", filter);
        List<PublicacionFacade> publicaciones = query.getResultList();
        return publicaciones;//reescribir metodo!
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