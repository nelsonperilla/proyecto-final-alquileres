/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.ejb.session.AlquileresBeanLocal;
import com.alquilacosas.ejb.session.PedidosBeanLocal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "pedidosBean")
@ViewScoped
public class PedidosMBean {

    @EJB
    private AlquileresBeanLocal alquileresBean;
    @EJB
    private PedidosBeanLocal pedidosBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean loginBean;
    private Integer usuarioLogueado;
    private List<AlquilerDTO> pedidos;
    private Integer alquilerId;

    @PostConstruct
    public void init() {
        usuarioLogueado = loginBean.getUsuarioId();
        if (usuarioLogueado == null) {
            return;
        }
        pedidos = alquileresBean.getPedidos(usuarioLogueado);
    }

    public void confirmarPedido(ActionEvent event) {
        FacesMessage msg = null;
        try {
            alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
            if (alquilerId == null) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error al cargar pagina", "No se brindo el id del alquiler");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
            pedidosBean.confirmarPedidoDeAlquiler(alquilerId);
            pedidos = alquileresBean.getPedidos(loginBean.getUsuarioId());
            msg = new FacesMessage("Alquiler Confirmado");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (AlquilaCosasException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "No se pudo confirmar el alquiler", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void rechazarPedido(ActionEvent event) {
        FacesMessage msg = null;
        try {
            alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
            if (alquilerId == null) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error al cargar pagina", "No se brindo el id del alquiler");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
            pedidosBean.rechazarPedidoDeAlquiler(alquilerId);
            pedidos = alquileresBean.getPedidos(loginBean.getUsuarioId());
            msg = new FacesMessage("Alquiler Rechazado");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (AlquilaCosasException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "No se pudo confirmar el alquiler", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public void cancelarPedido(ActionEvent event) {
        FacesMessage msg = null;
        try {
            alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
            if( alquilerId == null ){
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al cargar pagina", "No se brindo el id del alquiler");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }    
            
            pedidosBean.cancelarPedidoDeAlquiler(alquilerId);
            pedidos = alquileresBean.getPedidos(loginBean.getUsuarioId());
            msg = new FacesMessage("Alquiler Cancelado");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (AlquilaCosasException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "No se pudo confirmar el alquiler", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
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
