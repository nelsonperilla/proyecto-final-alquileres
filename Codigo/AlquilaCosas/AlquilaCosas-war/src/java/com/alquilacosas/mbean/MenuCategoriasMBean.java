/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.CategoriaFacade;
import com.alquilacosas.ejb.session.CategoriaBeanLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "menuCatBean")
@SessionScoped
public class MenuCategoriasMBean implements Serializable {

    @EJB
    private CategoriaBeanLocal catBean;
    private List<CategoriaFacade> categorias;
    private Integer categoriaSeleccionada;

    /** Creates a new instance of MenuCategoriasMBean */
    public MenuCategoriasMBean() {
    }

    @PostConstruct
    public void init() {
        cargarMenu();
    }

    public void cargarMenu() {
        categorias = catBean.getCategoriaFacade();
        for (int i = 0; i < categorias.size(); i++) {
            CategoriaFacade fc = categorias.get(i);
            for (int j = 0; j < categorias.size(); j++) {
                CategoriaFacade fc2 = categorias.get(j);
                if (fc2.getPadreId() == fc.getId()) {
                    fc.getSubcategorias().add(fc2);
                }
            }
        }
        for (int i = 0; i < categorias.size(); i++) {
            CategoriaFacade fc = categorias.get(i);
            if (fc.getPadreId() > 0) {
                categorias.remove(fc);
                i--;
            }
        }
    }

    public List<CategoriaFacade> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaFacade> categorias) {
        this.categorias = categorias;
    }

    public Integer getCategoriaSeleccionada() {
        return categoriaSeleccionada;
    }

    public void setCategoriaSeleccionada(Integer categoriaSeleccionada) {
        this.categoriaSeleccionada = categoriaSeleccionada;
    }
}
