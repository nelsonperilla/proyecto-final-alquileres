/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "DENUNCIA_X_ESTADO")
@XmlRootElement
public class DenunciaXEstado implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DENUNCIA_X_ESTADO_ID")
    private Integer denunciaXEstadoId;

    @Column(name = "FECHA_DESDE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDesde;
    
    @Column(name = "FECHA_HASTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHasta;
    
    @JoinColumn(name = "ESTADO_FK", referencedColumnName = "ESTADO_DENUNCIA_ID")
    @ManyToOne(optional = false)
    private EstadoDenuncia estadoDenunciaFk;
    
    @JoinColumn(name = "DENUNCIA_FK", referencedColumnName = "DENUNCIA_ID")
    @ManyToOne(optional = false)
    private Denuncia denunciaFk;

    public DenunciaXEstado() {   
    }
    
    public DenunciaXEstado(Denuncia denuncia, EstadoDenuncia estado) {
         this.fechaDesde = new Date();
         this.fechaHasta = null;
        this.denunciaFk = denuncia;
        this.estadoDenunciaFk = estado;
    }
    
    public Integer getDenunciaXEstadoId() {
        return denunciaXEstadoId;
    }

    public void setDenunciaXEstadoId(Integer denunciaXEstadoId) {
        this.denunciaXEstadoId = denunciaXEstadoId;
    }

    public Denuncia getDenunciaFk() {
        return denunciaFk;
    }

    public void setDenunciaFk(Denuncia denunciaFk) {
        this.denunciaFk = denunciaFk;
    }

    public EstadoDenuncia getEstadoDenunciaFk() {
        return estadoDenunciaFk;
    }

    public void setEstadoDenunciaFk(EstadoDenuncia estadoDenunciaFk) {
        this.estadoDenunciaFk = estadoDenunciaFk;
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
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (denunciaXEstadoId != null ? denunciaXEstadoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DenunciaXEstado)) {
            return false;
        }
        DenunciaXEstado other = (DenunciaXEstado) object;
        if ((this.denunciaXEstadoId == null && other.denunciaXEstadoId != null) 
                || (this.denunciaXEstadoId != null && 
                !this.denunciaXEstadoId.equals(other.denunciaXEstadoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.DenunciaXEstado[ denunciaXEstadoId=" + denunciaXEstadoId + " ]";
    }
    
}
