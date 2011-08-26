/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.CategoriaDTO;
import com.alquilacosas.ejb.entity.Categoria;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wilson
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CategoriaBean implements CategoriaBeanLocal {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager entityManager;
    @Resource
    private SessionContext context;

    @Override
    public List<Categoria> getCategorias() {
        Query query = entityManager.createNamedQuery("Categoria.findAll");
        List<Categoria> categorias = query.getResultList();
        return categorias;
    }

    @Override
    public Categoria getCategoriaPadre(int categoriaId) {
        Categoria cat = entityManager.find(Categoria.class, categoriaId);
        Categoria padre = cat.getCategoriaFk();
        return padre;
    }

    @Override
    public void registrarCategoria(String nombre, String descripcion, Categoria categoriaPadre) throws AlquilaCosasException {
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre(nombre);
        nuevaCategoria.setDescripcion(descripcion);
        if (categoriaPadre != null) {
            categoriaPadre.agregarSubcategoria(nuevaCategoria);
            try {
                entityManager.merge(categoriaPadre);
            } catch (Exception e) {
                throw new AlquilaCosasException("Error al insertar la Categoria: " + e.getMessage());
            }
        }
        else {
            entityManager.persist(nuevaCategoria);
        }
    }

    @Override
    public List<CategoriaDTO> getCategoriasPrincipal() {

        Query query = entityManager.createQuery(""
                + "select cat FROM Categoria cat "
                + "where cat.categoriaFk IS NULL");
        List<Categoria> categorias = query.getResultList();
        List<CategoriaDTO> categoriasFacade = new ArrayList<CategoriaDTO>();
        for (Categoria c : categorias) {
            categoriasFacade.add(new CategoriaDTO(c.getCategoriaId(), c.getNombre()));
        }
        return categoriasFacade;
    }

    @Override
    public void borrarCategoria(int categoriaId) {
        Categoria categoria = entityManager.find(Categoria.class, categoriaId);
        Categoria categoriaPadre = categoria.getCategoriaFk();
        if (categoriaPadre != null) {
            categoriaPadre.removerSubcategoria(categoria);
            entityManager.merge(categoriaPadre);
        }
        entityManager.remove(categoria);
    }

    @Override
    public void modificarCategoria(Categoria categoria) {
        Categoria modifCategoria = entityManager.find(Categoria.class, categoria.getCategoriaId());
        modifCategoria.setNombre(categoria.getNombre());
        modifCategoria.setDescripcion(categoria.getDescripcion());
        entityManager.merge(modifCategoria);
    }

    @Override
    public List<CategoriaDTO> getCategoriaFacade() {
        List<Categoria> categorias;
        Query query = entityManager.createNamedQuery("Categoria.findAll");
        categorias = query.getResultList();
        List<CategoriaDTO> catFacade = new ArrayList<CategoriaDTO>();
        for (Categoria c : categorias) {
            Categoria padre = c.getCategoriaFk();
            int padreId = 0;
            if (padre != null) {
                padreId = padre.getCategoriaId();
            }
            CategoriaDTO cat = new CategoriaDTO(c.getCategoriaId(),
                    padreId, c.getNombre(), c.getDescripcion());
            catFacade.add(cat);
        }
        return catFacade;
    }

    @Override
    public List<CategoriaDTO> getSubCategorias(int categoria) {

        Categoria cat = entityManager.find(Categoria.class, categoria);
        Query subcatQuery = entityManager.createNamedQuery("Categoria.findByCategoriaFk");
        subcatQuery.setParameter("categoria", cat);
        List<Categoria> categorias = subcatQuery.getResultList();
        List<CategoriaDTO> subcategorias = new ArrayList<CategoriaDTO>();
        for (Categoria c : categorias) {
            subcategorias.add(new CategoriaDTO(c.getCategoriaId(), c.getNombre()));
        }
        return subcategorias;
    }

    @Override
    public List<Integer> getCategoriasPadre(int categoriaId) {

        Categoria categoria = entityManager.find(Categoria.class, categoriaId);
        List<Integer> ids = new ArrayList<Integer>();

        while (categoria != null) {
            categoria = categoria.getCategoriaFk();
            if (categoria != null) {
                ids.add(categoria.getCategoriaId());
            }
        }
        Collections.reverse(ids);
        return ids;

    }
}
