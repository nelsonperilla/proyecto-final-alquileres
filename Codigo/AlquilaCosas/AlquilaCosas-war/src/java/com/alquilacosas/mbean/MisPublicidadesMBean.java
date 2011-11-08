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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
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
    
    public String republicar(){
        return "pregistrarPublicidad";
    }

    public Integer getPublicidadId() {
        System.out.println("id: " + publicidadId);
        return publicidadId;
    }

    public void setPublicidadId(Integer publicidadId) {
        this.publicidadId = publicidadId;
    }
}
