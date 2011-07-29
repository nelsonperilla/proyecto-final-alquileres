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
@Table(name = "SUSPENSION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Suspension.findAll", query = "SELECT s FROM Suspension s"),
    @NamedQuery(name = "Suspension.findBySuspensionId", query = "SELECT s FROM Suspension s WHERE s.suspensionId = :suspensionId"),
    @NamedQuery(name = "Suspension.findByDuracion", query = "SELECT s FROM Suspension s WHERE s.duracion = :duracion"),
    @NamedQuery(name = "Suspension.findByFecha", query = "SELECT s FROM Suspension s WHERE s.fecha = :fecha")})
public class Suspension implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "SUSPENSION_ID")
    private Integer suspensionId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DURACION")
    private String duracion;
    @JoinColumn(name = "USUARIO_FK", referencedColumnName = "USUARIO_ID")
    @ManyToOne(optional = false)
    private Usuario usuarioFk;

    public Suspension() {
    }

    public Suspension(Integer suspensionId) {
        this.suspensionId = suspensionId;
    }

    public Suspension(Integer suspensionId, String duracion, Date fecha) {
        this.suspensionId = suspensionId;
        this.duracion = duracion;
        this.fecha = fecha;
    }

    public Integer getSuspensionId() {
        return suspensionId;
    }

    public void setSuspensionId(Integer suspensionId) {
        this.suspensionId = suspensionId;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
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
        hash += (suspensionId != null ? suspensionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Suspension)) {
            return false;
        }
        Suspension other = (Suspension) object;
        if ((this.suspensionId == null && other.suspensionId != null) || (this.suspensionId != null && !this.suspensionId.equals(other.suspensionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.Suspension[ suspensionId=" + suspensionId + " ]";
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
}
