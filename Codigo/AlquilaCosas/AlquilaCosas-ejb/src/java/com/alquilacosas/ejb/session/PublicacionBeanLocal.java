/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.common.PrecioFacade;
import com.alquilacosas.common.PublicacionFacade;
import com.alquilacosas.ejb.entity.EstadoPublicacion;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Periodo;
import com.alquilacosas.ejb.entity.PublicacionXEstado;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ignaciogiagante
 */
@Local
public interface PublicacionBeanLocal {
    
    public void registrarPublicacion( String titulo, String descripcion, 
            Date fecha_desde, Date fecha_hasta, boolean destacada, int cantidad,
            int usuarioId, int categoria, List<PrecioFacade> precios, 
            List<ImagenPublicacion> imagenes ) throws AlquilaCosasException;
    
    public List<Periodo> getPeriodos();
    
    public Periodo getPeriodo( String nombrePeriodo );
    
   // public Publicacion getPublicacion( int id_publicacion );

    public PublicacionFacade getDatosPublicacion(int publicacionId);

    public void actualizarPublicacion( int publicacionId, String titulo, String descripcion, 
            Date fecha_desde, Date fecha_hasta, boolean destacada, int cantidad,
            int usuarioId, int categoria, List<PrecioFacade> precios, 
            List<ImagenPublicacion> imagenes, int estadoPublicacion ) throws AlquilaCosasException;

    public PublicacionXEstado getPublicacionEstado(int publicacionId);


    
}
