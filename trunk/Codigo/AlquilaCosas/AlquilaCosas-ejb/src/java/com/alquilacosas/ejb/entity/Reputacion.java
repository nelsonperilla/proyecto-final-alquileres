/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "REPUTACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reputacion.findAll", query = "SELECT r FROM Reputacion r"),
    @NamedQuery(name = "Reputacion.findByReputacionId", query = "SELECT r FROM Reputacion r WHERE r.reputacionId = :reputacionId"),
    @NamedQuery(name = "Reputacion.findByValor", query = "SELECT r FROM Reputacion r WHERE r.valor = :valor")})
public class Reputacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPUTACION_ID")
    private Integer reputacionId;
    
    @Basic(optional = false)
    @Column(name = "VALOR")
    private int valor;
    
    @JoinColumn(name = "PUNTUACION_FK", referencedColumnName = "PUNTUACION_ID")
    @ManyToOne(optional = false)
    private Puntuacion puntuacionFk;
    
    @JoinColumn(name = "USUARIO_FK", referencedColumnName = "USUARIO_ID")
    @ManyToOne(optional = false)
    private Usuario usuarioFk;

    public Reputacion() {
    }

    public Reputacion(Integer reputacionId) {
        this.reputacionId = reputacionId;
    }

    public Reputacion(Integer reputacionId, int valor) {
        this.reputacionId = reputacionId;
        this.valor = valor;
    }

    public Integer getReputacionId() {
        return reputacionId;
    }

    public void setReputacionId(Integer reputacionId) {
        this.reputacionId = reputacionId;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public Puntuacion getPuntuacionFk() {
        return puntuacionFk;
    }

    public void setPuntuacionFk(Puntuacion puntuacionFk) {
        this.puntuacionFk = puntuacionFk;
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
        hash += (reputacionId != null ? reputacionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Reputacion)) {
            return false;
        }
        Reputacion other = (Reputacion) object;
        if ((this.reputacionId == null && other.reputacionId != null) || (this.reputacionId != null && !this.reputacionId.equals(other.reputacionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.Reputacion[ reputacionId=" + reputacionId + " ]";
    }
    
}
