/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.ejb.entity.Alquiler;
import com.alquilacosas.ejb.entity.EstadoAlquiler;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.PedidoCambio;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.facade.AlquilerFacade;
import com.alquilacosas.facade.AlquilerXEstadoFacade;
import com.alquilacosas.facade.CalificacionFacade;
import com.alquilacosas.facade.EstadoAlquilerFacade;
import com.alquilacosas.facade.EstadoPedidoCambioFacade;
import com.alquilacosas.facade.PedidoCambioFacade;
import com.alquilacosas.facade.PedidoCambioXEstadoFacade;
import com.alquilacosas.facade.PrecioFacade;
import com.alquilacosas.facade.PublicacionFacade;
import com.alquilacosas.facade.PuntuacionFacade;
import com.alquilacosas.facade.UsuarioFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class AlquileresBean implements AlquileresBeanLocal {

    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private AlquilerFacade alquilerFacade;
    @EJB
    private CalificacionFacade calificacionFacade;
    @EJB
    private PuntuacionFacade puntuacionFacade;
    @EJB
    private EstadoAlquilerFacade estadoFacade;
    @EJB
    private PrecioFacade precioFacade;
    @EJB
    private AlquilerXEstadoFacade axeFacade;
    @EJB
    private PedidoCambioFacade pedidoFacade;
    @EJB
    private PedidoCambioXEstadoFacade pcxeFacade;
    @EJB
    private EstadoPedidoCambioFacade estadoPedidoFacade;
    @EJB
    private PublicacionFacade publicacionFacade;

    @Override
    public List<AlquilerDTO> getAlquileres(int usuarioId) {
        Usuario usuario = usuarioFacade.find(usuarioId);
        List<Alquiler> alquileres = alquilerFacade.getAlquileres(usuario);
        return convertirAlquileres(alquileres, usuario);
    }

    @Override
    public List<AlquilerDTO> getPedidos(int usuarioId) {
        Usuario usuario = usuarioFacade.find(usuarioId);
        List<Alquiler> alquileres = alquilerFacade.getPedidos(usuario);
        return convertirAlquileres(alquileres, usuario);
    }

    private List<AlquilerDTO> convertirAlquileres(List<Alquiler> alquileres, Usuario usuario) {
        List<AlquilerDTO> listaAlquileres = new ArrayList<AlquilerDTO>();
        for (Alquiler a : alquileres) {
            boolean tomado = false;
            if (a.getUsuarioFk().equals(usuario)) {
                tomado = true;
            }
            boolean calificado = calificacionFacade.isCalificacionExistente(usuario, a);
            Publicacion pub = a.getPublicacionFk();
            EstadoAlquiler estado = estadoFacade.getEstadoAlquiler(a);
            List<ImagenPublicacion> imagenes = pub.getImagenPublicacionList();
            int imagenId = -1;
            if (!imagenes.isEmpty()) {
                imagenId = imagenes.get(0).getImagenPublicacionId();
            }
            int usuarioId = 0;
            String username = "";
            if (tomado) {
                usuarioId = pub.getUsuarioFk().getUsuarioId();
                username = pub.getUsuarioFk().getLoginList().get(0).getUsername();
            } else {
                usuarioId = a.getUsuarioFk().getUsuarioId();
                username = a.getUsuarioFk().getLoginList().get(0).getUsername();
            }
            AlquilerDTO dto = new AlquilerDTO(pub.getPublicacionId(), usuarioId,
                    a.getAlquilerId(), imagenId, a.getFechaInicio(), a.getFechaFin(),
                    estado.getNombre(), pub.getTitulo(),
                    username, a.getCantidad(), a.getMonto(), calificado);
            dto.setTomado(tomado);
            // si el alquiler esta activo o confirmado; revisar si existe un pedido de cambio, y setearle su id al dto
            if ((estado.getNombre() == EstadoAlquiler.NombreEstadoAlquiler.CONFIRMADO
                    || estado.getNombre() == EstadoAlquiler.NombreEstadoAlquiler.ACTIVO) && pedidoFacade.hayPedidoEnviado(a)) {
                PedidoCambio pedido = pedidoFacade.getPedidoEnviado(a);
                int id = -1;
                if (pedido != null) {
                    id = pedido.getPedidoCambioId();
                }
                dto.setIdPedidoCambio(id);
            }
            listaAlquileres.add(dto);
        }
        return listaAlquileres;
    }

   
}
