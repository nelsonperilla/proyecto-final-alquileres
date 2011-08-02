/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

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
     List<Categoria> categorias;
     private TreeNode root;  
     private TreeNode selectedNode; 
     
     @PostConstruct
     public void init() {
          categorias = categoriaBean.getCategorias();
          root =  new DefaultTreeNode("Root", null);
          for (Categoria c : categorias){
               if (c.getCategoriaFk() == null){
                    TreeNode nuevoNodo = nuevoNodoConHijos(c,root);
               }
          }
     }
     
     public TreeNode nuevoNodoConHijos(Categoria categoriaPadre, TreeNode padre){
          TreeNode nuevo = new DefaultTreeNode(categoriaPadre.getNombre(),padre);    
          for (Categoria c : categoriaPadre.getCategoriaList()){
               TreeNode nuevoNodo = nuevoNodoConHijos(c, nuevo);
          }
          return nuevo;
     }
     
     public void registrarNuevaCategoria(){
          if(selectedNode != null) {
               //Crear Nueva Categoria
               
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Categoria Creada", selectedNode.getData().toString());  
               FacesContext.getCurrentInstance().addMessage(null, message);  
          }
          else {
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Seleccione Categoria", "Seleccione Categoria");  
               FacesContext.getCurrentInstance().addMessage(null, message);  
          }
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

     public TreeNode getRoot() {
          return root;
     }

     public void setRoot(TreeNode root) {
          this.root = root;
     }

     public TreeNode getSelectedNode() {
          return selectedNode;
     }

     public void setSelectedNode(TreeNode selectedNode) {
          this.selectedNode = selectedNode;
     }
}