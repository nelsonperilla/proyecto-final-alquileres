/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.ComentarioDTO;
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
import org.primefaces.context.RequestContext;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.json.JSONObject;

/**
 *
 * @author jorge
 */
@ManagedBean(name = "publicacionDesplegada")
@ViewScoped
public class DesplieguePublicacionMBean {

    @EJB
    private PublicacionBeanLocal publicationBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean usuarioLogueado;
    private PublicacionDTO publicacion;
    private String effect;
    private List<ComentarioDTO> comentarios;
    private String  comentario;
    private String fecha_hasta;
    private List<Date> fechas;
    private String myJson;
    private Date fechaInicio;
    private int periodoAlquiler;
    private List<SelectItem> periodos;
    private int periodoSeleccionado;    
    private Date today;
    private ComentarioDTO nuevaPregunta;
    private int cantidadProductos;
    private String action;

    /** Creates a new instance of DesplieguePublicacionMBean */
    public DesplieguePublicacionMBean() {
    }

    @PostConstruct
    public void init() {
        cantidadProductos = 1;
        fechaInicio = new Date();
        effect = "fade";
        today = new Date(); 
        periodos = new ArrayList<SelectItem>();
        List<Periodo> listaPeriodos = publicationBean.getPeriodos();
        periodoSeleccionado = 2; //alto hardCode, para que por defecto este seleccionado dia y no hora (Jorge)
        for(Periodo periodo: listaPeriodos)
            periodos.add(new SelectItem(periodo.getPeriodoId(),periodo.getNombre().name()));
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        setNuevaPregunta(new ComentarioDTO());
        if (id != null) {
            int publicationId = Integer.parseInt(id);
            setPublicacion(publicationBean.getPublicacion(publicationId));
            setComentarios(publicationBean.getPreguntas(publicationId));
            fechas = publicationBean.getFechasSinStock(publicationId,cantidadProductos);
            
            if (publicacion != null && publicacion.getFecha_hasta() != null) {
                setFecha_hasta(DateFormat.getDateInstance(DateFormat.SHORT).format(publicacion.getFecha_hasta()));
            }

        }
        createDictionary();
    }

    public void actualizarPreguntas() {
        comentarios = publicationBean.getPreguntas(publicacion.getId());
    }

    public void actualizarFechas()
    {
        boolean disponibilidad = true;
        if(cantidadProductos > publicacion.getCantidad())
        {
            disponibilidad = false;
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "La disponibilidad maxima es de " + publicacion.getCantidad() + " producto/s","" ));
        }
        else
            fechas = publicationBean.getFechasSinStock(publicacion.getId(),cantidadProductos);
        
        boolean logueado = usuarioLogueado.isLogueado();
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("logueado", logueado);
        context.addCallbackParam("hayDisponibilidad", disponibilidad);
        action = "alquilar";
    }
    
    public void preguntar() {
        boolean logueado = usuarioLogueado.isLogueado();
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("logueado", logueado);
        action = "preguntar";
    }
    
    public String guardarPregunta() {
        getNuevaPregunta().setUsuarioId(usuarioLogueado.getUsuarioId());
        getNuevaPregunta().setUsuario(usuarioLogueado.getUsername());
        getNuevaPregunta().setFecha(new Date());
        try {
            publicationBean.setPregunta(publicacion.getId(), getNuevaPregunta());
            setNuevaPregunta(new ComentarioDTO());
            //actualizarPreguntas();
        } catch (AlquilaCosasException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al enviar pregunta", e.getMessage()));
        }
        return null;
    }
    
    public void confirmarPedido()
    {
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTime(fechaInicio);
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(fechaInicio);
        
        switch (periodoSeleccionado)
        {
            case 1: //horas
                endDate.add(Calendar.HOUR_OF_DAY, periodoAlquiler);
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

        long minDuration = publicacion.getPeriodoMinimoValor();
        long maxDuration = publicacion.getPeriodoMaximoValor();
        
        switch (publicacion.getPeriodoMinimo().getPeriodoId())
        {
            case 1: //horas
                minDuration *= 60 * 60 * 1000;
                break;
            case 2: //dias
                minDuration *= 24 * 60 * 60 * 1000;
                break;
            case 3: //semanas
                minDuration *= 7 * 24 * 60 * 60 * 1000;
                break;
            case 4: //meses
                Date now = new Date();
                Calendar temp = Calendar.getInstance();
                temp.setTime(now);
                temp.add(Calendar.MONTH,(int)minDuration);
                minDuration = temp.getTimeInMillis() - now.getTime();
        }

        switch (publicacion.getPeriodoMaximo().getPeriodoId())
        {
            case 1: //horas
                maxDuration *= 60 * 60 * 1000;
                break;
            case 2: //dias
                maxDuration *= 24 * 60 * 60 * 1000;
                break;
            case 3: //semanas
                maxDuration *= 7 * 24 * 60 * 60 * 1000;
                break;
            case 4: //meses
                Date now = new Date();
                Calendar temp = Calendar.getInstance();
                temp.setTime(now);
                temp.add(Calendar.MONTH,(int)maxDuration);
                maxDuration = temp.getTimeInMillis() - now.getTime();
        }
        
        
        long periodoAlquilerEnMillis = endDate.getTimeInMillis() - beginDate.getTimeInMillis();
        if(periodoAlquilerEnMillis < minDuration || periodoAlquilerEnMillis > maxDuration )
            ;//mensaje de error, no respeta los periodos minimos o maximos
        else{
            Iterator<Date> itFechasSinStock = fechas.iterator();
            boolean noStockFlag = false;
            Calendar temp = Calendar.getInstance();
            while(!noStockFlag && itFechasSinStock.hasNext()){
                temp.setTime(itFechasSinStock.next());
                if( beginDate.before(temp) && endDate.after(temp))//la fecha sin stock cae en el periodo
                    noStockFlag = true;
            }
            if(noStockFlag)
                ;//mensaje de error, cae una fecha sin stock en el periodo seleccionado
            else{
                //calculo el monto
                double monto = 0;
                temp.setTime(beginDate.getTime());
                temp.add(Calendar.MONTH, 1);
                endDate.add(Calendar.SECOND, 1);
                while(endDate.after(temp)){
                    //hardCode muy duro, se el orden porque hice la consulta con orderby
                    //si se agregan periodos nuevos corregir esto
                    monto+= publicacion.getPrecios().get(3).getPrecio();
                    temp.add(Calendar.MONTH, 1);
                }
                
                temp.add(Calendar.MONTH, -1);
                temp.add(Calendar.WEEK_OF_YEAR, 1);
                while(endDate.after(temp)){
                    //hardCode muy duro, se el orden porque hice la consulta con orderby
                    //si se agregan periodos nuevos corregir esto
                    monto+= publicacion.getPrecios().get(2).getPrecio();
                    temp.add(Calendar.WEEK_OF_YEAR, 1);
                }
                
                temp.add(Calendar.WEEK_OF_YEAR, -1);
                temp.add(Calendar.DATE, 1);
                while(endDate.after(temp)){
                    //hardCode muy duro, se el orden porque hice la consulta con orderby
                    //si se agregan periodos nuevos corregir esto
                    monto+= publicacion.getPrecios().get(1).getPrecio();
                    temp.add(Calendar.DATE, 1);
                }
                
                temp.add(Calendar.DATE, -1);
                temp.add(Calendar.HOUR_OF_DAY, 1);
                while(endDate.after(temp)){
                    //hardCode muy duro, se el orden porque hice la consulta con orderby
                    //si se agregan periodos nuevos corregir esto
                    monto+= publicacion.getPrecios().get(0).getPrecio();
                    temp.add(Calendar.HOUR_OF_DAY, 1);
                }
                    
                endDate.add(Calendar.SECOND, -1);
                publicationBean.crearPedidoAlquiler(publicacion.getId(), 
                        usuarioLogueado.getUsuarioId(), beginDate.getTime(), 
                        endDate.getTime(), monto, cantidadProductos);

            }
        }
            

    }
    
    private void createDictionary() {
        try {      
            int month = -1;
            JSONObject yearJson = new JSONObject();
            JSONObject monthJson = new JSONObject();
            JSONObject dayJson = null;
            Calendar cal = Calendar.getInstance();
            for(Date d: fechas) {
                cal.setTime(d);
                if(cal.get(Calendar.MONTH) + 1 != month) {
                    if(dayJson != null) {
                        monthJson.putOpt(Integer.toString(month), dayJson);
                    }
                    dayJson = new JSONObject();
                    month = cal.get(Calendar.MONTH) + 1;
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    dayJson.putOpt(Integer.toString(day), true);
                }
                else {
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    dayJson.putOpt(Integer.toString(day), true);
                }
            }
            if(dayJson != null) {
                monthJson.putOpt(Integer.toString(month), dayJson);
            }
            int y = cal.get(Calendar.YEAR);
            yearJson.putOpt(Integer.toString(y), monthJson);
            myJson = yearJson.toString();
        } catch (Exception e) {
            //Logger.getLogger(this).error("Exception creating JSON dictionary: " + e);
        }
    }

    public String getFechas() {
       return myJson;
    }   
    
    public String seleccionarFecha(DateSelectEvent e)
    {
              fechaInicio = e.getDate();
              return "";
//        try {
//            absenceDetails = repEJB.readDayAbsenceDetail(sessionId, date); //se cargan los datos que queres mostrar en el dialogo
//        } catch (EraServiceException ex) {
//            Logger.getLogger(ReportsBean.class).error("Exception in dateSelected: " + ex);
//            absenceDetails = new ArrayList<AbsenceDetail>();
//        }  
    }

    /**
     * @return the comentarios
     */
    public List<ComentarioDTO> getComentarios() {
        return comentarios;
    }

    /**
     * @param comentarios the comentarios to set
     */
    public void setComentarios(List<ComentarioDTO> comentarios) {
        this.comentarios = comentarios;
    }

    /**
     * @return the publicacion
     */
    public PublicacionDTO getPublicacion() {
        return publicacion;
    }

    /**
     * @param publicacion the publicacion to set
     */
    public void setPublicacion(PublicacionDTO publicacion) {
        this.publicacion = publicacion;
    }

    /**
     * @return the effect
     */
    public String getEffect() {
        return effect;
    }

    /**
     * @param effect the effect to set
     */
    public void setEffect(String effect) {
        this.effect = effect;
    }

    /**
     * @return the usuarioLogueado
     */
    public ManejadorUsuarioMBean getUsuarioLogueado() {
        return usuarioLogueado;
    }

    /**
     * @param usuarioLogueado the usuarioLogueado to set
     */
    public void setUsuarioLogueado(ManejadorUsuarioMBean usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    /**
     * @return the fecha_hasta
     */
    public String getFecha_hasta() {
        return fecha_hasta;
    }

    /**
     * @param fecha_hasta the fecha_hasta to set
     */
    public void setFecha_hasta(String fecha_hasta) {
        this.fecha_hasta = fecha_hasta;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    /**
     * @return the fechaInicio
     */
    public Date getFechaInicio() {
        return fechaInicio;
    }

    /**
     * @param fechaInicio the fechaInicio to set
     */
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * @return the periodoAlquiler
     */
    public int getPeriodoAlquiler() {
        return periodoAlquiler;
    }

    /**
     * @param periodoAlquiler the periodoAlquiler to set
     */
    public void setPeriodoAlquiler(int periodoAlquiler) {
        this.periodoAlquiler = periodoAlquiler;
    }

    /**
     * @return the periodos
     */
    public List<SelectItem> getPeriodos() {
        return periodos;
    }

    /**
     * @param periodos the periodos to set
     */
    public void setPeriodos(List<SelectItem> periodos) {
        this.periodos = periodos;
    }

    /**
     * @return the periodoSeleccionado
     */
    public int getPeriodoSeleccionado() {
        return periodoSeleccionado;
    }

    /**
     * @param periodoSeleccionado the periodoSeleccionado to set
     */
    public void setPeriodoSeleccionado(int periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
    }

    /**
     * @return the today
     */
    public Date getToday() {
        return today;
    }

    /**
     * @param today the today to set
     */
    public void setToday(Date today) {
        this.today = today;
    }

    /**
     * @return the nuevaPregunta
     */
    public ComentarioDTO getNuevaPregunta() {
        return nuevaPregunta;
    }

    /**
     * @param nuevaPregunta the nuevaPregunta to set
     */
    public void setNuevaPregunta(ComentarioDTO nuevaPregunta) {
        this.nuevaPregunta = nuevaPregunta;
    }

    /**
     * @return the cantidadProductos
     */
    public int getCantidadProductos() {
        return cantidadProductos;
    }

    /**
     * @param cantidadProductos the cantidadProductos to set
     */
    public void setCantidadProductos(int cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }
}
