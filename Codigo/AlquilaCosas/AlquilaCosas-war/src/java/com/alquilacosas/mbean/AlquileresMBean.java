/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.dto.CalificacionDTO;
import com.alquilacosas.dto.PedidoCambioDTO;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.entity.Periodo.NombrePeriodo;
import com.alquilacosas.ejb.entity.Puntuacion;
import com.alquilacosas.ejb.session.AlquileresBeanLocal;
import com.alquilacosas.ejb.session.PublicacionBeanLocal;
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
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name="alquileresBean")
@ViewScoped
public class AlquileresMBean {

    @EJB
    private AlquileresBeanLocal alquileresBean;
    @EJB
    private PublicacionBeanLocal publicacionBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean loginBean;
    private Integer usuarioLogueado;
    private List<AlquilerDTO> alquileres;
    private Integer alquilerId;
    // calificaciones
    private String comentario;
    private Integer puntuacionId;
    private List<SelectItem> puntuaciones;
    private CalificacionDTO calificacionOfrece, calificacionToma;
    private Boolean ofreceCalifico, tomaCalifico, ofrece, toma, tomado;
    private AlquilerDTO alquiler;
    // pedido de cambio recibido
    private int pedidoCambioId;
    private double montoPedido;
    private Date fechaPedido;
    
    /** Creates a new instance of AlquileresMBean */
    public AlquileresMBean() {
    }
    
    @PostConstruct
    public void init() {
        usuarioLogueado = loginBean.getUsuarioId();
        if (usuarioLogueado == null) {
            return;
        }
        alquileres = alquileresBean.getAlquileres(usuarioLogueado);
        
        puntuaciones = new ArrayList<SelectItem>();
        List<Puntuacion> listaPuntuacion = alquileresBean.getPuntuaciones();
        for (Puntuacion p : listaPuntuacion) {
            puntuaciones.add(new SelectItem(p.getPuntuacionId(), p.getNombre()));
        }
    }
    
    public void cancelarAlquiler() {
        if(alquilerId == null || alquilerId < 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Error al cancelar alquiler.", "No se brindo un ID.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        boolean borrado = alquileresBean.cancelarAlquiler(alquilerId);
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
    
    public void prepararVerPedido(ActionEvent event) {
        int id = (Integer) event.getComponent().getAttributes().get("alq");
        if(alquilerId != null && alquilerId == id) {
            return;
        }
        alquilerId = id;
        pedidoCambioId = (Integer) event.getComponent().getAttributes().get("ped");
        PedidoCambioDTO pedido = alquileresBean.getPedidoCambio(pedidoCambioId);
        alquiler = alquileresBean.getAlquiler(usuarioLogueado, alquilerId);
        Calendar cal = Calendar.getInstance();
        cal.setTime(alquiler.getFechaInicio());
        if(pedido.getPeriodo() == NombrePeriodo.HORA) {
            cal.add(Calendar.HOUR_OF_DAY, pedido.getDuracion());
        } else if(pedido.getPeriodo() == NombrePeriodo.DIA) {
            cal.add(Calendar.DATE, pedido.getDuracion());
        } else if(pedido.getPeriodo() == NombrePeriodo.MES) {
            cal.add(Calendar.WEEK_OF_YEAR, pedido.getDuracion());
        } else if(pedido.getPeriodo() == NombrePeriodo.MES) {
            cal.add(Calendar.MONTH, pedido.getDuracion());
        }
        fechaPedido = cal.getTime();
        PublicacionDTO publicacion = publicacionBean.getPublicacion(alquiler.getIdPublicacion());
        montoPedido = calcularMonto(alquiler.getFechaInicio(), fechaPedido, publicacion);
    }
    
    public void aceptarPedidoCambio() {
        try {
            alquileresBean.responderPedidoCambio(pedidoCambioId, alquiler, fechaPedido, montoPedido, true);
            alquiler.setIdPedidoCambio(-1);
        } catch (AlquilaCosasException ex) {
            Logger.getLogger(AlquileresMBean.class).error("aceptarPedidoCambio(). Excepcion al responder pedido: " + ex.getMessage());
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al aceptar pedido de cambio", ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public void rechazarPedidoCambio() {
        try {
            alquileresBean.responderPedidoCambio(pedidoCambioId, alquiler, fechaPedido, montoPedido, true);
            alquiler.setIdPedidoCambio(-1);
            alquiler.setFechaFin(fechaPedido);
            alquiler.setMonto(montoPedido);
        } catch (AlquilaCosasException ex) {
            Logger.getLogger(AlquileresMBean.class).error("rechazarPedidoCambio(). Excepcion al responder pedido: " + ex.getMessage());
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al rechazar pedido de cambio", ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public void prepararCalificar(ActionEvent event) {
        alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
        alquiler = alquileresBean.getAlquiler(usuarioLogueado, alquilerId);
        tomado = alquiler.isTomado();
    }

    public void prepararVerCalificacion(ActionEvent event) {
        alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
        alquiler = alquileresBean.getAlquiler(usuarioLogueado, alquilerId);
        try {
            calificacionToma = alquileresBean.getCalificacionToma(alquilerId);
            calificacionOfrece = alquileresBean.getCalificacionOfrece(alquilerId);
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error  al leer las Calificaciones" + e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        ofreceCalifico = calificacionOfrece != null;
        tomaCalifico = calificacionToma != null;
        toma =alquiler.isTomado();
        tomado = alquiler.isTomado();
    }
    
    public void registrarCalificacion() {
        alquileresBean.registrarCalificacion(puntuacionId, alquilerId, comentario, usuarioLogueado, tomado);
        for(AlquilerDTO alq: alquileres) {
            if(alq.getIdAlquiler() == alquilerId) {
                alq.setCalificado(true);
                return;
            }
        }
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Calificacion registrada.", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void registrarReplicaToma() {
        alquileresBean.registrarReplica(calificacionToma.getIdCalificacion(), calificacionToma.getComentarioReplica(), usuarioLogueado);
        calificacionToma.setYaReplico(true);
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Replica registrada.", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void registrarReplicaOfrece() {
        alquileresBean.registrarReplica(calificacionOfrece.getIdCalificacion(), calificacionOfrece.getComentarioReplica(), usuarioLogueado);
        calificacionOfrece.setYaReplico(true);
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Replica registrada.", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    private double calcularMonto(Date fechaDesde, Date fechaHasta, PublicacionDTO publicacion) {
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(fechaHasta);
        double nuevoMonto = 0D;
        
        Calendar temp = Calendar.getInstance();
        temp.setTime(fechaDesde);
        temp.add(Calendar.MONTH, 1);
        endDate.add(Calendar.SECOND, 1);
        //int second = endDate.get(Calendar.SECOND);
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
        nuevoMonto += precioHoras;
        temp.add(Calendar.HOUR_OF_DAY, -1);
        endDate.add(Calendar.SECOND, -1);
        return nuevoMonto;
    }
    
    /*
     * Getters & Setters
     */

    public List<AlquilerDTO> getAlquileres() {
        return alquileres;
    }

    public void setAlquileres(List<AlquilerDTO> alquileres) {
        this.alquileres = alquileres;
    }

    public ManejadorUsuarioMBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(ManejadorUsuarioMBean loginBean) {
        this.loginBean = loginBean;
    }

    public int getAlquilerId() {
        return alquilerId;
    }

    public void setAlquilerId(int alquilerId) {
        this.alquilerId = alquilerId;
    }

    public Integer getPuntuacionId() {
        return puntuacionId;
    }

    public void setPuntuacionId(Integer puntuacionId) {
        this.puntuacionId = puntuacionId;
    }

    public List<SelectItem> getPuntuaciones() {
        return puntuaciones;
    }

    public void setPuntuaciones(List<SelectItem> puntuaciones) {
        this.puntuaciones = puntuaciones;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
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

    public Boolean getTomado() {
        return tomado;
    }

    public void setTomado(Boolean tomado) {
        this.tomado = tomado;
    }

    public Integer getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(Integer usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    public AlquilerDTO getAlquiler() {
        return alquiler;
    }

    public void setAlquiler(AlquilerDTO alquiler) {
        this.alquiler = alquiler;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public double getMontoPedido() {
        return montoPedido;
    }

    public void setMontoPedido(double montoPedido) {
        this.montoPedido = montoPedido;
    }

}
