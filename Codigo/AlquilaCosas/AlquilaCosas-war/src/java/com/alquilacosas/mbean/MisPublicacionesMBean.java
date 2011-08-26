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
    private ManejadorUsuarioMBean usuarioMBean;
    private List<PublicacionDTO> publicacionesFacade;
    private int publicacionId;
    
    public MisPublicacionesMBean() {
    }
    
    
    @PostConstruct
    public void init() {
        if( usuarioMBean.getUsuarioId() != null ){
            publicacionesFacade = misPublicacionesBean.getPublicaciones(usuarioMBean.getUsuarioId());
        }
  
    }

    public List<PublicacionDTO> getPublicacionesFacade() {
        return publicacionesFacade;
    }

    public void setPublicacionesFacade(List<PublicacionDTO> publicacionesFacade) {
        this.publicacionesFacade = publicacionesFacade;
    }
    

    public MisPublicacionesBeanLocal getMisPublicacionesBean() {
        return misPublicacionesBean;
    }

    public void setMisPublicacionesBean(MisPublicacionesBeanLocal misPublicacionesBean) {
        this.misPublicacionesBean = misPublicacionesBean;
    }

    public ManejadorUsuarioMBean getUsuarioMBean() {
        return usuarioMBean;
    }

    public void setUsuarioMBean(ManejadorUsuarioMBean usuarioMBean) {
        this.usuarioMBean = usuarioMBean;
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
