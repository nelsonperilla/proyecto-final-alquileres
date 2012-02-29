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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "USUARIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findByUsuarioId", query = "SELECT u FROM Usuario u WHERE u.usuarioId = :usuarioId"),
    @NamedQuery(name = "Usuario.findByApellido", query = "SELECT u FROM Usuario u WHERE u.apellido = :apellido"),
    @NamedQuery(name = "Usuario.findByEmail", query = "SELECT u FROM Usuario u WHERE u.email = :email"),
    @NamedQuery(name = "Usuario.findByDni", query = "SELECT u FROM Usuario u WHERE u.dni = :dni")})
public class Usuario implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USUARIO_ID")
    private Integer usuarioId;
    
    @Column(name = "NOMBRE")
    private String nombre;
    
    @Column(name = "APELLIDO")
    private String apellido;
    
    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Column(name = "EMAIL")
    private String email;
    
    @Column(name = "TELEFONO")
    private String telefono;
    
    @Column(name = "DNI")
    private String dni;
    
    @Column(name = "FECHA_NAC")
    @Temporal(TemporalType.DATE)
    private Date fechaNac;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioFk")
    private List<Publicidad> publicidadList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioFk")
    private List<Advertencia> advertenciaList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioFk")
    private List<Domicilio> domicilioList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioFk")
    private List<Publicacion> publicacionList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioFk")
    private List<Comentario> comentarioList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private List<UsuarioXEstado> usuarioXEstadoList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioFk")
    private List<Login> loginList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioFk")
    private List<Suspension> suspensionList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioFk")
    private List<Reputacion> reputacionList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioFk")
    private List<Alquiler> alquilerList;
    
    @OneToMany(mappedBy = "usuarioCalificadoFk")
    private List<Calificacion> calificacionList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioCalificadorFk")
    private List<Calificacion> calificacionList1;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioFk")
    private List<Pago> pagoList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioFk")
    private List<Favorito> favoritoList;

    public Usuario() {
        domicilioList = new ArrayList<Domicilio>();
        publicacionList = new ArrayList<Publicacion>();
        comentarioList = new ArrayList<Comentario>();
        loginList = new ArrayList<Login>();
        suspensionList = new ArrayList<Suspension>();
        advertenciaList = new ArrayList<Advertencia>();
        usuarioXEstadoList = new ArrayList<UsuarioXEstado>();
        favoritoList = new ArrayList<Favorito>();
    }

    public Usuario(Integer usuarioId) {
        this();
        this.usuarioId = usuarioId;
    }

    public Usuario(Integer usuarioId, String nombre, String apellido, String email, String dni, Date fechaNac) {
        this();
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.dni = dni;
        this.fechaNac = fechaNac;
    }
    
    public void agregarAdvertencia(Advertencia adv) {
        advertenciaList.add(adv);
        adv.setUsuarioFk(this);
    }
    
    public void agregarSuspension(Suspension sus) {
        suspensionList.add(sus);
        sus.setUsuarioFk(this);
    }
    
    public void agregarDomicilio(Domicilio dom) {
        domicilioList.add(dom);
        dom.setUsuarioFk(this);
    }
    
    public Domicilio removerDomicilio(Domicilio dom) {
        domicilioList.remove(dom);
        return dom;
    }
    
    public void agregarLogin(Login login) {
        loginList.add(login);
        login.setUsuarioFk(this);
    }
    
    public Login removerLogin(Login login) {
        loginList.remove(login);
        return login;
    }
    
    public void agregarUsuarioXEstado(UsuarioXEstado uxe) {
        usuarioXEstadoList.add(uxe);
        uxe.setUsuario(this);
    }
    
    public void agregarPublicacion(Publicacion publicacion) {
        publicacionList.add(publicacion);
        publicacion.setUsuarioFk(this);
    }
    
    public Publicacion removerPublicacion(Publicacion publicacion) {
        publicacionList.remove(publicacion);
        publicacion.setUsuarioFk(this);
        return publicacion;
    }
    
    public Publicacion getPublicacion(int publicacionId) {
        for(Publicacion p: publicacionList) {
            if(p.getPublicacionId() == publicacionId) 
                return p;
        }
        return null;
    }
    
    public void argegarFavorito( Favorito favorito ){
        favoritoList.add(favorito);
        favorito.setUsuarioFk(this);
    }
    
    public void removerFavorito( Favorito favorito ){
        for( int i = 0; i < favoritoList.size(); i++ ){
            if( favoritoList.get(i).getId() == favorito.getId() )
                favoritoList.remove(i);
        }
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    @XmlTransient
    public List<Advertencia> getAdvertenciaList() {
        return advertenciaList;
    }

    public void setAdvertenciaList(List<Advertencia> advertenciaList) {
        this.advertenciaList = advertenciaList;
    }

    @XmlTransient
    public List<Domicilio> getDomicilioList() {
        return domicilioList;
    }

    public void setDomicilioList(List<Domicilio> domicilioList) {
        this.domicilioList = domicilioList;
    }

    @XmlTransient
    public List<Publicacion> getPublicacionList() {
        return publicacionList;
    }

    public void setPublicacionList(List<Publicacion> publicacionList) {
        this.publicacionList = publicacionList;
    }

    @XmlTransient
    public List<Comentario> getComentarioList() {
        return comentarioList;
    }

    public void setComentarioList(List<Comentario> comentarioList) {
        this.comentarioList = comentarioList;
    }

    @XmlTransient
    public List<UsuarioXEstado> getUsuarioXEstadoList() {
        return usuarioXEstadoList;
    }

    public void setUsuarioXEstadoList(List<UsuarioXEstado> usuarioXEstadoList) {
        this.usuarioXEstadoList = usuarioXEstadoList;
    }

    @XmlTransient
    public List<Login> getLoginList() {
        return loginList;
    }

    public void setLoginList(List<Login> loginList) {
        this.loginList = loginList;
    }

    @XmlTransient
    public List<Suspension> getSuspensionList() {
        return suspensionList;
    }

    public void setSuspensionList(List<Suspension> suspensionList) {
        this.suspensionList = suspensionList;
    }
    
    @XmlTransient
    public List<Reputacion> getReputacionList() {
        return reputacionList;
    }

    public void setReputacionList(List<Reputacion> reputacionList) {
        this.reputacionList = reputacionList;
    }

    @XmlTransient
    public List<Alquiler> getAlquilerList() {
        return alquilerList;
    }

    public void setAlquilerList(List<Alquiler> alquilerList) {
        this.alquilerList = alquilerList;
    }

    @XmlTransient
    public List<Calificacion> getCalificacionList() {
        return calificacionList;
    }

    public void setCalificacionList(List<Calificacion> calificacionList) {
        this.calificacionList = calificacionList;
    }

    @XmlTransient
    public List<Calificacion> getCalificacionList1() {
        return calificacionList1;
    }

    public void setCalificacionList1(List<Calificacion> calificacionList1) {
        this.calificacionList1 = calificacionList1;
    }
    
    @XmlTransient
    public List<Publicidad> getPublicidadList() {
        return publicidadList;
    }

    public void setPublicidadList(List<Publicidad> publicidadList) {
        this.publicidadList = publicidadList;
    }

    @XmlTransient
    public List<Publicidad> getServicioList() {
        return publicidadList;
    }

    public void setServicioList(List<Publicidad> publicidadList) {
        this.publicidadList = publicidadList;
    }

    @XmlTransient
    public List<Pago> getPagoList() {
        return pagoList;
    }

    public void setPagoList(List<Pago> pagoList) {
        this.pagoList = pagoList;
    }
    
    public UsuarioXEstado getEstadoVigente() {
         UsuarioXEstado estado = null;
         for (UsuarioXEstado e : this.getUsuarioXEstadoList()) {
              if (e.getFechaHasta() == null)
                   estado = e;
         }
         return estado;
    }

    public List<Favorito> getFavoritoList() {
        return favoritoList;
    }

    public void setFavoritoList(List<Favorito> favoritoList) {
        this.favoritoList = favoritoList;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuarioId != null ? usuarioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.usuarioId == null && other.usuarioId != null) || 
                (this.usuarioId != null && !this.usuarioId.equals(other.usuarioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.Usuario[ usuarioId=" + usuarioId + " ]";
    }
    
}
