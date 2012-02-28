/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.Busqueda;
import com.alquilacosas.dto.CategoriaDTO;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.entity.Periodo.NombrePeriodo;
import com.alquilacosas.ejb.session.BuscarPublicacionBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.TreeNode;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "resultadosBean")
@ViewScoped
public class ResultadosMBean implements Serializable {

    @EJB
    private BuscarPublicacionBeanLocal buscarBean;
    @ManagedProperty(value="#{buscarBean}")
    private BuscarMBean buscarMBean;
    private LazyDataModel model;
    private List<PublicacionDTO> publicaciones;
    private List<CategoriaDTO> categorias;
    private Integer periodoSeleccionado;
    private List<SelectItem> periodos;
    private PublicacionDTO publicacionActual;
    private String busqueda, ubicacion;
    private Integer categoria;
    private int publicacionSeleccionada;
    private int totalRegistros;
    private boolean noBuscarEnModel = true;
    private boolean init = true;
    private Double precioDesde, precioHasta, precioMin, precioMax;
    private String range;
    private TreeNode raizCategorias;
    private TreeNode[] categoriasSeleccionadas;
    private List<TreeNode> seleccionadosAux;
    private List<Integer> categoriaIds;

    /** Creates a new instance of BuscarPublicacionMBean */
    public ResultadosMBean() {
    }

    @PostConstruct
    public void init() {
        Logger.getLogger(ResultadosMBean.class).debug("ResultadosMBean: postconstruct.");
        publicaciones = new ArrayList<PublicacionDTO>();
        busqueda = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("art");

        periodos = new ArrayList<SelectItem>();
        categoriaIds = new ArrayList<Integer>();
        seleccionadosAux = new ArrayList<TreeNode>();
        buscar(0, 10);
        armarCategorias();
        categoriasSeleccionadas = new TreeNode[seleccionadosAux.size()];
        categoriasSeleccionadas = seleccionadosAux.toArray(categoriasSeleccionadas);
        model = new LazyDataModel<PublicacionDTO>() {

            @Override
            public List<PublicacionDTO> load(int first, int pageSize, String sortFielf,
                    SortOrder sortOrder, Map<String, String> filters) {
                if (noBuscarEnModel) {
                    noBuscarEnModel = false;
                } else {
                    buscar(first, pageSize);
                }
                return publicaciones;
            }
        };
        model.setRowCount(totalRegistros);
        buscarMBean.setCriterio("");
        init = false;
    }

    public void filtrarUbicacion() {
        buscar(0, 10);
        noBuscarEnModel = true;
    }

    public void noFiltrarUbicacion() {
        ubicacion = null;
        buscar(0, 10);
        noBuscarEnModel = true;
    }

    public void filtrarPrecio() {
        precioDesde = 0D;
        precioHasta = 1000000D;
        try {
            precioDesde = Double.valueOf(range.split("-")[0]);
            precioHasta = Double.valueOf(range.split("-")[1]);
        } catch (Exception e) {
        }
        buscar(0, 10);
        noBuscarEnModel = true;
    }

    public void buscar(int first, int pageSize) {
        // preparar lista de ids de categorias seleccionadas
        categoriaIds.clear();
        if(categoriasSeleccionadas != null) {
            for(TreeNode node: categoriasSeleccionadas) {
                categoriaIds.add(((CategoriaDTO)node.getData()).getId());
            }
        }
        //si no se eligio ninguna categoria, no mostrar ninguna publicacion
        if(!init && categoriaIds.isEmpty()) {
            publicaciones.clear();
            return;
        }
        if(precioDesde == precioMin && precioHasta == precioMax) {
            precioDesde = precioHasta = null;
        }
        Busqueda b = buscarBean.buscar(busqueda, categoriaIds, ubicacion, NombrePeriodo.DIA,
                precioDesde, precioHasta, pageSize, first);
        publicaciones = b.getPublicaciones();
        categorias = b.getCategorias();
        if(init) {
            precioMin = precioDesde = b.getPrecioMinimo();
            precioMax = precioHasta = b.getPrecioMaximo();
        }
        if (first == 0) {
            totalRegistros = b.getTotalRegistros();
            if (model != null) {
                model.setRowCount(totalRegistros);
            }
        }
    }
    
    private void armarCategorias(){
        raizCategorias = new DefaultTreeNode("Categorias", null);
        for(CategoriaDTO cat: categorias) {
            TreeNode node = expandirNodo(cat, raizCategorias);
            node.setExpanded(true);
            node.setSelected(true);
            categoriaIds.add(cat.getId());
            seleccionadosAux.add(node);
        }
    }
    
    private TreeNode expandirNodo(CategoriaDTO cat, TreeNode padre) {
        TreeNode nuevo = new DefaultTreeNode(cat, padre);
        nuevo.setSelected(true);
        for(CategoriaDTO c: cat.getSubcategorias()) {
            TreeNode nuevo2 = expandirNodo(c, nuevo);
            nuevo2.setExpanded(true);
            nuevo2.setSelected(true);
            categoriaIds.add(c.getId());
            seleccionadosAux.add(nuevo2);
        }
        return nuevo;
    }

    public String verPublicacion() {
        return "mostrarPublicacion";
    }

    /*
     * Getters & Setters
     */
    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public List<PublicacionDTO> getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(List<PublicacionDTO> publicaciones) {
        this.publicaciones = publicaciones;
    }

    public PublicacionDTO getPublicacionActual() {
        return publicacionActual;
    }

    public void setPublicacionActual(PublicacionDTO publicacionActual) {
        this.publicacionActual = publicacionActual;
    }

    public LazyDataModel getModel() {
        return model;
    }

    public void setModel(LazyDataModel model) {
        this.model = model;
    }

    public int getPublicacionSeleccionada() {
        return publicacionSeleccionada;
    }

    public void setPublicacionSeleccionada(int publicacionSeleccionada) {
        this.publicacionSeleccionada = publicacionSeleccionada;
    }

    public List<CategoriaDTO> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaDTO> categorias) {
        this.categorias = categorias;
    }

    public Integer getCategoria() {
        return categoria;
    }

    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Integer getPeriodoSeleccionado() {
        return periodoSeleccionado;
    }

    public void setPeriodoSeleccionado(Integer periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
    }

    public List<SelectItem> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<SelectItem> periodos) {
        this.periodos = periodos;
    }

    public Double getPrecioDesde() {
        return precioDesde;
    }

    public void setPrecioDesde(Double precioDesde) {
        this.precioDesde = precioDesde;
    }

    public Double getPrecioHasta() {
        return precioHasta;
    }

    public void setPrecioHasta(Double precioHasta) {
        this.precioHasta = precioHasta;
    }

    public Double getPrecioMax() {
        return precioMax;
    }

    public void setPrecioMax(Double precioMax) {
        this.precioMax = precioMax;
    }

    public Double getPrecioMin() {
        return precioMin;
    }

    public void setPrecioMin(Double precioMin) {
        this.precioMin = precioMin;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public TreeNode getRaizCategorias() {
        return raizCategorias;
    }

    public void setRaizCategorias(TreeNode raizCategorias) {
        this.raizCategorias = raizCategorias;
    }

    public TreeNode[] getCategoriasSeleccionadas() {
        return categoriasSeleccionadas;
    }

    public void setCategoriasSeleccionadas(TreeNode[] categoriasSeleccionadas) {
        this.categoriasSeleccionadas = categoriasSeleccionadas;
    }

    public BuscarMBean getBuscarMBean() {
        return buscarMBean;
    }

    public void setBuscarMBean(BuscarMBean buscarMBean) {
        this.buscarMBean = buscarMBean;
    }

}
