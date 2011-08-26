/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.PrecioDTO;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.entity.Categoria;
import com.alquilacosas.ejb.entity.Domicilio;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Precio;
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
    
    @Override
    public List<PublicacionDTO> getPublicacionesRandom(int pagina) {
        Query query = entityManager.createNamedQuery("Publicacion.findAll");
        query.setMaxResults(10);

        List<Publicacion> publicaciones = query.getResultList();
        List<PublicacionDTO> resultado = new ArrayList<PublicacionDTO>();
        for(Publicacion publicacion: publicaciones) {
            PublicacionDTO tempPublication = new PublicacionDTO(publicacion.getPublicacionId(), publicacion.getTitulo(),
                    publicacion.getDescripcion(), publicacion.getFechaDesde(), publicacion.getFechaHasta(), publicacion.getDestacada(),
                    publicacion.getCantidad());
            List<Integer> imagenes = new ArrayList<Integer>();
            for(ImagenPublicacion imagen: publicacion.getImagenPublicacionList()) {
                imagenes.add(imagen.getImagenPublicacionId());
            }
            Domicilio domicilio = publicacion.getUsuarioFk().getDomicilioList().get(0);
            tempPublication.setPais(domicilio.getProvinciaFk().getPaisFk().getNombre());
            tempPublication.setCiudad(domicilio.getProvinciaFk().getNombre());               
            tempPublication.setImagenIds(imagenes);
            tempPublication.setPrecios(getPrecios(publicacion));
            resultado.add(tempPublication);
        }
        
        return resultado;        
    }

    @Override
    public List<PublicacionDTO> getPublicacionesPoCategoria(int pagina, int categoria) {
        Categoria filter = entityManager.find(Categoria.class, categoria);
        Query query = entityManager.createNamedQuery("Publicacion.findByCategoria");
        query.setParameter("categoria", filter);
        List<PublicacionDTO> publicaciones = query.getResultList();
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

    private List<PrecioDTO> getPrecios(Publicacion filter){
        List<PrecioDTO> resultado = new ArrayList<PrecioDTO>();
        List<Precio> precios;
        Query query = entityManager.createNamedQuery("Precio.findByPublicacion");
        query.setParameter("publicacion", filter);
        precios = query.getResultList();   
        
        for(Precio precio: precios)
            resultado.add(new PrecioDTO(precio.getPrecio(), precio.getPeriodoFk().getNombre()));
        
        return resultado;

    }    
    
}