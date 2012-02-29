    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "PRECIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Precio.findAll", query = "SELECT p FROM Precio p"),
    @NamedQuery(name = "Precio.findByPublicacion", query = "SELECT p FROM Precio p WHERE "
        + "p.publicacionFk = :publicacion AND p.fechaHasta IS NULL ORDER BY p.precio ASC"),
    @NamedQuery(name = "Precio.findByPrecioId", query = "SELECT p FROM Precio p WHERE p.precioId = :precioId")})
public class Precio implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRECIO_ID")
    private Integer precioId;
    
    @Column(name = "PRECIO")
    private double precio;
    
    @Column(name = "FECHA_DESDE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDesde;
    
    @Column(name = "FECHA_HASTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHasta;
    
    @JoinColumn(name = "PUBLICACION_FK", referencedColumnName = "PUBLICACION_ID")
    @ManyToOne
    private Publicacion publicacionFk;
    
    @JoinColumn(name = "PERIODO_FK", referencedColumnName = "PERIODO_ID")
    @ManyToOne
    private Periodo periodoFk;

    public Precio() {
    }

    public Precio(Double precio, Periodo periodoFk) {
        this.precio = precio;
        this.periodoFk = periodoFk;
        fechaDesde = new Date();
    }

    public Integer getPrecioId() {
        return precioId;
    }

    public void setPrecioId(Integer precioId) {
        this.precioId = precioId;
    }

    public Publicacion getPublicacionFk() {
        return publicacionFk;
    }

    public void setPublicacionFk(Publicacion publicacionFk) {
        this.publicacionFk = publicacionFk;
    }

    public Periodo getPeriodoFk() {
        return periodoFk;
    }

    public void setPeriodoFk(Periodo periodoFk) {
        this.periodoFk = periodoFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (precioId != null ? precioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Precio)) {
            return false;
        }
        Precio other = (Precio) object;
        if ((this.precioId == null && other.precioId != null) || (this.precioId != null && !this.precioId.equals(other.precioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.Precio[ precioId=" + precioId + " ]";
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
}
