/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.entity.Periodo;
import com.alquilacosas.ejb.session.PublicacionBeanLocal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.json.JSONObject;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "alquilarBean")
@ViewScoped
public class AlquilarMBean {

    @EJB
    private PublicacionBeanLocal publicacionBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean login;
    private PublicacionDTO publicacion;
    private List<Date> fechas;
    private String myJson;
    private Date fechaInicio, fechaDevolucion;
    private Integer publicacionId;
    private int periodoAlquiler;
    private List<SelectItem> periodos;
    private int periodoSeleccionado;
    private Date hoy;
    private int cantidadProductos;
    private double userRating, monto;

    @PostConstruct
    public void init() {
        Logger.getLogger(DesplieguePublicacionMBean.class).debug("DesplieguePublicacionMBean: postconstruct.");
        //String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");

//        if (id == null) {
//            redirect();
//            return;
//        }
//        ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession(true).setAttribute("param", "id=" + id);
//        
//        int publicationId = 0;
//        try {
//            publicationId = Integer.parseInt(id);
//        } catch (NumberFormatException e) {
//            Logger.getLogger(DesplieguePublicacionMBean.class).error("Excepcion al parsear ID de parametro.");
//            redirect();
//            return;
//        }
//        publicacion = publicationBean.getPublicacion(publicationId);

//        try {
//            fechas = publicationBean.getFechasSinStock(publicationId, cantidadProductos);
//        } catch (AlquilaCosasException e) {
//            Logger.getLogger(DesplieguePublicacionMBean.class).error("Excepcion al ejecutar getFechasSinStock(): " + e.getMessage());
//            redirect();
//            return;
//        }
//        if (publicacion != null && publicacion.getFechaHasta() != null) {
//            setFecha_hasta(DateFormat.getDateInstance(DateFormat.SHORT).format(publicacion.getFechaHasta()));
//        }
//        userRating = publicationBean.getUserRate(publicacion.getPropietario());
//
//        createDictionary();
        cantidadProductos = 1;
        fechaInicio = hoy = new Date();
        periodos = new ArrayList<SelectItem>();
        periodoAlquiler = 1;
        List<Periodo> listaPeriodos = publicacionBean.getPeriodos();
        periodoSeleccionado = 2; //alto hardCode, para que por defecto este seleccionado dia y no hora (Jorge)
        for (Periodo periodo : listaPeriodos) {
            periodos.add(new SelectItem(periodo.getPeriodoId(), periodo.getNombre().name()));
        }
    }

    public void cargarPublicacion() {
        if (publicacionId == null) {
            redirect();
            return;
        }
        publicacion = publicacionBean.getPublicacion(publicacionId);
        if (login.getUsuarioId() == publicacion.getPropietario().getId()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "No esta permitido alquilar sus propios productos", ""));
            redirect();
        }
        ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession(true).setAttribute("param", "id=" + publicacionId);
        try {
            fechas = publicacionBean.getFechasSinStock(publicacionId, cantidadProductos);
        } catch (AlquilaCosasException e) {
            Logger.getLogger(DesplieguePublicacionMBean.class).error("Excepcion al ejecutar getFechasSinStock(): " + e.getMessage());
            redirect();
            return;
        }
        userRating = publicacionBean.getUserRate(publicacion.getPropietario());

        createDictionary();
    }

    public void redirect() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("../inicio.xhtml");
        } catch (Exception e) {
            Logger.getLogger(DesplieguePublicacionMBean.class).error("Excepcion al ejecutar redirect().");
        }
    }
    
    public void actualizarMonto(DateSelectEvent event) {
        fechaDevolucion = event.getDate();
        calcularMonto();
    }

    public String confirmarPedido() {
        String redireccion = "";
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTime(fechaInicio);
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(fechaDevolucion);

        long minDuration = calcularDuracion(
                publicacion.getPeriodoMinimo().getPeriodoId(),
                publicacion.getPeriodoMinimoValor());

        long maxDuration;
        try {
            maxDuration = calcularDuracion(
                    publicacion.getPeriodoMaximo().getPeriodoId(),
                    publicacion.getPeriodoMaximoValor());
        } catch (NullPointerException e) {
            //si no hay un periodo maximo, le pongo el mayor valor posible.
            maxDuration = Long.MAX_VALUE;
        }

        long periodoAlquilerEnMillis = endDate.getTimeInMillis() - beginDate.getTimeInMillis();
        if (periodoAlquilerEnMillis < minDuration || periodoAlquilerEnMillis > maxDuration) {
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("El periodo minimo de alquiler es de ");
            mensaje.append(publicacion.getPeriodoMinimoValor());
            mensaje.append(" ");
            mensaje.append(publicacion.getPeriodoMinimo().getNombre());
            mensaje.append("/s ");
            if (maxDuration != Long.MAX_VALUE) {
                mensaje.append("y el maximo de ");
                mensaje.append(publicacion.getPeriodoMaximoValor());
                mensaje.append(" ");
                mensaje.append(publicacion.getPeriodoMaximo().getNombre());
                mensaje.append("/s ");
            }
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    mensaje.toString(), ""));
        } else {
            Iterator<Date> itFechasSinStock = fechas.iterator();
            boolean noStockFlag = false;
            Calendar temp = Calendar.getInstance();
            //Recorro la lista de fechas sin stock fijandome si alguna cae
            //en el periodo seleccionado
            while (!noStockFlag && itFechasSinStock.hasNext()) {
                temp.setTime(itFechasSinStock.next());
                if (beginDate.before(temp) && endDate.after(temp)) {//la fecha sin stock cae en el periodo
                    noStockFlag = true;
                }
            }
            if (noStockFlag) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Hay fechas sin stock en el periodo seleccionado", ""));
            } else {
                //calculo el monto
                monto = calcularMonto();

                try {
                    publicacionBean.crearPedidoAlquiler(publicacion.getId(),
                            login.getUsuarioId(), fechaInicio,
                            fechaDevolucion, monto, cantidadProductos);
                    redireccion = "pmisPedidosRealizados";
                } catch (AlquilaCosasException ex) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Ha ocurrido un error al registrar pedido.", ex.getMessage()));
                    Logger.getLogger(AlquilarMBean.class).error(
                            "Excepcion al crear pedido de alquiler: " + ex + ": " + ex.getMessage());
                }
            }
        }
        return redireccion;
    }

    private double calcularMonto() {
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(fechaDevolucion);
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTime(fechaInicio);
        monto = 0D;
        Calendar temp = Calendar.getInstance();
        temp.setTime(beginDate.getTime());
        temp.add(Calendar.MONTH, 1);
        endDate.add(Calendar.SECOND, 1);
        int second = endDate.get(Calendar.SECOND);
        while (endDate.after(temp) || endDate.equals(temp)) {
            if(publicacion.getPrecioMes() != null) {
                monto += publicacion.getPrecioMes();
            } else if(publicacion.getPrecioSemana() != null) {
                monto += publicacion.getPrecioSemana() * 4;
            } else {
                monto += publicacion.getPrecioDia() * 30;
            }
            temp.add(Calendar.MONTH, 1);
        }

        temp.add(Calendar.MONTH, -1);
        temp.add(Calendar.DATE, 7);
        while (endDate.after(temp) || endDate.equals(temp)) {
            if(publicacion.getPrecioSemana() != null) {
                monto += publicacion.getPrecioSemana();
            } else {
                monto += publicacion.getPrecioDia() * 7;
            }
            temp.add(Calendar.WEEK_OF_YEAR, 1);
        }

        temp.add(Calendar.DATE, -7);
        temp.add(Calendar.DATE, 1);
        while (endDate.after(temp) || endDate.equals(temp)) {
            monto += publicacion.getPrecioDia();
            temp.add(Calendar.DATE, 1);
        }
        temp.add(Calendar.DATE, -1);
        temp.add(Calendar.HOUR_OF_DAY, 1);
        double precioHoras = 0D;
        while (endDate.after(temp) || endDate.equals(temp)) {
            temp.add(Calendar.HOUR_OF_DAY, 1);
            if(publicacion.getPrecioHora() != null) {
                precioHoras = publicacion.getPrecioHora();
            } else {
                precioHoras = publicacion.getPrecioDia();
                break;
            }
        }
        // evitar que el precio de horas sea superior al precio de un dia.
        if(precioHoras > publicacion.getPrecioDia()) {
            precioHoras = publicacion.getPrecioDia();
        }
        monto += precioHoras;
        temp.add(Calendar.HOUR_OF_DAY, -1);
        endDate.add(Calendar.SECOND, -1);
        return monto;
    }

    private long calcularDuracion(int periodoId, long periodo) {
        switch (periodoId) {
            case 1: //horas
                periodo *= 60 * 60 * 1000;
                break;
            case 2: //dias
                periodo *= 24 * 60 * 60 * 1000;
                break;
            case 3: //semanas
                periodo *= 7 * 24 * 60 * 60 * 1000;
                break;
            case 4: //meses
                Date now = new Date();
                Calendar temp = Calendar.getInstance();
                temp.setTime(now);
                temp.add(Calendar.MONTH, (int) periodo);
                periodo = temp.getTimeInMillis() - now.getTime();
        }
        return periodo;
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

    /* Getters & Setters */
    public String getFechas() {
        return myJson;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getMyJson() {
        return myJson;
    }

    public void setMyJson(String myJson) {
        this.myJson = myJson;
    }

    public int getPeriodoAlquiler() {
        return periodoAlquiler;
    }

    public void setPeriodoAlquiler(int periodoAlquiler) {
        this.periodoAlquiler = periodoAlquiler;
    }

    public int getPeriodoSeleccionado() {
        return periodoSeleccionado;
    }

    public void setPeriodoSeleccionado(int periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
    }

    public List<SelectItem> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<SelectItem> periodos) {
        this.periodos = periodos;
    }

    public PublicacionDTO getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(PublicacionDTO publicacion) {
        this.publicacion = publicacion;
    }

    public Date getHoy() {
        return hoy;
    }

    public void setHoy(Date hoy) {
        this.hoy = hoy;
    }

    public ManejadorUsuarioMBean getLogin() {
        return login;
    }

    public void setLogin(ManejadorUsuarioMBean login) {
        this.login = login;
    }

    public int getCantidadProductos() {
        return cantidadProductos;
    }

    public void setCantidadProductos(int cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public Integer getPublicacionId() {
        return publicacionId;
    }

    public void setPublicacionId(Integer publicacionId) {
        this.publicacionId = publicacionId;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}
