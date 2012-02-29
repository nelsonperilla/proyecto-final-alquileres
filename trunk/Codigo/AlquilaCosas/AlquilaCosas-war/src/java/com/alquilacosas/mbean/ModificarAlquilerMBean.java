/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.session.AlquileresBeanLocal;
import com.alquilacosas.ejb.session.PublicacionBeanLocal;
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
import org.apache.log4j.Logger;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.json.JSONObject;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name="modAlquilerBean")
@ViewScoped
public class ModificarAlquilerMBean {

    @EJB
    private AlquileresBeanLocal alquileresBean;
    @EJB
    private PublicacionBeanLocal publicacionBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean loginBean;
    private Integer usuarioLogueado, alquilerId;
    private AlquilerDTO alquiler;
    private PublicacionDTO publicacion;
    private Date fechaHasta;
    private String myJson;
    private List<Date> fechas;
    private boolean tomado;
    private double nuevoMonto;
    
    /** Creates a new instance of ModificarAlquilerMBean */
    public ModificarAlquilerMBean() {
    }
    
    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        try {
            alquilerId = Integer.valueOf(id);
        } catch (Exception e) {
            redirect();
            return;
        }
        usuarioLogueado = loginBean.getUsuarioId();
        alquiler = alquileresBean.getAlquiler(usuarioLogueado, alquilerId);
        publicacion = publicacionBean.getPublicacion(alquiler.getIdPublicacion());
        fechaHasta = alquiler.getFechaFin();
        fechas = alquileresBean.getFechasSinStock(alquilerId);
        createDictionary();
        tomado = alquiler.isTomado();
        nuevoMonto = alquiler.getMonto();
    }
    
    public void redirect() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("articulos.xhtml");
        } catch (Exception e) {
            Logger.getLogger(ModificarAlquilerMBean.class).error("Excepcion al ejecutar redirect().");
        }
    }
    
    public void actualizarMonto(DateSelectEvent event) {
        fechaHasta = event.getDate();
        nuevoMonto = calcularMonto();
    }
    
    public String modificarAlquiler() {
        FacesMessage msg = null;
        FacesContext context = FacesContext.getCurrentInstance();
        // Si la duracion ingresada es 0, no permitir
        if(fechaHasta.equals(alquiler.getFechaFin())) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "No ha modificado la duracion del alquiler", "");
            context.addMessage(null, msg);
            return "";
        }
        
        // revisar si la nueva duracion no hace que el alquiler se solape
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTime(alquiler.getFechaInicio());
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(fechaHasta);

        Iterator<Date> itFechasSinStock = fechas.iterator();
        boolean noStockFlag = false;
        Calendar temp = Calendar.getInstance();
        //Recorro la lista de fechas sin stock fijandome si alguna cae en el periodo seleccionado
        while (!noStockFlag && itFechasSinStock.hasNext()) {
            temp.setTime(itFechasSinStock.next());
            if (beginDate.before(temp) && endDate.after(temp)) {//la fecha sin stock cae en el periodo
                noStockFlag = true;
            }
        }
        if (noStockFlag) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar alquiler", "Hay fechas sin stock en el periodo seleccionado");
            context.addMessage(null, msg);
            return "";
        }
        nuevoMonto = calcularMonto();
        try {
            alquiler = alquileresBean.modificarAlquiler(alquiler, fechaHasta, nuevoMonto);
        } catch (AlquilaCosasException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar alquiler", e.getMessage());
            context.addMessage(null, msg);
            return "";
        }
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alquiler modificado", "");
        context.addMessage(null, msg);
        return "/vistas/usuario/articulos";
    }
    
    public String solicitarCambio() {
        FacesMessage msg = null;
        FacesContext context = FacesContext.getCurrentInstance();
        // Si la duracion ingresada es 0, no permitir
        if(alquiler.getFechaFin().equals(fechaHasta)) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, 
                    "No ha modificado la duracion del alquiler", "");
            context.addMessage(null, msg); 
            return "";
        }
        // revisar si la nueva duracion no hace que el alquiler se solape
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTime(alquiler.getFechaInicio());
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(fechaHasta);
        
        Iterator<Date> itFechasSinStock = fechas.iterator();
        boolean noStockFlag = false;
        Calendar temp = Calendar.getInstance();
        //Recorro la lista de fechas sin stock fijandome si alguna cae en el periodo seleccionado
        while(!noStockFlag && itFechasSinStock.hasNext()){
            temp.setTime(itFechasSinStock.next());
            if(beginDate.before(temp) && endDate.after(temp))//la fecha sin stock cae en el periodo
                noStockFlag = true;
        }
        if(noStockFlag) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al solicitar cambio de alquiler", 
                    "Hay fechas sin stock en el periodo seleccionado");
            context.addMessage(null, msg);
            return "";
        }
        try {
            alquileresBean.solicitarCambioAlquiler(alquilerId, fechaHasta);
        } catch (AlquilaCosasException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al solicitar cambio de alquiler", e.getMessage());
            context.addMessage(null, msg);
            return "";
        }
        return  "/vistas/usuario/articulos";
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
            Logger.getLogger(ModificarAlquilerMBean.class).error("Exception creating JSON dictionary: " + e);
        }
    }
    
    private double calcularMonto() {
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(fechaHasta);
        nuevoMonto = 0D;
        Calendar temp = Calendar.getInstance();
        temp.setTime(alquiler.getFechaInicio());
        temp.add(Calendar.MONTH, 1);
        endDate.add(Calendar.SECOND, 1);
        int second = endDate.get(Calendar.SECOND);
        while (endDate.after(temp) || endDate.equals(temp)) {
            if(publicacion.getPrecioMes() != null) {
                nuevoMonto += publicacion.getPrecioMes();
            } else if(publicacion.getPrecioSemana() != null) {
                nuevoMonto += publicacion.getPrecioSemana() * 4;
            } else {
                nuevoMonto += publicacion.getPrecioDia() * 30;
            }
            temp.add(Calendar.MONTH, 1);
        }

        temp.add(Calendar.MONTH, -1);
        temp.add(Calendar.DATE, 7);
        while (endDate.after(temp) || endDate.equals(temp)) {
            if(publicacion.getPrecioSemana() != null) {
                nuevoMonto += publicacion.getPrecioSemana();
            } else {
                nuevoMonto += publicacion.getPrecioDia() * 7;
            }
            temp.add(Calendar.WEEK_OF_YEAR, 1);
        }

        temp.add(Calendar.DATE, -7);
        temp.add(Calendar.DATE, 1);
        while (endDate.after(temp) || endDate.equals(temp)) {
            nuevoMonto += publicacion.getPrecioDia();
            temp.add(Calendar.DATE, 1);
        }
        temp.add(Calendar.DATE, -1);
        temp.add(Calendar.HOUR_OF_DAY, 1);
        double precioHoras = 0D;
        while (endDate.after(temp) || endDate.equals(temp)) {
            temp.add(Calendar.HOUR_OF_DAY, 1);
            if(publicacion.getPrecioHora() != null) {
                precioHoras += publicacion.getPrecioHora();
            } else {
                precioHoras = publicacion.getPrecioDia();
                break;
            }
        }
        // evitar que el precio de horas sea superior al precio de un dia.
        if(precioHoras > publicacion.getPrecioDia()) {
            precioHoras = publicacion.getPrecioDia();
        }
        nuevoMonto += precioHoras;
        temp.add(Calendar.HOUR_OF_DAY, -1);
        endDate.add(Calendar.SECOND, -1);
        return nuevoMonto;
    }
    
    /*
     * Getters & Setters
     */

    public AlquilerDTO getAlquiler() {
        return alquiler;
    }

    public void setAlquiler(AlquilerDTO alquiler) {
        this.alquiler = alquiler;
    }

    public Integer getAlquilerId() {
        return alquilerId;
    }

    public void setAlquilerId(Integer alquilerId) {
        this.alquilerId = alquilerId;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public String getMyJson() {
        return myJson;
    }

    public void setMyJson(String myJson) {
        this.myJson = myJson;
    }

    public PublicacionDTO getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(PublicacionDTO publicacion) {
        this.publicacion = publicacion;
    }

    public ManejadorUsuarioMBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(ManejadorUsuarioMBean loginBean) {
        this.loginBean = loginBean;
    }

    public boolean isTomado() {
        return tomado;
    }

    public void setTomado(boolean tomado) {
        this.tomado = tomado;
    }

    public double getNuevoMonto() {
        return nuevoMonto;
    }

    public void setNuevoMonto(double nuevoMonto) {
        this.nuevoMonto = nuevoMonto;
    }
    
}
