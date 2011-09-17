/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.ejb.entity.Alquiler;
import com.alquilacosas.ejb.entity.AlquilerXEstado;
import com.alquilacosas.ejb.entity.Calificacion;
import com.alquilacosas.ejb.entity.EstadoAlquiler;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Login;
import com.alquilacosas.ejb.entity.Periodo.NombrePeriodo;
import com.alquilacosas.ejb.entity.Precio;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.Puntuacion;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.facade.AlquilerFacade;
import com.alquilacosas.facade.AlquilerXEstadoFacade;
import com.alquilacosas.facade.CalificacionFacade;
import com.alquilacosas.facade.EstadoAlquilerFacade;
import com.alquilacosas.facade.PrecioFacade;
import com.alquilacosas.facade.PuntuacionFacade;
import com.alquilacosas.facade.UsuarioFacade;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        axeCancelado.setEstadoAlquilerFk(estado);
        alquiler.agregarAlquilerXEstado(axeCancelado);
        alquilerFacade.edit(alquiler);
        return true;
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
    
    /**
     * 
     * @param periodo1
     * @param duracion1
     * @param periodo2
     * @param duracion2
     * @return Devuelve un entero mayor a 0 si el periodo1/duracion1 es mayor al
     * periodo2/duracion2; devuelve 0 si ambos periodos/duraciones son iguales;
     * y devuelve un entero menor a 0 si el periodo1/duracion1 es menos al 
     * periodo2/duracion2
     */
//    private int isMayor(NombrePeriodo periodo1, int duracion1, NombrePeriodo periodo2, int duracion2) {
//        if(periodo1 == NombrePeriodo.HORA) {
//            if(periodo2 == NombrePeriodo.HORA) {
//                return duracion1 - duracion2;
//            } else {
//                return -1;
//            }
//        } else if(periodo1 == NombrePeriodo.DIA) {
//            if(periodo2 == NombrePeriodo.HORA) {
//                return 1;
//            } else if(periodo2 == NombrePeriodo.DIA) {
//                return duracion1 - duracion2;
//            } else {
//                return -1;
//            }
//        } else if(periodo1 == NombrePeriodo.SEMANA) {
//            if(periodo2 == NombrePeriodo.HORA) {
//                return 1;
//            } else if(periodo2 == NombrePeriodo.DIA) {
//                return duracion1 - (duracion2 / 7);
//            } else if(periodo2 == NombrePeriodo.SEMANA)
//        }
//    }
    
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
