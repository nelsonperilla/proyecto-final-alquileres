/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.entity.Favorito;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ignaciogiagante
 */
@Local
public interface FavoritoLocalBean {

    List<PublicacionDTO> getFavoritos(Integer usuarioId);

    void agregarFavorito( Integer usuarioId, PublicacionDTO pDto) throws AlquilaCosasException;

    void eliminarFavorito(Integer usuarioId, Integer publicacionId) throws AlquilaCosasException;

    Favorito getFavorito(Integer usuarioId, Integer publicacionId);
    
}
