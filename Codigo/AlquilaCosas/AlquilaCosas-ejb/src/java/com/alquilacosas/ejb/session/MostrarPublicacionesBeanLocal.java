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
 * @author jorge
 */


@Local
public interface MostrarPublicacionesBeanLocal {
    
    List<PublicacionFacade> getPublicacionesRandom(int pagina);
    List<PublicacionFacade> getPublicacionesPoCategoria(int pagina,int categoria);
    List<byte[]> getImage(int id);
//    void setSelectedPublication(int id);
//    Publicacion getSelectedPublication();
}