/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "PEDIDO_CAMBIO")
@XmlRootElement
public class PedidoCambio implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PEDIDO_CAMBIO_ID")
    private Integer pedidoCambioId;
    
    @Column(name = "DURACION")
    private int duracion;
    
    @JoinColumn(name = "ALQUILER_FK", referencedColumnName = "ALQUILER_ID")
    @ManyToOne(optional = false)
    private Alquiler alquilerFk;
    
    @JoinColumn(name = "PERIODO_FK", referencedColumnName = "PERIODO_ID")
    @ManyToOne(optional = false)
    private Periodo periodoFk;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedidoCambioFk")
    private List<PedidoCambioXEstado> pedidoCambioXEstadoList;

    public PedidoCambio() {
        pedidoCambioXEstadoList = new ArrayList<PedidoCambioXEstado>();
    }
    
    public void agregarPedidoCambioXEstado(PedidoCambioXEstado pcxe) {
        pcxe.setPedidoCambioFk(this);
        pedidoCambioXEstadoList.add(pcxe);
    }
    
    /*
     * Getters & Setters
     */
    
    public Alquiler getAlquilerFk() {
        return alquilerFk;
    }

    public void setAlquilerFk(Alquiler alquilerFk) {
        this.alquilerFk = alquilerFk;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public Integer getPedidoCambioId() {
        return pedidoCambioId;
    }

    public void setPedidoCambioId(Integer pedidoCambioId) {
        this.pedidoCambioId = pedidoCambioId;
    }

    @XmlTransient
    public List<PedidoCambioXEstado> getPedidoCambioXEstadoList() {
        return pedidoCambioXEstadoList;
    }

    public void setPedidoCambioXEstadoList(List<PedidoCambioXEstado> pedidoCambioXEstadoList) {
        this.pedidoCambioXEstadoList = pedidoCambioXEstadoList;
    }

    public Periodo getPeriodoFk() {
        return periodoFk;
    }

    public void setPeriodoFk(Periodo periodoFk) {
        this.periodoFk = periodoFk;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pedidoCambioId != null ? pedidoCambioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PedidoCambio)) {
            return false;
        }
        PedidoCambio other = (PedidoCambio) object;
        if ((this.pedidoCambioId == null && other.pedidoCambioId != null) || 
                (this.pedidoCambioId != null && !this.pedidoCambioId.equals(other.pedidoCambioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.PedidoCambio[ id=" + pedidoCambioId + " ]";
    }
    
}
