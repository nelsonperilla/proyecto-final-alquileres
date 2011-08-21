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
@Table(name = "PUBLICACION_X_ESTADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PublicacionXEstado.findAll", query = "SELECT p FROM PublicacionXEstado p"),
    @NamedQuery(name = "PublicacionXEstado.findByPublicacionFk", query = "SELECT p FROM PublicacionXEstado p WHERE p.publicacion = :publicacion"),
    @NamedQuery(name = "PublicacionXEstado.findByEstadoFk", query = "SELECT p FROM PublicacionXEstado p WHERE p.estadoPublicacion = :estadoPublicacion"),
    @NamedQuery(name = "PublicacionXEstado.findByFechaDesde", query = "SELECT p FROM PublicacionXEstado p WHERE p.fechaDesde = :fechaDesde"),
    @NamedQuery(name = "PublicacionXEstado.findByFechaHasta", query = "SELECT p FROM PublicacionXEstado p WHERE p.fechaHasta = :fechaHasta")})
public class PublicacionXEstado implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "PUBLICACION_X_ESTADO_ID")
    private Integer publicacionXEstadoId;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_DESDE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDesde;
    
    @Column(name = "FECHA_HASTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHasta;
    
    @JoinColumn(name = "ESTADO_FK", referencedColumnName = "ESTADO_PUBLICACION_ID")
    @ManyToOne(optional = false)
    private EstadoPublicacion estadoPublicacion;
    
    @JoinColumn(name = "PUBLICACION_FK", referencedColumnName = "PUBLICACION_ID")
    @ManyToOne(optional = false)
    private Publicacion publicacion;

    public PublicacionXEstado() {
    }
    
    public PublicacionXEstado(Publicacion publicacion, EstadoPublicacion estado) {
        this.publicacion = publicacion;
        this.estadoPublicacion = estado;
        fechaDesde = new Date();
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

    public EstadoPublicacion getEstadoPublicacion() {
        return estadoPublicacion;
    }

    public void setEstadoPublicacion(EstadoPublicacion estadoPublicacion) {
        this.estadoPublicacion = estadoPublicacion;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public PublicacionXEstado(Integer publicacionXEstadoId) {
        this.publicacionXEstadoId = publicacionXEstadoId;
    }

    public PublicacionXEstado(Integer publicacionXEstadoId, Date fechaDesde) {
        this.publicacionXEstadoId = publicacionXEstadoId;
        this.fechaDesde = fechaDesde;
    }

    public Integer getPublicacionXEstadoId() {
        return publicacionXEstadoId;
    }

    public void setPublicacionXEstadoId(Integer publicacionXEstadoId) {
        this.publicacionXEstadoId = publicacionXEstadoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (publicacionXEstadoId != null ? publicacionXEstadoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PublicacionXEstado)) {
            return false;
        }
        PublicacionXEstado other = (PublicacionXEstado) object;
        if ((this.publicacionXEstadoId == null && other.publicacionXEstadoId != null) || (this.publicacionXEstadoId != null && !this.publicacionXEstadoId.equals(other.publicacionXEstadoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.PublicacionXEstado[ publicacionXEstadoId=" + publicacionXEstadoId + " ]";
    }
    
}
