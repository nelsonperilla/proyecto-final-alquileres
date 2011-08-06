/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.PrecioFacade;
import com.alquilacosas.ejb.entity.Categoria;
import com.alquilacosas.ejb.entity.EstadoPublicacion;
import com.alquilacosas.ejb.entity.EstadoPublicacion.PublicacionEstado;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Periodo;
import com.alquilacosas.ejb.entity.Precio;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.PublicacionXEstado;
import com.alquilacosas.ejb.entity.Usuario;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ignaciogiagante
 */
@Stateless
public class PublicacionBean implements PublicacionBeanLocal {
    
    @PersistenceContext(unitName="AlquilaCosas-ejbPU") 
    private EntityManager entityManager;
    
    
    @Override
    public void registrarPublicacion( String titulo, String descripcion, 
            Date fecha_desde, Date fecha_hasta, boolean destacada, int cantidad,
            int usuarioId, int categoria, List<PrecioFacade> precios, List<ImagenPublicacion> imagenes ){
        
        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo(titulo);
        publicacion.setDescripcion(descripcion);
        publicacion.setFechaDesde(fecha_desde);
        publicacion.setFechaHasta(fecha_hasta);
        publicacion.setDestacada(destacada);
        publicacion.setCantidad(cantidad);
        
        Usuario usuario = entityManager.find(Usuario.class, usuarioId);        
        publicacion.setUsuarioFk(usuario);       
        
        Categoria c = entityManager.find(Categoria.class, categoria);
        publicacion.setCategoriaFk(c); 
        
        Query query = entityManager.createNamedQuery("EstadoPublicacion.findByNombre");
        query.setParameter("nombre", PublicacionEstado.ACTIVA);
        EstadoPublicacion estadoPublicacion = (EstadoPublicacion) query.getSingleResult();
        
        PublicacionXEstado pxe = new PublicacionXEstado();
        pxe.setPublicacion(publicacion);
        pxe.setEstadoPublicacion(estadoPublicacion);
        pxe.setFechaDesde(new Date());
        
        entityManager.persist(publicacion);
        
        Precio precio = null;
        Periodo periodo = null;
        
        for( PrecioFacade p : precios ){
            
            periodo = this.getPeriodo(p.getPeriodoNombre());
            precio = new Precio();
            precio.setPeriodoFk(periodo);
            precio.setPrecio(p.getPrecio());
            precio.setPublicacionFk(publicacion);
            
            entityManager.persist(precio);
            
        }
        
        for( ImagenPublicacion ip : imagenes ){
            ip.setPublicacionFk(publicacion);
            entityManager.persist(ip);
        }   
        
    }
    
    @Override
    public List<Periodo> getPeriodos(){
        Query query = entityManager.createNamedQuery("Periodo.findAll");
        return query.getResultList();
    }
    
    @Override
    public Periodo getPeriodo( String nombrePeriodo ){
        Query query = entityManager.createNamedQuery("Periodo.findByNombre");
        query.setParameter("nombre", nombrePeriodo);
        return (Periodo)query.getSingleResult();
    }   
    
    @Override
    public List<Publicacion> getPublicaciones(){
        return null;
    }
    
    
}


