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
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author jorge
 */
@ManagedBean (name = "desplieguePreguntas")
@ViewScoped
public class ResponderPreguntasMBean {

    @EJB
    private PublicacionBeanLocal publicationBean;
    @ManagedProperty (value="#{login}")
    private ManejadorUsuarioMBean usuarioLogueado;
    //private CategoriaFacade categoria;
    //private List<PrecioFacade> precios;
    private PublicacionFacade publicacion;
    private String effect;
    private List<ComentarioFacade> comentarios;
    private ComentarioFacade nuevaRespuesta;
    private ComentarioFacade selectedPregunta;
    private int cantidad;
    
    
    
    /** Creates a new instance of ResponderPreguntasMBean */
    public ResponderPreguntasMBean() { }
    
    @PostConstruct
    public void init(){
        setEffect("fade"); 
//        String id = FacesContext.getCurrentInstance().getExternalContext()
//                .getRequestParameterMap().get("id");
            setNuevaRespuesta(new ComentarioFacade());
            setComentarios(publicationBean.getPreguntasSinResponder(getUsuarioLogueado().getUsuarioId()));
            setCantidad(comentarios.size());


        }

     public String guardarPregunta() {  
         
        nuevaRespuesta.setUsuarioId(getUsuarioLogueado().getUsuarioId());
        nuevaRespuesta.setUsuario(getUsuarioLogueado().getUsername());
        nuevaRespuesta.setFecha(new Date());
        selectedPregunta.setRespuesta(nuevaRespuesta);
        publicationBean.setRespuesta(selectedPregunta);
        setNuevaRespuesta(new ComentarioFacade());  
        return null;  
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
     * @return the nuevaRespuesta
     */
    public ComentarioFacade getNuevaRespuesta() {
        return nuevaRespuesta;
    }

    /**
     * @param nuevaRespuesta the nuevaRespuesta to set
     */
    public void setNuevaRespuesta(ComentarioFacade nuevaRespuesta) {
        this.nuevaRespuesta = nuevaRespuesta;
    }

    /**
     * @return the selectedPregunta
     */
    public ComentarioFacade getSelectedPregunta() {
        return selectedPregunta;
    }

    /**
     * @param selectedPregunta the selectedPregunta to set
     */
    public void setSelectedPregunta(ComentarioFacade selectedPregunta) {
        this.selectedPregunta = selectedPregunta;
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

    /**
     * @return the cantidad
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
}
