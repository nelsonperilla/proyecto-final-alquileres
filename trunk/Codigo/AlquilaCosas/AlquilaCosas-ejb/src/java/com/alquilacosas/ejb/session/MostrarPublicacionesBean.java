/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.PrecioDTO;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.entity.Domicilio;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Precio;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.facade.PrecioFacade;
import com.alquilacosas.facade.PublicacionFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge
 */
@Stateless
public class MostrarPublicacionesBean implements MostrarPublicacionesBeanLocal {
    
    @EJB
    private PublicacionFacade publicacionFacade;
    @EJB
    private PrecioFacade precioFacade;
    
    @Override
    public List<PublicacionDTO> getPublicacionesRandom(int pagina) {
        List<Publicacion> publicaciones = publicacionFacade.getPublicacionesInicio();
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
            tempPublication.setProvincia(domicilio.getProvinciaFk().getNombre());
            tempPublication.setCiudad(domicilio.getCiudad());               
            tempPublication.setImagenIds(imagenes);
            tempPublication.setPrecios(getPrecios(publicacion));
            resultado.add(tempPublication);
        }
        
        return resultado;        
    }
    
    @Override
    public List<byte[]> getImage(int publicacionId)
    {
        List<ImagenPublicacion> images = null;
        Publicacion publicacion = publicacionFacade.find(publicacionId);
        images = publicacion.getImagenPublicacionList();
        List<byte[]> result=new ArrayList<byte[]>();
        for(ImagenPublicacion image : images)
            result.add(image.getImagen());
        return result;
    }

    private List<PrecioDTO> getPrecios(Publicacion filter){
        List<PrecioDTO> resultado = new ArrayList<PrecioDTO>();
        List<Precio> precios = precioFacade.findByPublicacion(filter);
        
        for(Precio precio: precios) {
            PrecioDTO dto = new PrecioDTO(precio.getPrecio(), precio.getPeriodoFk().getNombre());
            resultado.add(dto);
        }
        
        return resultado;
    }    
    
}