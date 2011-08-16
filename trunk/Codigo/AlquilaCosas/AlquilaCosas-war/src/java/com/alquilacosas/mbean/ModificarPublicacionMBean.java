/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
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
@ManagedBean( name = "modPublicacion" )
@ViewScoped
public class ModificarPublicacionMBean {

    @EJB 
    private PublicacionBeanLocal publicacionBean;
    @EJB 
    private CategoriaBeanLocal categoriaBean;
    @EJB 
    private MisPublicacionesBeanLocal misPublicacionesBean;
    
    @ManagedProperty (value = "#{login}")
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
    //Select Items
    private List<SelectItem> categorias;
    private int selectedCategoria;
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
        
        publicacionId = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("id"));
        
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
        for( ImagenPublicacion ip : imagenes ){
            imagenIds.add(ip.getImagenPublicacionId());
        }
        
        today = new Date();
        categorias = new ArrayList<SelectItem>();
        estados = new ArrayList<SelectItem>();
        
        List<Categoria> listaCategoria = categoriaBean.getCategorias();
        for(Categoria category : listaCategoria) {
           categorias.add(new SelectItem(category.getCategoriaId(), category.getNombre()));
        }
        
        estadosPublicaciones = misPublicacionesBean.getEstados();
        for( EstadoPublicacion ep : estadosPublicaciones ){
            estados.add( new SelectItem( ep.getEstadoPublicacionId(), 
                    ep.getNombre().name() ));
        }
        
        selectedEstado = pf.getEstado().getEstadoPublicacionId();
        
        }
    
    public String actualizarPublicacion(){
        try {
            publicacionBean.actualizarPublicacion(publicacionId, titulo, 
                    descripcion, fechaDesde, fechaHasta, destacada, cantidad, 
                    login.getUsuarioId(), selectedCategoria, precios, imagenesAgregar, 
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
    
    public void handleFileUpload(FileUploadEvent event){
        
        try {

            if( imagenes.size() + imagenesAgregar.size() <  6 ){
                    imagenesAgregar.add(event.getFile().getContents()); 
              }else{
                   throw new AlquilaCosasException("No se puede cargar más de 5 fotos");
            }
      
            FacesMessage msg = new FacesMessage("Excelente", 
                    event.getFile().getFileName() + "fue cargado correctamente");      
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
        } catch (Exception e) {
            
            FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "El archivo no pudo ser cargado!", " ");
            FacesContext.getCurrentInstance().addMessage(null, error);
        }      
       
    }
    
    public void removeImagen( ActionEvent e ){
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

    public CategoriaBeanLocal getCategoriaBean() {
        return categoriaBean;
    }

    public void setCategoriaBean(CategoriaBeanLocal categoriaBean) {
        this.categoriaBean = categoriaBean;
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

    public PublicacionBeanLocal getPublicacionBean() {
        return publicacionBean;
    }

    public void setPublicacionBean(PublicacionBeanLocal publicacionBean) {
        this.publicacionBean = publicacionBean;
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
}
