/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;


import com.alquilacosas.dto.ComentarioDTO;
import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.PrecioDTO;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.entity.EstadoPublicacion.NombreEstadoPublicacion;
import com.alquilacosas.ejb.entity.Periodo;
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
            int usuarioId, int categoria, List<PrecioDTO> precios, 
            List<byte[]> imagenes, int periodoMinimo, int periodoMinimoFK, 
            int periodoMaximo, int periodoMaximoFk ) throws AlquilaCosasException;

    PublicacionDTO getPublicacion(int id);

    List<ComentarioDTO> getPreguntas(int publicationId);

    void setPregunta(int publicacionId,ComentarioDTO nuevaPregunta) 
            throws AlquilaCosasException;
    
    List<ComentarioDTO> getPreguntasSinResponder(int usuarioId);
    
    void setRespuesta(ComentarioDTO preguntaConRespuesta)
            throws AlquilaCosasException ;

    PublicacionDTO getDatosPublicacion(int publicacionId) throws AlquilaCosasException;

    void actualizarPublicacion( int publicacionId, String titulo, String descripcion, 
            Date fecha_desde, Date fecha_hasta, boolean destacada, int cantidad,
            int usuarioId, int categoria, List<PrecioDTO> precios, 
            List<byte[]> imagenesAgregar, List<Integer> imagenesABorrar, 
            NombreEstadoPublicacion estadoPublicacion  ) throws AlquilaCosasException;
    
    List<Periodo> getPeriodos();

    public void borrarPublicacion( Integer publicacionId ) throws AlquilaCosasException;
}
