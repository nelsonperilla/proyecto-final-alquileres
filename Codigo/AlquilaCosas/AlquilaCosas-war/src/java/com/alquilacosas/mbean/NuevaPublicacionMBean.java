package com.alquilacosas.mbean;

/**
 *
 * @author jorge
 */
import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.CategoriaDTO;
import com.alquilacosas.dto.PrecioDTO;
import com.alquilacosas.ejb.entity.Domicilio;
import com.alquilacosas.ejb.entity.Periodo;
import com.alquilacosas.ejb.entity.Usuario;
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
import javax.faces.model.SelectItem;
import org.primefaces.event.FileUploadEvent;

@ManagedBean(name = "publicacion")
@ViewScoped
public class NuevaPublicacionMBean implements Serializable {

    public NuevaPublicacionMBean() {
    }
    @EJB
    private PublicacionBeanLocal publicacionBean;
    @EJB
    private CategoriaBeanLocal categoriaBean;
    @EJB
    private PeriodoAlquilerBeanLocal periodosBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean login;
    //Datos de la publicacion
    private String titulo;
    private String descripcion;
    private Date fechaDesde;
    private Date fechaHasta;
    private boolean destacada;
    private int cantidad;
    //Select Items
    private List<SelectItem> categorias;
    private int selectedCategoria;
    private List<SelectItem> subCategorias;
    private int selectedSubCategoria;
    private List<SelectItem> subCategorias1;
    private int selectedSubCategoria1;
    private boolean subCategoriaRender;
    private boolean subCategoria1Render;
    //Object Precio
    private List<PrecioDTO> precios;
    private PrecioDTO precioFacade;
    private Date today;
    private List<byte[]> imagenes;
    private Usuario usuario;
    private Domicilio domicilio;

    @PostConstruct
    public void init() {

        imagenes = new ArrayList<byte[]>();
        precios = new ArrayList<PrecioDTO>();
        for(Periodo p: periodosBean.getPeriodos()) {
            precios.add(new PrecioDTO(0, 0.0, p.getNombre()));
        }
        today = new Date();
        categorias = new ArrayList<SelectItem>();
        subCategorias = new ArrayList<SelectItem>();
        subCategorias1 = new ArrayList<SelectItem>();
        subCategoriaRender = false;
        subCategoria1Render = false;
        List<CategoriaDTO> listaCategoria = categoriaBean.getCategoriasPrincipal();
        for (CategoriaDTO categoria : listaCategoria) {
            categorias.add(new SelectItem(categoria.getId(), categoria.getNombre()));
        }

    }

    public String crearPublicacion() {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage msg = null;

        if (precios.get(1).getPrecio() == 0) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al crear publicacion", "El precio por día es obligatorio");
            return null;
        }

        int categoria = 0;
        if (selectedSubCategoria1 > 0) {
            categoria = selectedSubCategoria1;
        } else if (selectedSubCategoria > 0) {
            categoria = selectedSubCategoria;
        } else {
            categoria = selectedCategoria;
        }
        try {
            publicacionBean.registrarPublicacion(titulo, descripcion,
                    new Date(), new Date(), destacada, cantidad,
                    login.getUsuarioId(), categoria, precios, imagenes);

            context.addMessage(null,
                    new FacesMessage("Publicacion Creada"));

            return "misPublicaciones";

        } catch (AlquilaCosasException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al crear publicacion", e.getMessage());
            context.addMessage(null, msg);
            return null;
        }

    }

    public void handleFileUpload(FileUploadEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage msg = null;
        try {
            if (imagenes.size() < 6) {
                byte[] img = event.getFile().getContents();
                imagenes.add(img);
                msg = new FacesMessage("Excelente",
                        event.getFile().getFileName() + "fue cargado correctamente");

                context.addMessage(null, msg);
            } else {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error al cargar imagen",
                        "Se permiten hasta 5 imagenes por publicacion.");
                context.addMessage(null, msg);
            }
        } catch (Exception e) {

            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "El archivo no pudo ser cargado!", e.getMessage());
            context.addMessage(null, msg);
        }

    }

    public void actualizarCategorias() {
        subCategorias.clear();
        subCategorias1.clear();
        selectedSubCategoria = 0;
        selectedSubCategoria1 = 0;
        List<CategoriaDTO> categoriaList = categoriaBean.getSubCategorias(selectedCategoria);
        if (!categoriaList.isEmpty()) {
            subCategoriaRender = true;
            for (CategoriaDTO c : categoriaList) {
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
        List<CategoriaDTO> categoriaList = categoriaBean.getSubCategorias(selectedSubCategoria);
        if (!categoriaList.isEmpty()) {
            subCategoria1Render = true;
            for (CategoriaDTO c : categoriaList) {
                subCategorias1.add(new SelectItem(c.getId(), c.getNombre()));
            }
        } else {
            subCategoria1Render = false;
        }
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
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

    public List<byte[]> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<byte[]> imagenes) {
        this.imagenes = imagenes;
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

    public ManejadorUsuarioMBean getLogin() {
        return login;
    }

    public void setLogin(ManejadorUsuarioMBean login) {
        this.login = login;
    }

    public int getSelectedSubcategoria() {
        return selectedSubCategoria;
    }

    public void setSelectedSubcategoria(int selectedSubcategoria) {
        this.selectedSubCategoria = selectedSubcategoria;
    }

    public List<SelectItem> getSubCategorias() {
        return subCategorias;
    }

    public void setSubCategorias(List<SelectItem> subCategorias) {
        this.subCategorias = subCategorias;
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

    public List<SelectItem> getSubCategorias1() {
        return subCategorias1;
    }

    public void setSubCategorias1(List<SelectItem> subCategorias1) {
        this.subCategorias1 = subCategorias1;
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
}
