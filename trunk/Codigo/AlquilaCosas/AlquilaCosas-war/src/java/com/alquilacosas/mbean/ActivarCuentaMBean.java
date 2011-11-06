/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.ejb.session.LoginBeanLocal;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name="activarCuenta")
@ViewScoped
public class ActivarCuentaMBean implements Serializable {

    @EJB
    private LoginBeanLocal login;
    public boolean activada;
    public String usuario;
    
    /** Creates a new instance of ActivarCuentaMBean */
    public ActivarCuentaMBean() {
    }
    
    @PostConstruct
    public void init() {
        Logger.getLogger(ActivarCuentaMBean.class).info("ActivarCuentaMBean: postconstruct.");
        usuario = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("user");
        String codigo = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("codigo");
        try {
            activada = login.activarCuenta(usuario, codigo);
        } catch(AlquilaCosasException e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al activar cuenta", e.getMessage()));
        }
    }

    public boolean isActivada() {
        return activada;
    }

    public void setActivada(boolean activada) {
        this.activada = activada;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    
}
