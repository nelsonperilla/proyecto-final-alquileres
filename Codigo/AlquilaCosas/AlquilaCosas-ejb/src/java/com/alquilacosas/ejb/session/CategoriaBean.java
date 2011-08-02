/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

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
          Query query = entityManager.createNamedQuery("Categoria.findAll");
          List<Categoria> categorias = query.getResultList();
          return categorias;
     }

     // Add business logic below. (Right-click in editor and choose
     // "Insert Code > Add Business Method")
     
     
}
