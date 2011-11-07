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
@Table(name = "TIPO_PUBLICIDAD")
@XmlRootElement
public class TipoPublicidad implements Serializable {
    
    public enum UbicacionPublicidad {
        CARRUSEL("Carrusel"), 
        LATERAL_IZQUIERDA("Lateral izquierda"), 
        LATERAL_DERECHA("Lateral derecha");
        String label;
        UbicacionPublicidad(String label) {
            this.label = label;
        }
        @Override
        public String toString() {
            return label;
        }
    };
    public enum DuracionPublicidad {
        SEMANAL("Semanal"), 
        BISEMANAL("Bisemanal"), 
        MENSUAL("Mensual"), 
        BIMENSUAL("Bimensual");
        String label;
        DuracionPublicidad(String label) {
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
    @Column(name = "TIPO_PUBLICIDAD_ID")
    private Integer tipoPublicidadId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "UBICACION")
    private UbicacionPublicidad ubicacion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "DURACION")
    private DuracionPublicidad duracion;
    
    public TipoPublicidad() {
    }
    
    public TipoPublicidad(UbicacionPublicidad ubicacion, DuracionPublicidad duracion) {
        this.ubicacion = ubicacion;
        this.duracion = duracion;
    }

    public DuracionPublicidad getDuracion() {
        return duracion;
    }

    public void setDuracion(DuracionPublicidad duracion) {
        this.duracion = duracion;
    }

    public Integer getTipoPublicidadId() {
        return tipoPublicidadId;
    }

    public void setTipoPublicidadId(Integer tipoPublicidadId) {
        this.tipoPublicidadId = tipoPublicidadId;
    }

    public UbicacionPublicidad getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(UbicacionPublicidad ubicacion) {
        this.ubicacion = ubicacion;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipoPublicidadId != null ? tipoPublicidadId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoPublicidad)) {
            return false;
        }
        TipoPublicidad other = (TipoPublicidad) object;
        if ((this.tipoPublicidadId == null && other.tipoPublicidadId != null) || 
                (this.tipoPublicidadId != null && 
                !this.tipoPublicidadId.equals(other.tipoPublicidadId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.TipoPublicidad[ tipoPublicidadId=" + tipoPublicidadId + " ]";
    }
    
}
