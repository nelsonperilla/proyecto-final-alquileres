/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.PublicidadDTO;
import com.alquilacosas.ejb.entity.TipoPublicidad.UbicacionPublicidad;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author damiancardozo
 */
@Local
public interface MostrarPublicidadBeanLocal {

    public List<PublicidadDTO> getPublicidades(UbicacionPublicidad ubicacion, int cantidad);

    public byte[] leerImagen(int id);
    
}
