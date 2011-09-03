/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.dto;

import com.alquilacosas.ejb.entity.EstadoAlquiler;
import java.util.Date;

/**
 *
 * @author ignaciogiagante
 */
public class AlquilerDTO {
    
    private int idPublicacion;
    private int idUsuario;
    private int idAlquiler;
    
    private Date fechaInicioAlquiler;
    private Date fechaFinAlquiler;
    private int cantidad;
    
    private EstadoAlquiler estadoAlquiler;
    
    public AlquilerDTO(){
        
    }
    
    public AlquilerDTO( int idPublicacion, int idUsuario, int idAlquiler,
            Date fechaInicioAlquiler, Date fechaFinAlquiler, int cantidad,
            EstadoAlquiler estadoAlquiler){
        
        this.idPublicacion = idPublicacion;
        this.idUsuario = idUsuario;
        this.idAlquiler = idAlquiler;
        this.fechaInicioAlquiler = fechaInicioAlquiler;
        this.fechaFinAlquiler = fechaFinAlquiler;
        this.cantidad = cantidad;
        this.estadoAlquiler = estadoAlquiler;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public EstadoAlquiler getEstadoAlquiler() {
        return estadoAlquiler;
    }

    public void setEstadoAlquiler(EstadoAlquiler estadoAlquiler) {
        this.estadoAlquiler = estadoAlquiler;
    }

    public Date getFechaFinAlquiler() {
        return fechaFinAlquiler;
    }

    public void setFechaFinAlquiler(Date fechaFinAlquiler) {
        this.fechaFinAlquiler = fechaFinAlquiler;
    }

    public Date getFechaInicioAlquiler() {
        return fechaInicioAlquiler;
    }

    public void setFechaInicioAlquiler(Date fechaInicioAlquiler) {
        this.fechaInicioAlquiler = fechaInicioAlquiler;
    }

    public int getIdAlquiler() {
        return idAlquiler;
    }

    public void setIdAlquiler(int idAlquiler) {
        this.idAlquiler = idAlquiler;
    }

    public int getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(int idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
