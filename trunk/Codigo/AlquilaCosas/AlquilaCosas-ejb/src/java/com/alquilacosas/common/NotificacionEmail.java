/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.common;

import java.io.Serializable;

/**
 *
 * @author damiancardozo
 */
public class NotificacionEmail implements Serializable {
    
    private String destinatario, asunto, texto;

    public NotificacionEmail(String dest, String asunto, String texto) {
        this.destinatario = dest;
        this.asunto = asunto;
        this.texto = texto;
    }
    
    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }    
    
}
