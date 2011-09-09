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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "VARIABLE_DE_SISTEMA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VariableDeSistema.findAll", query = "SELECT v FROM VariableDeSistema v"),
    @NamedQuery(name = "VariableDeSistema.findByVariableId", query = "SELECT v FROM VariableDeSistema v WHERE v.variableId = :variableId"),
    @NamedQuery(name = "VariableDeSistema.findByNombre", query = "SELECT v FROM VariableDeSistema v WHERE v.nombre = :nombre"),
    @NamedQuery(name = "VariableDeSistema.findByDescripcion", query = "SELECT v FROM VariableDeSistema v WHERE v.descripcion = :descripcion"),
    @NamedQuery(name = "VariableDeSistema.findByValorNumerico", query = "SELECT v FROM VariableDeSistema v WHERE v.valorNumerico = :valorNumerico"),
    @NamedQuery(name = "VariableDeSistema.findByValorString", query = "SELECT v FROM VariableDeSistema v WHERE v.valorString = :valorString")})
public class VariableDeSistema implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VARIABLE_ID")
    private Integer variableId;
    
    @Column(name = "NOMBRE")
    private String nombre;
    
    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "VALOR_NUMERICO")
    private Double valorNumerico;
    
    @Column(name = "VALOR_STRING")
    private String valorString;

    public VariableDeSistema() {
    }

    public VariableDeSistema(Integer variableId) {
        this.variableId = variableId;
    }

    public VariableDeSistema(Integer variableId, String nombre) {
        this.variableId = variableId;
        this.nombre = nombre;
    }

    public Integer getVariableId() {
        return variableId;
    }

    public void setVariableId(Integer variableId) {
        this.variableId = variableId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getValorNumerico() {
        return valorNumerico;
    }

    public void setValorNumerico(Double valorNumerico) {
        this.valorNumerico = valorNumerico;
    }

    public String getValorString() {
        return valorString;
    }

    public void setValorString(String valorString) {
        this.valorString = valorString;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (variableId != null ? variableId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VariableDeSistema)) {
            return false;
        }
        VariableDeSistema other = (VariableDeSistema) object;
        if ((this.variableId == null && other.variableId != null) || (this.variableId != null && !this.variableId.equals(other.variableId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.VariableDeSistema[ variableId=" + variableId + " ]";
    }
    
}
