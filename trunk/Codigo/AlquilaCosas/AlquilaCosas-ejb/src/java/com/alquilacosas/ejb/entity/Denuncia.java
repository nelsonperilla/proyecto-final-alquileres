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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "DENUNCIA")
@XmlRootElement
public class Denuncia implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DENUNCIA_ID")
    private Integer denunciaId;
    
    @Column(name = "EXPLICACION")
    private String explicacion;
    
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    
    @JoinColumn(name = "MOTIVO_FK", referencedColumnName = "MOTIVO_DENUNCIA_ID")
    @ManyToOne(optional = false)
    private MotivoDenuncia motivoFk;
    
    @JoinColumn(name = "USUARIO_FK", referencedColumnName = "USUARIO_ID")
    @ManyToOne(optional = false)
    private Usuario usuarioFk;
    
    @JoinColumn(name = "COMENTARIO_FK", referencedColumnName = "COMENTARIO_ID")
    @ManyToOne(optional = false)
    private Comentario comentarioFk;
    
    @JoinColumn(name = "PUBLICACION_FK", referencedColumnName = "PUBLICACION_ID")
    @ManyToOne(optional = false)
    private Publicacion pulicacionFk;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "denunciaFk")
    private List<DenunciaXEstado> denunciaXEstadoList;
    
    public Denuncia() {
        denunciaXEstadoList = new ArrayList<DenunciaXEstado>();
    }
    
    public void agregarDenunciaXEstado(DenunciaXEstado dxe) {
        denunciaXEstadoList.add(dxe);
        dxe.setDenunciaFk(this);
    }

    public Comentario getComentarioFk() {
        return comentarioFk;
    }

    public void setComentarioFk(Comentario comentarioFk) {
        this.comentarioFk = comentarioFk;
    }

    public Integer getDenunciaId() {
        return denunciaId;
    }

    public void setDenunciaId(Integer denunciaId) {
        this.denunciaId = denunciaId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Publicacion getPulicacionFk() {
        return pulicacionFk;
    }

    public void setPulicacionFk(Publicacion pulicacionFk) {
        this.pulicacionFk = pulicacionFk;
    }

    public Usuario getUsuarioFk() {
        return usuarioFk;
    }

    public void setUsuarioFk(Usuario usuarioFk) {
        this.usuarioFk = usuarioFk;
    }

    public MotivoDenuncia getMotivoFk() {
        return motivoFk;
    }

    public void setMotivoFk(MotivoDenuncia motivoFk) {
        this.motivoFk = motivoFk;
    }

    public String getExplicacion() {
        return explicacion;
    }

    public void setExplicacion(String explicacion) {
        this.explicacion = explicacion;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (denunciaId != null ? denunciaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Denuncia)) {
            return false;
        }
        Denuncia other = (Denuncia) object;
        if ((this.denunciaId == null && other.denunciaId != null) || (this.denunciaId != null && !this.denunciaId.equals(other.denunciaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.Denuncia[ denunciaId=" + denunciaId + " ]";
    }
}
