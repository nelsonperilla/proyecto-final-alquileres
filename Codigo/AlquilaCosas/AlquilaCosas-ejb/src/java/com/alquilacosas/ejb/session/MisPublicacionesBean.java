/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.PrecioDTO;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.entity.Domicilio;
import com.alquilacosas.ejb.entity.EstadoPublicacion;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Precio;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.facade.EstadoPublicacionFacade;
import com.alquilacosas.facade.PrecioFacade;
import com.alquilacosas.facade.PublicacionFacade;
import com.alquilacosas.facade.UsuarioFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ignaciogiagante
 */
@Stateless
public class MisPublicacionesBean implements MisPublicacionesBeanLocal {
    
    @EJB
    private UsuarioFacade usuarioFacade;
    
    @EJB
    private PublicacionFacade publicacionFacade;
    @EJB
    private PrecioFacade precioFacade;
    @EJB
    private EstadoPublicacionFacade estadoFacade;

    @Override
    public List<PublicacionDTO> getPublicaciones(int usuarioId) {

        Usuario usuario = usuarioFacade.find(usuarioId);
        List<Publicacion> listaPublicaciones = usuario.getPublicacionList();
        List<PublicacionDTO> listaFacade = new ArrayList<PublicacionDTO>();
        for (Publicacion p : listaPublicaciones) {
            PublicacionDTO dto = new PublicacionDTO(p.getPublicacionId(), p.getTitulo(),
                    p.getDescripcion(), p.getFechaDesde(), p.getFechaHasta(), p.getDestacada(),
                    p.getCantidad());
            List<Integer> imagenes = new ArrayList<Integer>();
            for (ImagenPublicacion ip : p.getImagenPublicacionList()) {
                imagenes.add(ip.getImagenPublicacionId());
            }
            dto.setImagenIds(imagenes);
            dto.setEstado(p.getEstadoPublicacionVigente().getEstadoPublicacion().getNombre());

            Domicilio d = p.getUsuarioFk().getDomicilioList().get(0);
            dto.setProvincia(d.getProvinciaFk().getNombre());
            dto.setCiudad(d.getCiudad());

            dto.setPrecios(getPrecios(p));

            listaFacade.add(dto);
        }
        return listaFacade;
    }
    
    @Override
    public void borrarPublicacion( Integer publicacionId ) throws AlquilaCosasException{
        Publicacion p = publicacionFacade.find(publicacionId);
        Usuario usuario = p.getUsuarioFk();
        usuario.removerPublicacion(p);
        publicacionFacade.remove(p);
    }

    private List<PrecioDTO> getPrecios(Publicacion publicacion) {
        List<PrecioDTO> resultado = new ArrayList<PrecioDTO>();
        List<Precio> precios = precioFacade.buscarActualesPorPublicacion(publicacion);

        for (Precio precio : precios) {
            resultado.add(new PrecioDTO(precio.getPrecio(), precio.getPeriodoFk().getNombre()));
        }
        return resultado;
    }

    @Override
    public List<EstadoPublicacion> getEstados() {
        return estadoFacade.findAll();
    }
}
