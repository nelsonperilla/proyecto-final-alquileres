/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.CategoriaDTO;
import com.alquilacosas.dto.PeriodoDTO;
import com.alquilacosas.dto.PrecioDTO;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.entity.Categoria;
import com.alquilacosas.ejb.entity.EstadoPublicacion.NombreEstadoPublicacion;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.session.CategoriaBeanLocal;
import com.alquilacosas.ejb.session.PeriodoAlquilerBeanLocal;
import com.alquilacosas.ejb.session.PublicacionBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.map.DefaultMapModel;  
import org.primefaces.model.map.LatLng;  
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author ignaciogiagante
 */
@ManagedBean(name = "modPublicacion")
@ViewScoped
public class ModificarPublicacionMBean implements Serializable {

    @EJB
    private PublicacionBeanLocal publicacionBean;
    @EJB
    private CategoriaBeanLocal categoriaBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean login;
    @EJB
    private PeriodoAlquilerBeanLocal periodosBean;
    
    private PublicacionDTO pf;
    //Datos de la publicacion
    private String titulo;
    private String descripcion;
    private Date fechaDesde;
    private Date fechaHasta;
    private boolean destacada;
    private int cantidad;
    private Categoria categoria;
    //Select Items categorias
    private List<SelectItem> categorias, subcategorias1, subcategorias2, subcategorias3;
    private int selectedCategoria, selectedSubcategoria1, selectedSubcategoria2, selectedSubcategoria3;
    private boolean subcategoria1Render, subcategoria2Render, subcategoria3Render;
    //estado
    private List<SelectItem> estados;
    private NombreEstadoPublicacion selectedEstado;
    //Object Precio
    private List<PrecioDTO> precios;
    private PrecioDTO precioFacade;
    private int periodoMinimo;
    private Integer periodoMaximo;
    private List<SelectItem> periodoMinimos;
    private int selectedPeriodoMinimo;
    private List<SelectItem> periodoMaximos;
    private Integer selectedPeriodoMaximo;
    
    private Date today;
    private List<ImagenPublicacion> imagenes;
    private List<byte[]> imagenesAgregar;
    private List<Integer> imagenesABorrar;
    private List<Integer> imagenIds;
    private int publicacionId;
    private List<NombreEstadoPublicacion> estadosPublicaciones;
    private int imagenABorrar;

    private MapModel gMap;  
    private double lat;  
    private double lng;     
    
    public ModificarPublicacionMBean() {
    }

    @PostConstruct
    public void init() {
        Logger.getLogger(ModificarPublicacionMBean.class).debug("ModificarPublicacionMBean: postconstruct.");
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if(id == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al cargar pagina", "No se brindo el id de publicacion");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        publicacionId = Integer.parseInt(id);
        try {
            pf = publicacionBean.getDatosPublicacion(publicacionId);
        } catch(AlquilaCosasException e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al cargar pagina", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        titulo = pf.getTitulo();
        descripcion = pf.getDescripcion();
        fechaDesde = pf.getFechaDesde();
        fechaHasta = pf.getFechaHasta();
        destacada = pf.getDestacada();
        cantidad = pf.getCantidad();
        categoria = pf.getCategoria();
        precios = pf.getPrecios();
        imagenes = pf.getImagenes();
        imagenIds = new ArrayList<Integer>();
        imagenesAgregar = new ArrayList<byte[]>();
        imagenesABorrar = new ArrayList<Integer>();
        for (ImagenPublicacion ip : imagenes) {
            imagenIds.add(ip.getImagenPublicacionId());
        }

        today = new Date();
        categorias = new ArrayList<SelectItem>();
        subcategorias1 = new ArrayList<SelectItem>();
        subcategorias2 = new ArrayList<SelectItem>();
        subcategorias3 = new ArrayList<SelectItem>();
        estados = new ArrayList<SelectItem>();
        
        periodoMinimos = new ArrayList<SelectItem>();
        periodoMaximos = new ArrayList<SelectItem>();
        this.periodoMinimo = pf.getPeriodoMinimoValor();
        this.periodoMaximo = pf.getPeriodoMaximoValor();
        this.selectedPeriodoMinimo = pf.getPeriodoMinimo().getPeriodoId();
        
        if( !(pf.getPeriodoMaximo() == null) )
            this.selectedPeriodoMaximo = pf.getPeriodoMaximo().getPeriodoId();
        else
            this.selectedPeriodoMaximo = 0;
        
        List<PeriodoDTO> periodos = periodosBean.getPeriodos();
        for ( PeriodoDTO p : periodos ){
            periodoMinimos.add( new SelectItem( p.getId(), p.getNombre().name() ));
            periodoMaximos.add( new SelectItem( p.getId(), p.getNombre().name() ));
        }
        
        List<CategoriaDTO> listaCategoria = categoriaBean.getCategoriasPrincipal();
        for (CategoriaDTO category : listaCategoria) {
            categorias.add(new SelectItem(category.getId(), category.getNombre()));
        }
        
        int categoriaId = categoria.getCategoriaId();
        inicializarCategorias(categoriaId);

        for(NombreEstadoPublicacion ep: NombreEstadoPublicacion.values()) {
            estados.add( new SelectItem( ep, ep.toString() ));
        }

        selectedEstado = pf.getEstado().getNombre();
        
        setgMap(new DefaultMapModel());
        LatLng position = new LatLng(pf.getLatitud(), pf.getLongitud()); 
        Marker marcador = new Marker(position, pf.getTitulo());
        marcador.setDraggable(true);
        getgMap().addOverlay(marcador);         

    }

    public void inicializarCategorias(int categoriaId) {
        List<Integer> padres = categoriaBean.getCategoriasPadre(categoriaId);
        if (padres.isEmpty()) {
            selectedCategoria = categoriaId;
        } else if (padres.size() == 1) {
            selectedCategoria = padres.get(0);
            List<CategoriaDTO> cats = categoriaBean.getSubCategorias(selectedCategoria);
            for (CategoriaDTO c : cats) {
                subcategorias1.add(new SelectItem(c.getId(), c.getNombre()));
            }
            selectedSubcategoria1 = categoriaId;
            subcategoria1Render = true;
        } else if (padres.size() == 2) {
            selectedCategoria = padres.get(0);

            List<CategoriaDTO> cats = categoriaBean.getSubCategorias(selectedCategoria);
            for (CategoriaDTO c : cats) {
                subcategorias1.add(new SelectItem(c.getId(), c.getNombre()));
            }
            selectedSubcategoria1 = padres.get(1);
            subcategoria1Render = true;
            cats = categoriaBean.getSubCategorias(selectedSubcategoria1);
            for (CategoriaDTO c : cats) {
                subcategorias2.add(new SelectItem(c.getId(), c.getNombre()));
            }
            selectedSubcategoria2 = categoriaId;
            subcategoria2Render = true;
        } else if (padres.size() == 3) {
            selectedCategoria = padres.get(0);

            List<CategoriaDTO> cats = categoriaBean.getSubCategorias(selectedCategoria);
            for (CategoriaDTO c : cats) {
                subcategorias1.add(new SelectItem(c.getId(), c.getNombre()));
            }
            selectedSubcategoria1 = padres.get(1);
            subcategoria1Render = true;
            cats = categoriaBean.getSubCategorias(selectedSubcategoria1);
            for (CategoriaDTO c : cats) {
                subcategorias2.add(new SelectItem(c.getId(), c.getNombre()));
            }
            selectedSubcategoria2 = padres.get(2);
            subcategoria2Render = true;
            cats = categoriaBean.getSubCategorias(selectedSubcategoria2);
            for (CategoriaDTO c : cats) {
                subcategorias3.add(new SelectItem(c.getId(), c.getNombre()));
            }
            selectedSubcategoria3 = categoriaId;
            subcategoria3Render = true;
        } else {
            System.out.println("demasiadas subcategorias!!");
        }
    }

    public String actualizarPublicacion() {
        int cat = 0;
        if (selectedSubcategoria2 > 0) {
            cat = selectedSubcategoria2;
        } else if (selectedSubcategoria1 > 0) {
            cat = selectedSubcategoria1;
        } else {
            cat = selectedCategoria;
        }
        try {
            publicacionBean.actualizarPublicacion(publicacionId, titulo,
                    descripcion, fechaDesde, fechaHasta, destacada, cantidad,
                    login.getUsuarioId(), cat, precios, imagenesAgregar,
                    imagenesABorrar, periodoMinimo, selectedPeriodoMinimo, periodoMaximo, 
                    selectedPeriodoMaximo, selectedEstado,lat,lng);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Los datos fueron guardados correctamente"));
            return "pmisPublicaciones";
        } catch (AlquilaCosasException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al actualizar usuario", e.getMessage()));
        }
        return null;
    }

    public void categoriaSeleccionadaCambio() {
        subcategorias1.clear();
        subcategorias2.clear();
        subcategorias3.clear();
        selectedSubcategoria1 = 0;
        selectedSubcategoria2 = 0;
        selectedSubcategoria3 = 0;
        List<CategoriaDTO> categoriaList = categoriaBean.getSubCategorias(selectedCategoria);
        if (!categoriaList.isEmpty()) {
            subcategoria1Render = true;
            for (CategoriaDTO c : categoriaList) {
                subcategorias1.add(new SelectItem(c.getId(), c.getNombre()));
            }
        } else {
            subcategoria1Render = false;
        }
        subcategoria2Render = false;
        subcategoria3Render = false;
    }

    public void subcategoria1SeleccionadaCambio() {
        subcategorias2.clear();
        subcategorias3.clear();
        selectedSubcategoria2 = 0;
        selectedSubcategoria3 = 0;
        List<CategoriaDTO> categoriaList = categoriaBean.getSubCategorias(selectedSubcategoria1);
        if (!categoriaList.isEmpty()) {
            subcategoria2Render = true;
            for (CategoriaDTO c : categoriaList) {
                subcategorias2.add(new SelectItem(c.getId(), c.getNombre()));
            }
        } else {
            subcategoria2Render = false;
        }
        subcategoria3Render = false;
    }
    
    public void subcategoria2SeleccionadaCambio() {
        subcategorias3.clear();
        selectedSubcategoria3 = 0;
        List<CategoriaDTO> categoriaList = categoriaBean.getSubCategorias(selectedSubcategoria2);
        if (!categoriaList.isEmpty()) {
            subcategoria3Render = true;
            for (CategoriaDTO c : categoriaList) {
                subcategorias3.add(new SelectItem(c.getId(), c.getNombre()));
            }
        } else {
            subcategoria3Render = false;
        }
    }

    public void handleFileUpload(FileUploadEvent event) {

        try {
            imagenesAgregar.add(event.getFile().getContents());
            FacesMessage msg = new FacesMessage("Excelente",
                    event.getFile().getFileName() + "fue cargado correctamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } catch (Exception e) {

            FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "El archivo no pudo ser cargado!", " ");
            FacesContext.getCurrentInstance().addMessage(null, error);
        }

    }

    public void removerImagen(ActionEvent e) {
        Integer id = (Integer) e.getComponent().getAttributes().get("idBorrar");
        imagenIds.remove(id);
        imagenesABorrar.add(id);
    }
    
    /*
     * Getters & Setters
     */

    public List<Integer> getImagenesABorrar() {
        return imagenesABorrar;
    }

    public void setImagenesABorrar(List<Integer> imagenesABorrar) {
        this.imagenesABorrar = imagenesABorrar;
    }

    public List<byte[]> getImagenesAgregar() {
        return imagenesAgregar;
    }

    public void setImagenesAgregar(List<byte[]> imagenesAgregar) {
        this.imagenesAgregar = imagenesAgregar;
    }

    public List<Integer> getImagenIds() {
        return imagenIds;
    }

    public void setImagenIds(List<Integer> imagenIds) {
        this.imagenIds = imagenIds;
    }

    public int getPublicacionId() {
        return publicacionId;
    }

    public void setPublicacionId(int publicacionId) {
        this.publicacionId = publicacionId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<SelectItem> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<SelectItem> categorias) {
        this.categorias = categorias;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isDestacada() {
        return destacada;
    }

    public void setDestacada(boolean destacada) {
        this.destacada = destacada;
    }

    public List<SelectItem> getEstados() {
        return estados;
    }

    public void setEstados(List<SelectItem> estados) {
        this.estados = estados;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public List<ImagenPublicacion> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ImagenPublicacion> imagenes) {
        this.imagenes = imagenes;
    }

    public ManejadorUsuarioMBean getLogin() {
        return login;
    }

    public void setLogin(ManejadorUsuarioMBean login) {
        this.login = login;
    }

    public PublicacionDTO getPf() {
        return pf;
    }

    public void setPf(PublicacionDTO pf) {
        this.pf = pf;
    }

    public PrecioDTO getPrecioFacade() {
        return precioFacade;
    }

    public void setPrecioFacade(PrecioDTO precioFacade) {
        this.precioFacade = precioFacade;
    }

    public List<PrecioDTO> getPrecios() {
        return precios;
    }

    public void setPrecios(List<PrecioDTO> precios) {
        this.precios = precios;
    }

    public int getSelectedCategoria() {
        return selectedCategoria;
    }

    public void setSelectedCategoria(int selectedCategoria) {
        this.selectedCategoria = selectedCategoria;
    }

    public NombreEstadoPublicacion getSelectedEstado() {
        return selectedEstado;
    }

    public void setSelectedEstado(NombreEstadoPublicacion selectedEstado) {
        this.selectedEstado = selectedEstado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public int getImagenABorrar() {
        return imagenABorrar;
    }

    public void setImagenABorrar(int imagenABorrar) {
        this.imagenABorrar = imagenABorrar;
    }


    public int getSelectedSubcategoria1() {
        return selectedSubcategoria1;
    }

    public void setSelectedSubcategoria1(int selectedSubcategoria1) {
        this.selectedSubcategoria1 = selectedSubcategoria1;
    }

    public int getSelectedSubcategoria2() {
        return selectedSubcategoria2;
    }

    public void setSelectedSubcategoria2(int selectedSubcategoria2) {
        this.selectedSubcategoria2 = selectedSubcategoria2;
    }

    public int getSelectedSubcategoria3() {
        return selectedSubcategoria3;
    }

    public void setSelectedSubcategoria3(int selectedSubcategoria3) {
        this.selectedSubcategoria3 = selectedSubcategoria3;
    }

    public boolean isSubcategoria1Render() {
        return subcategoria1Render;
    }

    public void setSubcategoria1Render(boolean subcategoria1Render) {
        this.subcategoria1Render = subcategoria1Render;
    }

    public boolean isSubcategoria2Render() {
        return subcategoria2Render;
    }

    public void setSubcategoria2Render(boolean subcategoria2Render) {
        this.subcategoria2Render = subcategoria2Render;
    }

    public boolean isSubcategoria3Render() {
        return subcategoria3Render;
    }

    public void setSubcategoria3Render(boolean subcategoria3Render) {
        this.subcategoria3Render = subcategoria3Render;
    }

    public List<SelectItem> getSubcategorias1() {
        return subcategorias1;
    }

    public void setSubcategorias1(List<SelectItem> subcategorias1) {
        this.subcategorias1 = subcategorias1;
    }

    public List<SelectItem> getSubcategorias2() {
        return subcategorias2;
    }

    public void setSubcategorias2(List<SelectItem> subcategorias2) {
        this.subcategorias2 = subcategorias2;
    }

    public List<SelectItem> getSubcategorias3() {
        return subcategorias3;
    }

    public void setSubcategorias3(List<SelectItem> subcategorias3) {
        this.subcategorias3 = subcategorias3;
    }

    public List<SelectItem> getPeriodoMaximos() {
        return periodoMaximos;
    }

    public void setPeriodoMaximos(List<SelectItem> periodoMaximos) {
        this.periodoMaximos = periodoMaximos;
    }

    public List<SelectItem> getPeriodoMinimos() {
        return periodoMinimos;
    }

    public void setPeriodoMinimos(List<SelectItem> periodoMinimos) {
        this.periodoMinimos = periodoMinimos;
    }

    public Integer getSelectedPeriodoMaximo() {
        return selectedPeriodoMaximo;
    }

    public void setSelectedPeriodoMaximo(Integer selectedPeriodoMaximo) {
        this.selectedPeriodoMaximo = selectedPeriodoMaximo;
    }

    public int getSelectedPeriodoMinimo() {
        return selectedPeriodoMinimo;
    }

    public void setSelectedPeriodoMinimo(int selectedPeriodoMinimo) {
        this.selectedPeriodoMinimo = selectedPeriodoMinimo;
    }

    public Integer getPeriodoMaximo() {
        return periodoMaximo;
    }

    public void setPeriodoMaximo(Integer periodoMaximo) {
        this.periodoMaximo = periodoMaximo;
    }

    public int getPeriodoMinimo() {
        return periodoMinimo;
    }

    public void setPeriodoMinimo(int periodoMinimo) {
        this.periodoMinimo = periodoMinimo;
    }

    /**
     * @return the gMap
     */
    public MapModel getgMap() {
        return gMap;
    }

    /**
     * @param gMap the gMap to set
     */
    public void setgMap(MapModel gMap) {
        this.gMap = gMap;
    }

    /**
     * @return the lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * @param lat the lat to set
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * @return the lng
     */
    public double getLng() {
        return lng;
    }

    /**
     * @param lng the lng to set
     */
    public void setLng(double lng) {
        this.lng = lng;
    }

    
}
