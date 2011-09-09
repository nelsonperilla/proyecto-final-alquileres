/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.dto;

import com.alquilacosas.ejb.entity.Periodo.NombrePeriodo;
import java.io.Serializable;

/**
 *
 * @author damiancardozo
 */
public class PeriodoDTO implements Serializable  {
    
    private int id;
    private NombrePeriodo nombre;

    public PeriodoDTO(int id, NombrePeriodo nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NombrePeriodo getNombre() {
        return nombre;
    }

    public void setNombre(NombrePeriodo nombre) {
        this.nombre = nombre;
    }
    
    
    
}
