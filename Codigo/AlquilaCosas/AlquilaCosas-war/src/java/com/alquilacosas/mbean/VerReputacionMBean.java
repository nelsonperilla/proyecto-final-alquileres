/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.dto.CalificacionDTO;
import com.alquilacosas.dto.UsuarioDTO;
import com.alquilacosas.ejb.session.VerReputacionBeanLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author wilson
 */
@ManagedBean(name = "reputacion")
@ViewScoped
public class VerReputacionMBean implements Serializable {
     
     @EJB
     private VerReputacionBeanLocal verReputacionBean;
     private Boolean usuarioCorrecto;
     private Integer usuarioId;
     private UsuarioDTO usuarioDTO;
     private List<CalificacionDTO> calificacionesOfrece;
     private List<CalificacionDTO> calificacionesToma;
     private String replica;

     /** Creates a new instance of VerReputacionMBean */
     public VerReputacionMBean() {
     }
     
     @PostConstruct
     public void init() {
          usuarioCorrecto = Boolean.TRUE;
          String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
          if (id != null) {
               try {
                    usuarioId = Integer.parseInt(id);
                    usuarioDTO = verReputacionBean.getUsuarioReputacionPorId(usuarioId);
                    calificacionesOfrece = verReputacionBean.getCalificacionOfrece(usuarioId);
                    calificacionesToma = verReputacionBean.getCalificacionToma(usuarioId);
               }
               catch (NumberFormatException e) {
                    usuarioCorrecto = Boolean.FALSE;
               }
          }
          else {
               usuarioCorrecto = Boolean.FALSE;
          }
     }
     
     public void verReplica(ActionEvent event) {
          replica = (String) event.getComponent().getAttributes().get("replica");
     }

     public List<CalificacionDTO> getCalificacionesOfrece() {
          return calificacionesOfrece;
     }

     public void setCalificacionesOfrece(List<CalificacionDTO> calificacionesOfrece) {
          this.calificacionesOfrece = calificacionesOfrece;
     }

     public List<CalificacionDTO> getCalificacionesToma() {
          return calificacionesToma;
     }

     public void setCalificacionesToma(List<CalificacionDTO> calificacionesToma) {
          this.calificacionesToma = calificacionesToma;
     }

     public Boolean getUsuarioCorrecto() {
          return usuarioCorrecto;
     }

     public void setUsuarioCorrecto(Boolean usuarioCorrecto) {
          this.usuarioCorrecto = usuarioCorrecto;
     }

     public Integer getUsuarioId() {
          return usuarioId;
     }

     public void setUsuarioId(Integer usuarioId) {
          this.usuarioId = usuarioId;
     }

     public UsuarioDTO getUsuarioDTO() {
          return usuarioDTO;
     }

     public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
          this.usuarioDTO = usuarioDTO;
     }

     public String getReplica() {
          return replica;
     }

     public void setReplica(String replica) {
          this.replica = replica;
     }
}

