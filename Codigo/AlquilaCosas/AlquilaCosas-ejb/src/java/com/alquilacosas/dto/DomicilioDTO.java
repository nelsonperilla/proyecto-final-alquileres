/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.dto;

import java.io.Serializable;

/**
 *
 * @author damiancardozo
 */
public class DomicilioDTO implements Serializable {
    
    private String calle, depto, barrio, ciudad, provincia, pais;
    private int numero;
    private Integer piso;
    private int provinciaId, paisId;
    
    public DomicilioDTO() {
        
    }
    
    public DomicilioDTO(String calle, int numero, int piso,
            String depto, String barrio, String ciudad) {
        this.calle = calle;
        this.numero = numero;
        this.piso = piso;
        this.depto = depto;
        this.barrio = barrio;
        this.ciudad = ciudad;
    }
    
    public DomicilioDTO(String calle, int numero, String barrio, 
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

    public int getPaisId() {
        return paisId;
    }

    public void setPaisId(int paisId) {
        this.paisId = paisId;
    }

    public int getProvinciaId() {
        return provinciaId;
    }

    public void setProvinciaId(int provinciaId) {
        this.provinciaId = provinciaId;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
    
}
