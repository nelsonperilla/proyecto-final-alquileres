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
@Table(name = "PEDIDO_CAMBIO_X_ESTADO")
@XmlRootElement
public class PedidoCambioXEstado implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PEDIDO_CAMBIO_X_ESTADO_ID")
    private Integer pedidoCambioXEstadoId;

    @Column(name = "FECHA_DESDE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDesde;
    
    @Column(name = "FECHA_HASTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHasta;
    
    @JoinColumn(name = "ESTADO_FK", referencedColumnName = "ESTADO_PEDIDO_CAMBIO_ID")
    @ManyToOne(optional = false)
    private EstadoPedidoCambio estadoPedidoCambioFk;
    
    @JoinColumn(name = "PEDIDO_CAMBIO_FK", referencedColumnName = "PEDIDO_CAMBIO_ID")
    @ManyToOne(optional = false)
    private PedidoCambio pedidoCambioFk;

    public PedidoCambioXEstado() {   
    }
    
    public PedidoCambioXEstado(PedidoCambio pedido, EstadoPedidoCambio estado) {
        this.pedidoCambioFk = pedido;
        this.estadoPedidoCambioFk = estado;
    }
    
    public Integer getPedidoCambioXEstadoId() {
        return pedidoCambioXEstadoId;
    }

    public void setPedidoCambioXEstadoId(Integer pedidoCambioXEstadoId) {
        this.pedidoCambioXEstadoId = pedidoCambioXEstadoId;
    }
    
    public PedidoCambio getPedidoCambioFk() {
        return pedidoCambioFk;
    }

    public void setPedidoCambioFk(PedidoCambio pedidoCambioFk) {
        this.pedidoCambioFk = pedidoCambioFk;
    }

    public EstadoPedidoCambio getEstadoPedidoCambioFk() {
        return estadoPedidoCambioFk;
    }

    public void setEstadoPedidoCambioFk(EstadoPedidoCambio estadoPedidoCambioFk) {
        this.estadoPedidoCambioFk = estadoPedidoCambioFk;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pedidoCambioXEstadoId != null ? pedidoCambioXEstadoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PedidoCambioXEstado)) {
            return false;
        }
        PedidoCambioXEstado other = (PedidoCambioXEstado) object;
        if ((this.pedidoCambioXEstadoId == null && other.pedidoCambioXEstadoId != null) 
                || (this.pedidoCambioXEstadoId != null && 
                !this.pedidoCambioXEstadoId.equals(other.pedidoCambioXEstadoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.PedidoCambioXEstado[ id=" + pedidoCambioXEstadoId + " ]";
    }
    
}
