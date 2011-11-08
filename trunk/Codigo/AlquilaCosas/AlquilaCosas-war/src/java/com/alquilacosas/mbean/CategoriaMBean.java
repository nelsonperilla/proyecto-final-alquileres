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
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
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
     
     @ManagedProperty(value="#{menuCatBean}")
     private MenuCategoriasMBean menuBean;
     
     private String nombre;
     private String descripcion;
     // Nuevo
     List<Categoria> categorias;
     private TreeNode rootNuevo;  
     private TreeNode selectedNode; 
     private TreeNode nodoPrincipal;  
     // Modificar
     private TreeNode rootModificar;
     private Categoria categoriaSeleccionadaModificar;
     private String nombreM;
     private String descripcionM;
     
     @PostConstruct
     public void init() {
         Logger.getLogger(CategoriaMBean.class).debug("CategoriaMBean: postconstruct."); 
         cargarTrees();
     }
     
     public void cargarTrees() {
          categorias = categoriaBean.getCategorias();
          rootNuevo = new DefaultTreeNode(new Categoria(null, "ROOT"), null);
          nodoPrincipal = new DefaultTreeNode(new Categoria(null, "CATEGORIAS"),rootNuevo);
          nodoPrincipal.setExpanded(true);
          for (Categoria c : categorias) {
               if (c.getCategoriaFk() == null) {
                    TreeNode nuevoNodo = nuevoNodoConHijos(c, nodoPrincipal);
                    nuevoNodo.setExpanded(false);
               }
          }
          rootModificar = new DefaultTreeNode(new Categoria(null, "ROOT"), null);
          for (Categoria c : categorias) {
               if (c.getCategoriaFk() == null) {
                    TreeNode nuevoNodo = nuevoNodoConHijos(c, rootModificar);
                    nuevoNodo.setExpanded(true);
               }
          }
     }
     
     /*funcion recursiva que devuelve un nodo con sus hijos*/
     public TreeNode nuevoNodoConHijos(Categoria categoriaPadre, TreeNode padre){
          TreeNode nuevo = new DefaultTreeNode(categoriaPadre,padre);
          for (Categoria c : categoriaPadre.getCategoriaList()){
               TreeNode nuevoNodo = nuevoNodoConHijos(c, nuevo);
               nuevoNodo.setExpanded(true);
          }
          return nuevo;
     }
     
     public String registrarNuevaCategoria(){
          if(selectedNode != null) {
               //Crear Nueva Categoria
               try{
                    Categoria categoriaPadre = (Categoria)selectedNode.getData();
                    if(categoriaPadre.getNombre().equals("CATEGORIAS"))
                         categoriaPadre = null;
                    if (!existeCategoria(nombre,categoriaPadre)){
                         categoriaBean.registrarCategoria(nombre, descripcion, categoriaPadre);
                         cargarTrees();
                         menuBean.cargarMenu();
                         FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Categoria Creada", selectedNode.getData().toString());  
                         FacesContext.getCurrentInstance().addMessage(null, message);
                         return "pcategorias";
                    }
                    else{
                         FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "La Categoria ya Existe", "");  
                         FacesContext.getCurrentInstance().addMessage(null, message); 
                    }
               }
               catch(AlquilaCosasException e){
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.toString(), e.toString());  
                    FacesContext.getCurrentInstance().addMessage(null, message);
               } 
          }
          else {
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Seleccione Categoria", "Seleccione Categoria");  
               FacesContext.getCurrentInstance().addMessage(null, message);  
          }
          return null;
     }

     public void modificarCategoria(){
          boolean existe = false;
          for(Categoria c : categorias)
               if (c.getNombre().equals(nombreM) && c.getCategoriaFk() == 
                       categoriaSeleccionadaModificar.getCategoriaFk() && 
                       c.getCategoriaId() != categoriaSeleccionadaModificar.getCategoriaId())
                    existe=true;
          if (!existe){
               categoriaSeleccionadaModificar.setNombre(nombreM);
               categoriaSeleccionadaModificar.setDescripcion(descripcionM);
               categoriaBean.modificarCategoria(categoriaSeleccionadaModificar);
               menuBean.cargarMenu();
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Categoria Modificada", "");  
               FacesContext.getCurrentInstance().addMessage(null, message); 
          }
          else{
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "La Categoria ya Existe", "");  
               FacesContext.getCurrentInstance().addMessage(null, message); 
          }
     }
     
     public void borrarCategoria(){
         if(categoriaSeleccionadaModificar != null)  {
            categoriaBean.borrarCategoria(categoriaSeleccionadaModificar.getCategoriaId());
            cargarTrees();
            menuBean.cargarMenu();
         }
     }
     
     public boolean existeCategoria(String nombre, Categoria padre){
          boolean existe = false;
          for (Categoria c : categorias)
               if (c.getNombre().equals(nombre) && c.getCategoriaFk().equals(padre))
                    existe = true;
          return existe;
     }
     
     public String getDescripcionM() {
          return descripcionM;
     }

     public void setDescripcionM(String descripcionM) {
          this.descripcionM = descripcionM;
     }

     public String getNombreM() {
          return nombreM;
     }

     public void setNombreM(String nombreM) {
          this.nombreM = nombreM;
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

    public MenuCategoriasMBean getMenuBean() {
        return menuBean;
    }

    public void setMenuBean(MenuCategoriasMBean menuBean) {
        this.menuBean = menuBean;
    }
     
     
}