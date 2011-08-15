/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.common;

import com.alquilacosas.ejb.entity.Rol;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author damiancardozo
 */
public class UsuarioFacade {
    
    private Integer id;
    private String nombre, apellido, email, telefono, dni;
    private Date fechaNacimiento;
    private DomicilioFacade domicilio;
    private String username;
    private Date fechaDeRegistro;
    private Long numRoles;
    private String tipoUsuario;
    private boolean usuarioRol;
    private boolean adminRol;

    public UsuarioFacade(Integer id, String nombre, String apellido, String email, 
            String telefono, String dni, Date fechaNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public UsuarioFacade( Integer id, String username, String email, String nombre, String apellido,
            Date fechaDeRegistro, Long numRoles, String tipoUsuario){
        this.id = id;
        this.username = username;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaDeRegistro = fechaDeRegistro;
        this.numRoles = numRoles;
        this.tipoUsuario = tipoUsuario;
        
        if( numRoles == 1 && tipoUsuario.equalsIgnoreCase("usuario")){
            usuarioRol = true;
        } else if( numRoles == 1 && tipoUsuario.equalsIgnoreCase("admin")){
            adminRol = true;
        }
        else if( numRoles == 2 ){
            usuarioRol = true;
            adminRol = true;
        }
    }

    public UsuarioFacade(){}
    
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public DomicilioFacade getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(DomicilioFacade domicilio) {
        this.domicilio = domicilio;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaDeRegistro() {
        return fechaDeRegistro;
    }

    public void setFechaDeRegistro(Date fechaDeRegistro) {
        this.fechaDeRegistro = fechaDeRegistro;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getNumRoles() {
        return numRoles;
    }

    public void setNumRoles(Long numRoles) {
        this.numRoles = numRoles;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public boolean isAdminRol() {
        return adminRol;
    }

    public void setAdminRol(boolean adminRol) {
        this.adminRol = adminRol;
    }

    public boolean isUsuarioRol() {
        return usuarioRol;
    }

    public void setUsuarioRol(boolean usuarioRol) {
        this.usuarioRol = usuarioRol;
    }
    
    
    
}
