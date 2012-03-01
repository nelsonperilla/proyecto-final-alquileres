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
import com.alquilacosas.ejb.session.UsuarioBeanLocal;
import com.visural.common.StringUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    @EJB
    private UsuarioBeanLocal registrarBean;
    private UsuarioLogueado usuario;
    private String username, password;
    private String loginOnPageArg;
    private String fbId;
    private Integer usuarioId;
    private boolean logueado, administrador, publicitante, fbLogin;
    private String urlParams;
    
    private static final String api_key = "187196061385476";
    private static final String secret = "f43b355d1f187ec2f65e4e681dbce1c1";
    private static final String client_id = "187196061385476";
    private static final String redirect_uri = "http://localhost.com:8080/AlquilaCosas-war/vistas/fbAccess.jsf";
    private static final String[] perms = new String[]{"publish_stream", "email", "user_location"};

    /** Creates a new instance of LoginMBean */
    public ManejadorUsuarioMBean() {
    }

    @PostConstruct
    public void init() {
        Logger.getLogger(ManejadorUsuarioMBean.class).debug("ManejadorUsuarioMBean: postconstruct."); 
    }
    
    /**
     * Se ingresa al usuario al sistema. Si el login es exitoso y hay un redireccionamiento pendiente,
     * se envia al usuario a esa pagina. En caso contrario se lo envia a la pagina de inicio.
     */
    public void loguearse() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            usuario = loginBean.login(username, password);
        } catch (SeguridadException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Credenciales incorrectas", ""));
            username = password = "";
            return;
        }
        logueado = true;
        usuarioId = usuario.getId();
        administrador = usuario.getRoles().contains(NombreRol.ADMIN);
        publicitante = usuario.getRoles().contains(NombreRol.PUBLICITANTE);
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        String url = (String) req.getSession(true).getAttribute("redirectUrl");
        req.getSession(true).removeAttribute("redirectUrl");
        if(url == null)
            url = req.getContextPath() + "/vistas/inicio2.jsf";
        else
            url = req.getContextPath() + url;
        try {
            context.getExternalContext().redirect(url);
        } catch (IOException e) {
            Logger.getLogger(ManejadorUsuarioMBean.class).error("loguearse(). Excepcion al ejecutar redirect().");
        }
    }
    
    public void ajaxLogin() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            usuario = loginBean.login(username, password);
        } catch (SeguridadException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Credenciales incorrectas", ""));
            username = password = "";
            return;
        }
        logueado = true;
        usuarioId = usuario.getId();
        administrador = usuario.getRoles().contains(NombreRol.ADMIN);
        publicitante = usuario.getRoles().contains(NombreRol.PUBLICITANTE);
        RequestContext.getCurrentInstance().addCallbackParam("logueado", true);
    }
    
    public String recargarPagina() {
        return null;
    }
    
    /**
     * Comienza a efectuar los pasos para loguear al usuario a traves de Facebook.
     * El primer paso es redireccionar a una pagina de facebook brindando algunos datos.
     */
    public void fbLoginEnPagina() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest req = (HttpServletRequest) ec.getRequest();
        // si no hay parametro de redireccionamiento, obtener url actual y setearla como parametro de redireccionamiento
        String path = req.getServletPath();
        if(urlParams != null && !urlParams.equals("")) {
            path = path + "?" + urlParams;
        }
        ((HttpSession)ec.getSession(true)).setAttribute("redirectUrl", path);
        try {
            String url = getLoginRedirectURL();
            ec.redirect(url);
        } catch (Exception e) {
            Logger.getLogger(ManejadorUsuarioMBean.class).error("Excepcion al ejecutar redirect().");
        }
    }
    
    public void fbLogin() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            String url = getLoginRedirectURL();
            ec.redirect(url);
        } catch (Exception e) {
            Logger.getLogger(ManejadorUsuarioMBean.class).error("Excepcion al ejecutar redirect().");
        }
    }
    
    /**
     * Se completa el login del usuario a traves de Facebook. Este es el ultimo paso de
     * la interaccion con Facebook. Se recibe el email del usuario y se utiliza este para
     * loguear al usuario.
     * @param email Email del usuario obtenido a traes de Facebook
     * @return 
     */
    public boolean completeFbLogin(String email, String id) {
        if(email == null) {
            return false;
        }
        try {
            usuario = loginBean.facebookLogin(email);
        } catch (SeguridadException e) {
            Logger.getLogger(ManejadorUsuarioMBean.class).error("fbLogin(). Usuario no registrado.");
            return false;
        }
        fbId = id;
        logueado = fbLogin = true;
        usuarioId = usuario.getId();
        administrador = usuario.getRoles().contains(NombreRol.ADMIN);
        publicitante = usuario.getRoles().contains(NombreRol.PUBLICITANTE);
        return true;
    }
    
    /**
     * Registra un nuevo usuario a traves de los datos obtenidos por facebook. Si el registro
     * es exitoso, luego efectua el login del usuario.
     * @param nombre Nombre del usuario obtenido a traves de facebook
     * @param apellido Apellido del usuario obtenido a traves de facebook
     * @param email Email del usuario obtenido a traves de facebook
     * @return 
     */
    public boolean registrarFb(String nombre, String apellido, String email, String id) {
        try {
            registrarBean.registrarUsuarioConFacebook(email, nombre, apellido);   
        } catch (AlquilaCosasException e) {
            Logger.getLogger(ManejadorUsuarioMBean.class).error("registrarFb(). "
                    + "Error al registrar usuario a traves de facebook: " + e.getMessage());
            return false;
        }
        return completeFbLogin(email, id);
    }
    
    public void loginEnPagina() {
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
        logueado = administrador = publicitante = false;
        request.getSession().invalidate();
        return "/vistas/inicio2.jsf?faces-redirect=true";
    }
    
    @Remove
    public String salir() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        logueado = administrador = publicitante = fbLogin = false;
        usuario = null;
        request.getSession().invalidate();
        return "/vistas/inicio2.jsf?faces-redirect=true";
    }
    
    /**
     * Devuelve la URL a la cual hay que redireccionar para comenzar la comunicacion con Facebook.
     * @return 
     */
    public String getLoginRedirectURL() {
        return "https://graph.facebook.com/oauth/authorize?client_id="
                + client_id + "&display=page&redirect_uri="
                + redirect_uri + "&scope=" + StringUtil.delimitObjectsToString(",", perms);
    }

    /**
     * Devuelve la URL a la cual hay que redireccionar para obtener el access token (2do paso)
     * @param authCode
     * @return 
     */
    public static String getAuthURL(String authCode) {
        return "https://graph.facebook.com/oauth/access_token?client_id="
                + client_id + "&redirect_uri="
                + redirect_uri + "&client_secret=" + secret + "&code=" + authCode;
    }

    /*
     * Getters & Setters
     */
    
    public boolean isAdministrador() {
        return administrador;
    }
    
    public boolean isPublicitante() {
        return publicitante;
    }
    
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

    public boolean isFbLogin() {
        return fbLogin;
    }

    public void setFbLogin(boolean fbLogin) {
        this.fbLogin = fbLogin;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(String urlParams) {
        this.urlParams = urlParams;
    }
}
