/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.dto.ComentarioDTO;
import com.alquilacosas.dto.DenunciaDTO;
import com.alquilacosas.ejb.entity.MotivoDenuncia;
import com.alquilacosas.ejb.session.DenunciaPreguntaBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author jorge
 */
@ManagedBean(name = "denuncia")
@ViewScoped
@DeclareRoles({"USUARIO", "ADMIN"})
public class DenunciarPreguntaMBean implements Serializable {

    @EJB
    private DenunciaPreguntaBeanLocal denunciaBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean usuarioLogueado;
    
    private ComentarioDTO comentario;
    private String publicacion;
    private String descripcionDenuncia;
    private List<SelectItem> motivos;
    private int motivoSeleccionado;    
    
    /** Creates a new instance of DenunciarPreguntaMBean */
    public DenunciarPreguntaMBean() {
    }
    
    @PostConstruct
    public void init() {
        motivos = new ArrayList<SelectItem>();
        List<MotivoDenuncia> listaMotivos = denunciaBean.getMotivosDenuncia();
        motivoSeleccionado = 0; 
        for(MotivoDenuncia motivo: listaMotivos)
            motivos.add(new SelectItem(motivo.getMotivoDenunciaId(),motivo.getNombre()));        
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id != null) {
            int comentarioId = Integer.parseInt(id);
            setComentario(denunciaBean.getComentario(comentarioId));
            setPublicacion(denunciaBean.getPublication(comentario.getPublicacionId()));
        }
    }
    
    public String saveDenuncia()
    {
        if(getUsuarioLogueado().isLogueado())
        {
            DenunciaDTO nuevaDenuncia = new DenunciaDTO();
            nuevaDenuncia.setComentarioId(comentario.getId());
            nuevaDenuncia.setExplicacion(descripcionDenuncia);
            nuevaDenuncia.setFecha(new Date());
            nuevaDenuncia.setPublicacionId(comentario.getPublicacionId());
            nuevaDenuncia.setUsuarioId(getUsuarioLogueado().getUsuarioId());
            try{
                denunciaBean.saveDenuncia(nuevaDenuncia,motivoSeleccionado);
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Su denuncia ha sido agendada para revisión", ""));                
            }
            catch(Exception e){
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Su denuncia no ha podido ser ingresada, por favor intente nuevamente", ""));                
            }
        }
        return "";
    }

    /**
     * @return the comentario
     */
    public ComentarioDTO getComentario() {
        return comentario;
    }

    /**
     * @param comentario the comentario to set
     */
    public void setComentario(ComentarioDTO comentario) {
        this.comentario = comentario;
    }

    /**
     * @return the publicacion
     */
    public String getPublicacion() {
        return publicacion;
    }

    /**
     * @param publicacion the publicacion to set
     */
    public void setPublicacion(String publicacion) {
        this.publicacion = publicacion;
    }

    /**
     * @return the descripcionDenuncia
     */
    public String getDescripcionDenuncia() {
        return descripcionDenuncia;
    }

    /**
     * @param descripcionDenuncia the descripcionDenuncia to set
     */
    public void setDescripcionDenuncia(String descripcionDenuncia) {
        this.descripcionDenuncia = descripcionDenuncia;
    }

    /**
     * @return the motivos
     */
    public List<SelectItem> getMotivos() {
        return motivos;
    }

    /**
     * @param motivos the motivos to set
     */
    public void setMotivos(List<SelectItem> motivos) {
        this.motivos = motivos;
    }

    /**
     * @return the motivoSeleccionado
     */
    public int getMotivoSeleccionado() {
        return motivoSeleccionado;
    }

    /**
     * @param motivoSeleccionado the motivoSeleccionado to set
     */
    public void setMotivoSeleccionado(int motivoSeleccionado) {
        this.motivoSeleccionado = motivoSeleccionado;
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
