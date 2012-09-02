/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.dto.PublicidadDTO;
import com.alquilacosas.ejb.session.PublicidadBeanLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

/**
 *
 * @author ignaciogiagante
 */
@ManagedBean (name = "misPublicidades")
@ViewScoped
public class MisPublicidadesMBean implements Serializable {

    @ManagedProperty(value="#{login}")
    private ManejadorUsuarioMBean usuarioLogueado;
    @EJB
    private PublicidadBeanLocal publicidadBean;
    private List<PublicidadDTO> publicidades;
    private PublicidadDTO selectedPublicidad;
    private Integer publicidadId;
    
    /** Creates a new instance of MisPublicidadesMBean */
    public MisPublicidadesMBean() {
    }
    
    @PostConstruct
    public void init() {
        Logger.getLogger(MisPublicidadesMBean.class).debug("MisPublicidadesMBean: postconstruct."); 
        if( usuarioLogueado.getUsuarioId() != null ){
           publicidades = publicidadBean.getPublicidades(usuarioLogueado.getUsuarioId());
        }
    }
    
    public void eliminarPublicidad() {
        try {
            publicidadBean.eliminarPublicidad(selectedPublicidad.getId());
            publicidades.remove(selectedPublicidad);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar publicidad", e.getMessage()));
        }
    }
    
    public String republicar(){
        return "pregistrarPublicidad";
    }
    
    /*
     * Getters & Setters
     */

    public List<PublicidadDTO> getPublicidades() {
        return publicidades;
    }    
    
    public void setPublicidades(List<PublicidadDTO> publicidades) {
        this.publicidades = publicidades;
    }

    public ManejadorUsuarioMBean getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(ManejadorUsuarioMBean usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    public PublicidadDTO getSelectedPublicidad() {
        return selectedPublicidad;
    }

    public void setSelectedPublicidad(PublicidadDTO selectedPublicidad) {
        this.selectedPublicidad = selectedPublicidad;
    }

    public Integer getPublicidadId() {
        return publicidadId;
    }

    public void setPublicidadId(Integer publicidadId) {
        this.publicidadId = publicidadId;
    }
}
