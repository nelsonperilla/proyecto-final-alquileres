/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.Busqueda;
import com.alquilacosas.common.CategoriaFacade;
import com.alquilacosas.common.PublicacionFacade;
import com.alquilacosas.ejb.session.BuscarPublicacionBeanLocal;
import com.alquilacosas.ejb.session.CategoriaBeanLocal;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "verCategoriaBean")
@ViewScoped
public class VerCategoriaMBean {

    @EJB
    private CategoriaBeanLocal categoriaBean;
    @EJB
    private BuscarPublicacionBeanLocal buscarBean;
    private Integer id, publicacionSeleccionada;
    private LazyDataModel model;
    private List<PublicacionFacade> publicaciones;
    private List<CategoriaFacade> subcategorias;
    private boolean primeraVez = true;
    
    /** Creates a new instance of VerCategoriaMBean */
    public VerCategoriaMBean() {
    }
    
    @PostConstruct
    public void init() {
        String param = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("id");
        if(param != null)
            id = Integer.valueOf(param);
        if(id != null) {
            buscar(0, 10);
        }
        
        model = new LazyDataModel<PublicacionFacade>() {

            @Override
            public List<PublicacionFacade> load(int first, int pageSize, String sortFielf,
                    boolean sort, Map<String, String> filters) {
                if(!primeraVez)
                    buscar(first, pageSize);
                return publicaciones;
            }
        };
        model.setRowCount(10);
    }
    
    public void buscar(int first, int pageSize) {
        Busqueda b = buscarBean.buscarPublicacionesPorCategoria(id, pageSize, first);
        publicaciones = b.getPublicaciones();        
        
        if(primeraVez)
            primeraVez = false;
    }
    
    public String verPublicacion() {
        return "";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LazyDataModel getModel() {
        return model;
    }

    public void setModel(LazyDataModel model) {
        this.model = model;
    }

    public List<PublicacionFacade> getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(List<PublicacionFacade> publicaciones) {
        this.publicaciones = publicaciones;
    }

    public List<CategoriaFacade> getSubcategorias() {
        return subcategorias;
    }

    public void setSubcategorias(List<CategoriaFacade> subcategorias) {
        this.subcategorias = subcategorias;
    }

    public Integer getPublicacionSeleccionada() {
        return publicacionSeleccionada;
    }

    public void setPublicacionSeleccionada(Integer publicacionSeleccionada) {
        this.publicacionSeleccionada = publicacionSeleccionada;
    }
    
    
}
