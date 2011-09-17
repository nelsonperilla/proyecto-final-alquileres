/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.ejb.entity.Periodo.NombrePeriodo;
import com.alquilacosas.ejb.entity.Puntuacion;
import com.alquilacosas.ejb.session.AlquileresOfrecidosBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.primefaces.context.RequestContext;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "alquileresOfrecidos")
@ViewScoped
public class AlquileresOfrecidosMBean implements Serializable {

    @EJB
    private AlquileresOfrecidosBeanLocal alquileresBean;
    
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean loginBean;
    
    private List<SelectItem> filtros, puntuaciones;
    private Integer filtroSeleccionado, duracion, duracionAnterior;
    private NombrePeriodo periodo;
    private List<AlquilerDTO> alquileres;
    private Integer alquilerId, publicacionId, usuarioId, puntuacionId, usuarioLogueado;
    private String comentario;
    private Date hoy;
    private AlquilerDTO alquilerSeleccionado;
    
    /** Creates a new instance of MisAlquileresOfrecidosMBean */
    public AlquileresOfrecidosMBean() {
    }

    @PostConstruct
    public void init() {
        usuarioLogueado = loginBean.getUsuarioId();
        alquileres = alquileresBean.getAlquileresVigentes(usuarioLogueado);
        puntuaciones = new ArrayList<SelectItem>();
        List<Puntuacion> listaPuntuacion = alquileresBean.getPuntuaciones();
        for (Puntuacion p : listaPuntuacion) {
            puntuaciones.add(new SelectItem(p.getPuntuacionId(), p.getNombre()));
        }
        
        filtros = new ArrayList<SelectItem>();
        filtros.add(new SelectItem(1, "Alquileres vigentes"));
        filtros.add(new SelectItem(2, "Alquileres sin calificar"));
        filtros.add(new SelectItem(3, "Alquileres calificados"));
        filtroSeleccionado = 1;
        
        hoy = new Date();
    }

    public void cambioFiltro() {
        switch(filtroSeleccionado) {
            case 1: {
                alquileres = alquileresBean.getAlquileresVigentes(usuarioLogueado);
                break;
            }
            case 2: {
                alquileres = alquileresBean.getAlquileresSinCalificar(usuarioLogueado);
                break;
            }
            case 3: {
                alquileres = alquileresBean.getAlquileresCalificados(usuarioLogueado);
                break;
            }
        }
    }
    
    public String verPublicacion() {
        return "mostrarPublicacion";
    }

    public String verUsuario() {
        return "";
    }
    
    public void prepararCalificar(ActionEvent event) {
        alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
    }

    public void prepararVerCalificacion(ActionEvent event) {
        alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
    }
    
    public void prepararCancelarAlquiler(ActionEvent event) {
        alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
    }
    
    public void prepararModificar(ActionEvent event) {
        alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
        alquilerSeleccionado = null;
        for(AlquilerDTO alq: alquileres) {
            if(alq.getIdAlquiler() == alquilerId) {
                alquilerSeleccionado = alq;
                break;
            }
        }
        if(alquilerSeleccionado == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error preparando alquiler", "Alquiler no encontrado");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(alquilerSeleccionado.getFechaInicio());
        int dia1 = calendar.get(Calendar.DAY_OF_YEAR);
        int hora1 = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto1 = calendar.get(Calendar.MINUTE);
        calendar.setTime(alquilerSeleccionado.getFechaFin());
        int dia2 = calendar.get(Calendar.DAY_OF_YEAR);
        int hora2 = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto2 = calendar.get(Calendar.MINUTE);
        if((hora1 + minuto1 + hora2 + minuto2) != 0) {
            periodo = NombrePeriodo.HORA;
            duracion = hora2 - hora1;
        }
        else {
            periodo = NombrePeriodo.DIA;
            duracion = dia2 - dia1;
        }
        duracionAnterior = duracion;
    }
    
    public void modificarAlquiler() {
        FacesMessage msg = null;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqContext = RequestContext.getCurrentInstance(); 
        if(duracion == duracionAnterior) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, 
                    "No ha modificado la duracion del alquiler", "");
            context.addMessage(null, msg); 
            reqContext.addCallbackParam("modificado", false);
            return;
        }
        try{
            alquilerSeleccionado = alquileresBean.modificarAlquiler(alquilerSeleccionado, periodo, duracion);
        } catch(AlquilaCosasException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar alquiler", e.getMessage());
            context.addMessage(null, msg);
            System.out.println(e.getMessage());
            return;
        }
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alquiler modificado", "");
        context.addMessage(null, msg);
        for(AlquilerDTO alq: alquileres) {
            if(alq.getIdAlquiler() == alquilerSeleccionado.getIdAlquiler()) {
                alq.setMonto(alquilerSeleccionado.getMonto());
                alq.setFechaFin(alquilerSeleccionado.getFechaFin());
                break;
            }
        }
        reqContext.addCallbackParam("modificado", true);
    }
    
    public void cancelarAlquiler() {
        boolean borrado = false;
        try {
            borrado = alquileresBean.cancelarAlquiler(alquilerId);
        } catch(AlquilaCosasException e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al cancelar alquiler", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        if(borrado) {
            for(int i=0; i < alquileres.size(); i++) {
                AlquilerDTO alq = alquileres.get(i);
                if(alq.getIdAlquiler() == alquilerId) {
                    alquileres.remove(alq);
                    return;
                }
            }
        }
    }
    
    public void registrarCalificacion() {
        alquileresBean.registrarCalificacion(usuarioLogueado, alquilerId, puntuacionId, comentario);  
    }
    
    /*
     * Getters & Setters
     */

    public Integer getAlquilerId() {
        return alquilerId;
    }

    public void setAlquilerId(Integer alquilerId) {
        this.alquilerId = alquilerId;
    }

    public List<AlquilerDTO> getAlquileres() {
        return alquileres;
    }

    public void setAlquileres(List<AlquilerDTO> alquileres) {
        this.alquileres = alquileres;
    }

    public Integer getPublicacionId() {
        return publicacionId;
    }

    public void setPublicacionId(Integer publicacionId) {
        this.publicacionId = publicacionId;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public ManejadorUsuarioMBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(ManejadorUsuarioMBean loginBean) {
        this.loginBean = loginBean;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public List<SelectItem> getPuntuaciones() {
        return puntuaciones;
    }

    public void setPuntuaciones(List<SelectItem> puntuaciones) {
        this.puntuaciones = puntuaciones;
    }

    public Integer getPuntuacionId() {
        return puntuacionId;
    }

    public void setPuntuacionId(Integer puntuacionId) {
        this.puntuacionId = puntuacionId;
    }

    public Integer getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(Integer usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    public Integer getFiltroSeleccionado() {
        return filtroSeleccionado;
    }

    public void setFiltroSeleccionado(Integer filtroSeleccionado) {
        this.filtroSeleccionado = filtroSeleccionado;
    }

    public List<SelectItem> getFiltros() {
        return filtros;
    }

    public void setFiltros(List<SelectItem> filtros) {
        this.filtros = filtros;
    }

    public NombrePeriodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(NombrePeriodo periodo) {
        this.periodo = periodo;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public Date getHoy() {
        return hoy;
    }

    public void setHoy(Date hoy) {
        this.hoy = hoy;
    }
}
