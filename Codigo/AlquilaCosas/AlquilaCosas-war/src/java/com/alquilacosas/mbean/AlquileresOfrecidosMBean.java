/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.dto.CalificacionDTO;
import com.alquilacosas.dto.PedidoCambioDTO;
import com.alquilacosas.ejb.entity.Periodo.NombrePeriodo;
import com.alquilacosas.ejb.entity.Puntuacion;
import com.alquilacosas.ejb.session.AlquileresOfrecidosBeanLocal;
import java.io.Serializable;
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
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.json.JSONObject;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "alquileresOfrecidos")
@ViewScoped
public class AlquileresOfrecidosMBean implements Serializable {

    @EJB
    private AlquileresOfrecidosBeanLocal alquileresBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean loginBean;
    private List<SelectItem> filtros, puntuaciones;
    private Integer filtroSeleccionado, duracion, duracionAnterior;
    private NombrePeriodo periodo;
    private List<AlquilerDTO> alquileres;
    private Integer alquilerId, publicacionId, usuarioId, puntuacionId, 
            pedidoCambioId, usuarioLogueado;
    private String comentario;
    private Date fechaInicio;
    private AlquilerDTO alquilerSeleccionado;
    private List<Date> fechas;
    private String myJson;
    private Boolean ofrece;
    private Boolean toma;
    private CalificacionDTO calificacionOfrece;
    private CalificacionDTO calificacionToma;
    private Boolean ofreceCalifico;
    private Boolean tomaCalifico;

    /** Creates a new instance of MisAlquileresOfrecidosMBean */
    public AlquileresOfrecidosMBean() {
    }

    @PostConstruct
    public void init() {
        Logger.getLogger(AlquileresOfrecidosMBean.class).info("AlquileresOfrecidosMBean: postconstruct.");
        usuarioLogueado = loginBean.getUsuarioId();
        if (usuarioLogueado == null) {
            return;
        }
        alquileres = alquileresBean.getAlquileresVigentes(usuarioLogueado);
        puntuaciones = new ArrayList<SelectItem>();
        List<Puntuacion> listaPuntuacion = alquileresBean.getPuntuaciones();
        for (Puntuacion p : listaPuntuacion) {
            puntuaciones.add(new SelectItem(p.getPuntuacionId(), p.getNombre()));
        }

        filtros = new ArrayList<SelectItem>();
        filtros.add(new SelectItem(1, "Alquileres vigentes"));
        filtros.add(new SelectItem(2, "Alquileres finalizados sin calificar"));
        filtros.add(new SelectItem(3, "Alquileres finalizados calificados"));
        filtroSeleccionado = 1;

    }

    public void cambioFiltro() {
        switch (filtroSeleccionado) {
            case 1: {
                alquileres = alquileresBean.getAlquileresVigentes(usuarioLogueado);
                break;
            }
            case 2: {
                alquileres = alquileresBean.getAlquileresSinCalificar(usuarioLogueado);
                break;
            }
            case 3: {
                alquileres = alquileresBean.getAlquileresCalificados(usuarioLogueado);
                break;
            }
        }
    }

//    public String verPublicacion() {
//        return "mostrarPublicacion";
//    }
//
//    public String verUsuario() {
//        return "verReputacionUsuario";
//    }

    public void prepararCalificar(ActionEvent event) {
        alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
    }

    public void prepararVerCalificacion(ActionEvent event) {
        alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
        ofreceCalifico = true;
        tomaCalifico = true;
        try {
            calificacionOfrece = alquileresBean.getCalificacionOfrece(alquilerId);
            calificacionToma = alquileresBean.getCalificacionToma(alquilerId);
            // Lo siguiente sirve para mostrar los campos permitidos
            if (calificacionOfrece.getIdUsuarioCalidicador() != null) {
                ofrece = calificacionOfrece.getIdUsuarioCalidicador() == usuarioLogueado;
            } else {
                ofreceCalifico = false;
            }
            if (calificacionToma.getIdUsuarioCalidicador() != null) {
                toma = calificacionToma.getIdUsuarioCalidicador() == usuarioLogueado;
            } else {
                tomaCalifico = false;
            }
        } catch (AlquilaCosasException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error  al recuperar las Calificaciones" + e.toString(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void prepararVerPedido(ActionEvent event) {
        alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
        pedidoCambioId = (Integer) event.getComponent().getAttributes().get("ped");
        PedidoCambioDTO pedido = alquileresBean.getPedidoCambio(pedidoCambioId);
        duracion = pedido.getDuracion();
        periodo = pedido.getPeriodo();
    }

    public void prepararCancelarAlquiler(ActionEvent event) {
        alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
    }

    public void prepararModificar(ActionEvent event) {
        alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
        alquilerSeleccionado = null;
        for (AlquilerDTO alq : alquileres) {
            if (alq.getIdAlquiler() == alquilerId) {
                alquilerSeleccionado = alq;
                break;
            }
        }
        if (alquilerSeleccionado == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error preparando alquiler", "Alquiler no encontrado");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(alquilerSeleccionado.getFechaInicio());
        int dia1 = calendar.get(Calendar.DAY_OF_YEAR);
        int hora1 = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto1 = calendar.get(Calendar.MINUTE);
        calendar.setTime(alquilerSeleccionado.getFechaFin());
        int dia2 = calendar.get(Calendar.DAY_OF_YEAR);
        int hora2 = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto2 = calendar.get(Calendar.MINUTE);
        if ((hora1 + minuto1 + hora2 + minuto2) != 0) {
            periodo = NombrePeriodo.HORA;
            duracion = hora2 - hora1;
        } else {
            periodo = NombrePeriodo.DIA;
            duracion = dia2 - dia1;
        }
        duracionAnterior = duracion;
        fechaInicio = alquilerSeleccionado.getFechaInicio();
        fechas = alquileresBean.getFechasSinStock(alquilerId);
        createDictionary();
    }

    public void modificarAlquiler() {
        FacesMessage msg = null;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqContext = RequestContext.getCurrentInstance();
        // Si la duracion ingresada es 0, no permitir
        if (duracion == 0) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "La duracion del alquiler no puede ser 0", "");
            context.addMessage(null, msg);
            reqContext.addCallbackParam("modificado", false);
            return;
        } // si la duracion no se modifico, no hay que modificar nada
        else if (duracion == duracionAnterior) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "No ha modificado la duracion del alquiler", "");
            context.addMessage(null, msg);
            reqContext.addCallbackParam("modificado", false);
            return;
        } else if (periodo == NombrePeriodo.HORA && duracion >= 24) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Los alquileres por hora no pueden superar las 24 horas.", "");
            context.addMessage(null, msg);
            reqContext.addCallbackParam("modificado", false);
            return;
        }
        // revisar si la nueva duracion no hace que el alquiler se solape
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTime(fechaInicio);
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(fechaInicio);
        if (periodo == NombrePeriodo.HORA) {
            endDate.add(Calendar.HOUR_OF_DAY, duracion);
        } else {
            endDate.add(Calendar.DATE, duracion);
        }

        Iterator<Date> itFechasSinStock = fechas.iterator();
        boolean noStockFlag = false;
        Calendar temp = Calendar.getInstance();
        //Recorro la lista de fechas sin stock fijandome si alguna cae en el periodo seleccionado
        while (!noStockFlag && itFechasSinStock.hasNext()) {
            temp.setTime(itFechasSinStock.next());
            if (beginDate.before(temp) && endDate.after(temp))//la fecha sin stock cae en el periodo
            {
                noStockFlag = true;
            }
        }
        if (noStockFlag) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar alquiler", "Hay fechas sin stock en el periodo seleccionado");
            context.addMessage(null, msg);
            reqContext.addCallbackParam("modificado", false);
            return;
        }

        try {
            alquilerSeleccionado = alquileresBean.modificarAlquiler(alquilerSeleccionado, periodo, duracion);
        } catch (AlquilaCosasException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar alquiler", e.getMessage());
            context.addMessage(null, msg);
            return;
        }
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Alquiler modificado", "");
        context.addMessage(null, msg);
        for (AlquilerDTO alq : alquileres) {
            if (alq.getIdAlquiler() == alquilerSeleccionado.getIdAlquiler()) {
                alq.setMonto(alquilerSeleccionado.getMonto());
                alq.setFechaFin(alquilerSeleccionado.getFechaFin());
                break;
            }
        }
        reqContext.addCallbackParam("modificado", true);
    }

    public void cancelarAlquiler() {
        boolean borrado = false;
        try {
            borrado = alquileresBean.cancelarAlquiler(alquilerId);
        } catch (AlquilaCosasException e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al cancelar alquiler", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        if (borrado) {
            for (int i = 0; i < alquileres.size(); i++) {
                AlquilerDTO alq = alquileres.get(i);
                if (alq.getIdAlquiler() == alquilerId) {
                    alquileres.remove(alq);
                    return;
                }
            }
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Alquiler cancelado.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void responderPedidoCambio(boolean aceptado) {
        alquilerSeleccionado = null;
        for (AlquilerDTO alq : alquileres) {
            if (alq.getIdAlquiler() == alquilerId) {
                alquilerSeleccionado = alq;
                break;
            }
        }
        if (alquilerSeleccionado == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error aceptando pedido de cambio", "Alquiler no encontrado");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        try {
            alquileresBean.responderPedidoCambio(pedidoCambioId, alquilerSeleccionado, aceptado);
        } catch (AlquilaCosasException e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error aceptando pedido de cambio", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        for (AlquilerDTO alq : alquileres) {
            if (alq.getIdAlquiler() == alquilerSeleccionado.getIdAlquiler()) {
                alq.setMonto(alquilerSeleccionado.getMonto());
                alq.setFechaFin(alquilerSeleccionado.getFechaFin());
                alq.setIdPedidoCambio(-1);
                break;
            }
        }
        String accion = aceptado ? "aceptado" : "rechazado";
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Pedido de cambio " + accion, "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void registrarCalificacion() {
        alquileresBean.registrarCalificacion(usuarioLogueado, alquilerId, puntuacionId, comentario);
        //alquileres = alquileresBean.getAlquileresSinCalificar(usuarioLogueado);
        for(int i = 0; i < alquileres.size(); i++) {
            AlquilerDTO dto = alquileres.get(i);
            if(dto.getIdAlquiler() == alquilerId) {
                alquileres.remove(dto);
                return;
            }
        }
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Calificacion registrada.", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
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

    public void registrarReplicaOfrece() {
        try {
            alquileresBean.registrarReplica(calificacionToma.getIdCalificacion(), calificacionToma.getComentarioReplica(), usuarioLogueado);
            calificacionToma.setYaReplico(true);
        } catch (AlquilaCosasException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Registrar Replica " + e.toString(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Replica registrada.", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    /*
     * Getters & Setters
     */
    public Integer getAlquilerId() {
        return alquilerId;
    }

    public void setAlquilerId(Integer alquilerId) {
        this.alquilerId = alquilerId;
    }

    public List<AlquilerDTO> getAlquileres() {
        return alquileres;
    }

    public void setAlquileres(List<AlquilerDTO> alquileres) {
        this.alquileres = alquileres;
    }

    public Integer getPublicacionId() {
        return publicacionId;
    }

    public void setPublicacionId(Integer publicacionId) {
        this.publicacionId = publicacionId;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public ManejadorUsuarioMBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(ManejadorUsuarioMBean loginBean) {
        this.loginBean = loginBean;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public List<SelectItem> getPuntuaciones() {
        return puntuaciones;
    }

    public void setPuntuaciones(List<SelectItem> puntuaciones) {
        this.puntuaciones = puntuaciones;
    }

    public Integer getPuntuacionId() {
        return puntuacionId;
    }

    public void setPuntuacionId(Integer puntuacionId) {
        this.puntuacionId = puntuacionId;
    }

    public Integer getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(Integer usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    public Integer getFiltroSeleccionado() {
        return filtroSeleccionado;
    }

    public void setFiltroSeleccionado(Integer filtroSeleccionado) {
        this.filtroSeleccionado = filtroSeleccionado;
    }

    public List<SelectItem> getFiltros() {
        return filtros;
    }

    public void setFiltros(List<SelectItem> filtros) {
        this.filtros = filtros;
    }

    public NombrePeriodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(NombrePeriodo periodo) {
        this.periodo = periodo;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public String getFechas() {
        return myJson;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public CalificacionDTO getCalificacionOfrece() {
        return calificacionOfrece;
    }

    public void setCalificacionOfrece(CalificacionDTO calificacionOfrece) {
        this.calificacionOfrece = calificacionOfrece;
    }

    public CalificacionDTO getCalificacionToma() {
        return calificacionToma;
    }

    public void setCalificacionToma(CalificacionDTO calificacionToma) {
        this.calificacionToma = calificacionToma;
    }

    public Boolean getOfrece() {
        return ofrece;
    }

    public void setOfrece(Boolean ofrece) {
        this.ofrece = ofrece;
    }

    public Boolean getOfreceCalifico() {
        return ofreceCalifico;
    }

    public void setOfreceCalifico(Boolean ofreceCalifico) {
        this.ofreceCalifico = ofreceCalifico;
    }

    public Boolean getToma() {
        return toma;
    }

    public void setToma(Boolean toma) {
        this.toma = toma;
    }

    public Boolean getTomaCalifico() {
        return tomaCalifico;
    }

    public void setTomaCalifico(Boolean tomaCalifico) {
        this.tomaCalifico = tomaCalifico;
    }

    public Integer getDuracionAnterior() {
        return duracionAnterior;
    }

    public void setDuracionAnterior(Integer duracionAnterior) {
        this.duracionAnterior = duracionAnterior;
    }
}
