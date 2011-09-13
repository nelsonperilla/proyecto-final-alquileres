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
import javax.ejb.Remove;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "login")
@SessionScoped
public class ManejadorUsuarioMBean implements Serializable {

    @EJB
    private LoginBeanLocal loginBean;
    private String username;
    private String password;
    private String loginOnPageArg;
    private Integer usuarioId;
    private boolean logueado, administrador;

    /** Creates a new instance of LoginMBean */
    public ManejadorUsuarioMBean() {
    }

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        System.out.println(request.getContextPath());
        System.out.println(request.getPathInfo());
        System.out.println(request.getRequestURL());
    }
    
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.login(this.username, this.password);
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Credenciales incorrectas"+e.getMessage(), "Credenciales incorrectas"+e.getMessage()));
            return null;
        }
        try {
            usuarioId = loginBean.loginUsuario(username);
            administrador = context.getExternalContext().isUserInRole("ADMIN");
        } catch (AlquilaCosasException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    e.getMessage(), e.getMessage()));
            password = "";
            return null;
        }
        logueado = true;
        return "inicio";
    }
    
    public String loginEnPagina() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.login(this.username, this.password);
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Credenciales incorrectas", "Credenciales incorrectas"));
            return null;
        }
        try {
            usuarioId = loginBean.loginUsuario(username);
            administrador = context.getExternalContext().isUserInRole("ADMIN");
        } catch (AlquilaCosasException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    e.getMessage(), e.getMessage()));
            password = "";
            return null;
        }
        logueado = true;
        RequestContext reqContext = RequestContext.getCurrentInstance();
        reqContext.addCallbackParam("logueado", logueado);
        reqContext.addCallbackParam(loginOnPageArg, true);
        
        return "";
    }

    @Remove
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();
        } catch (ServletException e) {
        }
        usuarioId = null;
        username = "";
        password = "";
        logueado = false;
        administrador = false;
        return "inicio";
    }

    public boolean isAdministrador() {
        return administrador;
    }

    /*
     * Getters & Setters
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLogueado() {
        return logueado;
    }

    public void setLogueado(boolean logueado) {
        this.logueado = logueado;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     * @return the loginOnPageArg
     */
    public String getLoginOnPageArg() {
        return loginOnPageArg;
    }

    /**
     * @param loginOnPageArg the loginOnPageArg to set
     */
    public void setLoginOnPageArg(String loginOnPageArg) {
        this.loginOnPageArg = loginOnPageArg;
    }
}
