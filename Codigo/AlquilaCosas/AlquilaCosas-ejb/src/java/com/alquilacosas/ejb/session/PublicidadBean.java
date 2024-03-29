/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.PublicidadDTO;
import com.alquilacosas.dto.PublicidadDTO.EstadoPublicidad;
import com.alquilacosas.ejb.entity.Pago;
import com.alquilacosas.ejb.entity.PrecioTipoServicio;
import com.alquilacosas.ejb.entity.Publicidad;
import com.alquilacosas.ejb.entity.TipoPago;
import com.alquilacosas.ejb.entity.TipoPago.NombreTipoPago;
import com.alquilacosas.ejb.entity.TipoPublicidad;
import com.alquilacosas.ejb.entity.TipoPublicidad.DuracionPublicidad;
import com.alquilacosas.ejb.entity.TipoPublicidad.UbicacionPublicidad;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.facade.PrecioTipoServicioFacade;
import com.alquilacosas.facade.PublicidadFacade;
import com.alquilacosas.facade.TipoPagoFacade;
import com.alquilacosas.facade.TipoPublicidadFacade;
import com.alquilacosas.facade.UsuarioFacade;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 *
 * @author damiancardozo
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PublicidadBean implements PublicidadBeanLocal {

    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private PublicidadFacade publicidadFacade;
    @EJB
    private TipoPublicidadFacade tipoPubFacade;
    @EJB
    private PrecioTipoServicioFacade precioFacade;
    @EJB
    private TipoPagoFacade tipoPagoFacade;

    @Override
    public Integer registrarPublicidad(int usuarioId, String titulo, String url, String caption,
            UbicacionPublicidad ubicacion, DuracionPublicidad duracion, byte[] imagen,
            Date fechaDesde, Date fechaHasta, Double precio, NombreTipoPago nombreTipoPago)
            throws AlquilaCosasException {

        Usuario usuario = usuarioFacade.find(usuarioId);

        TipoPago tipoPago = tipoPagoFacade.findByNombre(nombreTipoPago);
        Pago pago = new Pago();
        pago.setFechaInicio(new Date());
        pago.setMonto(precio);
        pago.setTipoPagoFk(tipoPago);
        pago.setUsuarioFk(usuario);

        TipoPublicidad tipo = tipoPubFacade.findByUbicacionYDuracion(ubicacion, duracion);

        if (tipo == null) {
            throw new AlquilaCosasException("No se encontro el tipo de publicidad correspondiente en la base de datos.");
        }
        Publicidad publicidad = new Publicidad(titulo, url, caption, imagen);
        publicidad.setTipoPublicidadFk(tipo);
        publicidad.setFechaDesde(fechaDesde);
        publicidad.setFechaHasta(fechaHasta);
        publicidad.setUsuarioFk(usuario);
        publicidad.agregarPago(pago);

        publicidadFacade.create(publicidad);
        return pago.getPagoId();
    }

    @Override
    public Double getPrecio(DuracionPublicidad duracion, UbicacionPublicidad ubicacion) {
        TipoPublicidad tipo = tipoPubFacade.findByUbicacionYDuracion(ubicacion, duracion);
        if (tipo != null) {
            PrecioTipoServicio precio = precioFacade.getPrecioPublicidad(tipo);
            if (precio != null) {
                return precio.getPrecio();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void actualizarPublicidad(Integer publicidadId,
            String titulo, String url, String caption, byte[] imagen)
            throws AlquilaCosasException {

        Publicidad p = publicidadFacade.find(publicidadId);

        if (p == null) {
            throw new AlquilaCosasException("La publicidad no pudo ser encontrada");
        }

        p.setTitulo(titulo);
        p.setUrl(url);
        p.setCaption(caption);
        p.setImagen(imagen);

        publicidadFacade.edit(p);
    }
    
    @Override
    public void eliminarPublicidad(Integer publicidadId) throws AlquilaCosasException {
        Publicidad pub = publicidadFacade.find(publicidadId);
        if(pub != null) {
            publicidadFacade.remove(pub);
        } else {
            throw new AlquilaCosasException("No se pudo eliminar la publicidad");
        }
    }

    /**
     * Devuelve las publicidadesDTO de un usuario
     *
     * @param usuarioId
     * @return lista
     */
    @Override
    @PermitAll
    public List<PublicidadDTO> getPublicidades(int usuarioId) {

        List<Publicidad> publicidades = publicidadFacade.getPublicidadPorUsuario(usuarioId);
        List<PublicidadDTO> lista = new ArrayList<PublicidadDTO>();
        PublicidadDTO publicidadDto = null;

        Date hoy = new Date();
        for (Publicidad p : publicidades) {
            try {
                Pago pago = p.getPagoList().get(0);
                TipoPublicidad tp = p.getTipoPublicidadFk();
                EstadoPublicidad estado = null;
                if (pago.getFechaConfirmado() == null) {
                    estado = EstadoPublicidad.PAGO_PENDIENTE;
                } else if (p.getFechaDesde().after(hoy)) {
                    estado = EstadoPublicidad.PAGO_CONFIRMADO;
                } else if (p.getFechaHasta().before(hoy)) {
                    estado = EstadoPublicidad.VENCIDA;
                }  else if(p.getFechaDesde().before(hoy) && p.getFechaHasta().after(hoy)){
                    estado = EstadoPublicidad.ACTIVA;
                }
                publicidadDto = new PublicidadDTO(p.getServicioId(), p.getTitulo(), p.getUrl(),
                            p.getCaption(), p.getFechaDesde(), p.getFechaHasta(), pago.getMonto(),
                            estado, p.getImagen(), tp.getDuracion(), tp.getUbicacion());
                lista.add(publicidadDto);
            } catch (Exception e) {
                System.out.println("La publicidad no tiene un pago creado" + e.getMessage());
            }
        }
        return lista;
    }

    @Override
    @PermitAll
    public PublicidadDTO getPublicidad(Integer publicidadId) {

        Publicidad p = publicidadFacade.find(publicidadId);
        TipoPublicidad tp = p.getTipoPublicidadFk();
        PrecioTipoServicio precio = precioFacade.getPrecioPublicidad(tp);
        
        PublicidadDTO publicidad = new PublicidadDTO(publicidadId, p.getTitulo(), p.getUrl(),
                p.getCaption(), p.getFechaDesde(), p.getFechaHasta(), precio.getPrecio(),
                EstadoPublicidad.ACTIVA, p.getImagen(), tp.getDuracion(), tp.getUbicacion());

        return publicidad;
    }

    /**
     * Devuelve las fechas que no tienen stock. Se considera como fecha final de
     * control 60 dias a partir de hoy.
     *
     * @return respuesta
     */
    @Override
    @PermitAll
    public List<Date> getFechasSinDisponibilidad(UbicacionPublicidad ubicacion) {

        int disponibles = 0;

        List<Date> respuesta = new ArrayList<Date>();
        List<Publicidad> publicidades = publicidadFacade.getPublicidadesPorSector(ubicacion);
        Iterator<Publicidad> itPublicidad = publicidades.iterator();

        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        if (ubicacion.equals(UbicacionPublicidad.CARRUSEL)) {
            disponibles = 4;
        } else if (ubicacion.equals(UbicacionPublicidad.LATERAL_IZQUIERDA)) {
            disponibles = 50;
        }


        HashMap<String, Integer> dataCounter = new HashMap(60);//probablemente no existan pedidos mas haya de 60 dias desde hoy
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(new Date());

        while (itPublicidad.hasNext()) {

            Publicidad temp = itPublicidad.next();
            Calendar fechaDesde = Calendar.getInstance();
            fechaDesde.setTime(temp.getFechaDesde());
            fechaDesde.set(Calendar.HOUR_OF_DAY, 0);
            fechaDesde.set(Calendar.MINUTE, 0);
            fechaDesde.set(Calendar.SECOND, 0);

            Calendar fechaHasta = Calendar.getInstance();
            fechaHasta.setTime(temp.getFechaHasta());

            if (fechaHasta.get(Calendar.HOUR_OF_DAY) == 0 && fechaHasta.get(Calendar.MINUTE) == 0
                    && fechaHasta.get(Calendar.SECOND) == 0) //fechaFin.add(Calendar.SECOND, 1); 
            //No me importan las fechas anteriores a hoy, no son seleccionables
            {
                if (fechaDesde.before(today)) {
                    fechaDesde.setTime(today.getTime());
                }
            }
            //Reviso cada publicidad y acumulo una cantidad en el hashmap

            while (fechaDesde.before(fechaHasta)) {
                Integer acumulado = dataCounter.get(fechaDesde.getTime().toString());
                if (acumulado != null) {
                    dataCounter.put(fechaDesde.getTime().toString(), new Integer(acumulado + 1));
                } else {
                    dataCounter.put(fechaDesde.getTime().toString(), new Integer(1));
                }
                fechaDesde.add(Calendar.DATE, 1);
            }

            if (fechaDesde.after(lastDate)) {
                lastDate = fechaDesde;
            }

        }
        //Reviso el hashmap y voy llenando la lista de respuesta con las fechas que 
        //no me alcanza el stock disponible
        Calendar hoy = Calendar.getInstance();
        hoy.setTime(new Date());
        hoy.set(Calendar.HOUR_OF_DAY, 0);
        hoy.set(Calendar.MINUTE, 0);
        hoy.set(Calendar.SECOND, 0);

        if (lastDate.get(Calendar.HOUR_OF_DAY) == 0 && lastDate.get(Calendar.MINUTE) == 0
                && lastDate.get(Calendar.SECOND) == 0) {
            lastDate.add(Calendar.SECOND, 1);
        }
        while (hoy.before(lastDate)) {
            Integer acumulado = dataCounter.get(hoy.getTime().toString());
            if (acumulado != null) {
                if (acumulado >= disponibles) {
                    respuesta.add(hoy.getTime());
                }
            }
            hoy.add(Calendar.DATE, 1);
        }

        return respuesta;
    }
}
