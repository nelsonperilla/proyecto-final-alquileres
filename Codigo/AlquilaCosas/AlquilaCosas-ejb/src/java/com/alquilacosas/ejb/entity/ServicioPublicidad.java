/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "SERVICIO_PUBLICIDAD")
@DiscriminatorValue(value="P")
//@PrimaryKeyJoinColumn(name="SERVICIO_ID")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServicioPublicidad.findAll", query = "SELECT s FROM ServicioPublicidad s"),
    @NamedQuery(name = "ServicioPublicidad.findByServicioId", query = "SELECT s FROM ServicioPublicidad s WHERE s.servicioId = :servicioId")})
public class ServicioPublicidad extends Servicio implements Serializable {

    @JoinColumn(name = "PUBLICIDAD_FK", referencedColumnName = "PUBLICIDAD_ID")
    @ManyToOne(optional = false)
    private Publicidad publicidadFk;

    public ServicioPublicidad() {
    }

    public ServicioPublicidad(Integer servicioId) {
        this.servicioId = servicioId;
    }

    public Publicidad getPublicidadFk() {
        return publicidadFk;
    }

    public void setPublicidadFk(Publicidad publicidadFk) {
        this.publicidadFk = publicidadFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (servicioId != null ? servicioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServicioPublicidad)) {
            return false;
        }
        ServicioPublicidad other = (ServicioPublicidad) object;
        if ((this.servicioId == null && other.servicioId != null) || (this.servicioId != null && !this.servicioId.equals(other.servicioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.ServicioPublicidad[ servicioId=" + servicioId + " ]";
    }
    
}
