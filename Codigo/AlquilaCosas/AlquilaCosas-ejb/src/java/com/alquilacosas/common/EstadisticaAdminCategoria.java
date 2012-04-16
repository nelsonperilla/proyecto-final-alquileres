/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.common;

/**
 *
 * @author wilson
 */
public class EstadisticaAdminCategoria {
     private String nombre;
     private Integer cantidad;

     public EstadisticaAdminCategoria(String nombre, Integer cantidad) {
          this.nombre = nombre;
          this.cantidad = cantidad;
     }

     public Integer getCantidad() {
          return cantidad;
     }

     public void setCantidad(Integer cantidad) {
          this.cantidad = cantidad;
     }

     public String getNombre() {
          return nombre;
     }

     public void setNombre(String nombre) {
          this.nombre = nombre;
     }
     
     public void aumentarCantidad() {
          this.cantidad++;
     }
}
