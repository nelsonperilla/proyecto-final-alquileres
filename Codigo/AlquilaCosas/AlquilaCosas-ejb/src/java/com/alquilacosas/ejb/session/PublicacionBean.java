/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.ComentarioDTO;
import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.CategoriaDTO;
import com.alquilacosas.common.NotificacionEmail;
import com.alquilacosas.dto.PrecioDTO;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.dto.UsuarioDTO;
import com.alquilacosas.ejb.entity.Alquiler;
import com.alquilacosas.ejb.entity.Categoria;
import com.alquilacosas.ejb.entity.Comentario;
import com.alquilacosas.ejb.entity.Domicilio;
import com.alquilacosas.ejb.entity.EstadoAlquiler;
import com.alquilacosas.ejb.entity.EstadoPublicacion;
import com.alquilacosas.ejb.entity.EstadoPublicacion.NombreEstadoPublicacion;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Periodo;
import com.alquilacosas.ejb.entity.Periodo.NombrePeriodo;
import com.alquilacosas.ejb.entity.Precio;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.PublicacionXEstado;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.facade.AlquilerFacade;
import com.alquilacosas.facade.AlquilerXEstadoFacade;
import com.alquilacosas.facade.CalificacionFacade;
import com.alquilacosas.facade.CategoriaFacade;
import com.alquilacosas.facade.EstadoPublicacionFacade;
import com.alquilacosas.facade.ImagenPublicacionFacade;
import com.alquilacosas.facade.PeriodoFacade;
import com.alquilacosas.facade.PrecioFacade;
import com.alquilacosas.facade.PublicacionFacade;
import com.alquilacosas.facade.PublicacionXEstadoFacade;
import com.alquilacosas.facade.UsuarioFacade;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ignaciogiagante
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@DeclareRoles({"USUARIO", "ADMIN"})
public class PublicacionBean implements PublicacionBeanLocal {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager entityManager;
    @Resource(name = "emailConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(name = "jms/notificacionEmailQueue")
    private Destination destination;
    @Resource
    private SessionContext context;
    @EJB
    private PeriodoAlquilerBeanLocal periodoBean;
    @EJB
    private PrecioBeanLocal precioBean;
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private CategoriaFacade categoriaFacade;
    @EJB
    private PublicacionXEstadoFacade publicacionXEstadoFacade;
    @EJB
    private EstadoPublicacionFacade estadoPublicacionFacade;
    @EJB
    private PublicacionFacade publicacionFacade;
    @EJB
    private ImagenPublicacionFacade imagenPublicacionFacade;
    @EJB
    private PrecioFacade precioFacade;
    @EJB
    private PeriodoFacade periodoFacade;
    @EJB
    private AlquilerFacade alquilerFacade;
    @EJB
    private AlquilerXEstadoFacade estadoAlquiler;
    @EJB
    private CalificacionFacade calificacionFacade;

    @Override
    @RolesAllowed({"USUARIO", "ADMIN"})
    public Integer registrarPublicacion(String titulo, String descripcion,
            Date fechaDesde, Date fechaHasta, boolean destacada, int cantidad,
            int usuarioId, int categoria, List<PrecioDTO> precios,
            List<byte[]> imagenes, int periodoMinimo, int periodoMinimoFk,
            Integer periodoMaximo, Integer periodoMaximoFk) throws AlquilaCosasException {

        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo(titulo);
        publicacion.setDescripcion(descripcion);
        publicacion.setFechaDesde(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 2);
        publicacion.setFechaHasta(calendar.getTime());
        publicacion.setDestacada(destacada);
        publicacion.setCantidad(cantidad);

        Usuario usuario = null;

        try {
            usuario = usuarioFacade.find(usuarioId);
        } catch (NoResultException e) {
            context.setRollbackOnly();
            throw new AlquilaCosasException("No se encontro el Usuario en la "
                    + "base de datos.");
        }
        // Registrar categoria
        try {
            Categoria c = categoriaFacade.find(categoria);
            publicacion.setCategoriaFk(c);
        } catch (NoResultException e) {
            context.setRollbackOnly();
            throw new AlquilaCosasException("No se encontro la Categoria en la "
                    + "base de datos.");
        }

        // Fijar estado de publicacion
        EstadoPublicacion estadoPublicacion = null;
        try {
            estadoPublicacion = estadoPublicacionFacade.findByNombre(NombreEstadoPublicacion.ACTIVA);
        } catch (NoResultException e) {
            context.setRollbackOnly();
            throw new AlquilaCosasException("No se encontro el estado de la publicacion"
                    + " en la base de datos.");
        }
        PublicacionXEstado pxe = new PublicacionXEstado(publicacion, estadoPublicacion);
        publicacion.agregarPublicacionXEstado(pxe);

        // registrar periodos de alquiler
        Periodo periodo1 = periodoFacade.find(periodoMinimoFk);
        publicacion.setMinPeriodoAlquilerFk(periodo1);
        publicacion.setMinValor(periodoMinimo);

        if (periodoMaximoFk != null && periodoMaximoFk > 0) {
            Periodo periodo2 = periodoFacade.find(periodoMaximoFk);;
            publicacion.setMaxPeriodoAlquilerFk(periodo2);
            publicacion.setMaxValor(periodoMaximo);
        }
        // Registrar precios
        Precio precio = null;
        Periodo periodo = null;
        double precioDiario = precios.get(1).getPrecio();

        for (PrecioDTO p : precios) {

            periodo = periodoBean.getPeriodo(p.getPeriodoNombre());
            precio = new Precio();

            if (p.getPrecio() == 0) {
                if (p.getPeriodoNombre() == NombrePeriodo.HORA) {
                    precio.setPrecio(redondearDecimal(precioDiario / 24.0, 2));
                } else if (p.getPeriodoNombre() == NombrePeriodo.SEMANA) {
                    precio.setPrecio(precioDiario * 7.0);
                } else if (p.getPeriodoNombre() == NombrePeriodo.MES) {
                    precio.setPrecio(precioDiario * 30.0);
                }
            } else {
                precio.setPrecio(p.getPrecio());
            }

            precio.setFechaDesde(new Date());
            precio.setPeriodoFk(periodo);
            precio.setFechaDesde(new Date());
            publicacion.agregarPrecio(precio);
        }

        // Agregar imagenes
        for (byte[] bytes : imagenes) {
            ImagenPublicacion ip = new ImagenPublicacion();
            ip.setImagen(bytes);
            publicacion.agregarImagen(ip);
        }

        usuario.agregarPublicacion(publicacion);
        publicacion = publicacionFacade.create(publicacion);
        return publicacion.getPublicacionId();
    }

    @Override
    @PermitAll
    public PublicacionDTO getDatosPublicacion(int publicacionId) throws AlquilaCosasException {

        Publicacion p = publicacionFacade.find(publicacionId);
        PublicacionXEstado pxe = publicacionXEstadoFacade.getPublicacionXEstado(p);

        if (pxe == null) {
            throw new AlquilaCosasException("PublicacionXEstado no encontrado para la publicacion seleccionada.");
        }
        Periodo periodo1 = null;
        Periodo periodo2 = null;
        try {
            periodo1 = periodoFacade.getPeriodo(p.getMinPeriodoAlquilerFk().getPeriodoId());
            if (p.getMaxPeriodoAlquilerFk().getPeriodoId() != null) {
                periodo2 = periodoFacade.getPeriodo(p.getMaxPeriodoAlquilerFk().getPeriodoId());
            }

        } catch (Exception e) {
            System.out.println("el periodo es nulo" + e.getStackTrace());
        }

        PublicacionDTO publicacionDto = new PublicacionDTO(
                p.getPublicacionId(), p.getTitulo(), p.getDescripcion(),
                p.getFechaDesde(), p.getFechaHasta(), p.getDestacada(),
                p.getCantidad(), p.getCategoriaFk(), p.getImagenPublicacionList(),
                pxe.getEstadoPublicacion(), p.getMinValor(), periodo1,
                p.getMaxValor(), periodo2);

        publicacionDto.getPrecios().add(new PrecioDTO());
        publicacionDto.getPrecios().add(new PrecioDTO());
        publicacionDto.getPrecios().add(new PrecioDTO());
        publicacionDto.getPrecios().add(new PrecioDTO());


        for (Precio precio : precioFacade.getUltimoPrecios(p)) {
            if (precio != null) {
                publicacionDto.getPrecios().set(
                        precio.getPeriodoFk().getPeriodoId() - 1,
                        new PrecioDTO(precio.getPrecioId(),
                        precio.getPeriodoFk().getPeriodoId(),
                        precio.getPrecio(),
                        precio.getPeriodoFk().getNombre()));
            }
        }
        return publicacionDto;
    }

    @Override
    @RolesAllowed({"USUARIO", "ADMIN"})
    public void actualizarPublicacion(int publicacionId, String titulo, String descripcion,
            Date fechaDesde, Date fechaHasta, boolean destacada, int cantidad,
            int usuarioId, int categoria, List<PrecioDTO> precios,
            List<byte[]> imagenesAgregar, List<Integer> imagenesABorrar,
            int periodoMinimo, int periodoMinimoFk, Integer periodoMaximo, Integer periodoMaximoFk,
            NombreEstadoPublicacion estadoPublicacion) throws AlquilaCosasException {

        Publicacion publicacion = null;
        try {
            publicacion = publicacionFacade.find(publicacionId);
        } catch (NoResultException e) {
            throw new AlquilaCosasException("No se encontro la Publicacion en la "
                    + "base de datos.");
        }
        publicacion.setTitulo(titulo);
        publicacion.setDescripcion(descripcion);
        publicacion.setDestacada(destacada);
        publicacion.setCantidad(cantidad);

        // Actualizar categoria
        try {
            Categoria c = categoriaFacade.find(categoria);
            publicacion.setCategoriaFk(c);
        } catch (NoResultException e) {
            throw new AlquilaCosasException("No se encontro la Categoria en la "
                    + "base de datos." + e.getMessage());
        }

        // Actualizar estado
        PublicacionXEstado pxe = publicacionXEstadoFacade.getPublicacionXEstado(publicacion);
        if (pxe.getEstadoPublicacion().getNombre() != estadoPublicacion) {

            Calendar hoy = Calendar.getInstance();
            hoy.add(Calendar.DATE, 60);
            pxe.setFechaHasta(hoy.getTime());
            EstadoPublicacion ep = null;

            try {

                ep = estadoPublicacionFacade.findByNombre(estadoPublicacion);

            } catch (NoResultException e) {
                throw new AlquilaCosasException("No se encontro el Estado en la "
                        + "base de datos." + e.getMessage());
            }

            PublicacionXEstado pxeNuevo = new PublicacionXEstado(publicacion, ep);
            publicacion.agregarPublicacionXEstado(pxeNuevo);
        }

        // Actualizar precios
        double precioDiario = precios.get(1).getPrecio();
        DecimalFormat formatter = new DecimalFormat();
        formatter.applyPattern("0.0#");
        String precioHora = formatter.format(precioDiario / 24.0);
        for (PrecioDTO precioDto : precios) {

            if (precioDto.getPrecio() == 0.0) {

                if (precioDto.getPeriodoNombre() == NombrePeriodo.HORA) {
                    precioDto.setPrecio(Double.valueOf(precioHora));
                } else if (precioDto.getPeriodoNombre() == NombrePeriodo.SEMANA) {
                    precioDto.setPrecio(precioDiario * 7.0);
                } else if (precioDto.getPeriodoNombre() == NombrePeriodo.MES) {
                    precioDto.setPrecio(precioDiario * 30.0);
                }

            }
            Periodo periodo = periodoBean.getPeriodo(precioDto.getPeriodoNombre());
            publicacion.actualizarPrecio(precioDto.getPrecioId(), precioDto.getPrecio(), periodo);
        }

        // Actualizar periodos minimos y maximos de alquiler
        Periodo periodo1 = periodoFacade.find(periodoMinimoFk);;
        publicacion.setMinPeriodoAlquilerFk(periodo1);
        publicacion.setMinValor(periodoMinimo);

        if (periodoMaximoFk != null && periodoMaximoFk > 0) {
            Periodo periodo2 = periodoFacade.find(periodoMaximoFk);;
            publicacion.setMaxPeriodoAlquilerFk(periodo2);
            publicacion.setMaxValor(periodoMaximo);
        }

        // Borrar imagenes removidas
        if (imagenesABorrar != null) {
            for (Integer i : imagenesABorrar) {
                ImagenPublicacion ip = imagenPublicacionFacade.find(i);
                publicacion.removerImagen(ip);
            }
        }
        // agregar imagenes nuevas
        for (byte[] imagen : imagenesAgregar) {
            ImagenPublicacion ip = new ImagenPublicacion();
            ip.setImagen(imagen);
            publicacion.agregarImagen(ip);
        }
        publicacion = publicacionFacade.edit(publicacion);
    }

    @Override
    @PermitAll
    public PublicacionDTO getPublicacion(int id) {
        Publicacion publicacion = publicacionFacade.find(id);
        PublicacionDTO resultado = null;
        if (publicacion != null) {
            resultado = new PublicacionDTO(publicacion.getPublicacionId(), publicacion.getTitulo(),
                    publicacion.getDescripcion(), publicacion.getFechaDesde(), publicacion.getFechaHasta(),
                    publicacion.getDestacada(),
                    publicacion.getCantidad());

            resultado.setPeriodoMinimoValor(publicacion.getMinValor());
            resultado.setPeriodoMinimo(publicacion.getMinPeriodoAlquilerFk());
            if (publicacion.getMaxPeriodoAlquilerFk() != null) {
                resultado.setPeriodoMaximoValor(publicacion.getMaxValor());
                resultado.setPeriodoMaximo(publicacion.getMaxPeriodoAlquilerFk());
            } else {
                resultado.setPeriodoMaximoValor(100);
                Periodo temp = new Periodo();
                temp.setNombre(NombrePeriodo.DIA);
                resultado.setPeriodoMaximo(temp);
            }

            UsuarioDTO propietario = new UsuarioDTO(publicacion.getUsuarioFk());
            resultado.setPropietario(propietario);

            Domicilio domicilio = publicacion.getUsuarioFk().getDomicilioList().get(0);
            resultado.setProvincia(domicilio.getProvinciaFk().getNombre());
            resultado.setCiudad(domicilio.getProvinciaFk().getNombre());
            resultado.setBarrio(domicilio.getBarrio());

            resultado.setImagenIds(getIdImagenes(publicacion));

            List<PrecioDTO> precios = precioBean.getPrecios(publicacion);
            resultado.setPrecios(precios);
            resultado.setCategoriaF(new CategoriaDTO(publicacion.getCategoriaFk().getCategoriaId(),
                    publicacion.getCategoriaFk().getNombre()));
        }
        return resultado;
    }

    private List<Integer> getIdImagenes(Publicacion publicacion) {
        List<Integer> imagenes = new ArrayList<Integer>();
        for (ImagenPublicacion imagen : publicacion.getImagenPublicacionList()) {
            imagenes.add(imagen.getImagenPublicacionId());
        }
        if (imagenes.isEmpty()) {
            imagenes.add(new Integer(-1));
        }
        return imagenes;
    }

    @Override
    @PermitAll
    public List<ComentarioDTO> getPreguntas(int publicationId) {
        List<Comentario> comentarios;
        List<ComentarioDTO> resultado = new ArrayList<ComentarioDTO>();
        Publicacion filter = entityManager.find(Publicacion.class, publicationId);
        Query query = entityManager.createNamedQuery("Comentario.findPreguntasByPublicacion");
        query.setParameter("publicacion", filter);
        comentarios = query.getResultList();
        Comentario respuestaTemp;
        ComentarioDTO respuesta;
        for (Comentario comentario : comentarios) {
            respuesta = null;
            respuestaTemp = comentario.getRespuesta();
            if (respuestaTemp != null) {
                respuesta = new ComentarioDTO(respuestaTemp.getComentarioId(),
                        respuestaTemp.getComentario(), respuestaTemp.getFecha(),
                        respuestaTemp.getUsuarioFk().getUsuarioId(),
                        respuestaTemp.getUsuarioFk().getNombre(), null);
            }

            resultado.add(new ComentarioDTO(comentario.getComentarioId(),
                    comentario.getComentario(), comentario.getFecha(),
                    comentario.getUsuarioFk().getUsuarioId(),
                    comentario.getUsuarioFk().getNombre(), respuesta));
        }
        return resultado;
    }

    @Override
    @RolesAllowed({"USUARIO", "ADMIN"})
    public void setPregunta(int publicacionId, ComentarioDTO nuevaPregunta)
            throws AlquilaCosasException {
        Comentario pregunta = new Comentario();
        Publicacion publicacion = entityManager.find(Publicacion.class, publicacionId);
        pregunta.setComentario(nuevaPregunta.getComentario());
        pregunta.setFecha(nuevaPregunta.getFecha());
        pregunta.setPregunta(Boolean.TRUE);
        Usuario usuario = entityManager.find(Usuario.class, nuevaPregunta.getUsuarioId());
        pregunta.setUsuarioFk(usuario);
        pregunta.setPublicacionFk(publicacion);
        entityManager.persist(pregunta);

        // Enviar email de notificacion
        Usuario usuarioDueno = publicacion.getUsuarioFk();
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(true,
                    Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination);
            ObjectMessage message = session.createObjectMessage();

            String asunto = "Has recibido una pregunta por tu articulo " + publicacion.getTitulo();
            String texto = "<html>Hola " + usuarioDueno.getNombre() + ", <br/><br/>"
                    + "Has recibido una pregunta por tu articulo <b>" + publicacion.getTitulo() + "</b>: <br/><br/>"
                    + "'" + nuevaPregunta.getComentario() + "' <br/><br/>"
                    + "Para responder esta pregunta ingresa a tu panel de usuario. <br/><br/>"
                    + "Atentamente, <br/> <b>AlquilaCosas </b>";
            NotificacionEmail notificacion = new NotificacionEmail(usuarioDueno.getEmail(), asunto, texto);
            message.setObject(notificacion);
            producer.send(message);
            session.close();
            connection.close();

        } catch (Exception e) {
            throw new AlquilaCosasException("Excepcion al enviar la notificacion" + e.getMessage());
        }
    }

    @Override
    @RolesAllowed({"USUARIO", "ADMIN"})
    public List<ComentarioDTO> getPreguntasSinResponder(int usuarioId) {
        List<Comentario> comentarios;
        List<ComentarioDTO> resultado = new ArrayList<ComentarioDTO>();
        Usuario filter = entityManager.find(Usuario.class, usuarioId);
        Query query = entityManager.createNamedQuery("Comentario.findPreguntasSinResponderByUsuario");
        query.setParameter("usuario", filter);
        comentarios = query.getResultList();
        Comentario respuesta;
        for (Comentario comentario : comentarios) {
            resultado.add(new ComentarioDTO(comentario.getComentarioId(),
                    comentario.getComentario(), comentario.getFecha(),
                    comentario.getUsuarioFk().getUsuarioId(),
                    comentario.getUsuarioFk().getNombre(), comentario.getPublicacionFk().getPublicacionId(), null));
        }

        return resultado;
    }

    @Override
    @RolesAllowed({"USUARIO", "ADMIN"})
    public void setRespuesta(ComentarioDTO preguntaConRespuesta)
            throws AlquilaCosasException {

        Comentario pregunta = entityManager.find(Comentario.class, preguntaConRespuesta.getId());
        Publicacion publicacion = pregunta.getPublicacionFk();

        Comentario respuesta = new Comentario();
        respuesta.setComentario(preguntaConRespuesta.getRespuesta().getComentario());
        respuesta.setFecha(preguntaConRespuesta.getRespuesta().getFecha());
        respuesta.setPregunta(Boolean.FALSE);

        Usuario usuarioResponde = entityManager.find(Usuario.class, preguntaConRespuesta.getRespuesta().getUsuarioId());
        respuesta.setUsuarioFk(usuarioResponde);
        respuesta.setPublicacionFk(publicacion);

        pregunta.setRespuesta(respuesta);
        entityManager.persist(respuesta);

        Usuario usuarioPregunto = pregunta.getUsuarioFk();

        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(true,
                    Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination);
            ObjectMessage message = session.createObjectMessage();

            String asunto = "Han respondido tu pregunta por el articulo " + publicacion.getTitulo();
            String texto = "<html>Hola " + usuarioPregunto.getNombre() + ", <br/><br/>"
                    + "Han respondido tu pregunta por el articulo <b>" + publicacion.getTitulo() + "</b>: <br/><br/>"
                    + "'" + respuesta.getComentario() + "' <br/>"
                    + "<br/><br/>"
                    + "Atentamente, <br/> <b>AlquilaCosas </b>";
            NotificacionEmail notificacion = new NotificacionEmail(usuarioPregunto.getEmail(), asunto, texto);
            message.setObject(notificacion);
            producer.send(message);
            session.close();
            connection.close();

        } catch (Exception e) {
            throw new AlquilaCosasException(e.getMessage());
        }

    }

    @Override
    @PermitAll
    public List<Periodo> getPeriodos() {
        return periodoFacade.getPeriodosOrderByHoras();

    }

    @Override
    @PermitAll
    public List<Date> getFechasSinStock(int publicationId, int cantidad) {
        Publicacion publicacion = entityManager.find(Publicacion.class, publicationId);
        List<Date> respuesta = new ArrayList<Date>();
        List<Alquiler> alquileres = alquilerFacade.getAlquileresByPublicacionFromToday(publicacion);
        Iterator<Alquiler> itAlquiler = alquileres.iterator();

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
            if (fechaFin.get(Calendar.HOUR_OF_DAY) == 0 && fechaFin.get(Calendar.MINUTE) == 0
                    && fechaFin.get(Calendar.SECOND) == 0) //fechaFin.add(Calendar.SECOND, 1); 
            //No me importan las fechas anteriores a hoy, no son seleccionables
            {
                if (date.before(today)) {
                    date.setTime(today.getTime());
                }
            }
            //Me fijo si en los dias que dura el alquiler analizado hay disponibilidad de
            //productos para el pedido que estoy creando
            if (cantidad <= disponibles - temp.getCantidad()) //Si alcanzan los dias guardo en el hashmap el dia y la cantidad de
            //productos del alquiler, para ir acumulando esta cantidad para cada fecha
            //al final me fijo por cada fecha si alcanza, porque ya tengo todos los 
            //alquileres analizados
            {
                while (date.before(fechaFin)) {
                    Integer acumulado = dataCounter.get(date.getTime().toString());
                    if (acumulado != null) {
                        dataCounter.put(date.getTime().toString(), new Integer(acumulado + temp.getCantidad()));
                    } else {
                        dataCounter.put(date.getTime().toString(), new Integer(temp.getCantidad()));
                    }
                    date.add(Calendar.DATE, 1);
                }
            } else //si no alcanza, marco a todos los dias de este alquiler en el hasmap con el valor
            //de la disponibilidad total de la publicacion, lo cual significa que no va a alcanzar
            //ese dia para hacer el pedido ni siquiera de 1 producto
            {
                while (date.before(fechaFin)) { //temp.getFechaFin())){
                    dataCounter.put(date.getTime().toString(), new Integer(disponibles));
                    date.add(Calendar.DATE, 1);
                }
            }

            if (date.after(lastDate)) {
                lastDate = date;
            }

        }
        //Reviso el hashmap y voy llenando la lista de respuesta con las fechas que 
        //no me alcanza el stock disponible
        Calendar date = Calendar.getInstance();
        date.setTime(new Date());
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);

        if (lastDate.get(Calendar.HOUR_OF_DAY) == 0 && lastDate.get(Calendar.MINUTE) == 0
                && lastDate.get(Calendar.SECOND) == 0) {
            lastDate.add(Calendar.SECOND, 1);
        }
        while (date.before(lastDate)) {
            Integer acumulado = dataCounter.get(date.getTime().toString());
            if (acumulado != null) {
                if (cantidad > (disponibles - acumulado)) {
                    respuesta.add(date.getTime());
                }
            }
            date.add(Calendar.DATE, 1);
        }

        return respuesta;
    }

    @Override
    @RolesAllowed({"USUARIO", "ADMIN"})
    public void crearPedidoAlquiler(int publicationId, int usuarioId,
            Date beginDate, Date endDate, double monto, int cantidad) throws AlquilaCosasException {
        Alquiler nuevoPedido = new Alquiler();
        Publicacion publicacion = entityManager.find(Publicacion.class, publicationId);
        Usuario propietario = publicacion.getUsuarioFk();
        nuevoPedido.setPublicacionFk(publicacion);
        nuevoPedido.setUsuarioFk(entityManager.find(Usuario.class, usuarioId));
        nuevoPedido.setFechaInicio(beginDate);
        nuevoPedido.setFechaFin(endDate);
        nuevoPedido.setMonto(monto);
        nuevoPedido.setCantidad(cantidad);

        entityManager.persist(nuevoPedido);

        estadoAlquiler.saveState(nuevoPedido, EstadoAlquiler.NombreEstadoAlquiler.PEDIDO);

        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(true,
                    Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination);
            ObjectMessage message = session.createObjectMessage();

            String asunto = "Han solicitado alquilar el articulo " + publicacion.getTitulo();
            StringBuilder texto = new StringBuilder();
            texto.append("<html>Hola ");
            texto.append(propietario.getNombre());
            texto.append(", <br/><br/> Has recibido una solicitud de alquiler por el articulo <b>");
            texto.append(publicacion.getTitulo());
            texto.append("</b> <br/><br/>");
            texto.append(" Para aceptarlo, ingresa a tu panel de usuario en alquilaCosas.com.ar ");
            texto.append("<br/><br/><br/> Atentamente, <br/> <b>AlquilaCosas </b>");

            NotificacionEmail notificacion = new NotificacionEmail(propietario.getEmail(), asunto, texto.toString());
            message.setObject(notificacion);
            producer.send(message);
            session.close();
            connection.close();

        } catch (Exception e) {
            throw new AlquilaCosasException(e.getMessage());
        }
    }

    @Override
    @PermitAll
    public double getUserRate(UsuarioDTO propietario) {
        double rating = calificacionFacade.getCalificacionByUsuario(propietario.getId());
        rating *= 0.5;
        rating += 5;
        return rating;//hacer el metodo!!
    }

    private double redondearDecimal(double d, double c) {
        int temp = (int) ((d * Math.pow(10, c)));
        return (((double) temp) * Math.pow(10, -c));
    }
}
