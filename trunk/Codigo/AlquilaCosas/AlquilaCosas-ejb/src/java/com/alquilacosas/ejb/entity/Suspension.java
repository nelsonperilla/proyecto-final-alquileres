/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "SUSPENSION")
@XmlRootElement
public class Suspension implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUSPENSION_ID")
    private Integer suspensionId;
    
    @Column(name = "FECHA_DESDE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDesde;
    
    @Column(name = "FECHA_HASTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHasta;
    
    @JoinColumn(name = "USUARIO_FK", referencedColumnName = "USUARIO_ID")
    @ManyToOne(optional = false)
    private Usuario usuarioFk;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "suspensionFk")
    private List<Advertencia> advertenciaList;

    public Suspension() {
        advertenciaList = new ArrayList<Advertencia>();
    }

    public Suspension(Integer suspensionId) {
        this.suspensionId = suspensionId;
    }

    public Suspension(Integer suspensionId, Date fechaDesde, Date fechaHasta) {
        this.suspensionId = suspensionId;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
    }
    
    public void agregarAdvertencia(Advertencia advertencia) {
        advertenciaList.add(advertencia);
        advertencia.setSuspensionFk(this);
    }

    public Integer getSuspensionId() {
        return suspensionId;
    }

    public void setSuspensionId(Integer suspensionId) {
        this.suspensionId = suspensionId;
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

    public Usuario getUsuarioFk() {
        return usuarioFk;
    }

    public void setUsuarioFk(Usuario usuarioFk) {
        this.usuarioFk = usuarioFk;
    }

    public List<Advertencia> getAdvertenciaList() {
        return advertenciaList;
    }

    public void setAdvertenciaList(List<Advertencia> advertenciaList) {
        this.advertenciaList = advertenciaList;
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
    
}
