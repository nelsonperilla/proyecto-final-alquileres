/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.CategoriaDTO;
import com.alquilacosas.ejb.entity.Categoria;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wilson
 */
@Local
public interface CategoriaBeanLocal {

    public List<Categoria> getCategorias();

    public Categoria getCategoriaPadre(int categoriaId);

    public void registrarCategoria(String nombre, String descripcion, Categoria categoriaPadre) throws AlquilaCosasException;

    public void borrarCategoria(int categoriaId);

    public void modificarCategoria(Categoria categoria);

    public List<CategoriaDTO> getCategoriaFacade();

    public List<CategoriaDTO> getSubCategorias(int categoria);

    public List<CategoriaDTO> getCategoriasPrincipal();

    public List<Integer> getCategoriasPadre(int categoriaId);
    
    public Categoria getCategoriaRandom();
}
