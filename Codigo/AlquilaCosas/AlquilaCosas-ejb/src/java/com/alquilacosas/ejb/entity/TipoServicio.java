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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "TIPO_SERVICIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoServicio.findAll", query = "SELECT t FROM TipoServicio t"),
    @NamedQuery(name = "TipoServicio.findByTipoServicioId", query = "SELECT t FROM TipoServicio t WHERE t.tipoServicioId = :tipoServicioId"),
    @NamedQuery(name = "TipoServicio.findByNombre", query = "SELECT t FROM TipoServicio t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TipoServicio.findByDescripcion", query = "SELECT t FROM TipoServicio t WHERE t.descripcion = :descripcion")})
public class TipoServicio implements Serializable {
    
    public enum NombreTipoServicio {DESTACACION, PUBLICIDAD};
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TIPO_SERVICIO_ID")
    private Integer tipoServicioId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "NOMBRE")
    private NombreTipoServicio nombre;
    
    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoServicioFk")
    private List<PrecioServicio> precioServicioList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoServicioFk")
    private List<Servicio> servicioList;

    public TipoServicio() {
    }

    public TipoServicio(Integer tipoServicioId) {
        this.tipoServicioId = tipoServicioId;
    }

    public TipoServicio(Integer tipoServicioId, NombreTipoServicio nombre, String descripcion) {
        this.tipoServicioId = tipoServicioId;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getTipoServicioId() {
        return tipoServicioId;
    }

    public void setTipoServicioId(Integer tipoServicioId) {
        this.tipoServicioId = tipoServicioId;
    }

    public NombreTipoServicio getNombre() {
        return nombre;
    }

    public void setNombre(NombreTipoServicio nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<PrecioServicio> getPrecioServicioList() {
        return precioServicioList;
    }

    public void setPrecioServicioList(List<PrecioServicio> precioServicioList) {
        this.precioServicioList = precioServicioList;
    }

    @XmlTransient
    public List<Servicio> getServicioList() {
        return servicioList;
    }

    public void setServicioList(List<Servicio> servicioList) {
        this.servicioList = servicioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipoServicioId != null ? tipoServicioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoServicio)) {
            return false;
        }
        TipoServicio other = (TipoServicio) object;
        if ((this.tipoServicioId == null && other.tipoServicioId != null) || (this.tipoServicioId != null && !this.tipoServicioId.equals(other.tipoServicioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.TipoServicio[ tipoServicioId=" + tipoServicioId + " ]";
    }
    
}
