/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "TIPO_PAGO")
@XmlRootElement
public class TipoPago implements Serializable {
    
    public enum NombreTipoPago {PAYPAL}
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TIPO_PAGO_ID")
    private Integer tipoPagoId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "NOMBRE")
    private NombreTipoPago nombre;
    
    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoPagoFk")
    private List<Pago> pagoList;

    public TipoPago() {
    }

    public TipoPago(Integer tipoPagoId) {
        this.tipoPagoId = tipoPagoId;
    }

    public TipoPago(Integer tipoPagoId, NombreTipoPago nombre) {
        this.tipoPagoId = tipoPagoId;
        this.nombre = nombre;
    }

    public Integer getTipoPagoId() {
        return tipoPagoId;
    }

    public void setTipoPagoId(Integer tipoPagoId) {
        this.tipoPagoId = tipoPagoId;
    }

    public NombreTipoPago getNombre() {
        return nombre;
    }

    public void setNombre(NombreTipoPago nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Pago> getPagoList() {
        return pagoList;
    }

    public void setPagoList(List<Pago> pagoList) {
        this.pagoList = pagoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipoPagoId != null ? tipoPagoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoPago)) {
            return false;
        }
        TipoPago other = (TipoPago) object;
        if ((this.tipoPagoId == null && other.tipoPagoId != null) || 
                (this.tipoPagoId != null && !this.tipoPagoId.equals(other.tipoPagoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.TipoPago[ tipoPagoId=" + tipoPagoId + " ]";
    }
    
}
