/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.common;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ignaciogiagante
 */
public class PrecioFacade {
    
    private int periodoId;
    private int precioId;
    private String periodoNombre;
    private double precio;
    
    public PrecioFacade(){
        
    }
    
    public PrecioFacade( double precio, String periodoNombre){
   
        this.precio = precio;
        this.periodoNombre = periodoNombre;     
    }
    
    public PrecioFacade( int precioId, int periodoId, double precio, String periodoNombre ){
        this.precioId = precioId;
        this.periodoId = periodoId;
        this.precio = precio;
        this.periodoNombre = periodoNombre;      
    }

    public int getPrecioId() {
        return precioId;
    }

    public void setPrecioId(int precioId) {
        this.precioId = precioId;
    }
    
    

    public int getPeriodoId() {
        return periodoId;
    }

    public void setPeriodoId(int periodoId) {
        this.periodoId = periodoId;
    }
    
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

    public List<PrecioFacade> ordenar( List<PrecioFacade> precios ){
        
        List<PrecioFacade> temp = new ArrayList<PrecioFacade>();
        
        for( PrecioFacade precio : precios ){
            
        }
        
        return null;
    }
    
}
