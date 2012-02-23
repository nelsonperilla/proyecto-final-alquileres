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
import org.primefaces.json.JSONObject;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name="alquilarBean")
@ViewScoped
public class AlquilarMBean {
    
    @EJB
    private PublicacionBeanLocal publicationBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean login;
    private PublicacionDTO publicacion;
    private String fecha_hasta;
    private List<Date> fechas;
    private String myJson;
    private Date fechaInicio;
    private int periodoAlquiler;
    private List<SelectItem> periodos;
    private int periodoSeleccionado;
    private Date hoy;
    private int cantidadProductos;
    private double userRating;
    

    @PostConstruct
    public void init() {
        Logger.getLogger(DesplieguePublicacionMBean.class).debug("DesplieguePublicacionMBean: postconstruct.");
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");

        if (id == null) {
            redirect();
            return;
        }
        ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession(true).setAttribute("param", "id=" + id);
        
        int publicationId = 0;
        try {
            publicationId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            Logger.getLogger(DesplieguePublicacionMBean.class).error("Excepcion al parsear ID de parametro.");
            redirect();
            return;
        }
        publicacion = publicationBean.getPublicacion(publicationId);
        
        try {
            fechas = publicationBean.getFechasSinStock(publicationId, cantidadProductos);
        } catch (AlquilaCosasException e) {
            Logger.getLogger(DesplieguePublicacionMBean.class).error("Excepcion al ejecutar getFechasSinStock(): " + e.getMessage());
            redirect();
            return;
        }
        if (publicacion != null && publicacion.getFechaHasta() != null) {
            setFecha_hasta(DateFormat.getDateInstance(DateFormat.SHORT).format(publicacion.getFechaHasta()));
        }
        userRating = publicationBean.getUserRate(publicacion.getPropietario());

        createDictionary();
        cantidadProductos = 1;
        fechaInicio = hoy = new Date();
        periodos = new ArrayList<SelectItem>();
        periodoAlquiler = 1;
        List<Periodo> listaPeriodos = publicationBean.getPeriodos();
        periodoSeleccionado = 2; //alto hardCode, para que por defecto este seleccionado dia y no hora (Jorge)
        for (Periodo periodo : listaPeriodos) {
            periodos.add(new SelectItem(periodo.getPeriodoId(), periodo.getNombre().name()));
        }
    }
    
    public void redirect() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("inicio.xhtml");
        } catch (Exception e) {
            Logger.getLogger(DesplieguePublicacionMBean.class).error("Excepcion al ejecutar redirect().");
        }
    }
    
    public String confirmarPedido() {
        String redireccion = null;
        if (login.getUsuarioId() == publicacion.getPropietario().getId()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "No esta permitido alquilar sus propios productos", ""));
        } else {
            Calendar beginDate = Calendar.getInstance();
            beginDate.setTime(fechaInicio);
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(fechaInicio);

            switch (periodoSeleccionado) {
                case 1: //horas
                    break;
                case 2: //dias
                    endDate.add(Calendar.DATE, periodoAlquiler);
                    break;
                case 3: //semanas
                    endDate.add(Calendar.WEEK_OF_YEAR, periodoAlquiler);
                    break;
                case 4: //meses
                    endDate.add(Calendar.MONTH, periodoAlquiler);
            }

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
                    if (beginDate.before(temp) && endDate.after(temp))//la fecha sin stock cae en el periodo
                    {
                        noStockFlag = true;
                    }
                }
                if (noStockFlag) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Hay fechas sin stock en el periodo seleccionado", ""));
                } else {
                    //calculo el monto
                    double monto = calcularMonto(beginDate, endDate);


                    try {
                        publicationBean.crearPedidoAlquiler(publicacion.getId(),
                                login.getUsuarioId(), beginDate.getTime(),
                                endDate.getTime(), monto, cantidadProductos);
                        redireccion = "pmisPedidosRealizados";
                    } catch (AlquilaCosasException ex) {
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "El pedido de alquiler no se ha concretado, por favor intente de nuevo", ""));
                        Logger.getLogger(AlquilarMBean.class).error("Excepcion al crear pedido de alquiler: " + ex + ": " + ex.getMessage());
                        redireccion = null;
                    }

                }
            }
        }
        return redireccion;
    }

    private double calcularMonto(Calendar beginDate, Calendar endDate) {
        double monto = 0;
        Calendar temp = Calendar.getInstance();
        temp.setTime(beginDate.getTime());
        temp.add(Calendar.MONTH, 1);
        endDate.add(Calendar.SECOND, 1);
        int second = endDate.get(Calendar.SECOND);
        while (endDate.after(temp)) {
            //hardCode muy duro, se el orden porque hice la consulta con orderby
            //si se agregan periodos nuevos corregir esto
            monto += publicacion.getPrecios().get(3).getPrecio();
            temp.add(Calendar.MONTH, 1);
        }

        temp.add(Calendar.MONTH, -1);
        temp.add(Calendar.WEEK_OF_YEAR, 1);
        while (endDate.after(temp)) {
            //hardCode muy duro, se el orden porque hice la consulta con orderby
            //si se agregan periodos nuevos corregir esto
            monto += publicacion.getPrecios().get(2).getPrecio();
            temp.add(Calendar.WEEK_OF_YEAR, 1);


        }

        temp.add(Calendar.WEEK_OF_YEAR, -1);
        temp.add(Calendar.DATE, 1);
        while (endDate.after(temp)) {
            //hardCode muy duro, se el orden porque hice la consulta con orderby
            //si se agregan periodos nuevos corregir esto
            monto += publicacion.getPrecios().get(1).getPrecio();
            temp.add(Calendar.DATE, 1);
        }

        temp.add(Calendar.DATE, -1);
        temp.add(Calendar.HOUR_OF_DAY, 1);
        while (endDate.after(temp)) {
            //hardCode muy duro, se el orden porque hice la consulta con orderby
            //si se agregan periodos nuevos corregir esto
            monto += publicacion.getPrecios().get(0).getPrecio();
            temp.add(Calendar.HOUR_OF_DAY, 1);
        }
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

    public String getFecha_hasta() {
        return fecha_hasta;
    }

    public void setFecha_hasta(String fecha_hasta) {
        this.fecha_hasta = fecha_hasta;
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
    
}
