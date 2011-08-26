/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.ComentarioDTO;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.session.PublicacionBeanLocal;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author jorge
 */
@ManagedBean(name = "publicacionDesplegada")
@ViewScoped
public class DesplieguePublicacionMBean {

    @EJB
    private PublicacionBeanLocal publicationBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean usuarioLogueado;
    private PublicacionDTO publicacion;
    private String effect;
    private List<ComentarioDTO> comentarios;
    private String fecha_hasta, comentario;

    /** Creates a new instance of DesplieguePublicacionMBean */
    public DesplieguePublicacionMBean() {
    }

    @PostConstruct
    public void init() {
        effect = "fade";
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        //nuevaPregunta = new ComentarioFacade();
        if (id != null) {
            int publicationId = Integer.parseInt(id);
            setPublicacion(publicationBean.getPublicacion(publicationId));
            setComentarios(publicationBean.getPreguntas(publicationId));
            if (publicacion != null && publicacion.getFecha_hasta() != null) {
                setFecha_hasta(DateFormat.getDateInstance(DateFormat.SHORT).format(publicacion.getFecha_hasta()));
            }

        }
    }

    public void actualizarPreguntas() {
        comentarios = publicationBean.getPreguntas(publicacion.getId());
    }

    public void preguntar() {
        boolean logueado = usuarioLogueado.isLogueado();
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("logueado", logueado);
    }

    public void guardarPregunta() {
        ComentarioDTO nuevaPregunta = new ComentarioDTO();
        nuevaPregunta.setComentario(comentario);
        nuevaPregunta.setUsuarioId(usuarioLogueado.getUsuarioId());
        //nuevaPregunta.setUsuario(usuarioLogueado.getUsername());
        nuevaPregunta.setFecha(new Date());
        try {
            publicationBean.setPregunta(publicacion.getId(), nuevaPregunta);
            //setNuevaPregunta(new ComentarioFacade());
            actualizarPreguntas();
            comentario = "";
        } catch (AlquilaCosasException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al enviar pregunta", e.getMessage()));
        }
    }

    /**
     * @return the comentarios
     */
    public List<ComentarioDTO> getComentarios() {
        return comentarios;
    }

    /**
     * @param comentarios the comentarios to set
     */
    public void setComentarios(List<ComentarioDTO> comentarios) {
        this.comentarios = comentarios;
    }

    /**
     * @return the publicacion
     */
    public PublicacionDTO getPublicacion() {
        return publicacion;
    }

    /**
     * @param publicacion the publicacion to set
     */
    public void setPublicacion(PublicacionDTO publicacion) {
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

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
