/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.common.SeguridadException;
import com.alquilacosas.common.UsuarioLogueado;
import com.alquilacosas.ejb.entity.Rol.NombreRol;
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
import org.apache.log4j.Logger;
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
    private UsuarioLogueado usuario;
    private String username;
    private String password;
    private String loginOnPageArg;
    private Integer usuarioId;
    private boolean logueado, administrador, publicitante;

    /** Creates a new instance of LoginMBean */
    public ManejadorUsuarioMBean() {
    }

    @PostConstruct
    public void init() {
        Logger.getLogger(ManejadorUsuarioMBean.class).debug("ManejadorUsuarioMBean: postconstruct."); 
    }
    
    public String loguearse() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            usuario = loginBean.login(username, password);
        } catch (SeguridadException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Credenciales incorrectas", ""));
            return null;
        }
        logueado = true;
        usuarioId = usuario.getId();
        administrador = usuario.getRoles().contains(NombreRol.ADMIN);
        publicitante = usuario.getRoles().contains(NombreRol.PUBLICITANTE);
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        String url = (String) req.getSession().getAttribute("redirectUrl");
        if(url == null)
            url = "inicio.xhtml";
        return url;
    }
    
    public void ajaxLogin() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            usuario = loginBean.login(username, password);
        } catch (SeguridadException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Credenciales incorrectas", ""));
            return;
        }
        logueado = true;
        usuarioId = usuario.getId();
        administrador = usuario.getRoles().contains(NombreRol.ADMIN);
        publicitante = usuario.getRoles().contains(NombreRol.PUBLICITANTE);
    }
    
    public String login() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
//        try {
//            request.login(this.username, this.password);
//        } catch (ServletException e) {
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                    "Credenciales incorrectas", ""));
//            return null;
//        }
//        try {
//            usuarioId = loginBean.loginUsuario(username);
//            administrador = context.getExternalContext().isUserInRole("ADMIN");
//            publicitante = context.getExternalContext().isUserInRole("PUBLICITANTE");
//        } catch (AlquilaCosasException e) {
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                    "Error al obtener datos de usuario", e.getMessage()));
//            password = "";
//            return null;
//        }
//        logueado = true;
        return "inicio.xhtml";
    }
    
    public void loginEnPagina() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
//        try {
//            request.login(this.username, this.password);
//        } catch (Exception e) {
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                    "Credenciales incorrectas", ""));
//            return null;
//        }
//        try {
//            usuarioId = loginBean.loginUsuario(username);
//            administrador = context.getExternalContext().isUserInRole("ADMIN");
//        } catch (AlquilaCosasException e) {
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                    "Error al obtener datos de usuario", e.getMessage()));
//            password = "";
//            return null;
//        }
//        logueado = true;
//        RequestContext reqContext = RequestContext.getCurrentInstance();
//        reqContext.addCallbackParam("logueado", logueado);
//        reqContext.addCallbackParam(loginOnPageArg, true);
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            usuario = loginBean.login(username, password);
        } catch (SeguridadException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Credenciales incorrectas", ""));
            return;
        }
        logueado = true;
        usuarioId = usuario.getId();
        administrador = usuario.getRoles().contains(NombreRol.ADMIN);
        publicitante = usuario.getRoles().contains(NombreRol.PUBLICITANTE);
        RequestContext reqContext = RequestContext.getCurrentInstance();
        reqContext.addCallbackParam("logueado", logueado);
        reqContext.addCallbackParam(loginOnPageArg, true);
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
        request.getSession().invalidate();
        return "pinicio";
    }
    
    public String salir() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        logueado = false;
        administrador = false;
        publicitante = false;
        usuario = null;
        request.getSession().invalidate();
        return "/vistas/inicio";
    }

    public boolean isAdministrador() {
        return administrador;
    }
    
    public boolean isPublicitante() {
        return publicitante;
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

    public void setPublicitante(boolean publicitante) {
        this.publicitante = publicitante;
    }

    public UsuarioLogueado getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioLogueado usuario) {
        this.usuario = usuario;
    }
}
