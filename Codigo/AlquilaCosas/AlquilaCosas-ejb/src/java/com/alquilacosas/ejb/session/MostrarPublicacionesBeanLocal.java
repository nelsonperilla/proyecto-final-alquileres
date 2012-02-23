/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.PublicacionDTO;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge
 */


@Local
public interface MostrarPublicacionesBeanLocal {
    
    public List<PublicacionDTO> getPublicacionesRandom(int filas);
    
    public List<byte[]> getImage(int id);

}