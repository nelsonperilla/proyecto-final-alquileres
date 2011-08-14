/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.ComentarioFacade;
import com.alquilacosas.common.PublicacionFacade;
import com.alquilacosas.ejb.session.PublicacionBeanLocal;
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
    
//    
//    @EJB
//    private CategoriaBean categoriaBean;
    
    //Datos de la publicacion
//    private String titulo;
//    private String descripcion;
//    private Date fechaDesde;
//    private Date fechaHasta;
//    private boolean destacada;
//    private int cantidad;
//    
//    //Select Items
//    private List<Categoria> categorias;
//    private int selectedCategoria;
//    
////    private List<SelectItem> periodos;
////    private String selectedPeriodo;
//   
//    
//    //Object Precio
//    private double precio;
//    private List<PrecioFacade> precios;
//    private PrecioFacade precioFacade;
//    
//    private Date today;
//    private List<Integer> idImagenes;
//
//    
//    private PrecioFacade precioSeleccionado;
    private PublicacionFacade publicacion;
    private String effect;
    
    /* Comentarios */
    private List<ComentarioFacade> comentarios;
    private ComentarioFacade nuevaPregunta; 
    
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
            setComentarios(publicationBean.getComentarios(publicationId));
        }
    }

    
     public String guardarPregunta() {  
         
        nuevaPregunta.setUsuarioId(1);//usuarioLogueado.getUsuarioId());
        nuevaPregunta.setUsuario("pepe");//usuarioLogueado.getUsername());
        nuevaPregunta.setFecha(new Date());
        publicationBean.setComentario(publicacion.getId(), nuevaPregunta);
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


}
