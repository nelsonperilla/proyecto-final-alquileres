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
import com.alquilacosas.ejb.session.AlquileresTomadosBeanLocal;
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
 * @author wilson
 */
@ManagedBean(name = "alquileresTomados")
@ViewScoped
public class AlquileresTomadosMBean implements Serializable {

    @EJB
    private AlquileresTomadosBeanLocal alquileresTomadosBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean usuarioMBean;
    
    private List<SelectItem> filtros;
    private int filtroSeleccionado;
    private List<AlquilerDTO> alquileres;
    private Integer usuarioLogueadoId;
    private int usuarioId;
    private int publicacionId;
    private Integer alquilerId;
    private int pedidoCambioId;
    private List<SelectItem> puntuaciones;
    private int puntuacion;
    private String comentario;
    private Boolean ofrece;
    private Boolean toma;
    private CalificacionDTO calificacionOfrece;
    private CalificacionDTO calificacionToma;
    private Boolean ofreceCalifico;
    private Boolean tomaCalifico;
    //unicamente para la prueba
    private Integer puntuacionId = 1;
    private Date fechaInicio;
    private int duracion, duracionAnterior;
    private NombrePeriodo periodo;
    private List<Date> fechas;
    private String myJson;

    /** Creates a new instance of AlquileresTomadosMBean */
    public AlquileresTomadosMBean() {
    }

     @PostConstruct
     public void init() {
         Logger.getLogger(AlquileresTomadosMBean.class).debug("AlquileresTomadosMBean: postconstruct."); 
         usuarioLogueadoId = usuarioMBean.getUsuarioId();
          if(usuarioLogueadoId == null)
              return;
          filtros = new ArrayList<SelectItem>();
          filtros.add(new SelectItem(0, "Alquileres vigentes"));
          filtros.add(new SelectItem(1, "Alquileres finalizados sin calificar"));
          filtros.add(new SelectItem(2, "Alquileres finalizados calificados"));
          filtroSeleccionado = 1;
          puntuaciones = new ArrayList<SelectItem>();
          List<Puntuacion> listaPuntuacion = alquileresTomadosBean.getPuntuaciones();
          if (!listaPuntuacion.isEmpty()) {
               for (Puntuacion p : listaPuntuacion) {
                    puntuaciones.add(new SelectItem(p.getPuntuacionId(), p.getNombre()));
               }
          }
          actualizarAlquileres();
     }

    public void actualizarAlquileres() {
        switch (filtroSeleccionado) {
            /* Alquileres Activos y Confirmados */
            case 0: {
                alquileres = alquileresTomadosBean.getAlquileresActivosPorUsuario(usuarioLogueadoId);
                break;
            }
            /* Alquileres Finalizados, cancelados, cancelados por alquilador  Sin Calificación */
            case 1: {
                alquileres = alquileresTomadosBean.getAlquileresSinCalificarPorUsuario(usuarioLogueadoId);
                break;
            }
            /* Alquileres Finalizados, cancelados, cancelados por alquilador CON Clasificación */
            case 2: {
                alquileres = alquileresTomadosBean.getAlquileresConCalificarPorUsuario(usuarioLogueadoId);
                break;
            }
        }
    }

//    public String verPublicacion() {
//        return "mostrarPublicacion";
//    }
//
//     public String verUsuario() {
//          return "verReputacionUsuario";
//     }

    public void prepararCalificar(ActionEvent event) {
        alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
    }

     public void prepararVerCalificacion(ActionEvent event) {
          alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
          ofreceCalifico = true;
          tomaCalifico = true;
          try {
               calificacionOfrece = alquileresTomadosBean.getCalificacionOfrece(alquilerId);
               calificacionToma = alquileresTomadosBean.getCalificacionToma(alquilerId);
               // Lo siguiente sirve para mostrar los campos permitidos
               if (calificacionOfrece.getIdUsuarioCalidicador() != null) {
                    ofrece = calificacionOfrece.getIdUsuarioCalidicador() == usuarioLogueadoId;
               } else {
                    ofreceCalifico = false;
               }
               if (calificacionToma.getIdUsuarioCalidicador() != null) {
                    toma = calificacionToma.getIdUsuarioCalidicador() == usuarioLogueadoId;
               } else {
                    tomaCalifico = false;
               }
          } catch (AlquilaCosasException e) {
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error  al recuperar las Calificaciones" + e.toString(), "");
               FacesContext.getCurrentInstance().addMessage(null, message);
          }
     }

    public void prepararCancelarAlquiler(ActionEvent event) {
        alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
    }

    public void prepararPedirCambioAlquiler(ActionEvent event) {
        alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
        AlquilerDTO alquilerSeleccionado = null;
        for (AlquilerDTO alq : alquileres) {
            if (alq.getIdAlquiler() == alquilerId) {
                alquilerSeleccionado = alq;
                break;
            }
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
        fechas = alquileresTomadosBean.getFechasSinStock(alquilerId);
        createDictionary();
    }
    
    public void prepararCancelarPedido(ActionEvent event) {
        pedidoCambioId = (Integer) event.getComponent().getAttributes().get("ped");
        PedidoCambioDTO dto = alquileresTomadosBean.getPedidoCambio(pedidoCambioId);
        periodo = dto.getPeriodo();
        duracion = dto.getDuracion();
    }

    public void registrarCalificacion() {
        alquileresTomadosBean.registrarCalificacion(puntuacion, alquilerId, comentario, usuarioLogueadoId);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Calificacion registrada", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
        for(int i = 0; i < alquileres.size(); i++) {
            AlquilerDTO dto = alquileres.get(i);
            if(dto.getIdAlquiler() == alquilerId) {
                alquileres.remove(dto);
                return;
            }
        }
    }

     public void registrarReplicaToma() {
          try {
               alquileresTomadosBean.registrarReplica(calificacionOfrece.getIdCalificacion(), calificacionOfrece.getComentarioReplica(), usuarioLogueadoId);
               calificacionOfrece.setYaReplico(true);
          } catch (AlquilaCosasException e) {
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Registrar Replica " + e.toString(), "");
               FacesContext.getCurrentInstance().addMessage(null, message);
          }
     }

     public void registrarReplicaOfrece() {
          try {
               alquileresTomadosBean.registrarReplica(calificacionToma.getIdCalificacion(), calificacionToma.getComentarioReplica(), usuarioLogueadoId);
               calificacionToma.setYaReplico(true);
          } catch (AlquilaCosasException e) {
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Registrar Replica " + e.toString(), "");
               FacesContext.getCurrentInstance().addMessage(null, message);
          }
     }

    public void solicitarCambio() {
        FacesMessage msg = null;
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext reqContext = RequestContext.getCurrentInstance(); 
        // Si la duracion ingresada es 0, no permitir
        if(duracion == 0) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, 
                    "La duracion del alquiler no puede ser 0", "");
            context.addMessage(null, msg); 
            reqContext.addCallbackParam("enviado", false);
            return;
        } // si la duracion no se modifico, no hay que modificar nada
        else if(duracion == duracionAnterior) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, 
                    "No ha modificado la duracion del alquiler", "");
            context.addMessage(null, msg); 
            reqContext.addCallbackParam("enviado", false);
            return;
        } else if(periodo == NombrePeriodo.HORA && duracion >= 24) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, 
                    "Los alquileres por hora no pueden superar las 24 horas.", "");
            context.addMessage(null, msg); 
            reqContext.addCallbackParam("enviado", false);
            return;
        }
        // revisar si la nueva duracion no hace que el alquiler se solape
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTime(fechaInicio);
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(fechaInicio);
        if(periodo == NombrePeriodo.HORA)
            endDate.add(Calendar.HOUR_OF_DAY, duracion);
        else 
            endDate.add(Calendar.DATE, duracion);
        
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
            reqContext.addCallbackParam("enviado", false);
            return;
        }
        try {
            alquileresTomadosBean.solicitarCambioAlquiler(alquilerId, periodo, duracion);
        } catch (AlquilaCosasException e) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al solicitar cambio de alquiler", e.getMessage());
            context.addMessage(null, msg);
            reqContext.addCallbackParam("enviado", false);
        }
        alquileres = alquileresTomadosBean.getAlquileresActivosPorUsuario(usuarioLogueadoId);
        reqContext.addCallbackParam("enviado", true);
    }

    public void cancelarAlquiler() {
        boolean borrado = false;
        try {
            borrado = alquileresTomadosBean.cancelarAlquiler(alquilerId);
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
        }
    }
    
    public void cancelarPedidoCambio() {
        alquileresTomadosBean.cancelarPedidoCambio(pedidoCambioId);
        alquileres = alquileresTomadosBean.getAlquileresActivosPorUsuario(usuarioId);
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
    
    /*
     * Getters & Setters
     */

    public int getFiltroSeleccionado() {
        return filtroSeleccionado;
    }

    public void setFiltroSeleccionado(int filtroSeleccionado) {
        this.filtroSeleccionado = filtroSeleccionado;
    }

    public List<SelectItem> getFiltros() {
        return filtros;
    }

    public void setFiltros(List<SelectItem> filtros) {
        this.filtros = filtros;
    }

    public List<AlquilerDTO> getAlquileres() {
        return alquileres;
    }

    public void setAlquileres(List<AlquilerDTO> alquileres) {
        this.alquileres = alquileres;
    }

    public int getPublicacionId() {
        return publicacionId;
    }

    public void setPublicacionId(int publicacionId) {
        this.publicacionId = publicacionId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Integer getAlquilerId() {
        return alquilerId;
    }

    public void setAlquilerId(Integer alquilerId) {
        this.alquilerId = alquilerId;
    }

    public ManejadorUsuarioMBean getUsuarioMBean() {
        return usuarioMBean;
    }

    public void setUsuarioMBean(ManejadorUsuarioMBean usuarioMBean) {
        this.usuarioMBean = usuarioMBean;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public List<SelectItem> getPuntuaciones() {
        return puntuaciones;
    }

    public void setPuntuaciones(List<SelectItem> puntuaciones) {
        this.puntuaciones = puntuaciones;
    }

    public Boolean getOfrece() {
        return ofrece;
    }

    public void setOfrece(Boolean ofrece) {
        this.ofrece = ofrece;
    }

    public Boolean getToma() {
        return toma;
    }

    public void setToma(Boolean toma) {
        this.toma = toma;
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

    public Boolean getOfreceCalifico() {
        return ofreceCalifico;
    }

    public void setOfreceCalifico(Boolean OfreceCalifico) {
        this.ofreceCalifico = OfreceCalifico;
    }

    public Boolean getTomaCalifico() {
        return tomaCalifico;
    }

    public void setTomaCalifico(Boolean TomaCalifico) {
        this.tomaCalifico = TomaCalifico;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public NombrePeriodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(NombrePeriodo periodo) {
        this.periodo = periodo;
    }

    public Integer getPuntuacionId() {
        return puntuacionId;
    }

    public void setPuntuacionId(Integer puntuacionId) {
        this.puntuacionId = puntuacionId;
    }

    public String getFechas() {
        return myJson;
    }

     public Integer getUsuarioLogueadoId() {
          return usuarioLogueadoId;
     }

     public void setUsuarioLogueadoId(Integer usuarioLogueadoId) {
          this.usuarioLogueadoId = usuarioLogueadoId;
     }
}
