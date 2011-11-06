/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "MOTIVO_DENUNCIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MotivoDenuncia.findAll", query = "SELECT m FROM MotivoDenuncia m"),
    @NamedQuery(name = "MotivoDenuncia.findByTipo", query = "SELECT m FROM MotivoDenuncia m WHERE m.motivoPublicacion = :motivoPublicacion")})
public class MotivoDenuncia implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOTIVO_DENUNCIA_ID")
    private Integer motivoDenunciaId;
    
    @Column(name = "NOMBRE")
    private String nombre;
    
    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "MOTIVO_PUBLICACION")
    private boolean motivoPublicacion;
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getMotivoDenunciaId() {
        return motivoDenunciaId;
    }

    public void setMotivoDenunciaId(Integer motivoDenunciaId) {
        this.motivoDenunciaId = motivoDenunciaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (motivoDenunciaId != null ? motivoDenunciaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MotivoDenuncia)) {
            return false;
        }
        MotivoDenuncia other = (MotivoDenuncia) object;
        if ((this.motivoDenunciaId == null && other.motivoDenunciaId != null) || 
                (this.motivoDenunciaId != null && !this.motivoDenunciaId.equals(other.motivoDenunciaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.MotivoDenuncia[ motivoDenunciaId=" + motivoDenunciaId + " ]";
    }

    /**
     * @return the motivo_publicacion
     */
    public boolean isMotivoPublicacion() {
        return motivoPublicacion;
    }

    /**
     * @param motivo_publicacion the motivo_publicacion to set
     */
    public void setMotivoPublicacion(boolean motivoPublicacion) {
        this.motivoPublicacion = motivoPublicacion;
    }
    
}
