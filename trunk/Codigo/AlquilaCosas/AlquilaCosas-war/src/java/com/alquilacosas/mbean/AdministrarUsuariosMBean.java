/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.dto.UsuarioDTO;
import com.alquilacosas.ejb.session.AdministrarUsuariosBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author ignaciogiagante
 */
@ManagedBean (name = "administrarUsuarios")
@ViewScoped
public class AdministrarUsuariosMBean implements Serializable {
    
    @EJB
    private AdministrarUsuariosBeanLocal usuarioBean;
    
    private List<UsuarioDTO> usuarios;
    private List<Integer> tipos;
    
    public AdministrarUsuariosMBean() {    
    }
    
    @PostConstruct
    public void init() {
        usuarios = usuarioBean.getUsuariosList();
        tipos = new ArrayList<Integer>();
    }
    
    public void asignarRol(RowEditEvent ev){
        
        try {
            
            UsuarioDTO usuarioDTO = (UsuarioDTO)ev.getObject();
            if( usuarioDTO.isUsuarioRol() ){
                tipos.add(1);
            }
            if( usuarioDTO.isAdminRol() ){
                tipos.add(2);
            }
            if( usuarioDTO.isPublicitanteRol() ){
                tipos.add(3);
            }
            
            usuarioBean.setRoles(usuarioDTO, tipos);

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.toString()));
        }
    }

    public List<UsuarioDTO> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<UsuarioDTO> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Integer> getTipos() {
        return tipos;
    }

    public void setTipos(List<Integer> tipos) {
        this.tipos = tipos;
    }
    
    
}
