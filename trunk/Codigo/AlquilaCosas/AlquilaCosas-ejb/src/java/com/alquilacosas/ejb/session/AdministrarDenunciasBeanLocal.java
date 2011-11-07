/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.DenunciaDTO;
import com.alquilacosas.ejb.entity.Comentario;
import com.alquilacosas.ejb.entity.Denuncia;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wilson
 */
@Local
public interface AdministrarDenunciasBeanLocal {

     List<DenunciaDTO> getAllDenuncias();

     List<DenunciaDTO> getDenunciasPublicacion();

     List<DenunciaDTO> getDenunciasComentario();

     void aceptarDenuncia(int denunciaId);

     void rechazarDenuncia(int denunciaId);

     List<DenunciaDTO> convertirADenunciaDTO(List<Denuncia> denunciasList);

     void enviarEMailAdvertencia(Usuario usuario);

     void enviarEMailSuspencion(Usuario usuario);

}
