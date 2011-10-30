/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.ComentarioDTO;
import com.alquilacosas.dto.DenunciaDTO;
import com.alquilacosas.ejb.entity.MotivoDenuncia;
import java.util.List;
import javax.ejb.Local;
import javax.faces.model.SelectItem;


/**
 *
 * @author jorge
 */
@Local
public interface DenunciaPreguntaBeanLocal {

    public ComentarioDTO getComentario(int comentarioId);

    public String getPublication(int publicacionId);

    public List<MotivoDenuncia> getMotivosDenuncia();

    public void saveDenuncia(DenunciaDTO nuevaDenuncia, int motivoSeleccionado);


    
}
