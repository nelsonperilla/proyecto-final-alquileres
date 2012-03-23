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
@Table(name = "PRECIO_TIPO_SERVICIO")
@XmlRootElement
public class PrecioTipoServicio implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRECIO_TIPO_SERVICIO_ID")
    private Integer precioTipoServicioId;
    
    @Column(name = "PRECIO")
    private double precio;
    
    @Column(name = "FECHA_DESDE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDesde;
    
    @Column(name = "FECHA_HASTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHasta;
    
    @JoinColumn(name = "TIPO_DESTACACION_FK", referencedColumnName = "TIPO_DESTACACION_ID")
    @ManyToOne(optional = false)
    private TipoDestacacion tipoDestacacionFk;
    
    @JoinColumn(name = "TIPO_PUBLICIDAD_FK", referencedColumnName = "TIPO_PUBLICIDAD_ID")
    @ManyToOne(optional = false)
    private TipoPublicidad tipoPublicidadFk;

    public PrecioTipoServicio() {
    }

    public PrecioTipoServicio(Integer precioServicioId) {
        this.precioTipoServicioId = precioServicioId;
    }

    public PrecioTipoServicio(Integer precioServicioId, double precio, Date fechaDesde) {
        this.precioTipoServicioId = precioServicioId;
        this.precio = precio;
        this.fechaDesde = fechaDesde;
    }

    public Integer getPrecioServicioId() {
        return precioTipoServicioId;
    }

    public void setPrecioServicioId(Integer precioServicioId) {
        this.precioTipoServicioId = precioServicioId;
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
    
    public TipoDestacacion getTipoServicioFk() {
        return tipoDestacacionFk;
    }

    public void setTipoServicioFk(TipoDestacacion tipoServicioFk) {
        this.tipoDestacacionFk = tipoServicioFk;
    }

    public Integer getPrecioTipoServicioId() {
        return precioTipoServicioId;
    }

    public void setPrecioTipoServicioId(Integer precioTipoServicioId) {
        this.precioTipoServicioId = precioTipoServicioId;
    }

    public TipoDestacacion getTipoDestacacionFk() {
        return tipoDestacacionFk;
    }

    public void setTipoDestacacionFk(TipoDestacacion tipoDestacacionFk) {
        this.tipoDestacacionFk = tipoDestacacionFk;
    }

    public TipoPublicidad getTipoPublicidadFk() {
        return tipoPublicidadFk;
    }

    public void setTipoPublicidadFk(TipoPublicidad tipoPublicidadFk) {
        this.tipoPublicidadFk = tipoPublicidadFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (precioTipoServicioId != null ? precioTipoServicioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PrecioTipoServicio)) {
            return false;
        }
        PrecioTipoServicio other = (PrecioTipoServicio) object;
        if ((this.precioTipoServicioId == null && other.precioTipoServicioId != null) || 
                (this.precioTipoServicioId != null 
                && !this.precioTipoServicioId.equals(other.precioTipoServicioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.PrecioTipoServicio[ precioTipoServicioId=" + precioTipoServicioId + " ]";
    }
    
}
