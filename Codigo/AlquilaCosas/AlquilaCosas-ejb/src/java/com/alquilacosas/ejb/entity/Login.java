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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "LOGIN")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Login.findAll", query = "SELECT l FROM Login l"),
    @NamedQuery(name = "Login.findByLoginId", query = "SELECT l FROM Login l WHERE l.loginId = :loginId"),
    @NamedQuery(name = "Login.findByUsername", query = "SELECT l FROM Login l WHERE l.username = :username"),
    @NamedQuery(name = "Login.findByPassword", query = "SELECT l FROM Login l WHERE l.password = :password"),
    @NamedQuery(name = "Login.findByCodigoActivacion", query = "SELECT l FROM Login l WHERE l.codigoActivacion = :codigoActivacion"),
    @NamedQuery(name = "Login.findByUsuarioFk", query = "SELECT l FROM Login l WHERE l.usuarioFk = :usuarioFk")})
public class Login implements Serializable {
    
    //@Basic(optional = false)
    //@NotNull
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    
    @Column(name = "FECHA_ULTIMO_INGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUltimoIngreso;
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOGIN_ID")
    private Integer loginId;
    
    @Size(min = 1, max = 45)
    @Column(name = "USERNAME")
    private String username;
    
    @Size(min = 1, max = 45)
    @Column(name = "PASSWORD")
    private String password;
    
    @Size(min = 1, max = 45)
    @Column(name = "CODIGO_ACTIVACION")
    private String codigoActivacion;
    
    @JoinTable(name = "LOGIN_X_ROL", joinColumns = {
        @JoinColumn(name = "LOGIN_FK", referencedColumnName = "LOGIN_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "ROL_FK", referencedColumnName = "ROL_ID")})
    @ManyToMany(cascade= CascadeType.PERSIST)
    private List<Rol> rolList;
    
    @JoinColumn(name = "USUARIO_FK", referencedColumnName = "USUARIO_ID")
    @ManyToOne(optional = false)
    private Usuario usuarioFk;

    public Login() {
        rolList = new ArrayList<Rol>();
    }

    public Login(Integer loginId) {
        this();
        this.loginId = loginId;
    }

    public Login(Integer loginId, String username, String password, String codigoActivacion) {
        this();
        this.loginId = loginId;
        this.username = username;
        this.password = password;
        this.codigoActivacion = codigoActivacion;
    }
    
    public void agregarRol(Rol rol) {
        rolList.add(rol);
    }
    
    public void removerRol(Rol rol) {
        rolList.remove(rol);
    }

    public Integer getLoginId() {
        return loginId;
    }

    public void setLoginId(Integer loginId) {
        this.loginId = loginId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodigoActivacion() {
        return codigoActivacion;
    }

    public void setCodigoActivacion(String codigoActivacion) {
        this.codigoActivacion = codigoActivacion;
    }

    @XmlTransient
    public List<Rol> getRolList() {
        return rolList;
    }

    public void setRolList(List<Rol> rolList) {
        this.rolList = rolList;
    }

    public Usuario getUsuarioFk() {
        return usuarioFk;
    }

    public void setUsuarioFk(Usuario usuarioFk) {
        this.usuarioFk = usuarioFk;
    }
    
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaUltimoIngreso() {
        return fechaUltimoIngreso;
    }

    public void setFechaUltimoIngreso(Date fechaUltimoIngreso) {
        this.fechaUltimoIngreso = fechaUltimoIngreso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (loginId != null ? loginId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Login)) {
            return false;
        }
        Login other = (Login) object;
        if ((this.loginId == null && other.loginId != null) || (this.loginId != null && !this.loginId.equals(other.loginId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.Login[ loginId=" + loginId + " ]";
    }
    
}
