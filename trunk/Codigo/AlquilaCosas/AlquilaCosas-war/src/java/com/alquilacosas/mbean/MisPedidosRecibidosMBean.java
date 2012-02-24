/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.ejb.session.PedidosBeanLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.log4j.Logger;

/**
 *
 * @author ignaciogiagante
 */
@ManagedBean (name = "misPedidosRecibidos")
@ViewScoped
public class MisPedidosRecibidosMBean implements Serializable {
    
    @EJB
    private PedidosBeanLocal alquilerBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean usuarioLogueado;
    
    private List<AlquilerDTO> pedidosRecibidos;
    private Integer publicacionId;
    private Integer usuarioId;
    private Integer alquilerId;
    /** Creates a new instance of MisPedidosRecibidosMBean */
    public MisPedidosRecibidosMBean() {
    }
    
    @PostConstruct
    public void init() {
        Logger.getLogger(MisPedidosRecibidosMBean.class).debug("MisPedidosRecibidosMBean: postconstruct."); 
        pedidosRecibidos = alquilerBean.getPedidosRecibidos(usuarioLogueado.getUsuarioId());
    }
    
    public void confirmarAlquiler(ActionEvent event){
        FacesMessage msg = null;
        try {
            alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
            if( alquilerId == null ){
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al cargar pagina", "No se brindo el id del alquiler");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            }    
            alquilerBean.confirmarPedidoDeAlquiler(alquilerId);
            pedidosRecibidos = alquilerBean.getPedidosRecibidos(usuarioLogueado.getUsuarioId());
            msg = new FacesMessage("Alquiler Confirmado");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (AlquilaCosasException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "No se pudo confirmar el alquiler", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public void rechazarAlquiler(ActionEvent event){
        FacesMessage msg = null;
        
        try {
            alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
            if( alquilerId == null ){
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al cargar pagina", "No se brindo el id del alquiler");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            }    
            alquilerBean.rechazarPedidoDeAlquiler(alquilerId);
            pedidosRecibidos = alquilerBean.getPedidosRecibidos(usuarioLogueado.getUsuarioId());
            msg = new FacesMessage("Alquiler Rechazado");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (AlquilaCosasException e) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "No se pudo confirmar el alquiler", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
         
        }
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

    public PedidosBeanLocal getAlquilerBean() {
        return alquilerBean;
    }

    public void setAlquilerBean(PedidosBeanLocal alquilerBean) {
        this.alquilerBean = alquilerBean;
    }

    public List<AlquilerDTO> getPedidos() {
        return pedidosRecibidos;
    }

    public void setPedidos(List<AlquilerDTO> pedidos) {
        this.pedidosRecibidos = pedidos;
    }

    public Integer getAlquilerId() {
        return alquilerId;
    }

    public void setAlquilerId(Integer alquilerId) {
        this.alquilerId = alquilerId;
    }

    public List<AlquilerDTO> getPedidosRecibidos() {
        return pedidosRecibidos;
    }

    public void setPedidosRecibidos(List<AlquilerDTO> pedidosRecibidos) {
        this.pedidosRecibidos = pedidosRecibidos;
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

}
