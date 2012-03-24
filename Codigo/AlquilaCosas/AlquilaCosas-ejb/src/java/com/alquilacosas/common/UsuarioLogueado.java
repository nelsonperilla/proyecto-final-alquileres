/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.common;

import com.alquilacosas.ejb.entity.ImagenUsuario;
import com.alquilacosas.ejb.entity.Rol.NombreRol;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author damiancardozo
 */
public class UsuarioLogueado implements Serializable {
    
    private Integer id;
    private String nombre, apellido, ciudad, facebookId;
    private ImagenUsuario imagen;
    private List<NombreRol> roles;
    private boolean direccionRegistrada;

    public UsuarioLogueado(Integer id, String nombre, String apellido, String ciudad, 
            ImagenUsuario imagen, List<NombreRol> roles, String facebookId) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.ciudad = ciudad;
        this.imagen = imagen;
        this.roles = roles;
        this.facebookId = facebookId;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ImagenUsuario getImagen() {
        return imagen;
    }

    public void setImagen(ImagenUsuario imagen) {
        this.imagen = imagen;
    }

    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<NombreRol> getRoles() {
        return roles;
    }

    public void setRoles(List<NombreRol> roles) {
        this.roles = roles;
    }

    public boolean isDireccionRegistrada() {
        return direccionRegistrada;
    }

    public void setDireccionRegistrada(boolean direccionRegistrada) {
        this.direccionRegistrada = direccionRegistrada;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }
    
}
