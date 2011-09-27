/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.CalificacionDTO;
import com.alquilacosas.dto.UsuarioDTO;
import com.alquilacosas.ejb.session.VerReputacionBeanLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
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
        usuarioCorrecto = true;
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id == null) {
            usuarioCorrecto = false;
            return;
        }
        try {
            usuarioId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            usuarioCorrecto = false;
            return;
        }
        try {
            usuarioDTO = verReputacionBean.getUsuarioReputacionPorId(usuarioId);
        } catch (AlquilaCosasException e) {
            usuarioCorrecto = false;
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al leer reputacion de usuario", e.getMessage());
        }
        calificacionesOfrece = verReputacionBean.getCalificacionOfrece(usuarioId);
        calificacionesToma = verReputacionBean.getCalificacionToma(usuarioId);
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
