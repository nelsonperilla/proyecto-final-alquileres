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
@ManagedBean(name="pedidosBean")
@ViewScoped
public class PedidosMBean {

    /** Creates a new instance of PedidosMBean */
    public PedidosMBean() {
    }
    
    @EJB
    private AlquileresBeanLocal alquileresBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean loginBean;
    private Integer usuarioLogueado;
    private List<AlquilerDTO> pedidos;
    
    @PostConstruct
    public void init() {
        usuarioLogueado = loginBean.getUsuarioId();
        if (usuarioLogueado == null) {
            return;
        }
        pedidos = alquileresBean.getPedidos(usuarioLogueado);
    }
    
    public void cancelarAlquiler() {
        
    }

    public List<AlquilerDTO> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<AlquilerDTO> pedidos) {
        this.pedidos = pedidos;
    }

    public ManejadorUsuarioMBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(ManejadorUsuarioMBean loginBean) {
        this.loginBean = loginBean;
    }
}
