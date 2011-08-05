/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.Categoria;
import java.awt.image.BufferedImage;
import java.sql.Blob;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge
 */


@Local
public interface MostrarPublicacionesBeanLocal {
    
    List<Publicacion> getPublicacionesRandom(int pagina);
    List<Publicacion> getPublicacionesPoCategoria(int pagina,int categoria);
    List<byte[]> getImage(int id);
//    void setSelectedPublication(int id);
//    Publicacion getSelectedPublication();
}