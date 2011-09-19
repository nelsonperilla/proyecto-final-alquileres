/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.dto.CalificacionDTO;
import com.alquilacosas.ejb.entity.Puntuacion;
import com.alquilacosas.ejb.session.AlquileresTomadosBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
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
public class AlquileresTomadosMBean implements Serializable {

     @EJB
     private AlquileresTomadosBeanLocal alquileresTomadosBean;
     @ManagedProperty(value = "#{login}")
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
     private Boolean ofrece;
     private Boolean toma;
     private CalificacionDTO calificacionOfrece;
     private CalificacionDTO calificacionToma;
     private Boolean ofreceCalifico;
     private Boolean tomaCalifico;
     //unicamente para la prueba
     private Integer puntuacionId = 1;

     /** Creates a new instance of AlquileresTomadosMBean */
     public AlquileresTomadosMBean() {
     }

     @PostConstruct
     public void init() {
          usuarioId = usuarioMBean.getUsuarioId();
          filtros = new ArrayList<SelectItem>();
          filtros.add(new SelectItem(0, "Confirmados Y Activos"));
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
          switch (filtroSeleccionado) {
               /* Alquileres Activos y Confirmados */
               case 0: {
                    try {
                         alquileres = alquileresTomadosBean.getAlquileresActivosPorUsuario(usuarioId);
                    } catch (AlquilaCosasException e) {
                         FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error  al recuperar los Alquileres" + e.toString(), "");
                         FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                    break;
               }
               /* Alquileres Finalizados, cancelados, cancelados por alquilador  Sin Calificación */
               case 1: {
                    try {
                         alquileres = alquileresTomadosBean.getAlquileresSinCalificarPorUsuario(usuarioId);
                    } catch (AlquilaCosasException e) {
                         FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error  al recuperar los Alquileres" + e.toString(), "");
                         FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                    break;
               }
               /* Alquileres Finalizados, cancelados, cancelados por alquilador CON Clasificación */
               case 2: {
                    try {
                         alquileres = alquileresTomadosBean.getAlquileresConCalificarPorUsuario(usuarioId);
                    } catch (AlquilaCosasException e) {
                         FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error  al recuperar los Alquileres" + e.toString(), "");
                         FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                    break;
               }
          }
     }

     public String verPublicacion() {
          return "mostrarPublicacion";
     }

     public String verUsuario() {
          return "";
     }

     public void prepararCalificar(ActionEvent event) {
          alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
     }

     public void prepararVerCalificacion(ActionEvent event) {
          alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
          ofreceCalifico = true;
          tomaCalifico = true;
          try {
               calificacionOfrece = alquileresTomadosBean.getCalificacionOfrece(alquilerId);
               calificacionToma = alquileresTomadosBean.getCalificacionToma(alquilerId);
               // Lo siguiente sirve para mostrar los campos permitidos
               if (calificacionOfrece.getIdUsuarioCalidicador() != null) {
                    ofrece = calificacionOfrece.getIdUsuarioCalidicador() == usuarioId;
               } else {
                    ofreceCalifico = false;
               }
               if (calificacionToma.getIdUsuarioCalidicador() != null) {
                    toma = calificacionToma.getIdUsuarioCalidicador() == usuarioId;
               } else {
                    tomaCalifico = false;
               }
          } catch (AlquilaCosasException e) {
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error  al recuperar las Calificaciones" + e.toString(), "");
               FacesContext.getCurrentInstance().addMessage(null, message);
          }
     }

     public void prepararCancelarAlquiler(ActionEvent event) {
          alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
     }

     public void prepararPedirCambioAlquiler(ActionEvent event) {
          alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
     }

     public void registrarCalificacion() {
          try {
               alquileresTomadosBean.registrarCalificacion((Integer) puntuacion.getValue(), alquilerId, comentario, usuarioId);
          } catch (AlquilaCosasException e) {
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Registrar Calificación " + e.toString(), "");
               FacesContext.getCurrentInstance().addMessage(null, message);
          }
          //unicamente prueba
          /*
          try {
          alquileresTomadosBean.registrarCalificacion(1, 1, "lalalalala", 1);
          }
          catch (AlquilaCosasException e){
          FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Registrar Calificación " + e.toString(), "");  
          FacesContext.getCurrentInstance().addMessage(null, message);
          }
           * 
           */
     }

     public void registrarReplicaToma() {
          try {
               alquileresTomadosBean.registrarReplica(calificacionOfrece.getIdCalificacion(), calificacionOfrece.getComentarioReplica(), usuarioId);
               calificacionOfrece.setYaReplico(true);
          } catch (AlquilaCosasException e) {
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Registrar Replica " + e.toString(), "");
               FacesContext.getCurrentInstance().addMessage(null, message);
          }
     }

     public void registrarReplicaOfrece() {
          try {
               alquileresTomadosBean.registrarReplica(calificacionToma.getIdCalificacion(), calificacionToma.getComentarioReplica(), usuarioId);
               calificacionToma.setYaReplico(true);
          } catch (AlquilaCosasException e) {
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Registrar Replica " + e.toString(), "");
               FacesContext.getCurrentInstance().addMessage(null, message);
          }
     }

     public void cancelarAlquiler() {
          boolean borrado = false;
          try {
               borrado = alquileresTomadosBean.cancelarAlquiler(alquilerId);
          } catch (AlquilaCosasException e) {
               FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                       "Error al cancelar alquiler", e.getMessage());
               FacesContext.getCurrentInstance().addMessage(null, msg);
          }
          if (borrado) {
               for (int i = 0; i < alquileres.size(); i++) {
                    AlquilerDTO alq = alquileres.get(i);
                    if (alq.getIdAlquiler() == alquilerId) {
                         alquileres.remove(alq);
                         return;
                    }
               }
          }
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

     public Boolean getOfrece() {
          return ofrece;
     }

     public void setOfrece(Boolean ofrece) {
          this.ofrece = ofrece;
     }

     public Boolean getToma() {
          return toma;
     }

     public void setToma(Boolean toma) {
          this.toma = toma;
     }

     public CalificacionDTO getCalificacionOfrece() {
          return calificacionOfrece;
     }

     public void setCalificacionOfrece(CalificacionDTO calificacionOfrece) {
          this.calificacionOfrece = calificacionOfrece;
     }

     public CalificacionDTO getCalificacionToma() {
          return calificacionToma;
     }

     public void setCalificacionToma(CalificacionDTO calificacionToma) {
          this.calificacionToma = calificacionToma;
     }

     public Boolean getOfreceCalifico() {
          return ofreceCalifico;
     }

     public void setOfreceCalifico(Boolean OfreceCalifico) {
          this.ofreceCalifico = OfreceCalifico;
     }

     public Boolean getTomaCalifico() {
          return tomaCalifico;
     }

     public void setTomaCalifico(Boolean TomaCalifico) {
          this.tomaCalifico = TomaCalifico;
     }
}
