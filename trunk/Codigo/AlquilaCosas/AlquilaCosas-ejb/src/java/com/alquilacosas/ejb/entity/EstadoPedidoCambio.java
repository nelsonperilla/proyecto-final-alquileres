/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "ESTADO_PEDIDO_CAMBIO")
@XmlRootElement
public class EstadoPedidoCambio implements Serializable {
    
    public enum NombreEstadoPedidoCambio {
        ENVIADO("Enviado"), 
        ACEPTADO("Aceptado"), 
        RECHAZADO("Rechazado"), 
        CANCELADO("Cancelado");
        String label;
        NombreEstadoPedidoCambio(String label) {
            this.label = label;
        }
        @Override
        public String toString() {
            return label;
        }
    };
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ESTADO_PEDIDO_CAMBIO_ID")
    private Integer estadoPedidoCambioId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "NOMBRE")
    private NombreEstadoPedidoCambio nombre;
    
    @Size(max = 150)
    @Column(name = "DESCRIPCION")
    private String descripcion;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getEstadoPedidoCambioId() {
        return estadoPedidoCambioId;
    }

    public void setEstadoPedidoCambioId(Integer estadoPedidoCambioId) {
        this.estadoPedidoCambioId = estadoPedidoCambioId;
    }

    public NombreEstadoPedidoCambio getNombre() {
        return nombre;
    }

    public void setNombre(NombreEstadoPedidoCambio nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estadoPedidoCambioId != null ? estadoPedidoCambioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoPedidoCambio)) {
            return false;
        }
        EstadoPedidoCambio other = (EstadoPedidoCambio) object;
        if ((this.estadoPedidoCambioId == null && other.estadoPedidoCambioId != null) || (this.estadoPedidoCambioId != null && !this.estadoPedidoCambioId.equals(other.estadoPedidoCambioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.EstadoPedidoCambio[ id=" + estadoPedidoCambioId + " ]";
    }
    
}
