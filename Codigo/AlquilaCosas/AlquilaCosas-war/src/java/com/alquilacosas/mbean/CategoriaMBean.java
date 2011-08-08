/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.ejb.entity.Categoria;
import com.alquilacosas.ejb.session.CategoriaBeanLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author wilson
 */
@ManagedBean(name="categoria")
@ViewScoped
public class CategoriaMBean implements Serializable{
     
     /** Creates a new instance of CategoriaMBean */
     public CategoriaMBean() {
     }
     
     @EJB
     private CategoriaBeanLocal categoriaBean;
     private String nombre;
     private String descripcion;
     // Nuevo
     List<Categoria> categorias;
     private TreeNode rootNuevo;  
     private TreeNode selectedNode; 
     // Modificar
     private TreeNode rootModificar;
     private Categoria categoriaSeleccionadaModificar;
          
     @PostConstruct
     public void init() {
          categorias = categoriaBean.getCategorias();
          rootNuevo =  new DefaultTreeNode(new Categoria(null, "ROOT"), null);
          for (Categoria c : categorias){
               if (c.getCategoriaFk() == null){
                    TreeNode nuevoNodo = nuevoNodoConHijos(c,rootNuevo);
                    nuevoNodo.setExpanded(true);
               }
          }
          for (Categoria c : categorias){
               if (c.getCategoriaFk() == null){
                    TreeNode nuevoNodo = nuevoNodoConHijos(c,null);
                    nuevoNodo.setExpanded(true);
               }
          }
     }
     
     /*funcion recursiva que devuelve un nodo con sus hijos*/
     public TreeNode nuevoNodoConHijos(Categoria categoriaPadre, TreeNode padre){
          TreeNode nuevo = new DefaultTreeNode(categoriaPadre,padre);
          if (categoriaPadre.getCategoriaFk() == null){
               rootModificar =  nuevo;
          }
          for (Categoria c : categoriaPadre.getCategoriaList()){
               TreeNode nuevoNodo = nuevoNodoConHijos(c, nuevo);
               nuevoNodo.setExpanded(false);
          }
          return nuevo;
     }
     
     public void registrarNuevaCategoria(){
          if(selectedNode != null) {
               //Crear Nueva Categoria
               try{
                    Categoria categoriaPadre = (Categoria)selectedNode.getData();
                    categoriaBean.registrarCategoria(nombre, descripcion, categoriaPadre);
               }
               catch(AlquilaCosasException e){
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.toString(), e.toString());  
                    FacesContext.getCurrentInstance().addMessage(null, message);
               }               
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Categoria Creada", selectedNode.getData().toString());  
               FacesContext.getCurrentInstance().addMessage(null, message);  
          }
          else {
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Seleccione Categoria", "Seleccione Categoria");  
               FacesContext.getCurrentInstance().addMessage(null, message);  
          }
     }
     
     public void modificarCategoria(){
          categoriaSeleccionadaModificar.setNombre(nombre);
          categoriaSeleccionadaModificar.setDescripcion(descripcion);
          categoriaBean.modificarCategoria(categoriaSeleccionadaModificar);
     }
     
     public void borrarCategoria(){
          categoriaBean.borrarCategoria(categoriaSeleccionadaModificar);
     }
    
     public List<Categoria> getCategorias() {
          return categorias;
     }

     public void setCategorias(List<Categoria> categorias) {
          this.categorias = categorias;
     }

     public String getDescripcion() {
          return descripcion;
     }

     public void setDescripcion(String descripcion) {
          this.descripcion = descripcion;
     }

     public String getNombre() {
          return nombre;
     }

     public void setNombre(String nombre) {
          this.nombre = nombre;
     }

     public TreeNode getSelectedNode() {
          return selectedNode;
     }

     public void setSelectedNode(TreeNode selectedNode) {
          this.selectedNode = selectedNode;
     }     

     public TreeNode getRootModificar() {
          return rootModificar;
     }

     public void setRootModificar(TreeNode rootModificar) {
          this.rootModificar = rootModificar;
     }

     public TreeNode getRootNuevo() {
          return rootNuevo;
     }

     public void setRootNuevo(TreeNode rootNuevo) {
          this.rootNuevo = rootNuevo;
     }

     public Categoria getCategoriaSeleccionadaModificar() {
          return categoriaSeleccionadaModificar;
     }

     public void setCategoriaSeleccionadaModificar(Categoria categoriaSeleccionadaModificar) {
          this.categoriaSeleccionadaModificar = categoriaSeleccionadaModificar;
     }
     
     
}