/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "PRECIO_SERVICIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PrecioServicio.findAll", query = "SELECT p FROM PrecioServicio p"),
    @NamedQuery(name = "PrecioServicio.findByPrecioServicioId", query = "SELECT p FROM PrecioServicio p WHERE p.precioServicioId = :precioServicioId"),
    @NamedQuery(name = "PrecioServicio.findByPrecio", query = "SELECT p FROM PrecioServicio p WHERE p.precio = :precio"),
    @NamedQuery(name = "PrecioServicio.findByFechaDesde", query = "SELECT p FROM PrecioServicio p WHERE p.fechaDesde = :fechaDesde"),
    @NamedQuery(name = "PrecioServicio.findByFechaHasta", query = "SELECT p FROM PrecioServicio p WHERE p.fechaHasta = :fechaHasta")})
public class PrecioServicio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRECIO_SERVICIO_ID")
    private Integer precioServicioId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRECIO")
    private double precio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_DESDE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDesde;
    @Column(name = "FECHA_HASTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHasta;
    @JoinColumn(name = "SERVICIO_FK", referencedColumnName = "SERVICIO_ID")
    @ManyToOne(optional = false)
    private Servicio servicioFk;

    public PrecioServicio() {
    }

    public PrecioServicio(Integer precioServicioId) {
        this.precioServicioId = precioServicioId;
    }

    public PrecioServicio(Integer precioServicioId, double precio, Date fechaDesde) {
        this.precioServicioId = precioServicioId;
        this.precio = precio;
        this.fechaDesde = fechaDesde;
    }

    public Integer getPrecioServicioId() {
        return precioServicioId;
    }

    public void setPrecioServicioId(Integer precioServicioId) {
        this.precioServicioId = precioServicioId;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
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

    public Servicio getServicioFk() {
        return servicioFk;
    }

    public void setServicioFk(Servicio servicioFk) {
        this.servicioFk = servicioFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (precioServicioId != null ? precioServicioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrecioServicio)) {
            return false;
        }
        PrecioServicio other = (PrecioServicio) object;
        if ((this.precioServicioId == null && other.precioServicioId != null) || (this.precioServicioId != null && !this.precioServicioId.equals(other.precioServicioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.PrecioServicio[ precioServicioId=" + precioServicioId + " ]";
    }
    
}
