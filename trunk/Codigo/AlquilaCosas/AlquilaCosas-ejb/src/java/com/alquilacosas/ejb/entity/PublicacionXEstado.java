/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
    @NamedQuery(name = "PublicacionXEstado.findByPublicacionFk", query = "SELECT p FROM PublicacionXEstado p WHERE p.publicacionXEstadoPK.publicacionFk = :publicacionFk"),
    @NamedQuery(name = "PublicacionXEstado.findByEstadoFk", query = "SELECT p FROM PublicacionXEstado p WHERE p.publicacionXEstadoPK.estadoFk = :estadoFk"),
    @NamedQuery(name = "PublicacionXEstado.findByFechaDesde", query = "SELECT p FROM PublicacionXEstado p WHERE p.fechaDesde = :fechaDesde"),
    @NamedQuery(name = "PublicacionXEstado.findByFechaHasta", query = "SELECT p FROM PublicacionXEstado p WHERE p.fechaHasta = :fechaHasta")})
public class PublicacionXEstado implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_DESDE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDesde;
    @Column(name = "FECHA_HASTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHasta;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PublicacionXEstadoPK publicacionXEstadoPK;
    @JoinColumn(name = "ESTADO_FK", referencedColumnName = "ESTADO_PUBLICACION_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EstadoPublicacion estadoPublicacion;
    @JoinColumn(name = "PUBLICACION_FK", referencedColumnName = "PUBLICACION_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Publicacion publicacion;

    public PublicacionXEstado() {
    }

    public PublicacionXEstado(PublicacionXEstadoPK publicacionXEstadoPK) {
        this.publicacionXEstadoPK = publicacionXEstadoPK;
    }

    public PublicacionXEstado(PublicacionXEstadoPK publicacionXEstadoPK, Date fechaDesde) {
        this.publicacionXEstadoPK = publicacionXEstadoPK;
        this.fechaDesde = fechaDesde;
    }

    public PublicacionXEstado(int publicacionFk, int estadoFk) {
        this.publicacionXEstadoPK = new PublicacionXEstadoPK(publicacionFk, estadoFk);
    }

    public PublicacionXEstadoPK getPublicacionXEstadoPK() {
        return publicacionXEstadoPK;
    }

    public void setPublicacionXEstadoPK(PublicacionXEstadoPK publicacionXEstadoPK) {
        this.publicacionXEstadoPK = publicacionXEstadoPK;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (publicacionXEstadoPK != null ? publicacionXEstadoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PublicacionXEstado)) {
            return false;
        }
        PublicacionXEstado other = (PublicacionXEstado) object;
        if ((this.publicacionXEstadoPK == null && other.publicacionXEstadoPK != null) || (this.publicacionXEstadoPK != null && !this.publicacionXEstadoPK.equals(other.publicacionXEstadoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.PublicacionXEstado[ publicacionXEstadoPK=" + publicacionXEstadoPK + " ]";
    }
    
}
