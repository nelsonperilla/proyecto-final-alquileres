/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.session.FavoritoBeanLocal;
import com.alquilacosas.ejb.session.PublicacionBeanLocal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

/**
 *
 * @author ignaciogiagante
 */
@ManagedBean( name = "misFavoritos" )
@ViewScoped
public class FavoritoMBean {

    @EJB
    private FavoritoBeanLocal favoritoBean;
    @EJB
    private PublicacionBeanLocal publicacionBean;
    
    @ManagedProperty(value="#{login}")
    private ManejadorUsuarioMBean usuarioLogueado;
    private List<PublicacionDTO> publicacionesDto;
    private Integer publicacionId;
    /**
     * Creates a new instance of FavoritoMBean
     */
    public FavoritoMBean() {
    }
    
    @PostConstruct
    public void init() {
        Logger.getLogger(MisPublicacionesMBean.class).debug("MisPublicacionesMBean: postconstruct."); 
        if( usuarioLogueado.getUsuarioId() != null ){
            publicacionesDto = favoritoBean.getFavoritos(usuarioLogueado.getUsuarioId());
        }  
    }
    
    public boolean agregarFavorito( Integer userId, PublicacionDTO pDto){
        if(userId == null) {
            return false;
        }
        try {
            favoritoBean.agregarFavorito(userId, pDto);
            return true;
        } catch (Exception e) {
            System.out.println("todo mal...");
            return false;
        }
        
    }
    
    public void eliminarFavorito() {
        try {
            Integer userId = usuarioLogueado.getUsuarioId();        
            favoritoBean.eliminarFavorito(userId, publicacionId);
            quitarPublicacion(publicacionId);
        } catch (Exception e) {
            System.out.println("todo mal..." + e.getMessage());
        }
    }
    
    public void quitarPublicacion(Integer publicacionId) {
        for(int i = 0; i < publicacionesDto.size(); i++) {
            PublicacionDTO p = publicacionesDto.get(i);
            if(p.getId() == publicacionId) {
                publicacionesDto.remove(p);
                break;
            }
        }
    }

    public FavoritoBeanLocal getFavoritoBean() {
        return favoritoBean;
    }

    public void setFavoritoBean(FavoritoBeanLocal favoritoBean) {
        this.favoritoBean = favoritoBean;
    }

    public List<PublicacionDTO> getPublicacionesDto() {
        return publicacionesDto;
    }

    public void setPublicacionesDto(List<PublicacionDTO> publicacionesDto) {
        this.publicacionesDto = publicacionesDto;
    }

    public ManejadorUsuarioMBean getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(ManejadorUsuarioMBean usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    public PublicacionBeanLocal getPublicacionBean() {
        return publicacionBean;
    }

    public void setPublicacionBean(PublicacionBeanLocal publicacionBean) {
        this.publicacionBean = publicacionBean;
    }

    public Integer getPublicacionId() {
        return publicacionId;
    }

    public void setPublicacionId(Integer publicacionId) {
        this.publicacionId = publicacionId;
    }

        
}
