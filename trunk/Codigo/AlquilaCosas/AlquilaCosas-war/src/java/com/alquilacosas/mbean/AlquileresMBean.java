/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.ejb.session.AlquileresBeanLocal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

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
    }
    
    public void cancelarAlquiler() {
        
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
}
