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
public class UsuarioXEstadoPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "USUARIO_FK")
    private int usuarioFk;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ESTADO_FK")
    private int estadoFk;

    public UsuarioXEstadoPK() {
    }

    public UsuarioXEstadoPK(int usuarioFk, int estadoFk) {
        this.usuarioFk = usuarioFk;
        this.estadoFk = estadoFk;
    }

    public int getUsuarioFk() {
        return usuarioFk;
    }

    public void setUsuarioFk(int usuarioFk) {
        this.usuarioFk = usuarioFk;
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
        hash += (int) usuarioFk;
        hash += (int) estadoFk;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioXEstadoPK)) {
            return false;
        }
        UsuarioXEstadoPK other = (UsuarioXEstadoPK) object;
        if (this.usuarioFk != other.usuarioFk) {
            return false;
        }
        if (this.estadoFk != other.estadoFk) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.UsuarioXEstadoPK[ usuarioFk=" + usuarioFk + ", estadoFk=" + estadoFk + " ]";
    }
    
}
