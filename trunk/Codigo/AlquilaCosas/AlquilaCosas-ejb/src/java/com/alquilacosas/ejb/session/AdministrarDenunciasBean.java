/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.DenunciaDTO;
import com.alquilacosas.ejb.entity.Advertencia;
import com.alquilacosas.ejb.entity.Comentario;
import com.alquilacosas.ejb.entity.Denuncia;
import com.alquilacosas.ejb.entity.DenunciaXEstado;
import com.alquilacosas.ejb.entity.EstadoDenuncia;
import com.alquilacosas.ejb.entity.EstadoPublicacion;
import com.alquilacosas.ejb.entity.EstadoPublicacion.NombreEstadoPublicacion;
import com.alquilacosas.ejb.entity.EstadoUsuario;
import com.alquilacosas.ejb.entity.EstadoUsuario.NombreEstadoUsuario;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.PublicacionXEstado;
import com.alquilacosas.ejb.entity.Suspension;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.ejb.entity.UsuarioXEstado;
import com.alquilacosas.facade.ComentarioFacade;
import com.alquilacosas.facade.DenunciaFacade;
import com.alquilacosas.facade.EstadoDenunciaFacade;
import com.alquilacosas.facade.EstadoPublicacionFacade;
import com.alquilacosas.facade.EstadoUsuarioFacade;
import com.alquilacosas.facade.PublicacionFacade;
import com.alquilacosas.facade.UsuarioFacade;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author wilson
 */
@Stateless
public class AdministrarDenunciasBean implements AdministrarDenunciasBeanLocal {

     @EJB
     private DenunciaFacade denunciaFacade;
     @EJB
     private PublicacionFacade publicacionFacade;
     @EJB
     private ComentarioFacade comentarioFacade;
     @EJB
     private UsuarioFacade usuarioFacade;
     @EJB
     private EstadoDenunciaFacade estadoDenunciaFacade;
     @EJB
     private EstadoPublicacionFacade estadoPublicacionFacade;
     @EJB
     private EstadoUsuarioFacade estadoUsuarioFacade;

     // Add business logic below. (Right-click in editor and choose
     // "Insert Code > Add Business Method")
     @Override
     @RolesAllowed({"USUARIO", "ADMIN"})
     public List<DenunciaDTO> getAllDenuncias() {
          List<Denuncia> denuncias = denunciaFacade.getAllDenuncias();
          return convertirADenunciaDTO(denuncias);
     }

     @Override
     @RolesAllowed({"USUARIO", "ADMIN"})
     public List<DenunciaDTO> getDenunciasPublicacion() {
          List<Denuncia> denuncias = denunciaFacade.getDenunciasPublicacion();
          return convertirADenunciaDTO(denuncias);
     }

     @Override
     @RolesAllowed({"USUARIO", "ADMIN"})
     public List<DenunciaDTO> getDenunciasComentario() {
          List<Denuncia> denuncias = denunciaFacade.getDenunciasComentario();
          return convertirADenunciaDTO(denuncias);
     }

     @Override
     @RolesAllowed({"USUARIO", "ADMIN"})
     public void aceptarDenuncia(int denunciaId) {
          Denuncia denuncia = denunciaFacade.find(denunciaId);
          if (denuncia.getComentarioFk() == null) {
               Usuario usuario = usuarioFacade.find(denuncia.getPulicacionFk().getUsuarioFk().getUsuarioId());
               // Publicacion: Suspender Publicacion
               suspenderPublicacion(denuncia.getPulicacionFk().getPublicacionId(), usuario);
               // Advertir Usuario
               advertirUsuario(denuncia, usuario);
               usuarioFacade.edit(usuario);
          }
          else {
               Usuario usuario = usuarioFacade.find(denuncia.getComentarioFk().getUsuarioFk().getUsuarioId());
               // Comentario: Banear Comentario
               banearComentario(usuario,denuncia.getComentarioFk().getComentarioId());
               // Advertir Usuario
               advertirUsuario(denuncia, usuario);
               usuarioFacade.edit(usuario);
          }
          // Cambiar de Estado Denuncia
          EstadoDenuncia newEstadoDenuncia = estadoDenunciaFacade.findByNombre(EstadoDenuncia.NombreEstadoDenuncia.ACEPTADA);
          denuncia.getDenunciaXEstadoList().get(0).setFechaHasta(new Date());
          denuncia.agregarDenunciaXEstado(new DenunciaXEstado(denuncia, newEstadoDenuncia));
          denunciaFacade.edit(denuncia);
     }
     
     @Override
     @RolesAllowed({"USUARIO", "ADMIN"})
     public void rechazarDenuncia(int denunciaId) {
          // Cambiar de Estado Denuncia
          Denuncia denuncia = denunciaFacade.find(denunciaId);
          EstadoDenuncia newEstadoDenuncia = estadoDenunciaFacade.findByNombre(EstadoDenuncia.NombreEstadoDenuncia.RECHAZADA);
          denuncia.getDenunciaXEstadoList().get(0).setFechaHasta(new Date());
          denuncia.agregarDenunciaXEstado(new DenunciaXEstado(denuncia, newEstadoDenuncia));
          denunciaFacade.edit(denuncia);
     }

     @Override
     public List<DenunciaDTO> convertirADenunciaDTO(List<Denuncia> denunciasList) {
          List<DenunciaDTO> denunciasDTO = new ArrayList<DenunciaDTO>();
          for(Denuncia d : denunciasList) {
               DenunciaDTO denunciaDTO = new DenunciaDTO();
               denunciaDTO.setComentarioId(d.getComentarioFk() == null ? null : d.getComentarioFk().getComentarioId());
               denunciaDTO.setDenunciaId(d.getDenunciaId());
               denunciaDTO.setExplicacion(d.getExplicacion());
               denunciaDTO.setFecha(d.getFecha());
               denunciaDTO.setNombreMotivo(d.getMotivoFk().getNombre());
               denunciaDTO.setPublicacionId(d.getComentarioFk() == null ? d.getPulicacionFk().getPublicacionId() : d.getComentarioFk().getPublicacionFk().getPublicacionId());
               denunciaDTO.setTextoComentario(d.getComentarioFk() == null ? null : d.getComentarioFk().getComentario());
               denunciaDTO.setTextoPublicacion(d.getPulicacionFk() == null ? null : d.getPulicacionFk().getTitulo());
               denunciaDTO.setUsuarioId(d.getPulicacionFk() == null ? d.getComentarioFk().getUsuarioFk().getUsuarioId() : d.getPulicacionFk().getUsuarioFk().getUsuarioId());
               denunciaDTO.setUsuarioUsername(d.getPulicacionFk() == null ? d.getComentarioFk().getUsuarioFk().getLoginList().get(0).getUsername() : d.getPulicacionFk().getUsuarioFk().getLoginList().get(0).getUsername());
               denunciasDTO.add(denunciaDTO);
          }
          return denunciasDTO;
     }

     @Override
     public void suspenderPublicacion(int idPublicacion, Usuario usuario) {
          for (Publicacion p : usuario.getPublicacionList()) {
               if (p.getPublicacionId() == idPublicacion) {
                    EstadoPublicacion estadoSuspendido = estadoPublicacionFacade.findByNombre(NombreEstadoPublicacion.SUSPENDIDA);
                    PublicacionXEstado newEstadoPublicacion = new PublicacionXEstado(p, estadoSuspendido);
                    p.getEstadoPublicacionVigente().setFechaHasta(new Date());
                    p.agregarPublicacionXEstado(newEstadoPublicacion);
               }
          }
     }

     @Override
     public void banearComentario(Usuario usuario, int idComentario) {
          for (Comentario c : usuario.getComentarioList())
               if (c.getComentarioId() == idComentario)
                    c.setBaneado(Boolean.TRUE);
     }

     @Override
     public void advertirUsuario(Denuncia denuncia, Usuario usuario) {
          Advertencia advertencia = new Advertencia();
          advertencia.setDenuncia(denuncia);
          advertencia.setFecha(new Date());
          usuario.agregarAdvertencia(advertencia);
          // Si Aplica: Suspender Usuario
          if (usuario.getAdvertenciaList().size() % 3 == 0) {
               if (denuncia.getComentarioFk() == null)
                    suspenderUsuario(usuario, denuncia.getPulicacionFk().getPublicacionId());
               else
                    suspenderUsuario(usuario, -1);
          }
     }

     @Override
     public void suspenderUsuario(Usuario usuario, int idPublicacionYaSuspendida) {
          // Desactivar sus Publicaciones
          for(Publicacion p : usuario.getPublicacionList())
               if (p.getPublicacionId() != idPublicacionYaSuspendida)
                    suspenderPublicacion(p.getPublicacionId(), usuario);
          EstadoUsuario estadoUsuarioSuspendido = estadoUsuarioFacade.findByNombre(NombreEstadoUsuario.SUSPENDIDO);
          UsuarioXEstado newEstadoUsuario = new UsuarioXEstado(usuario, estadoUsuarioSuspendido);
          usuario.getEstadoVigente().setFechaHasta(new Date());
          usuario.agregarUsuarioXEstado(newEstadoUsuario);
          Suspension suspension = new Suspension();
          suspension.setFechaDesde(new Date());
          Calendar cal = Calendar.getInstance();
          cal.setTime(new Date());
          cal.add(Calendar.MONTH, 1);
          suspension.setFechaHasta(cal.getTime());
          for(Advertencia a : usuario.getAdvertenciaList())
               if (a.getSuspensionFk() == null) {
                    suspension.agregarAdvertencia(a);
                    a.setSuspensionFk(suspension);
               }          
          usuario.agregarSuspension(suspension);
     }
}
