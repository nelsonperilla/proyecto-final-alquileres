/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.pagos;

import com.alquilacosas.mbean.PagoConfirmadoMBean;
import com.paypal.sdk.exceptions.PayPalException;
import com.paypal.sdk.profiles.APIProfile;
import com.paypal.sdk.profiles.ProfileFactory;
import com.paypal.sdk.services.CallerServices;
import com.paypal.soap.api.BasicAmountType;
import com.paypal.soap.api.CurrencyCodeType;
import com.paypal.soap.api.DoExpressCheckoutPaymentRequestDetailsType;
import com.paypal.soap.api.DoExpressCheckoutPaymentRequestType;
import com.paypal.soap.api.DoExpressCheckoutPaymentResponseDetailsType;
import com.paypal.soap.api.DoExpressCheckoutPaymentResponseType;
import com.paypal.soap.api.GetExpressCheckoutDetailsRequestType;
import com.paypal.soap.api.GetExpressCheckoutDetailsResponseDetailsType;
import com.paypal.soap.api.GetExpressCheckoutDetailsResponseType;
import com.paypal.soap.api.PayerInfoType;
import com.paypal.soap.api.PaymentActionCodeType;
import com.paypal.soap.api.PaymentDetailsType;
import com.paypal.soap.api.PaymentInfoType;
import com.paypal.soap.api.PaymentStatusCodeType;
import com.paypal.soap.api.SetExpressCheckoutRequestDetailsType;
import com.paypal.soap.api.SetExpressCheckoutRequestType;
import com.paypal.soap.api.SetExpressCheckoutResponseType;
import org.apache.log4j.Logger;

/**
 *
 * @author damiancardozo
 */
public class PaypalUtil {
    
    private static String returnURL = "http://www.alquilacosas.com/pagoConfirmado.xhtml";
    private static String cancelURL = "http://www.alquilacosas.com/pagoCancelado.xhtml";
    private static CurrencyCodeType currencyCodeType = CurrencyCodeType.USD;
    private static PaymentActionCodeType paymentAction = PaymentActionCodeType.Sale;
    private static String paypalUrl = "https://www.sandbox.paypal.com/webscr?cmd=_express-checkout&useraction=commit&token=";
    
    /**
     * Primer metodo en la secuencia de comunicacion con paypal.
     * Se le provee a Paypal con la informacion acerca de la facturacion (monto, concepto y id de factura)
     * tambien se le informa el usuario que va a recibir el dinero
     * @param descripcion Descripcion del cobro, que aparecera en la factura.
     * @param idPago Id del pago a efectuar (proveniente de la BD).
     * @param identificador Algun identificador extra en caso de ser necesario. Ej: id de la publicacion a destacar
     * @param paymentAmount Monto a cobrar
     * @return 
     */
    public static String setExpressCheckout(String descripcion, String idPago, String identificador,
            String paymentAmount) {

        CallerServices caller = new CallerServices();
        String url = null;

        try {
            //construct and set the profile, these are the credentials we establish as "the shop" with Paypal
            APIProfile profile = ProfileFactory.createSignatureAPIProfile();
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
            paymentDetails.setOrderDescription(descripcion);
            paymentDetails.setInvoiceID(idPago);
            BasicAmountType orderTotal = new BasicAmountType(paymentAmount);
            orderTotal.setCurrencyID(currencyCodeType);
            paymentDetails.setOrderTotal(orderTotal);
            paymentDetails.setPaymentAction(paymentAction);
            details.setPaymentDetails(new PaymentDetailsType[]{paymentDetails});

            details.setReturnURL(returnURL);
            details.setCancelURL(cancelURL);
            if(identificador != null)
                details.setCustom(identificador);

            //set the details on the request
            pprequest.setSetExpressCheckoutRequestDetails(details);

            //call the actual webservice, passing the constructed request
            SetExpressCheckoutResponseType ppresponse = (SetExpressCheckoutResponseType) caller.call("SetExpressCheckout", pprequest);

            //get the token from the response
            url = paypalUrl + ppresponse.getToken();
        } catch (PayPalException e) {
            Logger.getLogger(PaypalUtil.class).
                    error("setExpressCheckout(). Excepcion al conectarse con Paypal: " 
                    + e + ": " + e.getMessage());
        }
        return url;
    }
    
    /**
     * Segundo metodo a invocar en la secuencia de comunicacion con paypal
     * @param token Parametro de la URL que envio Paypal para identificar el cobro
     * @return 
     */
    public static GetExpressCheckoutDetailsResponseDetailsType getExpressCheckoutDetails(String token) {

        GetExpressCheckoutDetailsResponseDetailsType details = null;
        CallerServices caller = new CallerServices();
        try {
            APIProfile profile = ProfileFactory.createSignatureAPIProfile();
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
            Logger.getLogger(PagoConfirmadoMBean.class)
                    .error("getExpressCheckoutDetails(). Excepcion al conectarse con Paypal: "
                    + e + ": " + e.getMessage());
        }
        return details;
    }

    /**
     * Tercer metodo a invocar en la secuencia de comunicacion con Paypal
     * @param response
     * @return El id del pago confirmado.
     */
    public static String doExpressCheckoutService(GetExpressCheckoutDetailsResponseDetailsType response) {

        DoExpressCheckoutPaymentResponseDetailsType type = null;
        CallerServices caller = new CallerServices();
        String pagoId = null;
        try {
            APIProfile profile = ProfileFactory.createSignatureAPIProfile();
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
            Logger.getLogger(PagoConfirmadoMBean.class)
                    .error("doExpressCheckoutService(). Excepcion al conectarse con Paypal: "
                    + e + ": " + e.getMessage());
            return null;
        }
        if (type != null) {
            PaymentInfoType paymentInfo = null;
            try {
                paymentInfo = type.getPaymentInfo(0);
                pagoId = response.getInvoiceID();
            } catch(Exception ex) {
                Logger.getLogger(PagoConfirmadoMBean.class)
                    .error("doExpressCheckoutService(). Excepcion en getPaymentInfo(0): "
                    + ex + ": " + ex.getMessage());
                return null;
            }
            if (paymentInfo != null && paymentInfo.getPaymentStatus().equals(PaymentStatusCodeType.fromString("Completed"))) {
                Logger.getLogger(PagoConfirmadoMBean.class)
                    .error("doExpressCheckoutService(). Pago completado.");
                return pagoId;
            } else {
                Logger.getLogger(PagoConfirmadoMBean.class)
                    .error("doExpressCheckoutService(). Pago no completado. PaymentInfoType es nulo o tiene un estado de pago incorrecto.");
                return null;
            }
        } else {
            Logger.getLogger(PagoConfirmadoMBean.class)
                    .error("doExpressCheckoutService(). Problema al ejecutar el metodo... "
                    + "Tal vez se trato de procesar un pago que ya fue procesado.");
            return null;
        }
    }
    
}
