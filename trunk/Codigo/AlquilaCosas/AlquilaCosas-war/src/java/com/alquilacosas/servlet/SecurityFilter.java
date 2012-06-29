/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.servlet;

import com.alquilacosas.ejb.entity.Rol.NombreRol;
import com.alquilacosas.mbean.ManejadorUsuarioMBean;
import com.sun.faces.application.resource.ResourceHandlerImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author damiancardozo
 */
public class SecurityFilter implements Filter {
    
    private FilterConfig filterConfig = null;
    private Map<String, NombreRol> patrones;
    private List<String> restringidos;
    private ManejadorUsuarioMBean login;
    
    public SecurityFilter() {
    }    
    
    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {

    }    
    
    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        
        String destino = req.getServletPath();
        String uri = req.getRequestURI();
        // Ignorar JSF2/Primefaces resources (which are also mapped on FacesServlet).
        if (destino.startsWith(ResourceHandler.RESOURCE_IDENTIFIER)) {
            chain.doFilter(request, response);
            return;
        }
        
        login = (ManejadorUsuarioMBean) req.getSession().getAttribute("login");
        boolean logueado = false;
        if(login != null)
            logueado = login.isLogueado();
        
        resp.setHeader("Cache-Control", "no-cache,no-store,must-revalidate"); // HTTP 1.1
        resp.setHeader("Pragma", "no-cache"); // HTTP 1.0
        resp.setDateHeader("Expires", 0); // Proxies.
        
        if(!logueado && req.getQueryString() != null && login != null) {
            login.setUrlParams(req.getQueryString());
        }
        
        for(String s: patrones.keySet()) {
            if(destino.contains(s)) {
                if(!logueado) {
                    if(req.getQueryString() != null && !req.getQueryString().equals("")) {
                        destino += "?" + req.getQueryString();
                    }
                    req.getSession(true).setAttribute("redirectUrl", destino);
                    resp.sendRedirect(req.getContextPath() + "/vistas/login.jsf");
                    return;
                }
                if(!login.getUsuario().getRoles().contains(patrones.get(s))){
                    resp.sendRedirect(req.getContextPath() + "/vistas/inicio.jsf");
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Init method for this filter 
     */
    @Override
    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
        patrones = new HashMap<String, NombreRol>();
        patrones.put("/usuario/", NombreRol.USUARIO);
        patrones.put("/admin/", NombreRol.ADMINISTRADOR);
        patrones.put("/pub/", NombreRol.PUBLICITANTE);
        restringidos = new ArrayList<String>();
        restringidos.add("/usuario/");
        restringidos.add("/admin/");
        restringidos.add("/pub/");
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("SecurityFilter()");
        }
        StringBuilder sb = new StringBuilder("SecurityFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    @Override
    public void destroy() {
        
    }

}
