/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.PublicidadDTO;
import com.alquilacosas.ejb.entity.Publicidad;
import com.alquilacosas.ejb.entity.TipoPublicidad.UbicacionPublicidad;
import com.alquilacosas.facade.PublicidadFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class MostrarPublicidadBean implements MostrarPublicidadBeanLocal {

    @EJB
    private PublicidadFacade publicidadFacade;
    
    @Override
    public List<PublicidadDTO> getPublicidades(UbicacionPublicidad ubicacion, int cantidad) {
        
        List<Publicidad> publicidades = publicidadFacade.getPublicidadesPorUbicacion(ubicacion);
        
        int n = publicidades.size();
        while(n > cantidad) {
            int indice = (int) (Math.random() * n);
            publicidades.remove(indice);
            n--;
        }
        
        List<PublicidadDTO> dtos = new ArrayList<PublicidadDTO>();
        for(Publicidad p: publicidades) {
            dtos.add(new PublicidadDTO(p.getServicioId(), p.getTitulo(), p.getCaption(), p.getUrl()));
        }
        return dtos;
    }
    
    @Override
    public byte[] leerImagen(int id) {
        return publicidadFacade.getImagenPublicidad(id);
    }
    
}
