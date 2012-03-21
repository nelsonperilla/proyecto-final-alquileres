/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "buscarBean")
@SessionScoped
public class BuscarMBean {

    private String criterio;

    public String buscar() {
        //navegacion definida en faces-config
        return "";
    }
    
    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

}
