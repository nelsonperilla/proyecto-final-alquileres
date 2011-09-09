/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "SERVICIO")
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="TIPO",
discriminatorType= DiscriminatorType.STRING, length=1)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Servicio.findAll", query = "SELECT s FROM Servicio s"),
    @NamedQuery(name = "Servicio.findByServicioId", query = "SELECT s FROM Servicio s WHERE s.servicioId = :servicioId")})
public abstract class Servicio implements Serializable {
    
    protected static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SERVICIO_ID")
    protected Integer servicioId;
    
    @Column(name = "FECHA_DESDE")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date fechaDesde;
    
    @Column(name = "FECHA_HASTA")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date fechaHasta;
    
    @JoinColumn(name = "USUARIO_FK", referencedColumnName = "USUARIO_ID")
    @ManyToOne(optional = false)
    private Usuario usuarioFk;
    
    @JoinColumn(name = "TIPO_SERVICIO_FK", referencedColumnName = "TIPO_SERVICIO_ID")
    @ManyToOne(optional = false)
    protected TipoServicio tipoServicioFk;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "servicioFk")
    protected List<Pago> pagoList;

    public Servicio() {
        pagoList = new ArrayList<Pago>();
    }

    public Servicio(Integer servicioId) {
        this();
        this.servicioId = servicioId;
    }
    
    public void agregarPago(Pago pago) {
        pagoList.add(pago);
        pago.setServicioFk(this);
    }


    public Integer getServicioId() {
        return servicioId;
    }

    public void setServicioId(Integer servicioId) {
        this.servicioId = servicioId;
    }

    @XmlTransient
    public List<Pago> getPagoList() {
        return pagoList;
    }

    public void setPagoList(List<Pago> pagoList) {
        this.pagoList = pagoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (servicioId != null ? servicioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Servicio)) {
            return false;
        }
        Servicio other = (Servicio) object;
        if ((this.servicioId == null && other.servicioId != null) || (this.servicioId != null && !this.servicioId.equals(other.servicioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.Servicio[ servicioId=" + servicioId + " ]";
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

    public TipoServicio getTipoServicioFk() {
        return tipoServicioFk;
    }

    public void setTipoServicioFk(TipoServicio tipoServicioFk) {
        this.tipoServicioFk = tipoServicioFk;
    }

    public Usuario getUsuarioFk() {
        return usuarioFk;
    }

    public void setUsuarioFk(Usuario usuarioFk) {
        this.usuarioFk = usuarioFk;
    }
    
}
