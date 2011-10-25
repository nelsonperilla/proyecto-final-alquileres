/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.dto.PublicidadDTO;
import com.alquilacosas.ejb.entity.TipoPublicidad.UbicacionPublicidad;
import com.alquilacosas.ejb.session.MostrarPublicidadBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name="mostrarPublicidad")
@ViewScoped
public class MostrarPublicidadMBean implements Serializable {

    @EJB
    private MostrarPublicidadBeanLocal publicidadBean;
    private List<PublicidadDTO> publicidadesCarrusel;
    private List<PublicidadDTO> publicidadesIzq;
    private List<PublicidadDTO> publicidadesDer;
    private PublicidadDTO pubCarrusel, pubIzq, pubDer;
    
    /** Creates a new instance of MostrarPublicidadMBean */
    public MostrarPublicidadMBean() {
    }
    
    @PostConstruct
    public void init() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = req.getPathInfo();
        if(url.contains("inicio.xhtml")) {
            publicidadesCarrusel = publicidadBean.getPublicidades(UbicacionPublicidad.CARRUSEL, 5);
        }
        publicidadesIzq = publicidadBean.getPublicidades(UbicacionPublicidad.LATERAL_IZQUIERDA, 1);
        publicidadesDer = publicidadBean.getPublicidades(UbicacionPublicidad.LATERAL_DERECHA, 3);
    }
    
    public void navegarUrl(ActionEvent event) {
        String url = (String) event.getComponent().getAttributes().get("url");
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (IOException e) {
        }
    }
    
    public List<PublicidadDTO> getPublicidadesCarrusel() {
        return publicidadesCarrusel;
    }

    public List<PublicidadDTO> getPublicidadesDer() {
        return publicidadesDer;
    }

    public List<PublicidadDTO> getPublicidadesIzq() {
        return publicidadesIzq;
    }
    
}
