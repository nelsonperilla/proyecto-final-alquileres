/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.ejb.entity.Puntuacion;
import com.alquilacosas.ejb.session.AlquileresOfrecidosBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

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
    private List<AlquilerDTO> alquileres;
    private Integer alquilerId, publicacionId, usuarioId, puntuacionId, usuarioLogueado;
    private String comentario;
    private List<SelectItem> puntuaciones;

    /** Creates a new instance of MisAlquileresOfrecidosMBean */
    public AlquileresOfrecidosMBean() {
    }

    @PostConstruct
    public void init() {
        usuarioLogueado = loginBean.getUsuarioId();
        alquileres = alquileresBean.getAlquileres(usuarioLogueado);
        puntuaciones = new ArrayList<SelectItem>();
        List<Puntuacion> listaPuntuacion = alquileresBean.getPuntuaciones();
        for (Puntuacion p : listaPuntuacion) {
            puntuaciones.add(new SelectItem(p.getPuntuacionId(), p.getNombre()));
        }
    }

    public void prepararCalificar(ActionEvent event) {
    }

    public void prepararVerCalificacion(ActionEvent event) {
    }

    public String verPublicacion() {
        return "mostrarPublicacion";
    }

    public String verUsuario() {
        return "";
    }

    public String modificarAlquiler() {
        return "";
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
}
