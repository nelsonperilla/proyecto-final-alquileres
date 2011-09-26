/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.ejb.session.DestacarPublicacionBeanLocal;
import com.paypal.sdk.exceptions.PayPalException;
import com.paypal.sdk.profiles.APIProfile;
import com.paypal.sdk.profiles.ProfileFactory;
import com.paypal.sdk.services.CallerServices;
import com.paypal.soap.api.DoExpressCheckoutPaymentRequestDetailsType;
import com.paypal.soap.api.DoExpressCheckoutPaymentRequestType;
import com.paypal.soap.api.DoExpressCheckoutPaymentResponseDetailsType;
import com.paypal.soap.api.DoExpressCheckoutPaymentResponseType;
import com.paypal.soap.api.GetExpressCheckoutDetailsRequestType;
import com.paypal.soap.api.GetExpressCheckoutDetailsResponseDetailsType;
import com.paypal.soap.api.GetExpressCheckoutDetailsResponseType;
import com.paypal.soap.api.PayerInfoType;
import com.paypal.soap.api.PaymentDetailsType;
import com.paypal.soap.api.PaymentInfoType;
import com.paypal.soap.api.PaymentStatusCodeType;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "pagoConfirmadoBean")
@ViewScoped
public class PagoConfirmadoMBean implements Serializable {

    @EJB
    private DestacarPublicacionBeanLocal destacarBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean login;
    private boolean pagoConfirmado;

    /** Creates a new instance of PagoConfirmadoMBean */
    public PagoConfirmadoMBean() {
    }

    @PostConstruct
    public void confirmar() {
        
        String token = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("token");
        if (token == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al confirmar pago", 
                    "Ha ocurrido un error al tratar de conectarse con el servidor de PayPal para obtener datos del pago.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        GetExpressCheckoutDetailsResponseDetailsType details = getExpressCheckoutDetails(token);
        if(details == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al confirmar pago", 
                    "Ha ocurrido un error al tratar de conectarse con el servidor de PayPal para obtener datos del pago.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        Integer publicacionId = doExpressCheckoutService(details);
        if(publicacionId != null) {
            pagoConfirmado = true;
            destacarBean.destacarPublicacion(publicacionId);
        }
    }

    private GetExpressCheckoutDetailsResponseDetailsType getExpressCheckoutDetails(String token) {

        GetExpressCheckoutDetailsResponseDetailsType details = null;
        CallerServices caller = new CallerServices();
        try {
            APIProfile profile = ProfileFactory.createSignatureAPIProfile();
//            profile.setAPIUsername("seller_1314645851_biz_api1.gmail.com");
//            profile.setAPIPassword("1314645889");
//            profile.setSignature("AdO-w7F17-IlSqAHGUzyrxYC7d8AAi-Mr9mYpAidn5Robg7h5He8Ize1");
            profile.setAPIUsername("cardozo.damian_api1.gmail.com");
            profile.setAPIPassword("EYGPM2859PNBMSHM");
            profile.setSignature("AAE24aY3lpsWe.zrr2Px0axspt29ABZlMNWmQdQaipmLr0OvBcfQeSOb");
            profile.setEnvironment("sandbox");
            caller.setAPIProfile(profile);

            GetExpressCheckoutDetailsRequestType pprequest = new GetExpressCheckoutDetailsRequestType();
            pprequest.setVersion("63.0");
            pprequest.setToken(token);

            GetExpressCheckoutDetailsResponseType ppresponse = (GetExpressCheckoutDetailsResponseType) caller.call("GetExpressCheckoutDetails", pprequest);
            details = ppresponse.getGetExpressCheckoutDetailsResponseDetails();
        } catch (PayPalException e) {
            System.out.println("exception in getExpressCheckoutDetails: " + e);
        }
        return details;
    }

    private Integer doExpressCheckoutService(GetExpressCheckoutDetailsResponseDetailsType response) {

        DoExpressCheckoutPaymentResponseDetailsType type = null;
        CallerServices caller = new CallerServices();
        Integer publicacionId = null;
        try {
            APIProfile profile = ProfileFactory.createSignatureAPIProfile();
//            profile.setAPIUsername("seller_1314645851_biz_api1.gmail.com");
//            profile.setAPIPassword("1314645889");
//            profile.setSignature("AdO-w7F17-IlSqAHGUzyrxYC7d8AAi-Mr9mYpAidn5Robg7h5He8Ize1");
            profile.setAPIUsername("cardozo.damian_api1.gmail.com");
            profile.setAPIPassword("EYGPM2859PNBMSHM");
            profile.setSignature("AAE24aY3lpsWe.zrr2Px0axspt29ABZlMNWmQdQaipmLr0OvBcfQeSOb");
            profile.setEnvironment("sandbox");
            caller.setAPIProfile(profile);

            DoExpressCheckoutPaymentRequestType pprequest = new DoExpressCheckoutPaymentRequestType();
            pprequest.setVersion("63.0");

            DoExpressCheckoutPaymentResponseType ppresponse = new DoExpressCheckoutPaymentResponseType();

            DoExpressCheckoutPaymentRequestDetailsType paymentDetailsRequestType = new DoExpressCheckoutPaymentRequestDetailsType();
            paymentDetailsRequestType.setToken(response.getToken());

            PayerInfoType payerInfo = response.getPayerInfo();
            paymentDetailsRequestType.setPayerID(payerInfo.getPayerID());

            PaymentDetailsType paymentDetails = response.getPaymentDetails(0);
            paymentDetailsRequestType.setPaymentAction(paymentDetails.getPaymentAction());

            paymentDetailsRequestType.setPaymentDetails(response.getPaymentDetails());
            pprequest.setDoExpressCheckoutPaymentRequestDetails(paymentDetailsRequestType);
            
            ppresponse = (DoExpressCheckoutPaymentResponseType) caller.call("DoExpressCheckoutPayment", pprequest);
            type = ppresponse.getDoExpressCheckoutPaymentResponseDetails();

        } catch (PayPalException e) {
            System.out.println("exception in doExpresCheckoutService(): " + e);
            return null;
        }
        if (type != null) {
            PaymentInfoType paymentInfo = null;
            try {
                paymentInfo = type.getPaymentInfo(0);
                publicacionId = Integer.valueOf(response.getCustom());
            } catch(Exception ex) {
                System.out.println("error: " + ex);
                return null;
            }
            if (paymentInfo != null && paymentInfo.getPaymentStatus().equals(PaymentStatusCodeType.fromString("Completed"))) {
                System.out.println("Payment completed.");
                return publicacionId;
            } else {
                System.out.println("Payment not completed.. (" + paymentInfo.getPaymentStatus() + ")");
                return null;
            }
        } else {
            System.out.println("Problem executing DoExpressCheckoutPayment.. Maybe you tried to process an ExpressCheckout that has already been processed.");
            return null;
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
