/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "PUNTUACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Puntuacion.findAll", query = "SELECT p FROM Puntuacion p ORDER BY p.nombre DESC"),
    @NamedQuery(name = "Puntuacion.findByPuntuacionId", query = "SELECT p FROM Puntuacion p WHERE p.puntuacionId = :puntuacionId"),
    @NamedQuery(name = "Puntuacion.findByNombre", query = "SELECT p FROM Puntuacion p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Puntuacion.findByPuntaje", query = "SELECT p FROM Puntuacion p WHERE p.puntaje = :puntaje")})
public class Puntuacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "PUNTUACION_ID")
    private Integer puntuacionId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOMBRE")
    private String nombre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "PUNTAJE")
    private BigDecimal puntaje;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "puntuacionFk")
    private List<Reputacion> reputacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "puntuacionFk")
    private List<Calificacion> calificacionList;

    public Puntuacion() {
    }

    public Puntuacion(Integer puntuacionId) {
        this.puntuacionId = puntuacionId;
    }

    public Puntuacion(Integer puntuacionId, String nombre, BigDecimal puntaje) {
        this.puntuacionId = puntuacionId;
        this.nombre = nombre;
        this.puntaje = puntaje;
    }

    public Integer getPuntuacionId() {
        return puntuacionId;
    }

    public void setPuntuacionId(Integer puntuacionId) {
        this.puntuacionId = puntuacionId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(BigDecimal puntaje) {
        this.puntaje = puntaje;
    }

    @XmlTransient
    public List<Reputacion> getReputacionList() {
        return reputacionList;
    }

    public void setReputacionList(List<Reputacion> reputacionList) {
        this.reputacionList = reputacionList;
    }

    @XmlTransient
    public List<Calificacion> getCalificacionList() {
        return calificacionList;
    }

    public void setCalificacionList(List<Calificacion> calificacionList) {
        this.calificacionList = calificacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (puntuacionId != null ? puntuacionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Puntuacion)) {
            return false;
        }
        Puntuacion other = (Puntuacion) object;
        if ((this.puntuacionId == null && other.puntuacionId != null) || (this.puntuacionId != null && !this.puntuacionId.equals(other.puntuacionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.Puntuacion[ puntuacionId=" + puntuacionId + " ]";
    }
    
}
