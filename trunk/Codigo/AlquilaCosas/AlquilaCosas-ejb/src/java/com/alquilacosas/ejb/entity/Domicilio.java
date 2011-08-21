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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "DOMICILIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Domicilio.findAll", query = "SELECT d FROM Domicilio d"),
    @NamedQuery(name = "Domicilio.findByDomicilioId", query = "SELECT d FROM Domicilio d WHERE d.domicilioId = :domicilioId"),
    @NamedQuery(name = "Domicilio.findByCalle", query = "SELECT d FROM Domicilio d WHERE d.calle = :calle"),
    @NamedQuery(name = "Domicilio.findByNumero", query = "SELECT d FROM Domicilio d WHERE d.numero = :numero"),
    @NamedQuery(name = "Domicilio.findByPiso", query = "SELECT d FROM Domicilio d WHERE d.piso = :piso"),
    @NamedQuery(name = "Domicilio.findByDepto", query = "SELECT d FROM Domicilio d WHERE d.depto = :depto"),
    @NamedQuery(name = "Domicilio.findByBarrio", query = "SELECT d FROM Domicilio d WHERE d.barrio = :barrio")})
public class Domicilio implements Serializable {
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "CIUDAD")
    private String ciudad;
    
    @JoinColumn(name = "PROVINCIA_FK", referencedColumnName = "PROVINCIA_ID")
    @ManyToOne(optional = false)
    private Provincia provinciaFk;
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "DOMICILIO_ID")
    private Integer domicilioId;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "CALLE")
    private String calle;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMERO")
    private int numero;
    
    @Basic(optional = false)
    @Column(name = "PISO")
    private int piso;
    
    @Basic(optional = false)
    @Size(max = 45)
    @Column(name = "DEPTO")
    private String depto;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "BARRIO")
    private String barrio;
    
    @JoinColumn(name = "USUARIO_FK", referencedColumnName = "USUARIO_ID")
    @ManyToOne(optional = false)
    private Usuario usuarioFk;

    public Domicilio() {
    }

    public Domicilio(Integer domicilioId) {
        this.domicilioId = domicilioId;
    }

    public Domicilio(Integer domicilioId, String calle, Integer numero, Integer piso, 
            String depto, String barrio) {
        this.domicilioId = domicilioId;
        this.calle = calle;
        this.numero = numero;
        this.piso = piso;
        this.depto = depto;
        this.barrio = barrio;
    }

    public Integer getDomicilioId() {
        return domicilioId;
    }

    public void setDomicilioId(Integer domicilioId) {
        this.domicilioId = domicilioId;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }

    public String getDepto() {
        return depto;
    }

    public void setDepto(String depto) {
        this.depto = depto;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
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
        hash += (domicilioId != null ? domicilioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Domicilio)) {
            return false;
        }
        Domicilio other = (Domicilio) object;
        if ((this.domicilioId == null && other.domicilioId != null) || (this.domicilioId != null && !this.domicilioId.equals(other.domicilioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.Domicilio[ domicilioId=" + domicilioId + " ]";
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Provincia getProvinciaFk() {
        return provinciaFk;
    }

    public void setProvinciaFk(Provincia provinciaFk) {
        this.provinciaFk = provinciaFk;
    }
    
}
