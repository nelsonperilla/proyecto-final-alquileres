/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.PrecioDTO;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.entity.*;
import com.alquilacosas.facade.FavoritoFacade;
import com.alquilacosas.facade.PrecioFacade;
import com.alquilacosas.facade.PublicacionFacade;
import com.alquilacosas.facade.UsuarioFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

/**
 *
 * @author ignaciogiagante
 */
@Stateless
public class FavoritoBean implements FavoritoLocalBean {
    
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private PrecioFacade precioFacade;
    @EJB
    private FavoritoFacade favoritoFacade;
    @EJB
    private PublicacionFacade publicacionFacade;
    @EJB
    private PublicacionBeanLocal publicacionBean;
    

    @Override
    public List<PublicacionDTO> getFavoritos(Integer usuarioId) {   
        
        Usuario usuario = usuarioFacade.find(usuarioId);
        List<Favorito> favoritos = usuario.getFavoritoList();
        List<PublicacionDTO> publicacionesDto = new ArrayList<PublicacionDTO>();
        for( Favorito f : favoritos ){
            Publicacion p = f.getPublicacionFk();
            PublicacionDTO pDto = new PublicacionDTO(p.getPublicacionId(), p.getTitulo(),
                    p.getDescripcion(), p.getFechaDesde(), p.getFechaHasta(), p.getDestacada(),
                    p.getCantidad());
            
            List<Integer> imagenes = new ArrayList<Integer>();
            for (ImagenPublicacion ip : p.getImagenPublicacionList()) {
                imagenes.add(ip.getImagenPublicacionId());
            }
            pDto.setImagenIds(imagenes);

            Domicilio d = p.getUsuarioFk().getDomicilioList().get(0);
            pDto.setProvincia(d.getProvinciaFk().getNombre());
            pDto.setCiudad(d.getCiudad());

            pDto.setPrecios(getPrecios(p));
            
            
            publicacionesDto.add(pDto);
        }
        return publicacionesDto;        
    }
    
    @Override
    public Favorito getFavorito(Integer usuarioId, Integer publicacionId) {
        List<Favorito> favoritoList = favoritoFacade.getFavorito(usuarioId, publicacionId);
        if( favoritoList.size() == 1 ){
            return favoritoList.get(0);
        }else{
            return null;
        }
    }
 
    
    @Override
    public void agregarFavorito( Integer usuarioId, PublicacionDTO pDto) throws AlquilaCosasException{        
        Usuario usuario = usuarioFacade.find(usuarioId);
        Publicacion p = null;
        try {
            p = publicacionFacade.find(pDto.getId());
        } catch (NoResultException e) {
            throw new AlquilaCosasException("No se pudo agregar el articulo a "
                    + "Mis Favoritos.");
        }
        
        Favorito favorito = new Favorito(usuario, p);
        usuario.argegarFavorito(favorito);
        usuarioFacade.edit(usuario);
    }
    
    @Override
    public void eliminarFavorito(Integer usuarioId, Integer publicacionId) throws AlquilaCosasException{
        
        Usuario usuario = usuarioFacade.find(usuarioId);  
        List<Favorito> favoritoList = favoritoFacade.getFavorito(usuarioId, publicacionId);
        Favorito favorito = favoritoList.get(0);
        usuario.removerFavorito(favorito);
        favoritoFacade.remove(favorito);
        usuarioFacade.edit(usuario);
        
    }
    
    private List<PrecioDTO> getPrecios(Publicacion publicacion) {
        List<PrecioDTO> resultado = new ArrayList<PrecioDTO>();
        List<Precio> precios = precioFacade.buscarActualesPorPublicacion(publicacion);

        for (Precio precio : precios) {
            resultado.add(new PrecioDTO(precio.getPrecio(), precio.getPeriodoFk().getNombre()));
        }
        return resultado;
    }

    
    
}
