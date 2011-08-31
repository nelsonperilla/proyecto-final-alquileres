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
import java.util.List;
import java.util.logging.Logger;
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

    /** Creates a new instance of DesplieguePublicacionMBean */
    public DesplieguePublicacionMBean() {
    }

    @PostConstruct
    public void init() {
        effect = "fade";
        fechas = new ArrayList<Date>();
        Calendar test =Calendar.getInstance();
        test.set(2011,7,29);
        fechas.add(test.getTime());
        createDictionary();
        fechaInicio = new Date();
        
        
        periodos = new ArrayList<SelectItem>();
        List<Periodo> listaPeriodos = publicationBean.getPeriodos();
        for(Periodo periodo: listaPeriodos)
            periodos.add(new SelectItem(periodo.getPeriodoId(),periodo.getNombre().name()));
        periodoSeleccionado = 1; //alto hardCode, para que por defecto este seleccionado dia y no hora (Jorge)
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        //nuevaPregunta = new ComentarioFacade();
        if (id != null) {
            int publicationId = Integer.parseInt(id);
            setPublicacion(publicationBean.getPublicacion(publicationId));
            setComentarios(publicationBean.getPreguntas(publicationId));
            if (publicacion != null && publicacion.getFecha_hasta() != null) {
                setFecha_hasta(DateFormat.getDateInstance(DateFormat.SHORT).format(publicacion.getFecha_hasta()));
            }

        }
    }

    public void actualizarPreguntas() {
        comentarios = publicationBean.getPreguntas(publicacion.getId());
    }

    public void preguntar() {
        boolean logueado = usuarioLogueado.isLogueado();
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("logueado", logueado);
    }

    public void guardarPregunta() {
        ComentarioDTO nuevaPregunta = new ComentarioDTO();
        nuevaPregunta.setComentario(comentario);
        nuevaPregunta.setUsuarioId(usuarioLogueado.getUsuarioId());
        //nuevaPregunta.setUsuario(usuarioLogueado.getUsername());
        nuevaPregunta.setFecha(new Date());
        try {
            publicationBean.setPregunta(publicacion.getId(), nuevaPregunta);
            //setNuevaPregunta(new ComentarioFacade());
            actualizarPreguntas();
            comentario = "";
        } catch (AlquilaCosasException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al enviar pregunta", e.getMessage()));
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
}
