/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "PAGO")
@XmlRootElement
public class Pago implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAGO_ID")
    private Integer pagoId;
    
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    
    @Column(name = "FECHA_CONFIRMADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaConfirmado;
    
    @Column(name = "MONTO")
    private double monto;
    
    @JoinColumn(name = "SERVICIO_FK", referencedColumnName = "SERVICIO_ID")
    @ManyToOne(optional = false)
    private Servicio servicioFk;
    
    @JoinColumn(name = "TIPO_PAGO_FK", referencedColumnName = "TIPO_PAGO_ID")
    @ManyToOne(optional = false)
    private TipoPago tipoPagoFk;
    
    @JoinColumn(name = "USUARIO_FK", referencedColumnName = "USUARIO_ID")
    @ManyToOne(optional = false)
    private Usuario usuarioFk;

    public Pago() {
    }

    public Pago(Integer pagoId) {
        this.pagoId = pagoId;
    }

    public Pago(Integer pagoId, Date fechaPago, double monto) {
        this.pagoId = pagoId;
        this.fechaInicio = fechaPago;
        this.monto = monto;
    }

    public Integer getPagoId() {
        return pagoId;
    }

    public void setPagoId(Integer pagoId) {
        this.pagoId = pagoId;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaPago) {
        this.fechaInicio = fechaPago;
    }

    public Date getFechaConfirmado() {
        return fechaConfirmado;
    }

    public void setFechaConfirmado(Date fechaConfirmado) {
        this.fechaConfirmado = fechaConfirmado;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Servicio getServicioFk() {
        return servicioFk;
    }

    public void setServicioFk(Servicio servicioFk) {
        this.servicioFk = servicioFk;
    }

    public TipoPago getTipoPagoFk() {
        return tipoPagoFk;
    }

    public void setTipoPagoFk(TipoPago tipoPagoFk) {
        this.tipoPagoFk = tipoPagoFk;
    }

    public Usuario getUsuarioFk() {
        return usuarioFk;
    }

    public void setUsuarioFk(Usuario usuarioFk) {
        this.usuarioFk = usuarioFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pagoId != null ? pagoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pago)) {
            return false;
        }
        Pago other = (Pago) object;
        if ((this.pagoId == null && other.pagoId != null) || (this.pagoId != null && !this.pagoId.equals(other.pagoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.Pago[ pagoId=" + pagoId + " ]";
    }
    
}
