/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.session.DestacarPublicacionBeanLocal;
import com.paypal.sdk.exceptions.PayPalException;
import com.paypal.sdk.profiles.APIProfile;
import com.paypal.sdk.profiles.ProfileFactory;
import com.paypal.sdk.services.CallerServices;
import com.paypal.soap.api.BasicAmountType;
import com.paypal.soap.api.CurrencyCodeType;
import com.paypal.soap.api.PaymentActionCodeType;
import com.paypal.soap.api.PaymentDetailsType;
import com.paypal.soap.api.SetExpressCheckoutRequestDetailsType;
import com.paypal.soap.api.SetExpressCheckoutRequestType;
import com.paypal.soap.api.SetExpressCheckoutResponseType;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

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
    private String returnURL = "http://www.alquilacosas.com/pagoConfirmado.xhtml";
    private String cancelURL = "http://www.alquilacosas.com/pagoCancelado.xhtml";
    private CurrencyCodeType currencyCodeType = CurrencyCodeType.USD;
    private PaymentActionCodeType paymentAction = PaymentActionCodeType.Sale;
    private String paypalUrl = "https://www.sandbox.paypal.com/webscr?cmd=_express-checkout&useraction=commit&token=";
    
    private PublicacionDTO publicacion;
    private Integer publicacionId;
    private String tituloPublicacion;
    private Date fechaPublicacion, fechaFinalizacion;
    private Double precio;
    
    
    /** Creates a new instance of DestacarPublicacionMBean */
    public DestacarPublicacionMBean() {
    }

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if(id == null || id.equals("")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "La publicacion no se puede destacar", "No se indico ninguna publicacion.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        publicacion = null;
        try {
            publicacion = destacarBean.getPublicacion(Integer.valueOf(id), login.getUsuarioId());
        } catch(AlquilaCosasException e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "La publicacion no se puede destacar", e.getLocalizedMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        
        if(publicacion == null)
            return;
        publicacion.setDestacada(true);
        publicacionId = publicacion.getId();
        tituloPublicacion = publicacion.getTitulo();
        fechaPublicacion = publicacion.getFecha_desde();
        fechaFinalizacion = publicacion.getFecha_hasta();
        precio = destacarBean.getPrecioDestacacion();
        if(precio == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "No se encontro el precio del servicio", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public String verPublicacion() {
        return "";
    }
    
    public void destacar() {

        String url = setExpressCheckout(publicacionId, precio.toString());
        if (url != null) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

            try {
                context.redirect(url);
            } catch (Exception e) {
            }
        } else {
            
        }

    }

    private String setExpressCheckout(Integer publicacionId, String paymentAmount) {

        CallerServices caller = new CallerServices();
        String url = null;

        try {
            //construct and set the profile, these are the credentials we establish as "the shop" with Paypal
            APIProfile profile = ProfileFactory.createSignatureAPIProfile();
//            profile.setAPIUsername("seller_1314645851_biz_api1.gmail.com");
//            profile.setAPIPassword("1314645889");
//            profile.setSignature("AdO-w7F17-IlSqAHGUzyrxYC7d8AAi-Mr9mYpAidn5Robg7h5He8Ize1");
            profile.setAPIUsername("cardozo.damian_api1.gmail.com");
            profile.setAPIPassword("EYGPM2859PNBMSHM");
            profile.setSignature("AAE24aY3lpsWe.zrr2Px0axspt29ABZlMNWmQdQaipmLr0OvBcfQeSOb");
            profile.setEnvironment("sandbox");
            caller.setAPIProfile(profile);

            //construct the request
            SetExpressCheckoutRequestType pprequest = new SetExpressCheckoutRequestType();
            pprequest.setVersion("63.0");

            //construct the details for the request
            SetExpressCheckoutRequestDetailsType details = new SetExpressCheckoutRequestDetailsType();

            PaymentDetailsType paymentDetails = new PaymentDetailsType();
            paymentDetails.setOrderDescription("Destacar publicacion: '" + tituloPublicacion + "'");
            paymentDetails.setInvoiceID("Pago " + Math.random());
            BasicAmountType orderTotal = new BasicAmountType(paymentAmount);
            orderTotal.setCurrencyID(currencyCodeType);
            paymentDetails.setOrderTotal(orderTotal);
            paymentDetails.setPaymentAction(paymentAction);
            details.setPaymentDetails(new PaymentDetailsType[]{paymentDetails});

            details.setReturnURL(returnURL);
            details.setCancelURL(cancelURL);
            details.setCustom(publicacionId.toString());

            //set the details on the request
            pprequest.setSetExpressCheckoutRequestDetails(details);

            //call the actual webservice, passing the constructed request
            SetExpressCheckoutResponseType ppresponse = (SetExpressCheckoutResponseType) caller.call("SetExpressCheckout", pprequest);

            //get the token from the response
            url = paypalUrl + ppresponse.getToken();
        } catch (PayPalException e) {
            //
        }
        return url;
    }

    // Getteers & Setters
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
    
    
}
