/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author wilson
 */
public class EstadisticaAdminPublicacion {
     private Date mes;
     private Integer cantidad;
     private Integer acumulado;

     public EstadisticaAdminPublicacion(Date mes, Integer cantidad, Integer acumulado) {
          this.mes = mes;
          this.cantidad = cantidad;
          this.acumulado = acumulado;
     }

     public EstadisticaAdminPublicacion() {
     }

     public Integer getAcumulado() {
          return acumulado;
     }

     public void setAcumulado(Integer acumulado) {
          this.acumulado = acumulado;
     }

     public Integer getCantidad() {
          return cantidad;
     }

     public void setCantidad(Integer cantidad) {
          this.cantidad = cantidad;
     }

     public Date getMes() {
          return mes;
     }

     public void setMes(Date mes) {
          this.mes = mes;
     }  
     
     public String getMesFormat() {
          SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM");
          return formato.format(mes);
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
