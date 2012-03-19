/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.common;

import com.alquilacosas.ejb.entity.EstadoAlquiler.NombreEstadoAlquiler;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author wilson
 */
public class EstadisticaAdminAlquiler {
     private Date mes;
     private NombreEstadoAlquiler estado;
     private Integer cantidad;
     private Integer acumulado;

     public EstadisticaAdminAlquiler(Date mes, NombreEstadoAlquiler estado, Integer cantidad, Integer acumulado) {
          this.mes = mes;
          this.estado = estado;
          this.cantidad = cantidad;
          this.acumulado = acumulado;
     }

     public EstadisticaAdminAlquiler() {
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

     public NombreEstadoAlquiler getEstado() {
          return estado;
     }

     public void setEstado(NombreEstadoAlquiler estado) {
          this.estado = estado;
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
