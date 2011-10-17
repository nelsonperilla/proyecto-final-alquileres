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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "DESTACACION")
@DiscriminatorValue(value="D")
//@PrimaryKeyJoinColumn(name="SERVICIO_ID")
@XmlRootElement
public class Destacacion extends Servicio implements Serializable {
    
    @JoinColumn(name = "PUBLICACION_FK", referencedColumnName = "PUBLICACION_ID")
    @ManyToOne(optional = false)
    private Publicacion publicacionFk;
    
    @JoinColumn(name = "TIPO_DESTACACION_FK", referencedColumnName = "TIPO_DESTACACION_ID")
    @ManyToOne(optional = false)
    private TipoDestacacion tipoDestacacionFk;

    public Destacacion() {
    }

    public Publicacion getPublicacionFk() {
        return publicacionFk;
    }

    public void setPublicacionFk(Publicacion publicacionFk) {
        this.publicacionFk = publicacionFk;
    }

    public TipoDestacacion getTipoDestacacionFk() {
        return tipoDestacacionFk;
    }

    public void setTipoDestacacionFk(TipoDestacacion tipoDestacacionFk) {
        this.tipoDestacacionFk = tipoDestacacionFk;
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
        if (!(object instanceof Destacacion)) {
            return false;
        }
        Destacacion other = (Destacacion) object;
        if ((this.servicioId == null && other.servicioId != null) || 
                (this.servicioId != null && 
                !this.servicioId.equals(other.servicioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.Destacacion[ servicioId=" + servicioId + " ]";
    }
    
}
