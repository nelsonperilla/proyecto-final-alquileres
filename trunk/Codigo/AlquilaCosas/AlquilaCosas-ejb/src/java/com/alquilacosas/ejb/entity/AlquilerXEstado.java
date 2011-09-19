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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "ALQUILER_X_ESTADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AlquilerXEstado.findAll", query = "SELECT a FROM AlquilerXEstado a"),
    @NamedQuery(name = "AlquilerXEstado.findByAlquilerXEstadoId", query = "SELECT a FROM AlquilerXEstado a WHERE a.alquilerXEstadoId = :alquilerXEstadoId"),
    @NamedQuery(name = "AlquilerXEstado.findByFechaDesde", query = "SELECT a FROM AlquilerXEstado a WHERE a.fechaDesde = :fechaDesde"),
    @NamedQuery(name = "AlquilerXEstado.findByFechaHasta", query = "SELECT a FROM AlquilerXEstado a WHERE a.fechaHasta = :fechaHasta"),
    @NamedQuery(name = "AlquilerXEstado.findByAlquiler", query = "SELECT axe FROM AlquilerXEstado axe WHERE axe.alquilerFk.alquilerId = :alquilerId and axe.fechaHasta IS NULL")})
public class AlquilerXEstado implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ALQUILER_X_ESTADO_ID")
    private Integer alquilerXEstadoId;
    
    @Column(name = "FECHA_DESDE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDesde;
    
    @Column(name = "FECHA_HASTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHasta;
    
    @JoinColumn(name = "ESTADO_ALQUILER_FK", referencedColumnName = "ESTADO_ALQUILER_ID")
    @ManyToOne(optional = false)
    private EstadoAlquiler estadoAlquilerFk;
    
    @JoinColumn(name = "ALQUILER_FK", referencedColumnName = "ALQUILER_ID")
    @ManyToOne(optional = false)
    private Alquiler alquilerFk;

    public AlquilerXEstado() {
    }

    public AlquilerXEstado(Integer alquilerXEstadoId) {
        this.alquilerXEstadoId = alquilerXEstadoId;
    }

    public AlquilerXEstado(Integer alquilerXEstadoId, Date fechaDesde) {
        this.alquilerXEstadoId = alquilerXEstadoId;
        this.fechaDesde = fechaDesde;
    }
    
    public AlquilerXEstado( Alquiler a, EstadoAlquiler ea, Date fechaDesde ) {
        this.alquilerFk = a;
        this.estadoAlquilerFk = ea;
        this.fechaDesde = fechaDesde;
    }

    public Integer getAlquilerXEstadoId() {
        return alquilerXEstadoId;
    }

    public void setAlquilerXEstadoId(Integer alquilerXEstadoId) {
        this.alquilerXEstadoId = alquilerXEstadoId;
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

    public EstadoAlquiler getEstadoAlquilerFk() {
        return estadoAlquilerFk;
    }

    public void setEstadoAlquilerFk(EstadoAlquiler estadoAlquilerFk) {
        this.estadoAlquilerFk = estadoAlquilerFk;
    }

    public Alquiler getAlquilerFk() {
        return alquilerFk;
    }

    public void setAlquilerFk(Alquiler alquilerFk) {
        this.alquilerFk = alquilerFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (alquilerXEstadoId != null ? alquilerXEstadoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AlquilerXEstado)) {
            return false;
        }
        AlquilerXEstado other = (AlquilerXEstado) object;
        if ((this.alquilerXEstadoId == null && other.alquilerXEstadoId != null) || (this.alquilerXEstadoId != null && !this.alquilerXEstadoId.equals(other.alquilerXEstadoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.AlquilerXEstado[ alquilerXEstadoId=" + alquilerXEstadoId + " ]";
    }
    
}
