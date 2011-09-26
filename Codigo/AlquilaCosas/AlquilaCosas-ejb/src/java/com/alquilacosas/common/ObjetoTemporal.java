/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.common;

import java.util.Date;

/**
 *
 * @author ignaciogiagante
 */
public class ObjetoTemporal {
    
    private Integer periodoDeCamdioId;
    private Date fechaInicio;
    private Date fechaFin;
    private Integer cantidad;
    private Integer periodo;
    private Integer duracion;
    
    public ObjetoTemporal( Integer alquilerId, Date fechaInicio, Date fechaFin,
            Integer cantidad, Integer periodo, Integer duracion){
        this.periodoDeCamdioId = alquilerId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cantidad = cantidad;
        this.periodo = periodo;
        this.duracion = duracion;
    }

    public Integer getPeriodoDeCambioId() {
        return periodoDeCamdioId;
    }

    public void setPeriodoDeCambioId(Integer alquilerId) {
        this.periodoDeCamdioId = alquilerId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }
    
    
}
