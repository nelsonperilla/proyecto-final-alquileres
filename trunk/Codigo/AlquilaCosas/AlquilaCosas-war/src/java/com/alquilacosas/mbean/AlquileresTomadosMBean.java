/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.ejb.entity.Alquiler;
import com.alquilacosas.ejb.entity.Calificacion;
import com.alquilacosas.ejb.entity.Puntuacion;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.ejb.session.AlquileresTomadosBeanLocal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

/**
 *
 * @author wilson
 */
@ManagedBean(name = "alquileresTomados")
@ViewScoped
public class AlquileresTomadosMBean {

     @EJB
     private AlquileresTomadosBeanLocal alquileresTomadosBean;
     @ManagedProperty(value="#{login}")
     private ManejadorUsuarioMBean usuarioMBean;
     private List<SelectItem> filtros;
     private int filtroSeleccionado;
     private List<AlquilerDTO> alquileres;
     private int usuarioId;
     private int publicacionId;
     private Integer alquilerId;
     private List<SelectItem> puntuaciones;
     private SelectItem puntuacion;
     private String comentario;
     
     /** Creates a new instance of AlquileresTomadosMBean */
     public AlquileresTomadosMBean() {
     }
     
     @PostConstruct
     public void init(){
          usuarioId = usuarioMBean.getUsuarioId();
          filtros = new ArrayList<SelectItem>();
          filtros.add(new SelectItem(0, "ACTIVO"));
          filtros.add(new SelectItem(1, "Finalizado SIN Calificación"));
          filtros.add(new SelectItem(2, "Finalizado CON Calificación"));
          filtroSeleccionado = 1;
          puntuaciones = new ArrayList<SelectItem>();          
          List<Puntuacion> listaPuntuacion = alquileresTomadosBean.getPuntuaciones();
          if (!listaPuntuacion.isEmpty()) {
               for (Puntuacion p : listaPuntuacion) {
                    puntuaciones.add(new SelectItem(p.getPuntuacionId(), p.getNombre()));
               }
          }
          actualizarAlquileres();
     }
     
     public void actualizarAlquileres() {
          switch(filtroSeleccionado){
               case 0:
                    
               case 1: /* Alquileres Finalizados Sin Calificación */
                    try {
                         alquileres = alquileresTomadosBean.getAlquileresSinCalificarPorUsuario(usuarioId);
                    }
                    catch(AlquilaCosasException e) {
                         FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error  al recuperar los Alquileres" + e.toString(), "");  
                         FacesContext.getCurrentInstance().addMessage(null, message);
                    }        
               case 2:
                    
          }
     }   
     
     public String verPublicacion() {
          return "";
     }
     
     public String verUsuario() {
          return "";
     }
     
     public void tomarAlquilerACalificar(ActionEvent event) {
          alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
     }
     
     public void verCalificacion(ActionEvent event) {
          alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
          // llama al bea y obtiene las calificaciones para este alquiler
          
     }
     
     public void registrarCalificacion() {
          System.out.println("algo");
          Alquiler alquiler = alquileresTomadosBean.getAlquilerPorId(alquilerId);
          Usuario usuario = alquileresTomadosBean.getUsuarioPorId(usuarioId);
          Puntuacion nuevaPuntuacion = alquileresTomadosBean.getPuntuacionesPorId((Integer)puntuacion.getValue());
          
          Calificacion nuevaCalificacion = new Calificacion();
          nuevaCalificacion.setFechaCalificacion(new Date());
          nuevaCalificacion.setPuntuacionFk(nuevaPuntuacion);
          nuevaCalificacion.setComentarioCalificador(comentario);          
          nuevaCalificacion.setAlquilerFk(alquiler);
          nuevaCalificacion.setUsuarioCalificadorFk(usuario);
          
          alquileresTomadosBean.registrarCalificacion(nuevaCalificacion);
     }

     public int getFiltroSeleccionado() {
          return filtroSeleccionado;
     }

     public void setFiltroSeleccionado(int filtroSeleccionado) {
          this.filtroSeleccionado = filtroSeleccionado;
     }

     public List<SelectItem> getFiltros() {
          return filtros;
     }

     public void setFiltros(List<SelectItem> filtros) {
          this.filtros = filtros;
     }
     
     public List<AlquilerDTO> getAlquileres() {
          return alquileres;
     }

     public void setAlquileres(List<AlquilerDTO> alquileres) {
          this.alquileres = alquileres;
     }

     public int getPublicacionId() {
          return publicacionId;
     }

     public void setPublicacionId(int publicacionId) {
          this.publicacionId = publicacionId;
     }

     public int getUsuarioId() {
          return usuarioId;
     }

     public void setUsuarioId(int usuarioId) {
          this.usuarioId = usuarioId;
     }

     public Integer getAlquilerId() {
          return alquilerId;
     }

     public void setAlquilerId(Integer alquilerId) {
          this.alquilerId = alquilerId;
     }

     public ManejadorUsuarioMBean getUsuarioMBean() {
          return usuarioMBean;
     }

     public void setUsuarioMBean(ManejadorUsuarioMBean usuarioMBean) {
          this.usuarioMBean = usuarioMBean;
     }

     public String getComentario() {
          return comentario;
     }

     public void setComentario(String comentario) {
          this.comentario = comentario;
     }

     public SelectItem getPuntuacion() {
          return puntuacion;
     }

     public void setPuntuacion(SelectItem puntuacion) {
          this.puntuacion = puntuacion;
     }

     public List<SelectItem> getPuntuaciones() {
          return puntuaciones;
     }

     public void setPuntuaciones(List<SelectItem> puntuaciones) {
          this.puntuaciones = puntuaciones;
     }

}
