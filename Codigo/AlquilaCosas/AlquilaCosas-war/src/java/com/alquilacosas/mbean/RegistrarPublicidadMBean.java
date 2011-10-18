/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.ejb.entity.TipoPago.NombreTipoPago;
import com.alquilacosas.ejb.entity.TipoPublicidad.DuracionPublicidad;
import com.alquilacosas.ejb.entity.TipoPublicidad.UbicacionPublicidad;
import com.alquilacosas.ejb.session.PublicidadBeanLocal;
import com.alquilacosas.pagos.PaypalUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "publicidadBean")
@ViewScoped
public class RegistrarPublicidadMBean implements Serializable {

    @EJB
    private PublicidadBeanLocal publicidadBean;
    @ManagedProperty(value="#{login}")
    private ManejadorUsuarioMBean loginMBean;
    private String titulo, url, caption;
    private Double precio;
    private List<SelectItem> duraciones, ubicaciones;
    private UbicacionPublicidad ubicacionSeleccionada;
    private DuracionPublicidad duracionSeleccionada;
    private byte[] imagen;

    /** Creates a new instance of RegistrarPublicidadMBean */
    public RegistrarPublicidadMBean() {
    }

    @PostConstruct
    public void init() {
        duraciones = new ArrayList<SelectItem>();
        for(DuracionPublicidad d: DuracionPublicidad.values()) {
            duraciones.add(new SelectItem(d, d.toString()));
        }
        ubicaciones = new ArrayList<SelectItem>();
        for(UbicacionPublicidad u: UbicacionPublicidad.values()) {
            ubicaciones.add(new SelectItem(u, u.toString()));
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        imagen = event.getFile().getContents();
        FacesMessage msg = new FacesMessage(event.getFile().getFileName() + 
                "fue cargado correctamente", "");

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void registrarPublicidad() {
        Integer pagoId = null;
        try {
            pagoId = publicidadBean.registrarPublicidad(loginMBean.getUsuarioId(), 
                        titulo, url, caption, ubicacionSeleccionada, 
                        duracionSeleccionada, imagen, precio, NombreTipoPago.PAYPAL);
        } catch (AlquilaCosasException e) {
            Logger.getLogger(RegistrarPublicidadMBean.class).
                    error("registrarPublicidad(). Excepcion al invocar registrarPublicidad(): " 
                    + e + ": " + e.getMessage());
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al registrar publicidad", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        if(pagoId == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al registrar publicidad", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        String descripcion = "Publicar anuncio: " + titulo;
        String redirectUrl = PaypalUtil.setExpressCheckout(descripcion, Integer.toString(pagoId), null, precio.toString());
        if (redirectUrl != null) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            try {
                context.redirect(redirectUrl);
            } catch (Exception e) {
                Logger.getLogger(RegistrarPublicidadMBean.class).
                    error("registrarPublicidad(). Excepcion al invocar redirect(): " 
                    + e + ": " + e.getMessage());
            }
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al comunicarse con paypal", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public void duracionCambio() {
        if(duracionSeleccionada != null && ubicacionSeleccionada != null) {
            precio = publicidadBean.getPrecio(duracionSeleccionada, ubicacionSeleccionada);
        } else {
            precio = null;
        }
    }

    public void ubicacionCambio() {
        if(duracionSeleccionada != null && ubicacionSeleccionada != null) {
            precio = publicidadBean.getPrecio(duracionSeleccionada, ubicacionSeleccionada);
        } else {
            precio = null;
        }
    }
    
    /*
     * Getters & Setters
     */
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public DuracionPublicidad getDuracionSeleccionada() {
        return duracionSeleccionada;
    }

    public void setDuracionSeleccionada(DuracionPublicidad duracionSeleccionada) {
        this.duracionSeleccionada = duracionSeleccionada;
    }

    public List<SelectItem> getDuraciones() {
        return duraciones;
    }

    public void setDuraciones(List<SelectItem> duraciones) {
        this.duraciones = duraciones;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public UbicacionPublicidad getUbicacionSeleccionada() {
        return ubicacionSeleccionada;
    }

    public void setUbicacionSeleccionada(UbicacionPublicidad ubicacionSeleccionada) {
        this.ubicacionSeleccionada = ubicacionSeleccionada;
    }

    public List<SelectItem> getUbicaciones() {
        return ubicaciones;
    }

    public void setUbicaciones(List<SelectItem> ubicaciones) {
        this.ubicaciones = ubicaciones;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ManejadorUsuarioMBean getLoginMBean() {
        return loginMBean;
    }

    public void setLoginMBean(ManejadorUsuarioMBean loginMBean) {
        this.loginMBean = loginMBean;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
