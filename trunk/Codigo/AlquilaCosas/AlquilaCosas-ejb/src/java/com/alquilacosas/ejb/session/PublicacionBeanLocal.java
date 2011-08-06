/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.PrecioFacade;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Periodo;
import com.alquilacosas.ejb.entity.Publicacion;
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
            int usuarioId, int categoria, List<PrecioFacade> precios, List<ImagenPublicacion> imagenes );
    
    public List<Publicacion> getPublicaciones();
    
    public List<Periodo> getPeriodos();
    
    public Periodo getPeriodo( String nombrePeriodo );
    
   // public Publicacion getPublicacion( int id_publicacion );
    
}
