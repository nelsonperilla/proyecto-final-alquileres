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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "ADVERTENCIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Advertencia.findAll", query = "SELECT a FROM Advertencia a"),
    @NamedQuery(name = "Advertencia.findByAdvertenciaId", query = "SELECT a FROM Advertencia a WHERE a.advertenciaId = :advertenciaId"),
    @NamedQuery(name = "Advertencia.findByFecha", query = "SELECT a FROM Advertencia a WHERE a.fecha = :fecha"),
    @NamedQuery(name = "Advertencia.findByMotivo", query = "SELECT a FROM Advertencia a WHERE a.motivo = :motivo")})
public class Advertencia implements Serializable {
    @Basic(optional =     false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ADVERTENCIA_ID")
    private Integer advertenciaId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "MOTIVO")
    private String motivo;
    @JoinColumn(name = "USUARIO_FK", referencedColumnName = "USUARIO_ID")
    @ManyToOne(optional = false)
    private Usuario usuarioFk;

    public Advertencia() {
    }

    public Advertencia(Integer advertenciaId) {
        this.advertenciaId = advertenciaId;
    }

    public Advertencia(Integer advertenciaId, Date fecha, String motivo) {
        this.advertenciaId = advertenciaId;
        this.fecha = fecha;
        this.motivo = motivo;
    }

    public Integer getAdvertenciaId() {
        return advertenciaId;
    }

    public void setAdvertenciaId(Integer advertenciaId) {
        this.advertenciaId = advertenciaId;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Usuario getUsuarioFk() {
        return usuarioFk;
    }

    public void setUsuarioFk(Usuario usuarioFk) {
        this.usuarioFk = usuarioFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (advertenciaId != null ? advertenciaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Advertencia)) {
            return false;
        }
        Advertencia other = (Advertencia) object;
        if ((this.advertenciaId == null && other.advertenciaId != null) || (this.advertenciaId != null && !this.advertenciaId.equals(other.advertenciaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.Advertencia[ advertenciaId=" + advertenciaId + " ]";
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
}
