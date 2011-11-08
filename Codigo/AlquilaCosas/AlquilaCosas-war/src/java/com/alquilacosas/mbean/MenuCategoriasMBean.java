/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.dto.CategoriaDTO;
import com.alquilacosas.ejb.session.CategoriaBeanLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "menuCatBean")
@SessionScoped
public class MenuCategoriasMBean implements Serializable {

    @EJB
    private CategoriaBeanLocal catBean;
    private List<CategoriaDTO> categorias;
    private Integer categoriaSeleccionada;

    /** Creates a new instance of MenuCategoriasMBean */
    public MenuCategoriasMBean() {
    }

    @PostConstruct
    public void init() {
        Logger.getLogger(MenuCategoriasMBean.class).debug("MenuCategoriasMBean: postconstruct."); 
        cargarMenu();
    }

    public void cargarMenu() {
        categorias = catBean.getCategoriaFacade();
        for (int i = 0; i < categorias.size(); i++) {
            CategoriaDTO fc = categorias.get(i);
            for (int j = 0; j < categorias.size(); j++) {
                CategoriaDTO fc2 = categorias.get(j);
                if (fc2.getPadreId() == fc.getId()) {
                    fc.getSubcategorias().add(fc2);
                }
            }
        }
        for (int i = 0; i < categorias.size(); i++) {
            CategoriaDTO fc = categorias.get(i);
            if (fc.getPadreId() > 0) {
                categorias.remove(fc);
                i--;
            }
        }
    }

    public List<CategoriaDTO> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaDTO> categorias) {
        this.categorias = categorias;
    }

    public Integer getCategoriaSeleccionada() {
        return categoriaSeleccionada;
    }

    public void setCategoriaSeleccionada(Integer categoriaSeleccionada) {
        this.categoriaSeleccionada = categoriaSeleccionada;
    }
}
