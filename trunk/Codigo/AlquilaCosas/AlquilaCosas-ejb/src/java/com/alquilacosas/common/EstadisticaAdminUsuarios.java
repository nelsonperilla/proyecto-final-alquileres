/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.common;

import com.alquilacosas.ejb.entity.EstadoUsuario.NombreEstadoUsuario;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author wilson
 */
public class EstadisticaAdminUsuarios {
     private Date mes;
     private NombreEstadoUsuario estado;
     private Integer cantidad;
     private Integer acumulado;

     public EstadisticaAdminUsuarios() {
     }

     public EstadisticaAdminUsuarios(Date anioMes, NombreEstadoUsuario estado, Integer cantidad, Integer acumulado) {
          this.mes = anioMes;
          this.estado = estado;
          this.cantidad = cantidad;
          this.acumulado = acumulado;
     }

     public Integer getAcumulado() {
          return acumulado;
     }

     public void setAcumulado(Integer acumulado) {
          this.acumulado = acumulado;
     }

     public Date getMes() {
          return mes;
     }

     public String getMesFormat() {
          SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM");
          return formato.format(mes);
     }
     
     public void setMes(Date Mes) {
          this.mes = Mes;
     }

     public Integer getCantidad() {
          return cantidad;
     }

     public void setCantidad(Integer cantidad) {
          this.cantidad = cantidad;
     }

     public NombreEstadoUsuario getEstado() {
          return estado;
     }

     public void setEstado(NombreEstadoUsuario estado) {
          this.estado = estado;
     }  
     
     public void incrementarCantidad()
     {
          cantidad++;
     }
     
     public void incrementarAcumulado()
     {
          acumulado++;
     }
}
