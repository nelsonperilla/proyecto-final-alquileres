/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.entity.Publicacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ignaciogiagante
 */
@Local
public interface FavoritoBeanLocal {

    public List<PublicacionDTO> getFavoritos(Integer usuarioId);

    public void agregarFavorito( Integer usuarioId, PublicacionDTO pDto) throws AlquilaCosasException;

    public void eliminarFavorito(Integer usuarioId, Integer publicacionId) throws AlquilaCosasException;

    public Publicacion getFavorito(Integer usuarioId, Integer publicacionId);
    
}
