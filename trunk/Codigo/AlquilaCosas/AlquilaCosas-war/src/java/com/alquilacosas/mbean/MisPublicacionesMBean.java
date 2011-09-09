/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.session.MisPublicacionesBeanLocal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

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
    private String tituloPublicacion;
    
    public MisPublicacionesMBean() {
    }
    
    
    @PostConstruct
    public void init() {
        if( usuarioLogueado.getUsuarioId() != null ){
            publicacionesDto = misPublicacionesBean.getPublicaciones(usuarioLogueado.getUsuarioId());
        }
  
    }
    
    public void prepararBorrar() {
        //publicacionId = (Integer) event.getComponent().getAttributes().get("id");
    }
    
    public String borrarPublicacion(){
        try {
            misPublicacionesBean.borrarPublicacion(publicacionId);
            FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("La publicacion fue eliminada correctamente"));
            //publicacionesFacade = misPublicacionesBean.getPublicaciones(usuarioMBean.getUsuarioId());
            return "misPublicaciones";
        } catch( AlquilaCosasException e ){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al actualizar usuario", e.getMessage()));
        }
        return null;
    }
    
    public String editarPublicacion(){
        return "modificarPublicacion";
    }
    
    public String mostrarPublicacion(){
       
        return "mostrarPublicacion";
        
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
