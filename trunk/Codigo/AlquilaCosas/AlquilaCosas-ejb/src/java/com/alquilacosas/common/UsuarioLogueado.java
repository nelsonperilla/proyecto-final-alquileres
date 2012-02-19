/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.common;

import com.alquilacosas.ejb.entity.Rol.NombreRol;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author damiancardozo
 */
public class UsuarioLogueado implements Serializable {
    
    private Integer id;
    private String nombre, apellido, ciudad;
    private byte[] imagen;
    private List<NombreRol> roles;

    public UsuarioLogueado(Integer id, String nombre, String apellido, String ciudad, byte[] imagen, List<NombreRol> roles) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.ciudad = ciudad;
        this.imagen = imagen;
        this.roles = roles;
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

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
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
    
}
