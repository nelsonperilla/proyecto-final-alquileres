/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.dto.CalificacionDTO;
import com.alquilacosas.ejb.entity.Puntuacion;
import com.alquilacosas.ejb.session.AlquileresBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name="alquileresBean")
@ViewScoped
public class AlquileresMBean {

    @EJB
    private AlquileresBeanLocal alquileresBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean loginBean;
    private Integer usuarioLogueado;
    private List<AlquilerDTO> alquileres;
    private Integer alquilerId;
    // calificaciones
    private String comentario;
    private Integer puntuacionId;
    private List<SelectItem> puntuaciones;
    private CalificacionDTO calificacionOfrece, calificacionToma;
    private Boolean ofreceCalifico, tomaCalifico, ofrece, toma, tomado;
    private AlquilerDTO alquiler;
    
    /** Creates a new instance of AlquileresMBean */
    public AlquileresMBean() {
    }
    
    @PostConstruct
    public void init() {
        usuarioLogueado = loginBean.getUsuarioId();
        if (usuarioLogueado == null) {
            return;
        }
        alquileres = alquileresBean.getAlquileres(usuarioLogueado);
        
        puntuaciones = new ArrayList<SelectItem>();
        List<Puntuacion> listaPuntuacion = alquileresBean.getPuntuaciones();
        for (Puntuacion p : listaPuntuacion) {
            puntuaciones.add(new SelectItem(p.getPuntuacionId(), p.getNombre()));
        }
    }
    
    public void cancelarAlquiler() {
        if(alquilerId == null || alquilerId < 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Error al cancelar alquiler.", "No se brindo un ID.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        boolean borrado = alquileresBean.cancelarAlquiler(alquilerId);
        if (borrado) {
            for (int i = 0; i < alquileres.size(); i++) {
                AlquilerDTO alq = alquileres.get(i);
                if (alq.getIdAlquiler() == alquilerId) {
                    alquileres.remove(alq);
                    return;
                }
            }
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Alquiler cancelado.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public void prepararCalificar(ActionEvent event) {
        alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
        alquiler = alquileresBean.getAlquiler(usuarioLogueado, alquilerId);
        tomado = alquiler.isTomado();
    }

    public void prepararVerCalificacion(ActionEvent event) {
        alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
        alquiler = alquileresBean.getAlquiler(usuarioLogueado, alquilerId);
        try {
            calificacionToma = alquileresBean.getCalificacionToma(alquilerId);
            calificacionOfrece = alquileresBean.getCalificacionOfrece(alquilerId);
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error  al leer las Calificaciones" + e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        ofreceCalifico = calificacionOfrece != null;
        tomaCalifico = calificacionToma != null;
        toma =alquiler.isTomado();
        tomado = alquiler.isTomado();
    }
    
    public void registrarCalificacion() {
        alquileresBean.registrarCalificacion(puntuacionId, alquilerId, comentario, usuarioLogueado, tomado);
        for(AlquilerDTO alq: alquileres) {
            if(alq.getIdAlquiler() == alquilerId) {
                alq.setCalificado(true);
                return;
            }
        }
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Calificacion registrada.", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void registrarReplicaToma() {
        alquileresBean.registrarReplica(calificacionToma.getIdCalificacion(), calificacionToma.getComentarioReplica(), usuarioLogueado);
        calificacionToma.setYaReplico(true);
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Replica registrada.", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void registrarReplicaOfrece() {
        alquileresBean.registrarReplica(calificacionOfrece.getIdCalificacion(), calificacionOfrece.getComentarioReplica(), usuarioLogueado);
        calificacionOfrece.setYaReplico(true);
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Replica registrada.", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public List<AlquilerDTO> getAlquileres() {
        return alquileres;
    }

    public void setAlquileres(List<AlquilerDTO> alquileres) {
        this.alquileres = alquileres;
    }

    public ManejadorUsuarioMBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(ManejadorUsuarioMBean loginBean) {
        this.loginBean = loginBean;
    }

    public int getAlquilerId() {
        return alquilerId;
    }

    public void setAlquilerId(int alquilerId) {
        this.alquilerId = alquilerId;
    }

    public Integer getPuntuacionId() {
        return puntuacionId;
    }

    public void setPuntuacionId(Integer puntuacionId) {
        this.puntuacionId = puntuacionId;
    }

    public List<SelectItem> getPuntuaciones() {
        return puntuaciones;
    }

    public void setPuntuaciones(List<SelectItem> puntuaciones) {
        this.puntuaciones = puntuaciones;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public CalificacionDTO getCalificacionOfrece() {
        return calificacionOfrece;
    }

    public void setCalificacionOfrece(CalificacionDTO calificacionOfrece) {
        this.calificacionOfrece = calificacionOfrece;
    }

    public CalificacionDTO getCalificacionToma() {
        return calificacionToma;
    }

    public void setCalificacionToma(CalificacionDTO calificacionToma) {
        this.calificacionToma = calificacionToma;
    }

    public Boolean getTomado() {
        return tomado;
    }

    public void setTomado(Boolean tomado) {
        this.tomado = tomado;
    }

    public Integer getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(Integer usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }
}
