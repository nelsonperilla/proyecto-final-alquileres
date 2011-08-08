/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "buscarBean")
@RequestScoped
public class BuscarMBean {

    private String buscar;
    
    /** Creates a new instance of BuscarMBean */
    public BuscarMBean() {
    }

    public String buscar() {
        return "";
    }
    
    public String getBuscar() {
        return buscar;
    }

    public void setBuscar(String buscar) {
        this.buscar = buscar;
    }

}
