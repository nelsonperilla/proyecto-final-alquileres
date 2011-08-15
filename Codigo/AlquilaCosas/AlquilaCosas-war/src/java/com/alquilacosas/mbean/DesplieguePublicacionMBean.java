/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.CategoriaFacade;
import com.alquilacosas.common.ComentarioFacade;
import com.alquilacosas.common.PrecioFacade;
import com.alquilacosas.common.PublicacionFacade;
import com.alquilacosas.ejb.session.PublicacionBeanLocal;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author jorge
 */
@ManagedBean (name = "publicacionDesplegada")
@ViewScoped
public class DesplieguePublicacionMBean {

    @EJB
    private PublicacionBeanLocal publicationBean;
    @ManagedProperty (value="#{login}")
    private ManejadorUsuarioMBean usuarioLogueado;
    private PublicacionFacade publicacion;
    private String effect;
    private List<ComentarioFacade> comentarios;
    private ComentarioFacade nuevaPregunta; 
    private String fecha_hasta;
    
    /** Creates a new instance of DesplieguePublicacionMBean */
    public DesplieguePublicacionMBean() { }
    
    
    @PostConstruct
    public void init(){
        effect="fade";
        String id = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("id");
        nuevaPregunta = new ComentarioFacade();
        if(id != null){
            int publicationId = Integer.parseInt(id);
            setPublicacion(publicationBean.getPublicacion(publicationId));
            setComentarios(publicationBean.getPreguntas(publicationId));
            setFecha_hasta(DateFormat.getDateInstance(DateFormat.SHORT).format(publicacion.getFecha_hasta()));

        }
    }

    
     public String guardarPregunta() {  
         
        nuevaPregunta.setUsuarioId(usuarioLogueado.getUsuarioId());
        nuevaPregunta.setUsuario(usuarioLogueado.getUsername());
        nuevaPregunta.setFecha(new Date());
        publicationBean.setPregunta(publicacion.getId(), nuevaPregunta);
        setNuevaPregunta(new ComentarioFacade());  
        return null;  
    }  
    
    /**
     * @return the comentarios
     */
    public List<ComentarioFacade> getComentarios() {
        return comentarios;
    }

    /**
     * @param comentarios the comentarios to set
     */
    public void setComentarios(List<ComentarioFacade> comentarios) {
        this.comentarios = comentarios;
    }
    
    /**
     * @return the publicacion
     */
    public PublicacionFacade getPublicacion() {
        return publicacion;
    }

    /**
     * @param publicacion the publicacion to set
     */
    public void setPublicacion(PublicacionFacade publicacion) {
        this.publicacion = publicacion;
    }

    /**
     * @return the effect
     */
    public String getEffect() {
        return effect;
    }

    /**
     * @param effect the effect to set
     */
    public void setEffect(String effect) {
        this.effect = effect;
    }

    /**
     * @return the nuevaPregunta
     */
    public ComentarioFacade getNuevaPregunta() {
        return nuevaPregunta;
    }

    /**
     * @param nuevaPregunta the nuevaPregunta to set
     */
    public void setNuevaPregunta(ComentarioFacade nuevaPregunta) {
        this.nuevaPregunta = nuevaPregunta;
    }

    /**
     * @return the usuarioLogueado
     */
    public ManejadorUsuarioMBean getUsuarioLogueado() {
        return usuarioLogueado;
    }

    /**
     * @param usuarioLogueado the usuarioLogueado to set
     */
    public void setUsuarioLogueado(ManejadorUsuarioMBean usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

//    /**
//     * @return the categoria
//     */
//    public CategoriaFacade getCategoria() {
//        return categoria;
//    }
//
//    /**
//     * @param categoria the categoria to set
//     */
//    public void setCategoria(CategoriaFacade categoria) {
//        this.categoria = categoria;
//    }
//
//    /**
//     * @return the precios
//     */
//    public List<PrecioFacade> getPrecios() {
//        return precios;
//    }
//
//    /**
//     * @param precios the precios to set
//     */
//    public void setPrecios(List<PrecioFacade> precios) {
//        this.precios = precios;
//    }

    /**
     * @return the fecha_hasta
     */
    public String getFecha_hasta() {
        return fecha_hasta;
    }

    /**
     * @param fecha_hasta the fecha_hasta to set
     */
    public void setFecha_hasta(String fecha_hasta) {
        this.fecha_hasta = fecha_hasta;
    }


}
