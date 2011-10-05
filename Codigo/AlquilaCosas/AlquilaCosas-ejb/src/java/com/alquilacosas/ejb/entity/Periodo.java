/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "PERIODO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Periodo.findAll", query = "SELECT p FROM Periodo p ORDER BY p.horas ASC"),
    @NamedQuery(name = "Periodo.findByPeriodoId", query = "SELECT p FROM Periodo p WHERE p.periodoId = :periodoId"),
    @NamedQuery(name = "Periodo.findByNombre", query = "SELECT p FROM Periodo p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Periodo.findByDescripcion", query = "SELECT p FROM Periodo p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "Periodo.findByHoras", query = "SELECT p FROM Periodo p WHERE p.horas = :horas")})
public class Periodo implements Serializable {
    
    public enum NombrePeriodo{
        HORA (1), 
        DIA (24), 
        SEMANA (168), 
        MES (720);
        private final int horas;
        NombrePeriodo(int horas) {
            this.horas = horas;
        }
        
        public int getHoras() {
            return horas;
        }
    };
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERIODO_ID")
    private Integer periodoId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "NOMBRE")
    private NombrePeriodo nombre;
    
    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    @Column(name = "HORAS")
    private int horas;
    
    @OneToMany(mappedBy = "periodoFk")
    private List<Precio> precioList;
    
    @OneToMany(mappedBy = "maxPeriodoAlquilerFk")
    private List<Publicacion> publicacionList;
    
    @OneToMany(mappedBy = "minPeriodoAlquilerFk")
    private List<Publicacion> publicacionList1;

    public Periodo() {
    }

    public Periodo(Integer periodoId) {
        this.periodoId = periodoId;
    }

    public Periodo(Integer periodoId, int horas) {
        this.periodoId = periodoId;
        this.horas = horas;
    }

    public Integer getPeriodoId() {
        return periodoId;
    }

    public void setPeriodoId(Integer periodoId) {
        this.periodoId = periodoId;
    }

    public NombrePeriodo getNombre() {
        return nombre;
    }

    public void setNombre(NombrePeriodo nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    @XmlTransient
    public List<Precio> getPrecioList() {
        return precioList;
    }

    public void setPrecioList(List<Precio> precioList) {
        this.precioList = precioList;
    }

    @XmlTransient
    public List<Publicacion> getPublicacionList() {
        return publicacionList;
    }

    public void setPublicacionList(List<Publicacion> publicacionList) {
        this.publicacionList = publicacionList;
    }

    @XmlTransient
    public List<Publicacion> getPublicacionList1() {
        return publicacionList1;
    }

    public void setPublicacionList1(List<Publicacion> publicacionList1) {
        this.publicacionList1 = publicacionList1;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (periodoId != null ? periodoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Periodo)) {
            return false;
        }
        Periodo other = (Periodo) object;
        if ((this.periodoId == null && other.periodoId != null) || (this.periodoId != null && !this.periodoId.equals(other.periodoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.Periodo[ periodoId=" + periodoId + " ]";
    }
    
}
