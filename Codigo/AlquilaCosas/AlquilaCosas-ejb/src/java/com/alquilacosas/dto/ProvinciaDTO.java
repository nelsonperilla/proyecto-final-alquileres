/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.dto;

/**
 *
 * @author wilson
 */
public class ProvinciaDTO {
     private int id, paisId;
     private String nombre, paisNombre;

     public String getPaisNombre() {
          return paisNombre;
     }

     public void setPaisNombre(String paisNombre) {
          this.paisNombre = paisNombre;
     }
     
     public ProvinciaDTO(int Id, String Nombre, int PaisId, String PaisNombre) {
          id=Id;
          nombre=Nombre;
          paisId=PaisId;
          paisNombre=PaisNombre;
     }

     public int getId() {
          return id;
     }

     public void setId(int id) {
          this.id = id;
     }

     public String getNombre() {
          return nombre;
     }

     public void setNombre(String nombre) {
          this.nombre = nombre;
     }

     public int getPaisId() {
          return paisId;
     }

     public void setPaisId(int paisId) {
          this.paisId = paisId;
     }
     
}
