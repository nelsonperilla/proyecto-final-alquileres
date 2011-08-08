/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.ejb.entity.Categoria;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wilson
 */
@Stateless
public class CategoriaBean implements CategoriaBeanLocal {

     @PersistenceContext(unitName="AlquilaCosas-ejbPU") 
     private EntityManager entityManager;
     
     @Override
     public List<Categoria> getCategorias() {
          List<Categoria> categorias;
          Query query = entityManager.createNamedQuery("Categoria.findAll");
          categorias = query.getResultList();
          return categorias;
     }
     
     // Add business logic below. (Right-click in editor and choose
     // "Insert Code > Add Business Method")
     
     @Override
     public void registrarCategoria(String nombre, String descripcion, Categoria categoriaPadre) throws AlquilaCosasException{
          Categoria nuevaCategoria = new Categoria();
          nuevaCategoria.setNombre(nombre);
          nuevaCategoria.setDescripcion(descripcion);
          nuevaCategoria.setCategoriaFk(categoriaPadre);
          try{
               entityManager.persist(nuevaCategoria);
          }
          catch(Exception e){
               throw new AlquilaCosasException("Error al insertar la Categoria - " + e.getMessage());
          }
          entityManager.flush();
     }
     
     @Override
     public void borrarCategoria(Categoria categoria) {
          //no se como se hace...
          Categoria modifCategoria = entityManager.find(Categoria.class, categoria.getCategoriaId());
          entityManager.remove(modifCategoria);
     }

     @Override
     public void modificarCategoria(Categoria categoria) {
          Categoria modifCategoria = entityManager.find(Categoria.class, categoria.getCategoriaId());
          modifCategoria.setNombre(categoria.getNombre());
          modifCategoria.setDescripcion(categoria.getDescripcion());
          entityManager.merge(modifCategoria);
     }

}
