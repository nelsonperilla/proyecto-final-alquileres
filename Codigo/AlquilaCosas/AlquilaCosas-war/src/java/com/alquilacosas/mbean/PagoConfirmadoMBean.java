/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.ejb.session.PagoBeanLocal;
import com.alquilacosas.pagos.PaypalUtil;
import com.paypal.soap.api.GetExpressCheckoutDetailsResponseDetailsType;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "pagoConfirmadoBean")
@ViewScoped
public class PagoConfirmadoMBean implements Serializable {

    @EJB
    private PagoBeanLocal pagoBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean login;
    private boolean pagoConfirmado;

    /** Creates a new instance of PagoConfirmadoMBean */
    public PagoConfirmadoMBean() {
    }

    @PostConstruct
    public void confirmar() {
        Logger.getLogger(PagoConfirmadoMBean.class).info("PagoConfirmadoMBean: postconstruct.");
        String token = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("token");
        if (token == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al confirmar pago", 
                    "Ha ocurrido un error al tratar de conectarse con el servidor de PayPal para obtener datos del pago.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        GetExpressCheckoutDetailsResponseDetailsType details = PaypalUtil.getExpressCheckoutDetails(token);
        if(details == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al confirmar pago", 
                    "Ha ocurrido un error al tratar de conectarse con el servidor de PayPal para obtener datos del pago.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        String pagoId = PaypalUtil.doExpressCheckoutService(details);
        if(pagoId != null) {
            pagoConfirmado = true;
            pagoBean.efectuarServicio(Integer.valueOf(pagoId));
        }
    }

    // Getters & Setters
    public boolean isPagoConfirmado() {
        return pagoConfirmado;
    }

    public void setPagoConfirmado(boolean pagoConfirmado) {
        this.pagoConfirmado = pagoConfirmado;
    }

    public ManejadorUsuarioMBean getLogin() {
        return login;
    }

    public void setLogin(ManejadorUsuarioMBean login) {
        this.login = login;
    }
}
