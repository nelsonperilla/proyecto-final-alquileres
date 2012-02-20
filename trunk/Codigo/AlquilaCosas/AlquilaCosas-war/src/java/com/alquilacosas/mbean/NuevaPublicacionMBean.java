package com.alquilacosas.mbean;

/**
 *
 * @author jorge
 */
import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.CategoriaDTO;
import com.alquilacosas.dto.DomicilioDTO;
import com.alquilacosas.dto.PeriodoDTO;
import com.alquilacosas.dto.PrecioDTO;
import com.alquilacosas.ejb.entity.Categoria;
import com.alquilacosas.ejb.entity.Pais;
import com.alquilacosas.ejb.entity.Provincia;
import com.alquilacosas.ejb.session.CategoriaBeanLocal;
import com.alquilacosas.ejb.session.ModificarUsuarioBeanLocal;
import com.alquilacosas.ejb.session.PeriodoAlquilerBeanLocal;
import com.alquilacosas.ejb.session.PublicacionBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import javax.faces.event.ActionEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

@ManagedBean(name = "publicacion")
@ViewScoped
public class NuevaPublicacionMBean implements Serializable {

    @EJB
    private PublicacionBeanLocal publicacionBean;
    @EJB
    private CategoriaBeanLocal categoriaBean;
    @EJB
    private PeriodoAlquilerBeanLocal periodosBean;
    @EJB
    private ModificarUsuarioBeanLocal usuarioBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean login;
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
    //Precios y Periodos
    private List<PrecioDTO> precios;
    private PrecioDTO precioFacade;
    private int periodoMinimo;
    private int periodoMaximo;
    private List<SelectItem> periodoMinimos;
    private int selectedPeriodoMinimo;
    private List<SelectItem> periodoMaximos;
    private Integer selectedPeriodoMaximo;
    private Date today;
    private List<byte[]> imagenes;
    private MapModel gMap;
    private double lat;
    private double lng;
    // Direccion (si no tiene ninguna registrada)
    private boolean pedirDireccion;
    private String calle, barrio, ciudad, depto;
    private Integer numero, piso;
    private int paisSeleccionado, provinciaSeleccionada;
    private List<SelectItem> paises, provincias;
    private Integer publicacionId;

    public NuevaPublicacionMBean() {
    }

    @PostConstruct
    public void init() {
        Logger.getLogger(NuevaPublicacionMBean.class).debug("NuevaPublicacionMBean: postconstruct.");
        imagenes = new ArrayList<byte[]>();
        precios = new ArrayList<PrecioDTO>();
        for (PeriodoDTO p : periodosBean.getPeriodos()) {
            precios.add(new PrecioDTO(0, 0.0, p.getNombre()));
        }
        today = new Date();
        categorias = new ArrayList<SelectItem>();
        subcategorias1 = new ArrayList<SelectItem>();
        subcategorias2 = new ArrayList<SelectItem>();
        subcategorias3 = new ArrayList<SelectItem>();

        periodoMinimos = new ArrayList<SelectItem>();
        periodoMaximos = new ArrayList<SelectItem>();

        List<CategoriaDTO> listaCategoria = categoriaBean.getCategoriasPrincipal();
        for (CategoriaDTO c : listaCategoria) {
            categorias.add(new SelectItem(c.getId(), c.getNombre()));
        }
        List<PeriodoDTO> periodos = periodosBean.getPeriodos();
        for (PeriodoDTO p : periodos) {
            periodoMinimos.add(new SelectItem(p.getId(), p.getNombre().name()));
            periodoMaximos.add(new SelectItem(p.getId(), p.getNombre().name()));
        }
        gMap = new DefaultMapModel();

        pedirDireccion = !login.getUsuario().isDireccionRegistrada();
        if (pedirDireccion) {
            paises = new ArrayList<SelectItem>();
            provincias = new ArrayList<SelectItem>();
            if (paisSeleccionado != 0) {
                actualizarProvincias();
            }

            List<Pais> listaPais = usuarioBean.getPaises();
            if (!listaPais.isEmpty()) {
                for (Pais p : listaPais) {
                    paises.add(new SelectItem(p.getPaisId(), p.getNombre()));
                }
            }
        }
    }

    public String crearPublicacion() {

        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage msg = null;

        if(pedirDireccion) {
            DomicilioDTO dom = new DomicilioDTO(calle, numero, piso, depto, barrio, ciudad);
            dom.setProvinciaId(provinciaSeleccionada);
            usuarioBean.agregarDomicilio(login.getUsuarioId(), dom);
            login.getUsuario().setDireccionRegistrada(true);
        }
        
        int cat = 0;
        if (selectedSubcategoria3 > 0) {
            cat = selectedSubcategoria3;
        } else if (selectedSubcategoria2 > 0) {
            cat = selectedSubcategoria2;
        } else if (selectedSubcategoria1 > 0) {
            cat = selectedSubcategoria1;
        } else {
            cat = selectedCategoria;
        }

        if (precios.get(1).getPrecio() == 0) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al crear publicacion", "El precio por día es obligatorio");
            context.addMessage(null, msg);
            return null;
        }

        try {
            Calendar hoy = Calendar.getInstance();
            hoy.add(Calendar.DATE, 60);
            if (selectedPeriodoMaximo == null) {
                selectedPeriodoMaximo = 0;
            }
            publicacionId = publicacionBean.registrarPublicacion(titulo, descripcion,
                    new Date(), hoy.getTime(), destacada, cantidad,
                    login.getUsuarioId(), cat, precios, imagenes,
                    periodoMinimo, selectedPeriodoMinimo, periodoMaximo,
                    selectedPeriodoMaximo, lat, lng);

            context.addMessage(null,
                    new FacesMessage("Publicacion Creada"));

            return "pdestacarPublicacion";

        } catch (AlquilaCosasException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al crear publicacion", e.getMessage());
            context.addMessage(null, msg);
            return null;
        }

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

    public void addMarker(ActionEvent actionEvent) {
        Marker marker = new Marker(new LatLng(getLat(), getLng()), titulo);
        getgMap().addOverlay(marker);
    }

    public void actualizarProvincias() {
        provincias.clear();
        List<Provincia> provList = usuarioBean.getProvincias(paisSeleccionado);
        if (!provList.isEmpty()) {
            for (Provincia p : provList) {
                provincias.add(new SelectItem(p.getProvinciaId(), p.getNombre()));
            }
        }
    }

    /*
     * Getters & Setters
     */
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

    public int getPeriodoMaximo() {
        return periodoMaximo;
    }

    public void setPeriodoMaximo(int periodoMaximo) {
        this.periodoMaximo = periodoMaximo;
    }

    public int getPeriodoMinimo() {
        return periodoMinimo;
    }

    public void setPeriodoMinimo(int periodoMinimo) {
        this.periodoMinimo = periodoMinimo;
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
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

    public Integer getPublicacionId() {
        return publicacionId;
    }

    public void setPublicacionId(Integer publicacionId) {
        this.publicacionId = publicacionId;
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

    public MapModel getgMap() {
        return gMap;
    }

    public void setgMap(MapModel gMap) {
        this.gMap = gMap;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
        System.out.print(lat + "\n");
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    /*
     * Gettere & Setters de direccion
     */
    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDepto() {
        return depto;
    }

    public void setDepto(String depto) {
        this.depto = depto;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public int getPaisSeleccionado() {
        return paisSeleccionado;
    }

    public void setPaisSeleccionado(int paisSeleccionado) {
        this.paisSeleccionado = paisSeleccionado;
    }

    public boolean isPedirDireccion() {
        return pedirDireccion;
    }

    public void setPedirDireccion(boolean pedirDireccion) {
        this.pedirDireccion = pedirDireccion;
    }

    public Integer getPiso() {
        return piso;
    }

    public void setPiso(Integer piso) {
        this.piso = piso;
    }

    public int getProvinciaSeleccionada() {
        return provinciaSeleccionada;
    }

    public void setProvinciaSeleccionada(int provinciaSeleccionada) {
        this.provinciaSeleccionada = provinciaSeleccionada;
    }

    public List<SelectItem> getPaises() {
        return paises;
    }

    public void setPaises(List<SelectItem> paises) {
        this.paises = paises;
    }

    public List<SelectItem> getProvincias() {
        return provincias;
    }

    public void setProvincias(List<SelectItem> provincias) {
        this.provincias = provincias;
    }
}
