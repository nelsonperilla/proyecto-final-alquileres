/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.ejb.session.AlquilerBeanLocal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author ignaciogiagante
 */
@ManagedBean (name = "misPedidosRealizados")
@RequestScoped
public class MisPedidosRealizados {
    
    @EJB
    private AlquilerBeanLocal alquilerBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean usuarioLogueado;
    
    private List<AlquilerDTO> pedidosRealizados;
    private Integer publicacionId;
    private Integer usuarioId;
    private Integer alquilerId;

    /** Creates a new instance of MisPedidosRealizados */
    public MisPedidosRealizados() {
    }
    
    @PostConstruct
    public void init() {
        pedidosRealizados = alquilerBean.getPedidosRealizados(usuarioLogueado.getUsuarioId());
    }

    public AlquilerBeanLocal getAlquilerBean() {
        return alquilerBean;
    }

    public void setAlquilerBean(AlquilerBeanLocal alquilerBean) {
        this.alquilerBean = alquilerBean;
    }

    public Integer getAlquilerId() {
        return alquilerId;
    }

    public void setAlquilerId(Integer alquilerId) {
        this.alquilerId = alquilerId;
    }

    public List<AlquilerDTO> getPedidosRealizados() {
        return pedidosRealizados;
    }

    public void setPedidosRealizados(List<AlquilerDTO> pedidosRealizados) {
        this.pedidosRealizados = pedidosRealizados;
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

    public ManejadorUsuarioMBean getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(ManejadorUsuarioMBean usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }
}
