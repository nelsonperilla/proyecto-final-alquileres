/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.entity.TipoDestacacion.NombreTipoDestacacion;
import com.alquilacosas.ejb.entity.TipoPago.NombreTipoPago;
import com.alquilacosas.ejb.session.DestacarPublicacionBeanLocal;
import com.alquilacosas.pagos.PaypalUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name="destacarBean")
@ViewScoped
public class DestacarPublicacionMBean implements Serializable {

    @EJB
    private DestacarPublicacionBeanLocal destacarBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean login;
    private List<SelectItem> tipos;
    private NombreTipoDestacacion tipoSeleccionado;
    private PublicacionDTO publicacion;
    private Integer publicacionId;
    private String tituloPublicacion;
    private Date fechaPublicacion, fechaFinalizacion;
    private Double precio;
    private int pagoId = -1;
    private String descripcion;
    
    
    /** Creates a new instance of DestacarPublicacionMBean */
    public DestacarPublicacionMBean() {
    }

    @PostConstruct
    public void init() {
        Logger.getLogger(DestacarPublicacionMBean.class).debug("DestacarPublicacionMBean: postconstruct."); 
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if(id == null || id.equals("")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "La publicacion no se puede destacar", "No se indico ninguna publicacion.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            redirect("/vistas/inicio.xhtml");
            return;
        }
        publicacion = null;
        try {
            publicacion = destacarBean.getPublicacion(Integer.valueOf(id), login.getUsuarioId());
        } catch(AlquilaCosasException e) {
            Logger.getLogger(DestacarPublicacionMBean.class).error("La publicacion indicada no se puede destacar: " + e.getMessage());
            redirect("/AlquilaCosas-war/faces/vistas/inicio.xhtml");
            return;
        }
        
        if(publicacion == null)
            return;
        publicacion.setDestacada(true);
        publicacionId = publicacion.getId();
        tituloPublicacion = publicacion.getTitulo();
        fechaPublicacion = publicacion.getFechaDesde();
        fechaFinalizacion = publicacion.getFechaHasta();
        tipos = new ArrayList<SelectItem>();
        for(NombreTipoDestacacion nombre: NombreTipoDestacacion.values()) {
            tipos.add(new SelectItem(nombre, nombre.toString()));
        }
    }
    
    public void redirect(String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (Exception e) {
            Logger.getLogger(DestacarPublicacionMBean.class).error("Excepcion al ejecutar redirect().");
        }
    }
    
    public String verPublicacion() {
        return "";
    }
    
    public void destacar() {
        pagoId = destacarBean.iniciarCobroDestacacion(login.getUsuarioId(), 
                publicacionId, tipoSeleccionado,  precio, NombreTipoPago.PAYPAL);
        if(pagoId < 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al registrar pago.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        descripcion = "Destacacion" + tipoSeleccionado.toString() + ". Publicacion: " + publicacion.getTitulo();
        String url = PaypalUtil.setExpressCheckout(descripcion, Integer.toString(pagoId), 
                Integer.toString(publicacionId), precio.toString());
        if (url != null) {
            redirect(url);
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al comunicarse con paypal", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public void destacarDm() {
        pagoId = destacarBean.iniciarCobroDestacacion(login.getUsuarioId(), 
                publicacionId, tipoSeleccionado,  precio, NombreTipoPago.DINEROMAIL);
        if(pagoId < 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al registrar pago.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().addCallbackParam("registrado", false);
            return;
        }
        descripcion = "Destacacion" + tipoSeleccionado.toString() + ". Publicacion: " + publicacion.getTitulo();
        RequestContext.getCurrentInstance().addCallbackParam("registrado", true);
    }
    
    public void tipoSeleccionado() {
        if(tipoSeleccionado == null)
            precio = null;
        else {
            precio = destacarBean.getPrecioDestacacion(tipoSeleccionado);
            if(precio == null) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "No se encontro el precio del servicio", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
    }

    // Getters & Setters
    
    public ManejadorUsuarioMBean getLogin() {
        return login;
    }

    public void setLogin(ManejadorUsuarioMBean login) {
        this.login = login;
    }

    public String getTituloPublicacion() {
        return tituloPublicacion;
    }

    public void setTituloPublicacion(String tituloPublicacion) {
        this.tituloPublicacion = tituloPublicacion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public PublicacionDTO getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(PublicacionDTO publicacion) {
        this.publicacion = publicacion;
    }

    public NombreTipoDestacacion getTipoSeleccionado() {
        return tipoSeleccionado;
    }

    public void setTipoSeleccionado(NombreTipoDestacacion tipoSeleccionado) {
        this.tipoSeleccionado = tipoSeleccionado;
    }

    public List<SelectItem> getTipos() {
        return tipos;
    }

    public void setTipos(List<SelectItem> tipos) {
        this.tipos = tipos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPagoId() {
        return pagoId;
    }

    public void setPagoId(int pagoId) {
        this.pagoId = pagoId;
    }
    
}
