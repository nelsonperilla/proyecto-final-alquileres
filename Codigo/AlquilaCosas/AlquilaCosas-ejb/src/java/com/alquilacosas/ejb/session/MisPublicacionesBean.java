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
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ignaciogiagante
 */
@Stateless
@DeclareRoles({"USUARIO", "ADMIN"})
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
    @RolesAllowed({"USUARIO", "ADMIN"})
    public List<PublicacionDTO> getPublicaciones(int usuarioId) {

        Usuario usuario = usuarioFacade.find(usuarioId);
        usuarioFacade.refresh(usuario);
        List<Publicacion> listaPublicaciones = usuario.getPublicacionList();
        List<PublicacionDTO> listaFacade = new ArrayList<PublicacionDTO>();
        for (Publicacion p : listaPublicaciones) {
            PublicacionDTO facade = new PublicacionDTO(p.getPublicacionId(), p.getTitulo(),
                    p.getDescripcion(), p.getFechaDesde(), p.getFechaHasta(), p.getDestacada(),
                    p.getCantidad());
            List<Integer> imagenes = new ArrayList<Integer>();
            for (ImagenPublicacion ip : p.getImagenPublicacionList()) {
                imagenes.add(ip.getImagenPublicacionId());
            }
            facade.setImagenIds(imagenes);

            Domicilio d = p.getUsuarioFk().getDomicilioList().get(0);
            facade.setProvincia(d.getProvinciaFk().getNombre());
            facade.setCiudad(d.getCiudad());

            facade.setPrecios(getPrecios(p));

            listaFacade.add(facade);
        }
        return listaFacade;
    }
    
    @Override
    @RolesAllowed({"USUARIO", "ADMIN"})
    public void borrarPublicacion( Integer publicacionId ) throws AlquilaCosasException{
        Publicacion p = publicacionFacade.find(publicacionId);
        Usuario usuario = p.getUsuarioFk();
        usuario.removerPublicacion(p);
        publicacionFacade.remove(p);
    }

    private List<PrecioDTO> getPrecios(Publicacion publicacion) {
        List<PrecioDTO> resultado = new ArrayList<PrecioDTO>();
        List<Precio> precios = precioFacade.getUltimoPrecios(publicacion);

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
