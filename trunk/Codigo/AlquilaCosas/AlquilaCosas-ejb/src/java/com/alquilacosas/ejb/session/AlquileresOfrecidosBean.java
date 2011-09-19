/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.dto.PedidoCambioDTO;
import com.alquilacosas.ejb.entity.Alquiler;
import com.alquilacosas.ejb.entity.AlquilerXEstado;
import com.alquilacosas.ejb.entity.Calificacion;
import com.alquilacosas.ejb.entity.EstadoAlquiler;
import com.alquilacosas.ejb.entity.EstadoPedidoCambio;
import com.alquilacosas.ejb.entity.EstadoPedidoCambio.NombreEstadoPedidoCambio;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Login;
import com.alquilacosas.ejb.entity.PedidoCambio;
import com.alquilacosas.ejb.entity.PedidoCambioXEstado;
import com.alquilacosas.ejb.entity.Periodo.NombrePeriodo;
import com.alquilacosas.ejb.entity.Precio;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.Puntuacion;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.facade.AlquilerFacade;
import com.alquilacosas.facade.AlquilerXEstadoFacade;
import com.alquilacosas.facade.CalificacionFacade;
import com.alquilacosas.facade.EstadoAlquilerFacade;
import com.alquilacosas.facade.EstadoPedidoCambioFacade;
import com.alquilacosas.facade.PedidoCambioFacade;
import com.alquilacosas.facade.PedidoCambioXEstadoFacade;
import com.alquilacosas.facade.PrecioFacade;
import com.alquilacosas.facade.PuntuacionFacade;
import com.alquilacosas.facade.UsuarioFacade;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author damiancardozo
 */
@Stateless
@DeclareRoles({"USUARIO", "ADMIN"})
public class AlquileresOfrecidosBean implements AlquileresOfrecidosBeanLocal {

    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private AlquilerFacade alquilerFacade;
    @EJB
    private CalificacionFacade calificacionFacade;
    @EJB
    private PuntuacionFacade puntuacionFacade;
    @EJB
    private EstadoAlquilerFacade estadoFacade;
    @EJB
    private PrecioFacade precioFacade;
    @EJB
    private AlquilerXEstadoFacade axeFacade;
    @EJB
    private PedidoCambioFacade pedidoFacade;
    @EJB
    private PedidoCambioXEstadoFacade pcxeFacade;
    @EJB
    private EstadoPedidoCambioFacade estadoPedidoFacade;

    @Override
    @RolesAllowed({"USUARIO", "ADMIN"})
    public List<AlquilerDTO> getAlquileresVigentes(int usuarioId) {
        Usuario usuario = usuarioFacade.find(usuarioId);
        List<Alquiler> alquileres = alquilerFacade.getAlquileresOfrecidosVigentes(usuario);
        return convertirAlquileres(alquileres, usuario);
    }

    @Override
    @RolesAllowed({"USUARIO", "ADMIN"})
    public List<AlquilerDTO> getAlquileresSinCalificar(int usuarioId) {
        Usuario usuario = usuarioFacade.find(usuarioId);
        List<Alquiler> alquileres = alquilerFacade.getAlquileresOfrecidosSinCalificar(usuario);
        return convertirAlquileres(alquileres, usuario);
    }

    @Override
    @RolesAllowed({"USUARIO", "ADMIN"})
    public List<AlquilerDTO> getAlquileresCalificados(int usuarioId) {
        Usuario usuario = usuarioFacade.find(usuarioId);
        List<Alquiler> alquileres = alquilerFacade.getAlquileresOfrecidosCalificados(usuario);
        return convertirAlquileres(alquileres, usuario);
    }

    @Override
    @RolesAllowed({"USUARIO", "ADMIN"})
    public void registrarCalificacion(int usuarioId, int alquilerId, int puntuacionId, String comentario) {
        Usuario calificador = usuarioFacade.find(usuarioId);
        Puntuacion puntuacion = puntuacionFacade.find(puntuacionId);
        Alquiler alquiler = alquilerFacade.find(alquilerId);

        Calificacion calificacion = new Calificacion();
        calificacion.setFechaCalificacion(new Date());
        calificacion.setUsuarioCalificadorFk(calificador);
        calificacion.setComentarioCalificador(comentario);
        calificacion.setPuntuacionFk(puntuacion);

        alquiler.agregarCalificacion(calificacion);
        alquilerFacade.edit(alquiler);
    }

    @Override
    @RolesAllowed({"USUARIO", "ADMIN"})
    public List<Puntuacion> getPuntuaciones() {
        return puntuacionFacade.findAll();
    }

    @Override
    public AlquilerDTO modificarAlquiler(AlquilerDTO dto, NombrePeriodo periodo, int duracion) throws AlquilaCosasException {
        int alquilerId = dto.getIdAlquiler();
        Alquiler alquiler = alquilerFacade.find(alquilerId);
        if(alquiler == null) {
            throw new AlquilaCosasException("Alquiler no encontrado");
        }
        
        Publicacion publicacion = alquiler.getPublicacionFk();
        
        Calendar cal = Calendar.getInstance();
        Date fechaInicio = alquiler.getFechaInicio();
        cal.setTime(fechaInicio);
        if (periodo == NombrePeriodo.HORA) {
            cal.add(Calendar.HOUR_OF_DAY, duracion);
        } else {
            cal.add(Calendar.DAY_OF_YEAR, duracion);
        }
        Date fechaFin = cal.getTime();
        
        long periodoAlquilerEnMillis = fechaFin.getTime() - fechaInicio.getTime();
        
        long minimaDuracion = calcularDuracion(publicacion.getMinPeriodoAlquilerFk().getNombre(), 
                publicacion.getMinValor());
        
        if(periodoAlquilerEnMillis < minimaDuracion) {
            throw new AlquilaCosasException("El periodo seleccionado no alcanza "
                    + "el minimo periodo de alquiler seleccionado para ese producto:"
                    + publicacion.getMinValor() + " " +
                    publicacion.getMinPeriodoAlquilerFk().getNombre().toString());
        }
        if(publicacion.getMaxPeriodoAlquilerFk() != null && publicacion.getMaxValor() != null) {
            long maximaDuracion = calcularDuracion(publicacion.getMaxPeriodoAlquilerFk().getNombre(), 
                    publicacion.getMaxValor());
            if(periodoAlquilerEnMillis > maximaDuracion) {
                throw new AlquilaCosasException("El periodo seleccionado supera "
                        + "el maximo periodo de alquiler seleccionado para ese producto."
                        + publicacion.getMaxValor() + " " + 
                        publicacion.getMaxPeriodoAlquilerFk().getNombre().toString());
            }
        }
        
        alquiler.setFechaFin(fechaFin);
        
        List<Precio> precios = precioFacade.findByPublicacion(publicacion);
        double monto = calcularMonto(fechaInicio, fechaFin, precios);
        alquiler.setMonto(monto);
        
        alquilerFacade.edit(alquiler);

        dto.setMonto(monto);
        dto.setFechaFin(fechaFin);
        return dto;
    }
    
    @Override
    public boolean cancelarAlquiler(int alquilerId) throws AlquilaCosasException {
        Alquiler alquiler = alquilerFacade.find(alquilerId);
        if(alquiler == null) {
            throw new AlquilaCosasException("Alquiler no encontrado");
        }
        AlquilerXEstado axe = axeFacade.findByAlquiler(alquilerId);
        axe.setFechaHasta(new Date());
        
        EstadoAlquiler estado = estadoFacade.findByNombre(EstadoAlquiler.NombreEstadoAlquiler.CANCELADO);
        AlquilerXEstado axeCancelado = new AlquilerXEstado();
        axeCancelado.setFechaDesde(new Date());
        axeCancelado.setEstadoAlquilerFk(estado);
        alquiler.agregarAlquilerXEstado(axeCancelado);
        alquilerFacade.edit(alquiler);
        return true;
    }
    
    @Override
    public PedidoCambioDTO getPedidoCambio(int pedidoCambioId) {
        PedidoCambio pedido = pedidoFacade.find(pedidoCambioId);
        return new PedidoCambioDTO(pedido.getPedidoCambioId(), pedido.getPeriodoFk().getNombre(), pedido.getDuracion());
    }
    
    @Override
    public AlquilerDTO responderPedidoCambio(int pedidoCambioId, AlquilerDTO alquilerDTO, boolean aceptado) throws AlquilaCosasException {
        PedidoCambio pedido = pedidoFacade.find(pedidoCambioId);
        
        PedidoCambioXEstado pcxe = pcxeFacade.getPedidoCambioXEstadoActual(pedido);
        pcxe.setFechaHasta(new Date());
        
        PedidoCambioXEstado pcxeNuevo = new PedidoCambioXEstado();
        pcxeNuevo.setPedidoCambioFk(pedido);
        
        EstadoPedidoCambio estado = null;
        if(aceptado) {
            estado = estadoPedidoFacade.findByNombre(NombreEstadoPedidoCambio.ACEPTADO);
            alquilerDTO = modificarAlquiler(alquilerDTO, pedido.getPeriodoFk().getNombre(), pedido.getDuracion());
        } else {
            estado = estadoPedidoFacade.findByNombre(NombreEstadoPedidoCambio.RECHAZADO);
        }
        pcxeNuevo.setEstadoPedidoCambioFk(estado);
        pcxeNuevo.setFechaDesde(new Date());
        pcxeFacade.edit(pcxe);
        pcxeFacade.create(pcxeNuevo);
        return alquilerDTO;
    }
    
    @Override
    public List<Date> getFechasSinStock(int alquilerId) {
        Alquiler alquiler = alquilerFacade.find(alquilerId);
        Publicacion publicacion = alquiler.getPublicacionFk();
        List<Date> respuesta = new ArrayList<Date>();
        List<Alquiler> alquileres = alquilerFacade.getAlquileresByPublicacionFromToday(publicacion);
        alquileres.remove(alquiler);
        Iterator<Alquiler> itAlquiler =  alquileres.iterator();
        
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        
        int disponibles = publicacion.getCantidad();
        
        HashMap<String, Integer> dataCounter = new HashMap(60);//probablemente no existan pedidos mas haya de 60 dias desde hoy
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(new Date());
       
        while (itAlquiler.hasNext()) {
            
            Alquiler temp = itAlquiler.next();
            Calendar date = Calendar.getInstance();
            date.setTime(temp.getFechaInicio());
            date.set(Calendar.HOUR_OF_DAY, 0);
            date.set(Calendar.MINUTE, 0);
            date.set(Calendar.SECOND, 0);
            Calendar fechaFin = Calendar.getInstance();
            fechaFin.setTime(temp.getFechaFin());
            if(fechaFin.get(Calendar.HOUR_OF_DAY) == 0 && fechaFin.get(Calendar.MINUTE)  == 0 
                    && fechaFin.get(Calendar.SECOND)  == 0 )
                //fechaFin.add(Calendar.SECOND, 1); 
            //No me importan las fechas anteriores a hoy, no son seleccionables
            if(date.before(today))
                date.setTime(today.getTime());
            //Me fijo si en los dias que dura el alquiler analizado hay disponibilidad de
            //productos para el pedido que estoy creando
            if(alquiler.getCantidad() <= disponibles - temp.getCantidad())
                //Si alcanzan los dias guardo en el hashmap el dia y la cantidad de
                //productos del alquiler, para ir acumulando esta cantidad para cada fecha
                //al final me fijo por cada fecha si alcanza, porque ya tengo todos los 
                //alquileres analizados
                while(date.before(fechaFin)) {
                    Integer acumulado = dataCounter.get(date.getTime().toString());
                    if(acumulado != null)
                        dataCounter.put(date.getTime().toString(),new Integer(acumulado + temp.getCantidad()));
                    else
                        dataCounter.put(date.getTime().toString(),new Integer(temp.getCantidad()));
                    date.add(Calendar.DATE, 1);
                }
            else
                //si no alcanza, marco a todos los dias de este alquiler en el hasmap con el valor
                //de la disponibilidad total de la publicacion, lo cual significa que no va a alcanzar
                //ese dia para hacer el pedido ni siquiera de 1 producto
                
                while(date.before(fechaFin)){
                    dataCounter.put(date.getTime().toString(),new Integer(disponibles));
                    date.add(Calendar.DATE, 1);
                }
                
                if(date.after(lastDate))
                    lastDate = date;

        }
        //Reviso el hashmap y voy llenando la lista de respuesta con las fechas que 
        //no me alcanza el stock disponible
        Calendar date = Calendar.getInstance();
        date.setTime(new Date());
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        
        if(lastDate.get(Calendar.HOUR_OF_DAY) == 0 && lastDate.get(Calendar.MINUTE)  == 0 
                && lastDate.get(Calendar.SECOND)  == 0 )
            lastDate.add(Calendar.SECOND, 1); 
        while(date.before(lastDate)){
            Integer acumulado = dataCounter.get(date.getTime().toString());
            if(acumulado != null)
                if(alquiler.getCantidad() > (disponibles - acumulado))
                    respuesta.add(date.getTime());
            date.add(Calendar.DATE, 1);
        }
        
        return respuesta;
    }

    private List<AlquilerDTO> convertirAlquileres(List<Alquiler> alquileres, Usuario usuario) {
        List<AlquilerDTO> listaAlquileres = new ArrayList<AlquilerDTO>();
        for (Alquiler a : alquileres) {
            boolean calificado = calificacionFacade.isCalificacionExistente(usuario, a);
            Publicacion pub = a.getPublicacionFk();
            Login alquilador = a.getUsuarioFk().getLoginList().get(0);
            EstadoAlquiler estado = estadoFacade.getEstadoAlquiler(a);
            List<ImagenPublicacion> imagenes = pub.getImagenPublicacionList();
            int imagenId = -1;
            if (!imagenes.isEmpty()) {
                imagenId = imagenes.get(0).getImagenPublicacionId();
            }
            AlquilerDTO dto = new AlquilerDTO(pub.getPublicacionId(), usuario.getUsuarioId(),
                    a.getAlquilerId(), imagenId, a.getFechaInicio(), a.getFechaFin(),
                    estado.getNombre(), pub.getTitulo(),
                    alquilador.getUsername(), a.getCantidad(), a.getMonto(), calificado);
            // si el alquiler esta activo o confirmado; revisar si existe un pedido de cambio, y setearle su id al dto
            if(estado.getNombre() == EstadoAlquiler.NombreEstadoAlquiler.CONFIRMADO || 
                    estado.getNombre() == EstadoAlquiler.NombreEstadoAlquiler.ACTIVO) {
                PedidoCambio pedido = pedidoFacade.getPedidoEnviado(a);
                int id = -1;
                if(pedido != null)
                    id = pedido.getPedidoCambioId();
                dto.setIdPedidoCambio(id);
            }
            listaAlquileres.add(dto);
        }
        return listaAlquileres;
    }

    private double calcularMonto(Date inicio, Date fin, List<Precio> precios) {
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTime(inicio);
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(fin);
        Calendar temp = Calendar.getInstance();
        double monto = 0;
        
        temp.setTime(beginDate.getTime());
        temp.add(Calendar.MONTH, 1);
        endDate.add(Calendar.SECOND, 1);
        while (endDate.after(temp)) {
            //hardCode muy duro, se el orden porque hice la consulta con orderby
            //si se agregan periodos nuevos corregir esto
            monto += precios.get(3).getPrecio();
            temp.add(Calendar.MONTH, 1);
        }

        temp.add(Calendar.MONTH, -1);
        temp.add(Calendar.WEEK_OF_YEAR, 1);
        while (endDate.after(temp)) {
            //hardCode muy duro, se el orden porque hice la consulta con orderby
            //si se agregan periodos nuevos corregir esto
            monto += precios.get(2).getPrecio();
            temp.add(Calendar.WEEK_OF_YEAR, 1);
        }

        temp.add(Calendar.WEEK_OF_YEAR, -1);
        temp.add(Calendar.DATE, 1);
        while (endDate.after(temp)) {
            //hardCode muy duro, se el orden porque hice la consulta con orderby
            //si se agregan periodos nuevos corregir esto
            monto += precios.get(1).getPrecio();
            temp.add(Calendar.DATE, 1);
        }

        temp.add(Calendar.DATE, -1);
        temp.add(Calendar.HOUR_OF_DAY, 1);
        while (endDate.after(temp)) {
            //hardCode muy duro, se el orden porque hice la consulta con orderby
            //si se agregan periodos nuevos corregir esto
            monto += precios.get(0).getPrecio();
            temp.add(Calendar.HOUR_OF_DAY, 1);
        }
        return monto;
    }
    
    private long calcularDuracion(NombrePeriodo nombrePeriodo, long periodo) {
        
        if(nombrePeriodo == NombrePeriodo.HORA) {
            periodo *= 60 * 60 * 1000;
        } else if(nombrePeriodo == NombrePeriodo.DIA) {
            periodo *= 24 * 60 * 60 * 1000;
        } else if(nombrePeriodo == NombrePeriodo.SEMANA) {
            periodo *= 7 * 24 * 60 * 60 * 1000;
        } else if(nombrePeriodo == NombrePeriodo.MES) {
            Date now = new Date();
                Calendar temp = Calendar.getInstance();
                temp.setTime(now);
                temp.add(Calendar.MONTH,(int)periodo);
                periodo = temp.getTimeInMillis() - now.getTime();
        }
        return periodo;
    }
    
}
