/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.Busqueda;
import com.alquilacosas.common.CategoriaFacade;
import com.alquilacosas.common.PublicacionFacade;
import com.alquilacosas.ejb.entity.Categoria;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Publicacion;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class BuscarPublicacionBean implements BuscarPublicacionBeanLocal {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager entityManager;
    
    @Resource
    private SessionContext context;
    
    @Override
    public Busqueda buscarPublicaciones(String palabra, int registros, int desde) {
        System.out.println("buscando...");
        Query publicacionesQuery = entityManager.createNamedQuery("Publicacion.findByPalabraClave");
        palabra = "%" + palabra + "%";
        publicacionesQuery.setParameter(1, palabra);
        publicacionesQuery.setMaxResults(registros);
        publicacionesQuery.setFirstResult(desde);
        
        List<Publicacion> publicaciones = publicacionesQuery.getResultList();
        List<PublicacionFacade> pubFacadeList = new ArrayList<PublicacionFacade>();
        List<Categoria> categorias = new ArrayList<Categoria>();
        for(Publicacion p: publicaciones) {
            PublicacionFacade facade = new PublicacionFacade(p.getPublicacionId(), p.getTitulo(),
                    p.getDescripcion(), p.getFechaDesde(), p.getFechaHasta(), p.getDestacada(),
                    p.getCantidad());
            List<Integer> imagenes = new ArrayList<Integer>();
            for(ImagenPublicacion ip: p.getImagenPublicacionList()) {
                imagenes.add(ip.getImagenPublicacionId());
            }
            facade.setImagenIds(imagenes);
            pubFacadeList.add(facade);
            Categoria c = p.getCategoriaFk();
            if(!categorias.contains(c))
                categorias.add(c);
        }
        
        List<CategoriaFacade> catFacade = new ArrayList<CategoriaFacade>();
        for(Categoria c: categorias) {
            CategoriaFacade categoria = new CategoriaFacade(c.getCategoriaId(), c.getNombre());
            catFacade.add(categoria);
        }
        Busqueda busqueda = new Busqueda(pubFacadeList, catFacade);
        return busqueda;
    }
    
    @Override
    public Busqueda buscarPublicaciones(String palabra, int categoriaId, 
            int registros, int desde) {
        Categoria categoria = entityManager.find(Categoria.class, categoriaId);
        
        Query publicacionesQuery = entityManager.createNamedQuery("Publicacion.findByPalabraClaveAndCat");
        palabra = "%" + palabra + "%";
        publicacionesQuery.setParameter(1, palabra);
        publicacionesQuery.setParameter("categoriaFk", categoria);
        publicacionesQuery.setMaxResults(registros);
        publicacionesQuery.setFirstResult(desde);
        
        List<Publicacion> publicaciones = publicacionesQuery.getResultList();
        List<PublicacionFacade> pubFacadeList = new ArrayList<PublicacionFacade>();
        List<Categoria> categorias = new ArrayList<Categoria>();
        for(Publicacion p: publicaciones) {
            PublicacionFacade facade = new PublicacionFacade(p.getPublicacionId(), p.getTitulo(),
                    p.getDescripcion(), p.getFechaDesde(), p.getFechaHasta(), p.getDestacada(),
                    p.getCantidad());
            List<Integer> imagenes = new ArrayList<Integer>();
            for(ImagenPublicacion ip: p.getImagenPublicacionList()) {
                imagenes.add(ip.getImagenPublicacionId());
            }
            facade.setImagenIds(imagenes);
            pubFacadeList.add(facade);
        }
        
        List<CategoriaFacade> catFacade = new ArrayList<CategoriaFacade>();
        for(Categoria c: categorias) {
            CategoriaFacade cat = new CategoriaFacade(c.getCategoriaId(), c.getNombre());
            catFacade.add(cat);
        }
        Busqueda busqueda = new Busqueda(pubFacadeList, catFacade);
        return busqueda;
    }
    
    @Override
    public Busqueda buscarPublicacionesPorCategoria(int categoriaId, int registros, int desde) {
        Categoria categoria = entityManager.find(Categoria.class, categoriaId);
        
        Query publicacionesQuery = entityManager.createNamedQuery("Publicacion.findByCategoria");
        publicacionesQuery.setParameter("categoriaFk", categoria);
        publicacionesQuery.setMaxResults(registros);
        publicacionesQuery.setFirstResult(desde);
        
        List<Publicacion> publicaciones = publicacionesQuery.getResultList();
        List<PublicacionFacade> pubFacadeList = new ArrayList<PublicacionFacade>();
        for(Publicacion p: publicaciones) {
            PublicacionFacade facade = new PublicacionFacade(p.getPublicacionId(), p.getTitulo(),
                    p.getDescripcion(), p.getFechaDesde(), p.getFechaHasta(), p.getDestacada(),
                    p.getCantidad());
            List<Integer> imagenes = new ArrayList<Integer>();
            for(ImagenPublicacion ip: p.getImagenPublicacionList()) {
                imagenes.add(ip.getImagenPublicacionId());
            }
            facade.setImagenIds(imagenes);
            pubFacadeList.add(facade);
        }
        
        List<CategoriaFacade> categorias = new ArrayList<CategoriaFacade>();
        categorias.add(new CategoriaFacade(categoria.getCategoriaId(), categoria.getNombre()));
        
        Busqueda busqueda = new Busqueda(pubFacadeList, categorias);
        return busqueda;
    }

    @Override
    public byte[] leerImagen(int id) {
        ImagenPublicacion im = entityManager.find(ImagenPublicacion.class, id);
        if(im != null)
            return im.getImagen();
        return null;
    }

    

    
    
}
