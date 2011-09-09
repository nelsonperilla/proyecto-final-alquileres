/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.dto;

import com.alquilacosas.ejb.entity.Periodo.NombrePeriodo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ignaciogiagante
 */
public class PrecioDTO implements Serializable  {
    
    private int periodoId;
    private int precioId;
    private NombrePeriodo periodoNombre;
    private Double precio;
    
    public PrecioDTO(){
    }
    
    public PrecioDTO( double precio, NombrePeriodo periodoNombre){
        this.precio = precio;
        this.periodoNombre = periodoNombre;     
    }
    
    public PrecioDTO( int precioId, double precio, NombrePeriodo periodoNombre ){
        this.precioId = precioId;
        this.precio = precio;
        this.periodoNombre = periodoNombre;      
    }
    
    public PrecioDTO( int precioId, int periodoId, double precio, NombrePeriodo periodoNombre ){
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

    public NombrePeriodo getPeriodoNombre() {
        return periodoNombre;
    }

    public void setPeriodoNombre(NombrePeriodo periodoNombre) {
        this.periodoNombre = periodoNombre;
    }

    public List<PrecioDTO> ordenar( List<PrecioDTO> precios ){
        
        List<PrecioDTO> temp = new ArrayList<PrecioDTO>();
        
        for( PrecioDTO precio : precios ){
            
        }
        
        return null;
    }
    
}
