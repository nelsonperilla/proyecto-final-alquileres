/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;


import com.alquilacosas.common.ComentarioFacade;
import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.common.PrecioFacade;
import com.alquilacosas.common.PublicacionFacade;
import com.alquilacosas.ejb.entity.EstadoPublicacion.PublicacionEstado;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ignaciogiagante
 */
@Local
public interface PublicacionBeanLocal {
    
    void registrarPublicacion( String titulo, String descripcion, 
            Date fecha_desde, Date fecha_hasta, boolean destacada, int cantidad,
            int usuarioId, int categoria, List<PrecioFacade> precios, 
            List<byte[]> imagenes ) throws AlquilaCosasException;

    PublicacionFacade getPublicacion(int id);

    List<ComentarioFacade> getPreguntas(int publicationId);

    void setPregunta(int publicacionId,ComentarioFacade nuevaPregunta) 
            throws AlquilaCosasException;
    
    List<ComentarioFacade> getPreguntasSinResponder(int usuarioId);
    
    void setRespuesta(ComentarioFacade preguntaConRespuesta)
            throws AlquilaCosasException ;

    PublicacionFacade getDatosPublicacion(int publicacionId) throws AlquilaCosasException;

    void actualizarPublicacion( int publicacionId, String titulo, String descripcion, 
            Date fecha_desde, Date fecha_hasta, boolean destacada, int cantidad,
            int usuarioId, int categoria, List<PrecioFacade> precios, 
            List<byte[]> imagenesAgregar, List<Integer> imagenesABorrar, 
            PublicacionEstado estadoPublicacion  ) throws AlquilaCosasException;

}
