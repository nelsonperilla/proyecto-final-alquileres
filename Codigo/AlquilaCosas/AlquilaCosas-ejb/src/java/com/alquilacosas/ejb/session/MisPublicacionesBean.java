/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.PrecioFacade;
import com.alquilacosas.common.PublicacionFacade;
import com.alquilacosas.ejb.entity.Domicilio;
import com.alquilacosas.ejb.entity.EstadoPublicacion;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Precio;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.Usuario;
import java.util.ArrayList;
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
public class MisPublicacionesBean implements MisPublicacionesBeanLocal {
    
    @PersistenceContext(unitName="AlquilaCosas-ejbPU") 
    private EntityManager entityManager;
    
    @Override
    public List<PublicacionFacade> getPublicaciones( int usuarioId ){
       
        Usuario usuario = entityManager.find(Usuario.class, usuarioId);
        List<Publicacion> listaPublicaciones = usuario.getPublicacionList();
        List<PublicacionFacade> listaFacade = new ArrayList<PublicacionFacade>();
        for(Publicacion p: listaPublicaciones) {
            PublicacionFacade facade = new PublicacionFacade(p.getPublicacionId(), p.getTitulo(),
                    p.getDescripcion(), p.getFechaDesde(), p.getFechaHasta(), p.getDestacada(),
                    p.getCantidad());
            List<Integer> imagenes = new ArrayList<Integer>();
            for(ImagenPublicacion ip: p.getImagenPublicacionList()) {
                imagenes.add(ip.getImagenPublicacionId());
            }
            facade.setImagenIds(imagenes);
            
            Domicilio d = p.getUsuarioFk().getDomicilioList().get(0);
            facade.setPais(d.getProvinciaFk().getPaisFk().getNombre());
            facade.setCiudad(d.getProvinciaFk().getNombre());
            
            facade.setPrecios(getPrecios(p));
            
            listaFacade.add(facade);
        }
        return listaFacade;
    }
    
    private List<PrecioFacade> getPrecios(Publicacion filter){
        List<PrecioFacade> resultado = new ArrayList<PrecioFacade>();
        List<Precio> precios;
        Query query = entityManager.createNamedQuery("Precio.findByPublicacion");
        query.setParameter("publicacion", filter);
        precios = query.getResultList();   
        
        for(Precio precio: precios)
            resultado.add(new PrecioFacade(precio.getPrecio(), precio.getPeriodoFk().getNombre()));
        
        return resultado;

    }    
    
    @Override
    public List<EstadoPublicacion> getEstados() {
       return entityManager.createNamedQuery("EstadoPublicacion.findAll").getResultList();
    }
}
