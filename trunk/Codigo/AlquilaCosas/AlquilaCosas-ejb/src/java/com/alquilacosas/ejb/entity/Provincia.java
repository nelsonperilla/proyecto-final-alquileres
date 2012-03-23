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
@Table(name = "PROVINCIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Provincia.findAll", query = "SELECT p FROM Provincia p"),
    @NamedQuery(name = "Provincia.findByProvinciaId", query = "SELECT p FROM Provincia p WHERE p.provinciaId = :provinciaId"),
    @NamedQuery(name = "Provincia.findByNombre", query = "SELECT p FROM Provincia p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Provincia.findByPais", query = "SELECT p FROM Provincia p WHERE p.paisFk = :pais")})
public class Provincia implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROVINCIA_ID")
    private Integer provinciaId;
    
    @Column(name = "NOMBRE")
    private String nombre;
    
    @JoinColumn(name = "PAIS_FK", referencedColumnName = "PAIS_ID")
    @ManyToOne(optional = false)
    private Pais paisFk;
    
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "provinciaFk")
    //private List<Domicilio> domicilioList;

    public Provincia() {
    }

    public Provincia(Integer provinciaId) {
        this.provinciaId = provinciaId;
    }

    public Provincia(Integer provinciaId, String nombre) {
        this.provinciaId = provinciaId;
        this.nombre = nombre;
    }

    public Integer getProvinciaId() {
        return provinciaId;
    }

    public void setProvinciaId(Integer provinciaId) {
        this.provinciaId = provinciaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Pais getPaisFk() {
        return paisFk;
    }

    public void setPaisFk(Pais paisFk) {
        this.paisFk = paisFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (provinciaId != null ? provinciaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Provincia)) {
            return false;
        }
        Provincia other = (Provincia) object;
        if ((this.provinciaId == null && other.provinciaId != null) || (this.provinciaId != null && !this.provinciaId.equals(other.provinciaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.Provincia[ provinciaId=" + provinciaId + " ]";
    }
    
}
