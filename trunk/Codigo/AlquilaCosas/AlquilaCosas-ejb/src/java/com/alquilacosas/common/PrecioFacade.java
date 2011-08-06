/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.common;

import java.util.List;

/**
 *
 * @author ignaciogiagante
 */
public class PrecioFacade {
    
    private String periodoNombre;
    private double precio;

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getPeriodoNombre() {
        return periodoNombre;
    }

    public void setPeriodoNombre(String periodoNombre) {
        this.periodoNombre = periodoNombre;
    }
    
}
