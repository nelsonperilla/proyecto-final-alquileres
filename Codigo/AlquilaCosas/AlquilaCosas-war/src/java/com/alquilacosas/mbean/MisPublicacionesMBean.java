/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.session.MisPublicacionesBeanLocal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author ignaciogiagante
 */
@ManagedBean(name = "misPublicaciones")
@ViewScoped
public class MisPublicacionesMBean {

    @EJB
    private MisPublicacionesBeanLocal misPublicacionesBean;
    @ManagedProperty(value="#{login}")
    private ManejadorUsuarioMBean usuarioLogueado;
    private List<PublicacionDTO> publicacionesDto;
    private int publicacionId;
    
    public MisPublicacionesMBean() {
    }
    
    
    @PostConstruct
    public void init() {
        if( usuarioLogueado.getUsuarioId() != null ){
            publicacionesDto = misPublicacionesBean.getPublicaciones(usuarioLogueado.getUsuarioId());
        }
  
    }

    public List<PublicacionDTO> getPublicacionesDto() {
        return publicacionesDto;
    }

    public void setPublicacionesDto(List<PublicacionDTO> publicacionesDto) {
        this.publicacionesDto = publicacionesDto;
    }
    

    public MisPublicacionesBeanLocal getMisPublicacionesBean() {
        return misPublicacionesBean;
    }

    public void setMisPublicacionesBean(MisPublicacionesBeanLocal misPublicacionesBean) {
        this.misPublicacionesBean = misPublicacionesBean;
    }

    public ManejadorUsuarioMBean getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(ManejadorUsuarioMBean usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    public int getPublicacionId() {
        return publicacionId;
    }

    public void setPublicacionId(int publicacionId) {
        this.publicacionId = publicacionId;
    }

    public String editarPublicacion(){
        return "modificarPublicacion";
    }
    
    public String mostrarPublicacion(){
       
        return "mostrarPublicacion";
        
    }
    
}
