/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.PublicacionFacade;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author damiancardozo
 */
@Local
public interface BuscarPublicacionBeanLocal {

    List<PublicacionFacade> buscarPublicaciones(String palabra, int registros, int desde);

    List<PublicacionFacade> buscarPublicaciones(String palabra, int categoria, 
            int registros, int desde);
    
    List<PublicacionFacade> buscarPublicacionesPorCategoria(int categoriaId, int registros, int desde);
    
    byte[] leerImagen(int id);
    
}
