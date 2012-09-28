/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.PublicidadDTO;
import com.alquilacosas.ejb.entity.TipoPago.NombreTipoPago;
import com.alquilacosas.ejb.entity.TipoPublicidad.DuracionPublicidad;
import com.alquilacosas.ejb.entity.TipoPublicidad.UbicacionPublicidad;
import com.alquilacosas.ejb.session.PagosRecibidosBeanLocal;
import com.alquilacosas.ejb.session.PublicidadBeanLocal;
import com.alquilacosas.pagos.PaypalUtil;
import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.json.JSONObject;

/**
 *
 * @author ignaciogiagante
 */
@ManagedBean(name = "editarMBean")
@ViewScoped
public class ModificarRepublicarPublicidadMBean implements Serializable {

    @EJB
    private PublicidadBeanLocal publicidadBean;
    @EJB
    private PagosRecibidosBeanLocal pagoBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean loginMBean;
    private String titulo, url, caption, descripcion;
    private Double precio;
    private Date fechaDesde;
    private List<SelectItem> duraciones, ubicaciones;
    private UbicacionPublicidad ubicacionSeleccionada;
    private DuracionPublicidad duracionSeleccionada;
    private byte[] imagen;
    private List<Date> fechas;
    private String myJson;
    private Date fechaHasta;
    private Integer publicidadId, imagenId, pagoId;
    private boolean modificar;

    /**
     * Creates a new instance of ModificarRepublicarPublicidadMBean
     */
    public ModificarRepublicarPublicidadMBean() {
    }

    @PostConstruct
    public void init() {
        Logger.getLogger(RegistrarPublicidadMBean.class).info("RegistrarPublicidadMBean: postconstruct.");
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        String accion = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tipo");
        if (accion != null && accion.equals("modificar")) {
            modificar = true;
        }

        if (id != null) {
            publicidadId = Integer.parseInt(id);
            PublicidadDTO publicidad = publicidadBean.getPublicidad(publicidadId);

            this.setTitulo(publicidad.getTitulo());
            this.setCaption(publicidad.getCaption());
            this.setUrl(publicidad.getUrl());
            this.setPrecio(publicidad.getCosto());
            this.setFechaDesde(publicidad.getFechaDesde());
            this.setDuracionSeleccionada(publicidad.getDuracion());
            this.setUbicacionSeleccionada(publicidad.getUbicacion());
            this.setImagen(publicidad.getImagen());
            this.imagenId = publicidadId;
        }

        fechas = new ArrayList<Date>();
        this.createDictionary();

        duraciones = new ArrayList<SelectItem>();
        for (DuracionPublicidad d : DuracionPublicidad.values()) {
            duraciones.add(new SelectItem(d, d.toString()));
        }
        ubicaciones = new ArrayList<SelectItem>();
        for (UbicacionPublicidad u : UbicacionPublicidad.values()) {
            ubicaciones.add(new SelectItem(u, u.toString()));
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        imagen = event.getFile().getContents();
        FacesMessage msg = new FacesMessage(event.getFile().getFileName()
                + "fue cargado correctamente", "");

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public String actualizarPublicidad() {
        String http = "";
        if (imagen == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Debe cargar una imagen", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "";
        }

        try {
            if (!(url.startsWith("http://"))) {
                url = "http://" + url;
            }
            publicidadBean.actualizarPublicidad(publicidadId, titulo, url, caption, imagen);
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "La publicidad no pudo actualizarse", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "";
        }
        return "/vistas/pub/misPublicidades?faces-redirect=true";
    }
    
    public void validarImagen() {
        if (imagen == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Debe cargar una imagen", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            RequestContext.getCurrentInstance().execute("metodoPagoDlg.show()");
        }
    }

    public void registrarPublicidad() {
        if (imagen == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Debe cargar una imagen", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().execute("metodoPagoDlg.hide()");
            return;
        }
        pagoId = null;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaDesde);
            DuracionPublicidad duracion = duracionSeleccionada;
            if (duracion == DuracionPublicidad.SEMANAL) {
                cal.add(Calendar.DAY_OF_YEAR, 7);
                this.setFechaHasta(cal.getTime());
            } else if (duracion == DuracionPublicidad.BISEMANAL) {
                cal.add(Calendar.DAY_OF_YEAR, 14);
                this.setFechaHasta(cal.getTime());
            } else if (duracion == DuracionPublicidad.MENSUAL) {
                cal.add(Calendar.MONTH, 1);
                this.setFechaHasta(cal.getTime());
            } else if (duracion == DuracionPublicidad.BIMENSUAL) {
                cal.add(Calendar.MONTH, 2);
                this.setFechaHasta(cal.getTime());
            }

            Iterator<Date> itFechasSinStock = fechas.iterator();
            boolean noStockFlag = false;
            Calendar fecha = Calendar.getInstance();
            //Recorro la lista de fechas sin stock fijandome si alguna cae
            //en el periodo seleccionado
            while (!noStockFlag && itFechasSinStock.hasNext()) {
                fecha.setTime(itFechasSinStock.next());
                if (fechaDesde.before(fecha.getTime()) && fechaHasta.after(fecha.getTime()))//la fecha sin stock cae en el periodo
                {
                    noStockFlag = true;
                }
            }

            if (noStockFlag) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Hay fechas sin disponibilidad en el periodo seleccionado", ""));
                RequestContext.getCurrentInstance().execute("metodoPagoDlg.hide()");
            } else {

                if (!(url.contains("http://"))) {
                    url = "http://" + url;
                }

                pagoId = publicidadBean.registrarPublicidad(loginMBean.getUsuarioId(),
                        titulo, url, caption, ubicacionSeleccionada,
                        duracionSeleccionada, imagen, fechaDesde, fechaHasta, precio,
                        NombreTipoPago.PAYPAL);
            }

        } catch (AlquilaCosasException e) {
            Logger.getLogger(RegistrarPublicidadMBean.class).
                    error("registrarPublicidad(). Excepcion al invocar registrarPublicidad(): "
                    + e + ": " + e.getMessage());
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al registrar publicidad", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().execute("metodoPagoDlg.hide()");
            return;
        }
        if (pagoId == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al registrar publicidad", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().execute("metodoPagoDlg.hide()");
            return;
        }
        descripcion = "Publicar anuncio: " + titulo;
        String redirectUrl = PaypalUtil.setExpressCheckout(descripcion, Integer.toString(pagoId), null, precio.toString());
        if (redirectUrl != null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(redirectUrl);
            } catch (Exception e) {
                Logger.getLogger(RegistrarPublicidadMBean.class).error("Excepcion al ejecutar redirect().");
            }
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al comunicarse con Paypal", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().execute("metodoPagoDlg.hide()");
        }
    }

    public void registrarPublicidadInmediatamente() {
        if (imagen == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Debe cargar una imagen", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().execute("metodoPagoDlg.hide()");
            return;
        }

        pagoId = null;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaDesde);
            DuracionPublicidad duracion = duracionSeleccionada;
            if (duracion == DuracionPublicidad.SEMANAL) {
                cal.add(Calendar.DAY_OF_YEAR, 7);
                this.setFechaHasta(cal.getTime());
            } else if (duracion == DuracionPublicidad.BISEMANAL) {
                cal.add(Calendar.DAY_OF_YEAR, 14);
                this.setFechaHasta(cal.getTime());
            } else if (duracion == DuracionPublicidad.MENSUAL) {
                cal.add(Calendar.MONTH, 1);
                this.setFechaHasta(cal.getTime());
            } else if (duracion == DuracionPublicidad.BIMENSUAL) {
                cal.add(Calendar.MONTH, 2);
                this.setFechaHasta(cal.getTime());
            }

            Iterator<Date> itFechasSinStock = fechas.iterator();
            boolean noStockFlag = false;
            Calendar fecha = Calendar.getInstance();
            //Recorro la lista de fechas sin stock fijandome si alguna cae
            //en el periodo seleccionado
            while (!noStockFlag && itFechasSinStock.hasNext()) {
                fecha.setTime(itFechasSinStock.next());
                if (fechaDesde.before(fecha.getTime())
                        && fechaHasta.after(fecha.getTime()))//la fecha sin stock cae en el periodo
                {
                    noStockFlag = true;
                }
            }

            if (noStockFlag) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Hay fechas sin disponibilidad en el periodo seleccionado", ""));
                RequestContext.getCurrentInstance().execute("metodoPagoDlg.hide()");
            } else {

                if (!(url.contains("http://"))) {
                    url = "http://" + url;
                }

                pagoId = publicidadBean.registrarPublicidad(loginMBean.getUsuarioId(),
                        titulo, url, caption, ubicacionSeleccionada,
                        duracionSeleccionada, imagen, fechaDesde, fechaHasta, precio,
                        NombreTipoPago.PAYPAL);
            }

        } catch (AlquilaCosasException e) {
            Logger.getLogger(RegistrarPublicidadMBean.class).
                    error("registrarPublicidad(). Excepcion al invocar registrarPublicidad(): "
                    + e + ": " + e.getMessage());
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al registrar publicidad", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().execute("metodoPagoDlg.hide()");
            return;
        }
        if (pagoId == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al registrar publicidad", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().execute("metodoPagoDlg.hide()");
            return;
        }
        pagoBean.confirmarPago(Integer.valueOf(pagoId));
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getApplication().getNavigationHandler().
                    handleNavigation(facesContext, null,
                    "/vistas/pagoConfirmado2?faces-redirect=true");
        } catch (Exception e) {
        }
    }

    /*
     * Registrar una nueva publicidad y su pago asociado, utilizando el metodo
     * de pago DineroMail
     */
    public void registrarPublicidadDm() {
        if (imagen == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Debe cargar una imagen", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().execute("metodoPagoDlg.hide()");
            return;
        }
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaDesde);
            DuracionPublicidad duracion = duracionSeleccionada;
            if (duracion == DuracionPublicidad.SEMANAL) {
                cal.add(Calendar.DAY_OF_YEAR, 7);
                this.setFechaHasta(cal.getTime());
            } else if (duracion == DuracionPublicidad.BISEMANAL) {
                cal.add(Calendar.DAY_OF_YEAR, 14);
                this.setFechaHasta(cal.getTime());
            } else if (duracion == DuracionPublicidad.MENSUAL) {
                cal.add(Calendar.MONTH, 1);
                this.setFechaHasta(cal.getTime());
            } else if (duracion == DuracionPublicidad.BIMENSUAL) {
                cal.add(Calendar.MONTH, 2);
                this.setFechaHasta(cal.getTime());
            }

            Iterator<Date> itFechasSinStock = fechas.iterator();
            boolean noStockFlag = false;
            Calendar fecha = Calendar.getInstance();
            //Recorro la lista de fechas sin stock fijandome si alguna cae
            //en el periodo seleccionado
            while (!noStockFlag && itFechasSinStock.hasNext()) {
                fecha.setTime(itFechasSinStock.next());
                if (fechaDesde.before(fecha.getTime())
                        && fechaHasta.after(fecha.getTime()))//la fecha sin stock cae en el periodo
                {
                    noStockFlag = true;
                }
            }

            if (noStockFlag) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Hay fechas sin disponibilidad en el periodo seleccionado", ""));
                RequestContext.getCurrentInstance().execute("metodoPagoDlg.hide()");
            } else {

                if (!(url.contains("http://"))) {
                    url = "http://" + url;
                }

                pagoId = publicidadBean.registrarPublicidad(loginMBean.getUsuarioId(),
                        titulo, url, caption, ubicacionSeleccionada,
                        duracionSeleccionada, imagen, fechaDesde, fechaHasta, precio,
                        NombreTipoPago.PAYPAL);
            }
        } catch (Exception e) {
        }
        if (pagoId == null || pagoId < 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al registrar pago.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().addCallbackParam("registrado", false);
            return;
        }
        descripcion = "Publicar anuncio: " + titulo;
        RequestContext.getCurrentInstance().addCallbackParam("registrado", true);
    }

    public void duracionCambio() {
        if (duracionSeleccionada != null && ubicacionSeleccionada != null) {
            precio = publicidadBean.getPrecio(duracionSeleccionada, ubicacionSeleccionada);
        } else {
            precio = null;
        }
    }

    public void ubicacionCambio() {
        if (duracionSeleccionada != null && ubicacionSeleccionada != null) {
            precio = publicidadBean.getPrecio(duracionSeleccionada, ubicacionSeleccionada);
            fechas = publicidadBean.getFechasSinDisponibilidad(ubicacionSeleccionada);
            this.createDictionary();
        } else {
            precio = null;
        }
    }

    /*
     * Getters & Setters
     */
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public DuracionPublicidad getDuracionSeleccionada() {
        return duracionSeleccionada;
    }

    public void setDuracionSeleccionada(DuracionPublicidad duracionSeleccionada) {
        this.duracionSeleccionada = duracionSeleccionada;
    }

    public List<SelectItem> getDuraciones() {
        return duraciones;
    }

    public void setDuraciones(List<SelectItem> duraciones) {
        this.duraciones = duraciones;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public UbicacionPublicidad getUbicacionSeleccionada() {
        return ubicacionSeleccionada;
    }

    public void setUbicacionSeleccionada(UbicacionPublicidad ubicacionSeleccionada) {
        this.ubicacionSeleccionada = ubicacionSeleccionada;
    }

    public List<SelectItem> getUbicaciones() {
        return ubicaciones;
    }

    public void setUbicaciones(List<SelectItem> ubicaciones) {
        this.ubicaciones = ubicaciones;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ManejadorUsuarioMBean getLoginMBean() {
        return loginMBean;
    }

    public void setLoginMBean(ManejadorUsuarioMBean loginMBean) {
        this.loginMBean = loginMBean;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public String getFechas() {
        return myJson;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    private void createDictionary() {
        try {
            int month = -1;
            JSONObject yearJson = new JSONObject();
            JSONObject monthJson = new JSONObject();
            JSONObject dayJson = null;
            Calendar cal = Calendar.getInstance();
            for (Date d : fechas) {
                cal.setTime(d);
                if (cal.get(Calendar.MONTH) + 1 != month) {
                    if (dayJson != null) {
                        monthJson.putOpt(Integer.toString(month), dayJson);
                    }
                    dayJson = new JSONObject();
                    month = cal.get(Calendar.MONTH) + 1;
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    dayJson.putOpt(Integer.toString(day), true);
                } else {
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    dayJson.putOpt(Integer.toString(day), true);
                }
            }
            if (dayJson != null) {
                monthJson.putOpt(Integer.toString(month), dayJson);
            }
            int y = cal.get(Calendar.YEAR);
            yearJson.putOpt(Integer.toString(y), monthJson);
            myJson = yearJson.toString();
        } catch (Exception e) {
            //Logger.getLogger(this).error("Exception creating JSON dictionary: " + e);
        }
    }

    public Integer getPublicidadId() {
        return publicidadId;
    }

    public void setPublicidadId(Integer publicidadId) {
        this.publicidadId = publicidadId;
    }

    public boolean getModificar() {
        return modificar;
    }

    public void setModificar(boolean modificar) {
        this.modificar = modificar;
    }

    public Integer getImagenId() {
        return imagenId;
    }

    public void setImagenId(Integer imagenId) {
        this.imagenId = imagenId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getPagoId() {
        return pagoId;
    }

    public void setPagoId(Integer pagoId) {
        this.pagoId = pagoId;
    }
}
