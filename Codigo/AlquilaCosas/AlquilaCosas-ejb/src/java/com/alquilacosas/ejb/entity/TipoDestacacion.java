/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "TIPO_DESTACACION")
@XmlRootElement
public class TipoDestacacion implements Serializable {
    
    public enum NombreTipoDestacacion {
        SEMANAL("Semanal"), 
        BISEMANAL("Bisemanal"), 
        MENSUAL("Mensual"), 
        BIMENSUAL("Bimensual");
        String label;
        NombreTipoDestacacion(String label) {
            this.label = label;
        }
        @Override
        public String toString() {
            return label;
        }
    };
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TIPO_DESTACACION_ID")
    private Integer tipoDestacacionId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "NOMBRE")
    private NombreTipoDestacacion nombre;
    
    @Column(name = "DESCRIPCION")
    private String descripcion;
    
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoServicioFk")
//    private List<PrecioServicio> precioServicioList;
//    
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoServicioFk")
//    private List<Servicio> servicioList;

    public TipoDestacacion() {
    }

    public TipoDestacacion(Integer tipoServicioId) {
        this.tipoDestacacionId = tipoServicioId;
    }

    public TipoDestacacion(Integer tipoServicioId, NombreTipoDestacacion nombre, String descripcion) {
        this.tipoDestacacionId = tipoServicioId;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getTipoServicioId() {
        return tipoDestacacionId;
    }

    public void setTipoServicioId(Integer tipoServicioId) {
        this.tipoDestacacionId = tipoServicioId;
    }

    public NombreTipoDestacacion getNombre() {
        return nombre;
    }

    public void setNombre(NombreTipoDestacacion nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipoDestacacionId != null ? tipoDestacacionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoDestacacion)) {
            return false;
        }
        TipoDestacacion other = (TipoDestacacion) object;
        if ((this.tipoDestacacionId == null && other.tipoDestacacionId != null) || 
                (this.tipoDestacacionId != null && 
                !this.tipoDestacacionId.equals(other.tipoDestacacionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.TipoDestacacion[ tipoDestacacionId=" + tipoDestacacionId + " ]";
    }
    
}
