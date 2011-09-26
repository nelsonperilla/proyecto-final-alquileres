/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.ejb.session.AlquilerBeanLocal;
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
 * @author ignaciogiagante
 */
@ManagedBean (name = "misPedidosRealizados")
@ViewScoped
public class MisPedidosRealizadosMBean {
    
    @EJB
    private AlquilerBeanLocal alquilerBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean usuarioLogueado;
    
    private List<AlquilerDTO> pedidosRealizados;
    private Integer publicacionId;
    private Integer usuarioId;
    private Integer alquilerId;

    /** Creates a new instance of MisPedidosRealizadosMBean    */
    
    public MisPedidosRealizadosMBean() {
    }
    
    @PostConstruct
    public void init() {
        pedidosRealizados = alquilerBean.getPedidosRealizados(usuarioLogueado.getUsuarioId());
    }
    
    public void cancelarPedidoDeAlquiler( ActionEvent event ){
        FacesMessage msg = null;
        try {
            alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
            if( alquilerId == null ){
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al cargar pagina", "No se brindo el id del alquiler");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            }    
            
            alquilerBean.cancelarPedidoDeAlquiler(alquilerId);
            pedidosRealizados = alquilerBean.getPedidosRealizados(usuarioLogueado.getUsuarioId());
            msg = new FacesMessage("Alquiler Confirmado");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (AlquilaCosasException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "No se pudo confirmar el alquiler", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public String mostrarPublicacion(){
        return "mostrarPublicacion";    
    }
    
    public String mostrarUsuario(){
        return "mostrarUsuario";    
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
