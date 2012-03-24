/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.dto;

import com.alquilacosas.ejb.entity.EstadoUsuario.NombreEstadoUsuario;
import com.alquilacosas.ejb.entity.Rol;
import com.alquilacosas.ejb.entity.Usuario;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author damiancardozo
 */
public class UsuarioDTO implements Serializable {

     private Integer id;
     private String nombre, apellido, email, telefono, dni, facebookId;
     private Date fechaNacimiento;
     private DomicilioDTO domicilio;
     private String username;
     private Date fechaDeRegistro;
     private List<Rol> roles;
     private String tipoUsuario;
     private boolean usuarioRol;
     private boolean adminRol;
     private boolean publicitanteRol;
     private double userRating;
     private Integer userOfrecePositivas;
     private double userOfrecePositivasPorcentaje;
     private Integer userOfreceNeutrales;
     private double userOfreceNeutralesPorcentaje;
     private Integer userOfreceNegativas;
     private double userOfreceNegativasPorcentaje;
     private Integer userTomaPositivas;
     private double userTomaPositivasPorcentaje;
     private Integer userTomaNeutrales;
     private double userTomaNeutralesPorcentaje;
     private Integer userTomaNegativas;
     private double userTomaNegativasPorcentaje;
     private NombreEstadoUsuario estadoUsuario;
     private Integer cantidadAdvertencia;
     private Integer cantidadSuspencion;

     public UsuarioDTO(Integer id, String nombre, String apellido, String email,
             String telefono, String dni, Date fechaNacimiento) {
          this.id = id;
          this.nombre = nombre;
          this.apellido = apellido;
          this.email = email;
          this.telefono = telefono;
          this.dni = dni;
          this.fechaNacimiento = fechaNacimiento;
     }

     public UsuarioDTO(Usuario usuario) {
          this.id = usuario.getUsuarioId();
          this.nombre = usuario.getNombre();
          this.apellido = usuario.getApellido();
          this.email = usuario.getEmail();
          this.telefono = usuario.getTelefono();
          this.dni = usuario.getDni();
          this.fechaNacimiento = usuario.getFechaNac();
          this.facebookId = usuario.getFacebookId();
     }

     public UsuarioDTO(Integer id, String username, String email, String nombre, String apellido,
             Date fechaDeRegistro, List<Rol> roles, NombreEstadoUsuario estadoUsuario) {
          this.id = id;
          this.username = username;
          this.email = email;
          this.nombre = nombre;
          this.apellido = apellido;
          this.fechaDeRegistro = fechaDeRegistro;
          this.roles = roles;
          this.estadoUsuario = estadoUsuario;

          for (Rol r : roles) {
               if (r.getRolId() == 1) {
                    usuarioRol = true;
               }
               if (r.getRolId() == 2) {
                    adminRol = true;
               }
               if (r.getRolId() == 3) {
                    publicitanteRol = true;
               }
          }
     }

     public UsuarioDTO() {
     }

     public String getAntiguedad() {
          Date actual = new Date();
          long antiguedad;
          String unidad;
          antiguedad = (actual.getTime() - fechaDeRegistro.getTime()) / 86400000L;
          if (antiguedad > 730) {
               unidad = " Años";
               antiguedad = Math.round(antiguedad / 365);
          } else {
               if (antiguedad > 365) {
                    unidad = " Año";
                    antiguedad = Math.round(antiguedad / 365);
               } else {
                    if (antiguedad > 30) {
                         unidad = " Meses";
                         antiguedad = Math.round(antiguedad / 30);
                    } else {
                         unidad = " Dias";
                         antiguedad = Math.round(antiguedad);
                    }
               }
          }
          return "+" + String.valueOf(antiguedad) + unidad;
     }

     public String getDni() {
          return dni;
     }

     public void setDni(String dni) {
          this.dni = dni;
     }

     public DomicilioDTO getDomicilio() {
          return domicilio;
     }

     public void setDomicilio(DomicilioDTO domicilio) {
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

     public List<Rol> getRoles() {
          return roles;
     }

     public void setRoles(List<Rol> roles) {
          this.roles = roles;
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

     public boolean isPublicitanteRol() {
          return publicitanteRol;
     }

     public void setPublicitanteRol(boolean publicitanteRol) {
          this.publicitanteRol = publicitanteRol;
     }

     public Integer getUserOfreceNegativas() {
          return userOfreceNegativas;
     }

     public void setUserOfreceNegativas(Integer userOfreceNegativas) {
          this.userOfreceNegativas = userOfreceNegativas;
     }

     public double getUserOfreceNegativasPorcentaje() {
          return userOfreceNegativasPorcentaje;
     }

     public void setUserOfreceNegativasPorcentaje(double userOfreceNegativasPorcentaje) {
          this.userOfreceNegativasPorcentaje = userOfreceNegativasPorcentaje;
     }

     public Integer getUserOfreceNeutrales() {
          return userOfreceNeutrales;
     }

     public void setUserOfreceNeutrales(Integer userOfreceNeutrales) {
          this.userOfreceNeutrales = userOfreceNeutrales;
     }

     public double getUserOfreceNeutralesPorcentaje() {
          return userOfreceNeutralesPorcentaje;
     }

     public void setUserOfreceNeutralesPorcentaje(double userOfreceNeutralesPorcentaje) {
          this.userOfreceNeutralesPorcentaje = userOfreceNeutralesPorcentaje;
     }

     public Integer getUserOfrecePositivas() {
          return userOfrecePositivas;
     }

     public void setUserOfrecePositivas(Integer userOfrecePositivas) {
          this.userOfrecePositivas = userOfrecePositivas;
     }

     public double getUserOfrecePositivasPorcentaje() {
          return userOfrecePositivasPorcentaje;
     }

     public void setUserOfrecePositivasPorcentaje(double userOfrecePositivasPorcentaje) {
          this.userOfrecePositivasPorcentaje = userOfrecePositivasPorcentaje;
     }

     public double getUserRating() {
          return userRating;
     }

     public void setUserRating(double userRating) {
          this.userRating = userRating;
     }

     public Integer getUserTomaNegativas() {
          return userTomaNegativas;
     }

     public void setUserTomaNegativas(Integer userTomaNegativas) {
          this.userTomaNegativas = userTomaNegativas;
     }

     public double getUserTomaNegativasPorcentaje() {
          return userTomaNegativasPorcentaje;
     }

     public void setUserTomaNegativasPorcentaje(double userTomaNegativasPorcentaje) {
          this.userTomaNegativasPorcentaje = userTomaNegativasPorcentaje;
     }

     public Integer getUserTomaNeutrales() {
          return userTomaNeutrales;
     }

     public void setUserTomaNeutrales(Integer userTomaNeutrales) {
          this.userTomaNeutrales = userTomaNeutrales;
     }

     public double getUserTomaNeutralesPorcentaje() {
          return userTomaNeutralesPorcentaje;
     }

     public void setUserTomaNeutralesPorcentaje(double userTomaNeutralesPorcentaje) {
          this.userTomaNeutralesPorcentaje = userTomaNeutralesPorcentaje;
     }

     public Integer getUserTomaPositivas() {
          return userTomaPositivas;
     }

     public void setUserTomaPositivas(Integer userTomaPositivas) {
          this.userTomaPositivas = userTomaPositivas;
     }

     public double getUserTomaPositivasPorcentaje() {
          return userTomaPositivasPorcentaje;
     }

     public void setUserTomaPositivasPorcentaje(double userTomaPositivasPorcentaje) {
          this.userTomaPositivasPorcentaje = userTomaPositivasPorcentaje;
     }

     public String getEstadoUsuario() {
          return estadoUsuario.name();
     }

     public void setEstadoUsuario(NombreEstadoUsuario estadoUsuario) {
          this.estadoUsuario = estadoUsuario;
     }

     public Integer getCantidadAdvertencia() {
          return cantidadAdvertencia;
     }

     public void setCantidadAdvertencia(Integer cantidadAdvertencia) {
          this.cantidadAdvertencia = cantidadAdvertencia;
     }

     public Integer getCantidadSuspencion() {
          return cantidadSuspencion;
     }

     public void setCantidadSuspencion(Integer cantidadSuspencion) {
          this.cantidadSuspencion = cantidadSuspencion;
     }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }
}
