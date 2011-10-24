/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.dto;

import com.alquilacosas.ejb.entity.TipoPago.NombreTipoPago;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author damiancardozo
 */
public class PagoDTO implements Serializable {
    
    public enum EstadoPago {INICIADO, CONFIRMADO};
    public enum TipoServicio {DESTACACION, PUBLICIDAD};
    
    private int id;
    private Date fechaIniciado, fechaConfirmado;
    private double monto;
    private NombreTipoPago tipoPago;
    private EstadoPago estado;
    private TipoServicio tipoServicio;

    public PagoDTO(int id, Date fechaIniciado, Date fechaConfirmado, double monto, NombreTipoPago tipoPago, EstadoPago estado, TipoServicio tipoServicio) {
        this.id = id;
        this.fechaIniciado = fechaIniciado;
        this.fechaConfirmado = fechaConfirmado;
        this.monto = monto;
        this.tipoPago = tipoPago;
        this.estado = estado;
        this.tipoServicio = tipoServicio;
    }

    public Date getFechaConfirmado() {
        return fechaConfirmado;
    }

    public void setFechaConfirmado(Date fechaConfirmado) {
        this.fechaConfirmado = fechaConfirmado;
    }

    public Date getFechaIniciado() {
        return fechaIniciado;
    }

    public void setFechaIniciado(Date fechaIniciado) {
        this.fechaIniciado = fechaIniciado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getTipoPago() {
        return tipoPago.toString();
    }

    public void setTipoPago(NombreTipoPago tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getEstado() {
        return estado.toString();
    }

    public void setEstado(EstadoPago estado) {
        this.estado = estado;
    }

    public TipoServicio getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }
    
}
