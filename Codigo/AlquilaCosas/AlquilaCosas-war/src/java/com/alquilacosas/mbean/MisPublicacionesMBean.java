/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.session.MisPublicacionesBeanLocal;
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
@ManagedBean(name = "misPublicaciones")
@ViewScoped
public class MisPublicacionesMBean implements Serializable {

    @EJB
    private MisPublicacionesBeanLocal misPublicacionesBean;
    @ManagedProperty(value="#{login}")
    private ManejadorUsuarioMBean usuarioLogueado;
    private List<PublicacionDTO> publicacionesDto;
    private int publicacionId;
    private String tituloPublicacion;
    
    public MisPublicacionesMBean() {
    }
    
    
    @PostConstruct
    public void init() {
        Logger.getLogger(MisPublicacionesMBean.class).debug("MisPublicacionesMBean: postconstruct."); 
        if( usuarioLogueado.getUsuarioId() != null ){
            publicacionesDto = misPublicacionesBean.getPublicaciones(usuarioLogueado.getUsuarioId());
        }
  
    }
    
    public String borrarPublicacion(){
        System.out.println("publicacionId: " + publicacionId);
        try {
            misPublicacionesBean.borrarPublicacion(publicacionId);
            FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("La publicacion fue eliminada correctamente"));
            return "/vistas/usuario/articulos";
        } catch( AlquilaCosasException e ){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al actualizar usuario", e.getMessage()));
        }
        return null;
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
    
}
