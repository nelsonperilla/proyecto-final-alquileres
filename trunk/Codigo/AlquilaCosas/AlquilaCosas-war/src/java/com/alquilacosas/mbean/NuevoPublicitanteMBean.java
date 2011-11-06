/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.ejb.session.AdministrarUsuariosBeanLocal;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name="nuevoPublicitante")
@ViewScoped
public class NuevoPublicitanteMBean implements Serializable {

    @EJB
    private AdministrarUsuariosBeanLocal usuarioBean;
    @ManagedProperty(value="#{login}")
    private ManejadorUsuarioMBean loginBean;
    
    /** Creates a new instance of NuevoPublicitante */
    public NuevoPublicitanteMBean() {
    }
    
    @PostConstruct
    public void init() {
        Logger.getLogger(NuevoPublicitanteMBean.class).info("NuevoPublicitanteMBean: postconstruct.");
        if(loginBean.isPublicitante()) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("inicio.xhtml");
                FacesContext.getCurrentInstance().responseComplete();
            } catch (Exception e) {
                Logger.getLogger(NuevoPublicitanteMBean.class).error("Excepcion al ejecutar redirect().");
            }
        }
    }
    
    public String registrar() {
        usuarioBean.registrarPublicitante(loginBean.getUsuarioId());
        loginBean.setPublicitante(true);
        return "pinicio";
    }

    public ManejadorUsuarioMBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(ManejadorUsuarioMBean loginBean) {
        this.loginBean = loginBean;
    }
}
