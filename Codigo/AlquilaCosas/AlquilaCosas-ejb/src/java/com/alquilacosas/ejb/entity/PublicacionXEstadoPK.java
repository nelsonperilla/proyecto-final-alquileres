/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author damiancardozo
 */
@Embeddable
public class PublicacionXEstadoPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "PUBLICACION_FK")
    private int publicacionFk;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ESTADO_FK")
    private int estadoFk;

    public PublicacionXEstadoPK() {
    }

    public PublicacionXEstadoPK(int publicacionFk, int estadoFk) {
        this.publicacionFk = publicacionFk;
        this.estadoFk = estadoFk;
    }

    public int getPublicacionFk() {
        return publicacionFk;
    }

    public void setPublicacionFk(int publicacionFk) {
        this.publicacionFk = publicacionFk;
    }

    public int getEstadoFk() {
        return estadoFk;
    }

    public void setEstadoFk(int estadoFk) {
        this.estadoFk = estadoFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) publicacionFk;
        hash += (int) estadoFk;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PublicacionXEstadoPK)) {
            return false;
        }
        PublicacionXEstadoPK other = (PublicacionXEstadoPK) object;
        if (this.publicacionFk != other.publicacionFk) {
            return false;
        }
        if (this.estadoFk != other.estadoFk) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.PublicacionXEstadoPK[ publicacionFk=" + publicacionFk + ", estadoFk=" + estadoFk + " ]";
    }
    
}
