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
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "ADVERTENCIA")
@XmlRootElement
public class Advertencia implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADVERTENCIA_ID")
    private Integer advertenciaId;
    
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    
    @JoinColumn(name = "USUARIO_FK", referencedColumnName = "USUARIO_ID")
    @ManyToOne(optional = false)
    private Usuario usuarioFk;
    
    @JoinColumn(name = "SUSPENSION_FK", referencedColumnName = "SUSPENSION_ID")
    @ManyToOne(optional = false)
    private Suspension suspensionFk;
    
    @OneToOne
    @PrimaryKeyJoinColumn(name="ADVERTENCIA_ID", referencedColumnName="DENUNCIA_ID")
    private Denuncia denuncia;

    public Advertencia() {
    }

    public Advertencia(Integer advertenciaId) {
        this.advertenciaId = advertenciaId;
    }

    public Advertencia(Integer advertenciaId, Date fecha) {
        this.advertenciaId = advertenciaId;
        this.fecha = fecha;
    }

    public Integer getAdvertenciaId() {
        return advertenciaId;
    }

    public void setAdvertenciaId(Integer advertenciaId) {
        this.advertenciaId = advertenciaId;
    }

    public Usuario getUsuarioFk() {
        return usuarioFk;
    }

    public void setUsuarioFk(Usuario usuarioFk) {
        this.usuarioFk = usuarioFk;
    }

    public Denuncia getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }

    public Suspension getSuspensionFk() {
        return suspensionFk;
    }

    public void setSuspensionFk(Suspension suspensionFk) {
        this.suspensionFk = suspensionFk;
    }
    
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
    
}
