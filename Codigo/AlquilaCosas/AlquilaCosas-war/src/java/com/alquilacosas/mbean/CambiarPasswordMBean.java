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
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name="passwordBean")
@ViewScoped
public class CambiarPasswordMBean implements Serializable {

    @EJB
    private LoginBeanLocal loginBean;
    @ManagedProperty(value="#{login}")
    private ManejadorUsuarioMBean loginMBean;
    private int usuarioId;
    private String password, passwordNuevo, passwordNuevo2;
    
    /** Creates a new instance of CambiarPasswordMBean */
    public CambiarPasswordMBean() {
    }
    
    @PostConstruct
    public void init() {
        Logger.getLogger(BuscarMBean.class).info("BuscarMBean: postconstruct.");
        usuarioId = loginMBean.getUsuarioId();
    }
    
    public String cambiarPassword() {
        if(!password.equals(loginMBean.getPassword())) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al cambiar contraseña", "Las contraseña actual ingresada es incorrecta");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
        if(!passwordNuevo.equals(passwordNuevo2)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al cambiar contraseña", "Las contraseñas no coinciden");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
        try {
            loginBean.cambiarPassword(usuarioId, password, passwordNuevo);
        } catch(AlquilaCosasException e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al cambiar contraseña", e.getMessage());
            return null;
        }
        return "pinicio";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordNuevo() {
        return passwordNuevo;
    }

    public void setPasswordNuevo(String passwordNuevo) {
        this.passwordNuevo = passwordNuevo;
    }

    public String getPasswordNuevo2() {
        return passwordNuevo2;
    }

    public void setPasswordNuevo2(String passwordNuevo2) {
        this.passwordNuevo2 = passwordNuevo2;
    }

    public ManejadorUsuarioMBean getLoginMBean() {
        return loginMBean;
    }

    public void setLoginMBean(ManejadorUsuarioMBean loginMBean) {
        this.loginMBean = loginMBean;
    }
    
    
}
