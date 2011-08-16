/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.common.CategoriaFacade;
import com.alquilacosas.common.PrecioFacade;
import com.alquilacosas.common.PublicacionFacade;
import com.alquilacosas.ejb.entity.Categoria;
import com.alquilacosas.ejb.entity.EstadoPublicacion;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.session.CategoriaBeanLocal;
import com.alquilacosas.ejb.session.MisPublicacionesBeanLocal;
import com.alquilacosas.ejb.session.PublicacionBeanLocal;
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
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author ignaciogiagante
 */
@ManagedBean(name = "modPublicacion")
@ViewScoped
public class ModificarPublicacionMBean {

    @EJB
    private PublicacionBeanLocal publicacionBean;
    @EJB
    private CategoriaBeanLocal categoriaBean;
    @EJB
    private MisPublicacionesBeanLocal misPublicacionesBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean login;
    private PublicacionFacade pf;
    //Datos de la publicacion
    private String titulo;
    private String descripcion;
    private Date fechaDesde;
    private Date fechaHasta;
    private boolean destacada;
    private int cantidad;
    private Categoria categoria;
    //Select Items categorias
    private List<SelectItem> categorias;
    private int selectedCategoria;
    private List<SelectItem> subCategorias;
    private int selectedSubCategoria;
    private List<SelectItem> subCategorias1;
    private int selectedSubCategoria1;
    private boolean subCategoriaRender;
    private boolean subCategoria1Render;
    //estado
    private List<SelectItem> estados;
    private int selectedEstado;
    //Object Precio
    private List<PrecioFacade> precios;
    private PrecioFacade precioFacade;
    private Date today;
    private List<ImagenPublicacion> imagenes;
    private List<byte[]> imagenesAgregar;
    private List<Integer> imagenesABorrar;
    private List<Integer> imagenIds;
    private int publicacionId;
    private List<EstadoPublicacion> estadosPublicaciones;
    private int imagenABorrar;

    public ModificarPublicacionMBean() {
    }

    @PostConstruct
    public void init() {

        publicacionId = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));

        pf = publicacionBean.getDatosPublicacion(publicacionId);

        titulo = pf.getTitulo();
        descripcion = pf.getDescripcion();
        fechaDesde = pf.getFecha_desde();
        fechaHasta = pf.getFecha_hasta();
        destacada = pf.getDestacada();
        cantidad = pf.getCantidad();
        categoria = pf.getCategoria();
        selectedCategoria = categoria.getCategoriaId();
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
        subCategorias = new ArrayList<SelectItem>();
        subCategorias1 = new ArrayList<SelectItem>();
        estados = new ArrayList<SelectItem>();
        
        boolean encontrado = false;
        List<Categoria> listaCategoria = categoriaBean.getCategoriasPrincipal();
        for (Categoria category : listaCategoria) {
            categorias.add(new SelectItem(category.getCategoriaId(), category.getNombre()));
            if(category.getCategoriaId() == selectedCategoria) {
                encontrado = true;
            }
        }
        if(!encontrado) {
        Categoria padreSeleccionada = categoriaBean.getCategoriaPadre(selectedCategoria);
        if(padreSeleccionada != null) {
            List<CategoriaFacade> subcategorias1lista = categoriaBean.getSubCategorias(padreSeleccionada.getCategoriaId());

            Categoria padre2Seleccionada = categoriaBean.getCategoriaPadre(padreSeleccionada.getCategoriaId());
            if(padre2Seleccionada == null) {
                for(CategoriaFacade c: subcategorias1lista) {
                    subCategorias.add(new SelectItem(c.getId(), c.getNombre()));
                }
                selectedSubCategoria = selectedCategoria;
                selectedCategoria = padreSeleccionada.getCategoriaId();
                subCategoriaRender = true;
            }
            else {
                for(CategoriaFacade c: subcategorias1lista) {
                    subCategorias1.add(new SelectItem(c.getId(), c.getNombre()));
                }
                List<CategoriaFacade> subcategorias2lista = categoriaBean.getSubCategorias(padre2Seleccionada.getCategoriaId());
                for(CategoriaFacade c: subcategorias2lista) {
                    subCategorias.add(new SelectItem(c.getId(), c.getNombre()));
                }
                selectedSubCategoria1 = selectedCategoria;
                selectedSubCategoria = padreSeleccionada.getCategoriaId();
                selectedCategoria = padre2Seleccionada.getCategoriaId();
                subCategoria1Render = true;
                subCategoriaRender = true;
            }
        }
        }
        //actualizarCategorias();

        estadosPublicaciones = misPublicacionesBean.getEstados();
        for (EstadoPublicacion ep : estadosPublicaciones) {
            estados.add(new SelectItem(ep.getEstadoPublicacionId(),
                    ep.getNombre().name()));
        }

        selectedEstado = pf.getEstado().getEstadoPublicacionId();

    }

    public String actualizarPublicacion() {
        int cat = 0;
        if (selectedSubCategoria1 > 0) {
            cat = selectedSubCategoria1;
        } else if (selectedSubCategoria > 0) {
            cat = selectedSubCategoria;
        } else {
            cat = selectedCategoria;
        }
        try {
            publicacionBean.actualizarPublicacion(publicacionId, titulo,
                    descripcion, fechaDesde, fechaHasta, destacada, cantidad,
                    login.getUsuarioId(), cat, precios, imagenesAgregar,
                    imagenesABorrar, selectedEstado);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Los datos fueron guardados correctamente"));
            return "misPublicaciones";
        } catch(AlquilaCosasException e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al actualizar usuario", e.getMessage()));
        }
          return null;
    }

    public void actualizarCategorias() {
        subCategorias.clear();
        subCategorias1.clear();
        selectedSubCategoria = 0;
        selectedSubCategoria1 = 0;
        List<CategoriaFacade> categoriaList = categoriaBean.getSubCategorias(selectedCategoria);
        if (!categoriaList.isEmpty()) {
            subCategoriaRender = true;
            for (CategoriaFacade c : categoriaList) {
                subCategorias.add(new SelectItem(c.getId(), c.getNombre()));
            }
        } else {
            subCategoriaRender = false;
            subCategoria1Render = false;
        }
    }

    public void actualizarSubCategorias() {
        subCategorias1.clear();
        selectedSubCategoria1 = 0;
        List<CategoriaFacade> categoriaList = categoriaBean.getSubCategorias(selectedSubCategoria);
        if (!categoriaList.isEmpty()) {
            subCategoria1Render = true;
            for (CategoriaFacade c : categoriaList) {
                subCategorias1.add(new SelectItem(c.getId(), c.getNombre()));
            }
        } else {
            subCategoria1Render = false;
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

    public void removeImagen(ActionEvent e) {
        Integer id = (Integer) e.getComponent().getAttributes().get("idBorrar");
        imagenIds.remove(id);
        imagenesABorrar.add(id);
    }

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

    public PublicacionFacade getPf() {
        return pf;
    }

    public void setPf(PublicacionFacade pf) {
        this.pf = pf;
    }

    public PrecioFacade getPrecioFacade() {
        return precioFacade;
    }

    public void setPrecioFacade(PrecioFacade precioFacade) {
        this.precioFacade = precioFacade;
    }

    public List<PrecioFacade> getPrecios() {
        return precios;
    }

    public void setPrecios(List<PrecioFacade> precios) {
        this.precios = precios;
    }

    public int getSelectedCategoria() {
        return selectedCategoria;
    }

    public void setSelectedCategoria(int selectedCategoria) {
        this.selectedCategoria = selectedCategoria;
    }

    public int getSelectedEstado() {
        return selectedEstado;
    }

    public void setSelectedEstado(int selectedEstado) {
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

    public List<EstadoPublicacion> getEstadosPublicaciones() {
        return estadosPublicaciones;
    }

    public void setEstadosPublicaciones(List<EstadoPublicacion> estadosPublicaciones) {
        this.estadosPublicaciones = estadosPublicaciones;
    }

    public int getSelectedSubCategoria() {
        return selectedSubCategoria;
    }

    public void setSelectedSubCategoria(int selectedSubCategoria) {
        this.selectedSubCategoria = selectedSubCategoria;
    }

    public int getSelectedSubCategoria1() {
        return selectedSubCategoria1;
    }

    public void setSelectedSubCategoria1(int selectedSubCategoria1) {
        this.selectedSubCategoria1 = selectedSubCategoria1;
    }

    public boolean isSubCategoria1Render() {
        return subCategoria1Render;
    }

    public void setSubCategoria1Render(boolean subCategoria1Render) {
        this.subCategoria1Render = subCategoria1Render;
    }

    public boolean isSubCategoriaRender() {
        return subCategoriaRender;
    }

    public void setSubCategoriaRender(boolean subCategoriaRender) {
        this.subCategoriaRender = subCategoriaRender;
    }

    public List<SelectItem> getSubCategorias() {
        return subCategorias;
    }

    public void setSubCategorias(List<SelectItem> subCategorias) {
        this.subCategorias = subCategorias;
    }

    public List<SelectItem> getSubCategorias1() {
        return subCategorias1;
    }

    public void setSubCategorias1(List<SelectItem> subCategorias1) {
        this.subCategorias1 = subCategorias1;
    }
}
