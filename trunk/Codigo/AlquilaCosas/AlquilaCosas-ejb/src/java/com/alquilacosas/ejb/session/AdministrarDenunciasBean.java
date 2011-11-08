/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.NotificacionEmail;
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
import com.alquilacosas.facade.AdvertenciaFacade;
import com.alquilacosas.facade.ComentarioFacade;
import com.alquilacosas.facade.DenunciaFacade;
import com.alquilacosas.facade.EstadoDenunciaFacade;
import com.alquilacosas.facade.EstadoPublicacionFacade;
import com.alquilacosas.facade.EstadoUsuarioFacade;
import com.alquilacosas.facade.PublicacionFacade;
import com.alquilacosas.facade.SuspensionFacade;
import com.alquilacosas.facade.UsuarioFacade;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import org.apache.log4j.Logger;

/**
 *
 * @author wilson
 */
@Stateless
public class AdministrarDenunciasBean implements AdministrarDenunciasBeanLocal {

     @Resource(name = "emailConnectionFactory")
     private ConnectionFactory connectionFactory;
     @Resource(name = "jms/notificacionEmailQueue")
     private Destination destination;
     @EJB
     private DenunciaFacade denunciaFacade;
     @EJB
     private UsuarioFacade usuarioFacade;
     @EJB
     private PublicacionFacade publicacionFacade;
     @EJB
     private ComentarioFacade comentarioFacade;
     @EJB
     private SuspensionFacade suspensionFacade;
     @EJB
     private AdvertenciaFacade advertenciaFacade;
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
          // Cambiar de Estado Denuncia
          EstadoDenuncia newEstadoDenuncia = estadoDenunciaFacade.findByNombre(EstadoDenuncia.NombreEstadoDenuncia.ACEPTADA);
          denuncia.getDenunciaXEstadoList().get(0).setFechaHasta(new Date());
          denuncia.agregarDenunciaXEstado(new DenunciaXEstado(denuncia, newEstadoDenuncia));
          denunciaFacade.edit(denuncia);
          //Si es Publicacion
          if (denuncia.getComentarioFk() == null) {
               //Suspender Publicacion
               Publicacion publicacionSuspender = publicacionFacade.find(denuncia.getPulicacionFk().getPublicacionId());
               EstadoPublicacion estadoSuspendidoSuspender = estadoPublicacionFacade.findByNombre(NombreEstadoPublicacion.SUSPENDIDA);
               PublicacionXEstado newEstadoPublicacionSuspender = new PublicacionXEstado(publicacionSuspender, estadoSuspendidoSuspender);
               publicacionSuspender.getEstadoPublicacionVigente().setFechaHasta(new Date());
               publicacionSuspender.agregarPublicacionXEstado(newEstadoPublicacionSuspender);
               publicacionFacade.edit(publicacionSuspender);

               //Advertir Usuario
               Usuario usuario = usuarioFacade.find(denuncia.getPulicacionFk().getUsuarioFk().getUsuarioId());
               Advertencia advertencia = new Advertencia();
               advertencia.setDenuncia(denuncia);
               advertencia.setFecha(new Date());
               usuario.agregarAdvertencia(advertencia);
               usuarioFacade.edit(usuario);
               usuarioFacade.refresh(usuario);

               //Suspender Usuario Si aplica
               if (usuario.getAdvertenciaList().size() % 3 == 0) {
                    //Cambiar Estado Usuario
                    EstadoUsuario estadoUsuarioSuspendido = estadoUsuarioFacade.findByNombre(NombreEstadoUsuario.SUSPENDIDO);
                    UsuarioXEstado newEstadoUsuario = new UsuarioXEstado(usuario, estadoUsuarioSuspendido);
                    usuario.getEstadoVigente().setFechaHasta(new Date());
                    usuario.agregarUsuarioXEstado(newEstadoUsuario);
                    usuarioFacade.edit(usuario);

                    //Agregar Suspencion                    
                    Suspension suspension = new Suspension();
                    suspension.setFechaDesde(new Date());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    cal.add(Calendar.MONTH, 1);
                    suspension.setFechaHasta(cal.getTime());
                    usuario.agregarSuspension(suspension);

                    //Asignar Suspencion a Advertencias
                    for (Advertencia a : usuario.getAdvertenciaList()) {
                         if (a.getSuspensionFk() == null) {
                              suspension.agregarAdvertencia(a);
                         }
                    }
                    usuarioFacade.edit(usuario);

                    //Suspender publicaciones
                    for (Publicacion p : usuario.getPublicacionList()) {
                         if (p.getPublicacionId() != denuncia.getPulicacionFk().getPublicacionId()) {
                              if (p.getEstadoPublicacionVigente().getEstadoPublicacion().getNombre().equals(NombreEstadoPublicacion.ACTIVA)) {
                                   Publicacion publicacion = publicacionFacade.find(p.getPublicacionId());
                                   EstadoPublicacion estadoSuspendido = estadoPublicacionFacade.findByNombre(NombreEstadoPublicacion.SUSPENDIDA);
                                   PublicacionXEstado newEstadoPublicacion = new PublicacionXEstado(publicacion, estadoSuspendido);
                                   publicacion.getEstadoPublicacionVigente().setFechaHasta(new Date());
                                   publicacion.agregarPublicacionXEstado(newEstadoPublicacion);
                                   publicacionFacade.edit(publicacion);
                              }
                         }
                    }
               }
               usuarioFacade.refresh(usuario);
               enviarEMailAdvertencia(usuario);
          } //Si es Comentario
          else {
               //Banear Comentario
               Comentario comentario = comentarioFacade.find(denuncia.getComentarioFk().getComentarioId());
               comentario.setBaneado(Boolean.TRUE);
               comentarioFacade.edit(comentario);

               //Advertir Usuario
               Usuario usuario = usuarioFacade.find(denuncia.getComentarioFk().getUsuarioFk().getUsuarioId());
               Advertencia advertencia = new Advertencia();
               advertencia.setDenuncia(denuncia);
               advertencia.setFecha(new Date());
               usuario.agregarAdvertencia(advertencia);
               usuarioFacade.edit(usuario);
               usuarioFacade.refresh(usuario);

               //Suspender Usuario si aplica
               if (usuario.getAdvertenciaList().size() % 3 == 0) {
                    //Cambiar Estado Usuario
                    EstadoUsuario estadoUsuarioSuspendido = estadoUsuarioFacade.findByNombre(NombreEstadoUsuario.SUSPENDIDO);
                    UsuarioXEstado newEstadoUsuario = new UsuarioXEstado(usuario, estadoUsuarioSuspendido);
                    usuario.getEstadoVigente().setFechaHasta(new Date());
                    usuario.agregarUsuarioXEstado(newEstadoUsuario);
                    usuarioFacade.edit(usuario);

                    //Agregar Suspencion                    
                    Suspension suspension = new Suspension();
                    suspension.setFechaDesde(new Date());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    cal.add(Calendar.MONTH, 1);
                    suspension.setFechaHasta(cal.getTime());
                    usuario.agregarSuspension(suspension);

                    //Asignar Suspencion a Advertencias
                    for (Advertencia a : usuario.getAdvertenciaList()) {
                         if (a.getSuspensionFk() == null) {
                              suspension.agregarAdvertencia(a);
                         }
                    }
                    usuarioFacade.edit(usuario);

                    //Suspender publicaciones
                    for (Publicacion p : usuario.getPublicacionList()) {
                         if (p.getEstadoPublicacionVigente().getEstadoPublicacion().getNombre().equals(NombreEstadoPublicacion.ACTIVA)) {
                              Publicacion publicacion = publicacionFacade.find(p.getPublicacionId());
                              EstadoPublicacion estadoSuspendido = estadoPublicacionFacade.findByNombre(NombreEstadoPublicacion.SUSPENDIDA);
                              PublicacionXEstado newEstadoPublicacion = new PublicacionXEstado(publicacion, estadoSuspendido);
                              publicacion.getEstadoPublicacionVigente().setFechaHasta(new Date());
                              publicacion.agregarPublicacionXEstado(newEstadoPublicacion);
                              publicacionFacade.edit(publicacion);
                         }
                    }
               }
               usuarioFacade.refresh(usuario);
               enviarEMailAdvertencia(usuario);
          }
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
     @RolesAllowed({"USUARIO", "ADMIN"})
     public List<DenunciaDTO> convertirADenunciaDTO(List<Denuncia> denunciasList) {
          List<DenunciaDTO> denunciasDTO = new ArrayList<DenunciaDTO>();
          for (Denuncia d : denunciasList) {
               DenunciaDTO denunciaDTO = new DenunciaDTO();
               denunciaDTO.setComentarioId(d.getComentarioFk() == null ? null : d.getComentarioFk().getComentarioId());
               denunciaDTO.setDenunciaId(d.getDenunciaId());
               denunciaDTO.setExplicacion(d.getExplicacion());
               denunciaDTO.setFecha(d.getFecha());
               denunciaDTO.setNombreMotivo(d.getMotivoFk().getNombre());
               denunciaDTO.setPublicacionId(d.getComentarioFk() == null ? d.getPulicacionFk().getPublicacionId() : d.getComentarioFk().getPublicacionFk().getPublicacionId());
               denunciaDTO.setTextoComentario(d.getComentarioFk() == null ? null : d.getComentarioFk().getComentario());
               denunciaDTO.setTextoPublicacion(d.getComentarioFk() != null ? null : d.getPulicacionFk().getTitulo());

               //si viene la lista de denuncias de comentarios, el id
               denunciaDTO.setUsuarioId(d.getComentarioFk() != null ? d.getComentarioFk().getUsuarioFk().getUsuarioId() : d.getPulicacionFk().getUsuarioFk().getUsuarioId());
               denunciaDTO.setUsuarioUsername(d.getComentarioFk() != null ? d.getComentarioFk().getUsuarioFk().getLoginList().get(0).getUsername() : d.getPulicacionFk().getUsuarioFk().getLoginList().get(0).getUsername());
               denunciasDTO.add(denunciaDTO);
          }
          return denunciasDTO;
     }

     @Override
     public void enviarEMailAdvertencia(Usuario usuario) {
          // enviar email
          String asunto = "Has recibido una Advertencia de AlquilaCosas";
          String mensaje = "<html>Hola " + usuario.getNombre() + ",<br/><br/>"
                  + "Has recibido una advertencia por violar los terminos y condiciones de uso de AlquilaCosas. "
                  + "Recuerda que al recibir 3 (tres) advertencias, tu usuario será suspendido, para conocer tus advertencias dirigete a 'Ver Reputacion'<br/><br/>"
                  + "Atentamente,<br/>"
                  + "<b>AlquilaCosas</b></html>";
          NotificacionEmail email = new NotificacionEmail(usuario.getEmail(), asunto, mensaje);

          try {
               Connection connection = connectionFactory.createConnection();
               Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
               MessageProducer producer = session.createProducer(destination);
               ObjectMessage message = session.createObjectMessage();
               message.setObject(email);
               producer.send(message);
               session.close();
               connection.close();
          } catch (Exception e) {
               Logger.getLogger(AdministrarDenunciasBean.class).error("enviarEmail(). "
                       + "Excepcion al enviar email: " + e + ": " + e.getMessage());
          }
          if (usuario.getAdvertenciaList().size() % 3 == 0) {
               enviarEMailSuspencion(usuario);
          }
     }

     @Override
     public void enviarEMailSuspencion(Usuario usuario) {
          // enviar email
          String asunto = "Has Suspendido de AlquilaCosas";
          String mensaje = "<html>Hola " + usuario.getNombre() + ",<br/><br/>"
                  + "Has sido Suspendido por acumular 3 (tres) advertencias de violacion a los terminos y condiciones de uso de AlquilaCosas. "
                  + "Por el trascurso de 1 (un) mes no podrás ingresar con tu login en nuestro sitio<br/><br/>"
                  + "Atentamente,<br/>"
                  + "<b>AlquilaCosas</b></html>";
          NotificacionEmail email = new NotificacionEmail(usuario.getEmail(), asunto, mensaje);

          try {
               Connection connection = connectionFactory.createConnection();
               Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
               MessageProducer producer = session.createProducer(destination);
               ObjectMessage message = session.createObjectMessage();
               message.setObject(email);
               producer.send(message);
               session.close();
               connection.close();
          } catch (Exception e) {
               Logger.getLogger(AdministrarDenunciasBean.class).error("enviarEmail(). "
                       + "Excepcion al enviar email: " + e + ": " + e.getMessage());
          }
     }
}