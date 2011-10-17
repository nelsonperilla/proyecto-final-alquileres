/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "PUBLICIDAD")
@DiscriminatorValue(value="P")
//@PrimaryKeyJoinColumn(name="SERVICIO_ID")
@XmlRootElement
public class Publicidad extends Servicio implements Serializable {

    @Column(name = "TITULO")
    protected String titulo;
    
    @Column(name = "URL")
    protected String url;
    
    @Column(name = "CAPTION")
    protected String caption;
    
    @Lob
    @Column(name = "IMAGEN")
    private byte[] imagen;
    
    @JoinColumn(name = "USUARIO_FK", referencedColumnName = "USUARIO_ID")
    @ManyToOne(optional = false)
    private Usuario usuarioFk;
    
    @JoinColumn(name = "TIPO_PUBLICIDAD_FK", referencedColumnName = "TIPO_PUBLICIDAD_ID")
    @ManyToOne(optional = false)
    private TipoPublicidad tipoPublicidadFk;

    public Publicidad() {
    }
    
    public Publicidad(String titulo, String url, String caption, byte[] imagen) {
        super();
        this.titulo = titulo;
        this.url = url;
        this.caption = caption;
        this.imagen = imagen;
    }
    

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public TipoPublicidad getTipoPublicidadFk() {
        return tipoPublicidadFk;
    }

    public void setTipoPublicidadFk(TipoPublicidad tipoPublicidadFk) {
        this.tipoPublicidadFk = tipoPublicidadFk;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        hash += (servicioId != null ? servicioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Publicidad)) {
            return false;
        }
        Publicidad other = (Publicidad) object;
        if ((this.servicioId == null && other.servicioId != null) || (this.servicioId != null && !this.servicioId.equals(other.servicioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.ServicioPublicidad[ servicioId=" + servicioId + " ]";
    }
    
}
