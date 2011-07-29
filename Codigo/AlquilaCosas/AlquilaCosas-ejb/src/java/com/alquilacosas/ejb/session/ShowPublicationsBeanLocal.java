/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.Categoria;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge
 */


@Local
public interface ShowPublicationsBeanLocal {
    
    List<Publicacion> getPublicacionesRandom(int pagina);
    List<Publicacion> getPublicacionesPoCategoria(int pagina,int categoria);
    
   
    
}