/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.ejb.entity.Alquiler;
import com.alquilacosas.ejb.entity.Calificacion;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.Puntuacion;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.facade.AlquilerFacade;
import com.alquilacosas.facade.CalificacionFacade;
import com.alquilacosas.facade.PuntuacionFacade;
import com.alquilacosas.facade.UsuarioFacade;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class AlquileresOfrecidosBean implements AlquileresOfrecidosBeanLocal {

    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private AlquilerFacade alquilerFacade;
    @EJB
    private CalificacionFacade calificacionFacade;
    @EJB
    private PuntuacionFacade puntuacionFacade;
    
    @Override
    public List<AlquilerDTO> getAlquileres(int usuarioId) {
        Usuario usuario = usuarioFacade.find(usuarioId);
        List<Alquiler> alquileres = alquilerFacade.getAlquileresOfrecidos(usuario);
        List<AlquilerDTO> listaAlquileres = new ArrayList<AlquilerDTO>();
        for(Alquiler a: alquileres) {
            boolean calificado = calificacionFacade.isCalificacionExistente(usuario, a);
            Publicacion pub = a.getPublicacionFk();
            Usuario dueno = pub.getUsuarioFk();
            listaAlquileres.add(new AlquilerDTO(pub.getPublicacionId(), usuarioId, 
                    a.getAlquilerId(), a.getFechaInicio(), a.getFechaFin(), pub.getTitulo(), 
                    "Pepe", a.getCantidad(), a.getMonto(), calificado));
        }
        return listaAlquileres;
    }
    
    @Override
    public void registrarCalificacion(int usuarioId, int alquilerId, int puntuacionId, String comentario) {
        Usuario calificador = usuarioFacade.find(usuarioId);
        Puntuacion puntuacion = puntuacionFacade.find(puntuacionId);
        Alquiler alquiler = alquilerFacade.find(alquilerId);
        
        Calificacion calificacion = new Calificacion();
        calificacion.setFechaCalificacion(new Date());
        calificacion.setUsuarioCalificadorFk(calificador);
        calificacion.setComentarioCalificador(comentario);
        calificacion.setPuntuacionFk(puntuacion);
        
        alquiler.agregarCalificacion(calificacion);
        alquilerFacade.edit(alquiler);
    }
    
    @Override
    public List<Puntuacion> getPuntuaciones() {
        return puntuacionFacade.findAll();
     }
    
}
