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
public class PedidoCambioDTO implements Serializable {
    
    private int id;
    private int alquilerId;
    private NombrePeriodo periodo;
    private int duracion;

    public PedidoCambioDTO(int id, NombrePeriodo periodo, int duracion) {
        this.id = id;
        this.periodo = periodo;
        this.duracion = duracion;
    }

    public PedidoCambioDTO(int id, int alquilerId, NombrePeriodo periodo, int duracion) {
        this.id = id;
        this.alquilerId = alquilerId;
        this.periodo = periodo;
        this.duracion = duracion;
    }

    public int getAlquilerId() {
        return alquilerId;
    }

    public void setAlquilerId(int alquilerId) {
        this.alquilerId = alquilerId;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NombrePeriodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(NombrePeriodo periodo) {
        this.periodo = periodo;
    }
    
}
