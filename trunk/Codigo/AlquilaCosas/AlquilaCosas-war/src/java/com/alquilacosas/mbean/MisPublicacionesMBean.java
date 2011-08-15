/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.PublicacionFacade;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.session.MisPublicacionesBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

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
    private ManejadorUsuarioMBean usuarioMBean;

    private List<Publicacion> publicaciones;
    private List<PublicacionFacade> publicacionesFacade;
    private int publicacionId;
    
    public MisPublicacionesMBean() {
    }
    
    
    @PostConstruct
    public void init() {
        if( usuarioMBean.getUsuarioId() != null ){
            publicaciones = new ArrayList<Publicacion>();
            publicaciones = misPublicacionesBean.getPublicaciones(usuarioMBean.getUsuarioId());
            publicacionesFacade = getListPublicaciones(publicaciones);
        }
  
    }
    
    public List<PublicacionFacade> getListPublicaciones( List<Publicacion> publicaciones ){
        
        List<PublicacionFacade> pubFacadeList = new ArrayList<PublicacionFacade>();
        
        for(Publicacion p: publicaciones) {
            PublicacionFacade facade = new PublicacionFacade(p.getPublicacionId(), p.getTitulo(),
                    p.getDescripcion(), p.getFechaDesde(), p.getFechaHasta(), p.getDestacada(),
                    p.getCantidad());
            List<Integer> imagenes = new ArrayList<Integer>();
            for(ImagenPublicacion ip: p.getImagenPublicacionList()) {
                imagenes.add(ip.getImagenPublicacionId());
            }
            facade.setImagenIds(imagenes);
            pubFacadeList.add(facade);
        }
        
        return pubFacadeList;
    }

    public List<PublicacionFacade> getPublicacionesFacade() {
        return publicacionesFacade;
    }

    public void setPublicacionesFacade(List<PublicacionFacade> publicacionesFacade) {
        this.publicacionesFacade = publicacionesFacade;
    }
    

    public MisPublicacionesBeanLocal getMisPublicacionesBean() {
        return misPublicacionesBean;
    }

    public void setMisPublicacionesBean(MisPublicacionesBeanLocal misPublicacionesBean) {
        this.misPublicacionesBean = misPublicacionesBean;
    }

    public List<Publicacion> getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(List<Publicacion> publicaciones) {
        this.publicaciones = publicaciones;
    }

    public ManejadorUsuarioMBean getUsuarioMBean() {
        return usuarioMBean;
    }

    public void setUsuarioMBean(ManejadorUsuarioMBean usuarioMBean) {
        this.usuarioMBean = usuarioMBean;
    }

    public int getPublicacionId() {
        return publicacionId;
    }

    public void setPublicacionId(int publicacionId) {
        this.publicacionId = publicacionId;
    }

    public String editarPublicacion(){
        return "modificarPublicacion";
    }
    
    public String mostrarPublicacion(){
       
        return "mostrarPublicacion";
        
    }
    
}
