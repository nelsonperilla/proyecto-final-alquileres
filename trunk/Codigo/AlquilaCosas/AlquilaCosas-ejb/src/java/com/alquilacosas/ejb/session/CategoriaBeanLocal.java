/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.common.CategoriaFacade;
import com.alquilacosas.ejb.entity.Categoria;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wilson
 */
@Local
public interface CategoriaBeanLocal {

     List<Categoria> getCategorias();

     void registrarCategoria (String nombre, String descripcion, Categoria categoriaPadre) throws AlquilaCosasException;

     void borrarCategoria(Categoria categoria);

     void modificarCategoria(Categoria categoria);
     
     List<CategoriaFacade> getCategoriaFacade();

     List<CategoriaFacade> getSubCategorias(int categoria);
}
