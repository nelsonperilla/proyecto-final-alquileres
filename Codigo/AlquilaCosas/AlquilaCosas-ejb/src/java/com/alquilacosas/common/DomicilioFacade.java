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
public class DomicilioFacade implements Serializable {
    
    private String calle, depto, barrio, ciudad;
    private int numero;
    private Integer piso;
    
    public DomicilioFacade() {
        
    }
    
    public DomicilioFacade(String calle, int numero, int piso,
            String depto, String barrio, String ciudad) {
        this.calle = calle;
        this.numero = numero;
        this.piso = piso;
        this.depto = depto;
        this.barrio = barrio;
        this.ciudad = ciudad;
    }
    
    public DomicilioFacade(String calle, int numero, String barrio, 
            String ciudad) {
        this.calle = calle;
        this.numero = numero;
        this.barrio = barrio;
        this.ciudad = ciudad;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDepto() {
        return depto;
    }

    public void setDepto(String depto) {
        this.depto = depto;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Integer getPiso() {
        return piso;
    }

    public void setPiso(Integer piso) {
        this.piso = piso;
    }
    
}
