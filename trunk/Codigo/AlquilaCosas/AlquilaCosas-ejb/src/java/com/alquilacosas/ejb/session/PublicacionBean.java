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
import com.alquilacosas.ejb.entity.Categoria;
import com.alquilacosas.ejb.entity.Comentario;
import com.alquilacosas.ejb.entity.Domicilio;
import com.alquilacosas.ejb.entity.EstadoPublicacion;
import com.alquilacosas.ejb.entity.EstadoPublicacion.NombreEstadoPublicacion;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Periodo;
import com.alquilacosas.ejb.entity.Periodo.NombrePeriodo;
import com.alquilacosas.ejb.entity.Precio;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.PublicacionXEstado;
import com.alquilacosas.ejb.entity.Usuario;
import java.util.ArrayList;
import java.util.Date;
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
@DeclareRoles({"USER", "ADMIN"})
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

    @Override
    @RolesAllowed({"USUARIO", "ADMIN"})
    public void registrarPublicacion(String titulo, String descripcion,
            Date fecha_desde, Date fecha_hasta, boolean destacada, int cantidad,
            int usuarioId, int categoria, List<PrecioDTO> precios,
            List<byte[]> imagenes) throws AlquilaCosasException {

        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo(titulo);
        publicacion.setDescripcion(descripcion);
        publicacion.setFechaDesde(fecha_desde);
        publicacion.setFechaHasta(fecha_hasta);
        publicacion.setDestacada(destacada);
        publicacion.setCantidad(cantidad);

        Usuario usuario = null;

        try {
            usuario = entityManager.find(Usuario.class, usuarioId);
        } catch (NoResultException e) {
            throw new AlquilaCosasException("No se encontro el Usuario en la "
                    + "base de datos.");
        }

        try {
            Categoria c = entityManager.find(Categoria.class, categoria);
            publicacion.setCategoriaFk(c);
        } catch (NoResultException e) {
            throw new AlquilaCosasException("No se encontro la Categoria en la "
                    + "base de datos.");
        }

        EstadoPublicacion estadoPublicacion = null;
        try {
            Query query = entityManager.createNamedQuery("EstadoPublicacion.findByNombre");
            query.setParameter("nombre", NombreEstadoPublicacion.ACTIVA);
            estadoPublicacion = (EstadoPublicacion) query.getSingleResult();
        } catch (NoResultException e) {
            context.setRollbackOnly();
            throw new AlquilaCosasException("No se encontro el estado de la publicacion"
                    + " en la base de datos.");
        }

        PublicacionXEstado pxe = new PublicacionXEstado(publicacion, estadoPublicacion);
        publicacion.agregarPublicacionXEstado(pxe);

        Precio precio = null;
        Periodo periodo = null;
        double pre = precios.get(1).getPrecio();

        for (PrecioDTO p : precios) {

            periodo = periodoBean.getPeriodo(p.getPeriodoNombre());
            precio = new Precio();

            if (p.getPrecio() == 0) {
                if (p.getPeriodoNombre() == NombrePeriodo.HORA) {
                    p.setPrecio(pre / 10.0);
                } else if (p.getPeriodoNombre() == NombrePeriodo.SEMANA) {
                    p.setPrecio(pre * 7.0);
                } else if (p.getPeriodoNombre()  == NombrePeriodo.MES) {
                    p.setPrecio(pre * 30.0);
                }
            } else {
                precio.setPrecio(p.getPrecio());
            }


            precio.setPeriodoFk(periodo);
            publicacion.agregarPrecio(precio);
        }


        for (byte[] bytes : imagenes) {
            ImagenPublicacion ip = new ImagenPublicacion();
            ip.setImagen(bytes);
            publicacion.agregarImagen(ip);
        }

        usuario.agregarPublicacion(publicacion);
        entityManager.persist(publicacion);
    }

    @Override
    @PermitAll
    public PublicacionDTO getDatosPublicacion(int publicacionId) throws AlquilaCosasException {

        Publicacion p = entityManager.find(Publicacion.class, publicacionId);
        PublicacionXEstado pxe = this.getPublicacionEstado(p);

        if (pxe == null) {
            throw new AlquilaCosasException("PublicacionXEstado no encontrado para la publicacion seleccionada.");
        }

        PublicacionDTO pf = new PublicacionDTO(
                p.getPublicacionId(), p.getTitulo(), p.getDescripcion(),
                p.getFechaDesde(), p.getFechaHasta(), p.getDestacada(),
                p.getCantidad(), p.getCategoriaFk(), p.getImagenPublicacionList(),
                pxe.getEstadoPublicacion());

        pf.getPrecios().add(new PrecioDTO());
        pf.getPrecios().add(new PrecioDTO());
        pf.getPrecios().add(new PrecioDTO());
        pf.getPrecios().add(new PrecioDTO());


        for (Precio precio : p.getPrecioList()) {
            if (precio != null) {
                pf.getPrecios().set(
                        precio.getPeriodoFk().getPeriodoId() - 1,
                        new PrecioDTO(precio.getPrecioId(),
                        precio.getPeriodoFk().getPeriodoId(),
                        precio.getPrecio(),
                        precio.getPeriodoFk().getNombre()));
            }
        }
        return pf;
    }

    @Override
    @RolesAllowed({"USUARIO", "ADMIN"})
    public void actualizarPublicacion(int publicacionId, String titulo, String descripcion,
            Date fecha_desde, Date fecha_hasta, boolean destacada, int cantidad,
            int usuarioId, int categoria, List<PrecioDTO> precios,
            List<byte[]> imagenesAgregar, List<Integer> imagenesABorrar,
            NombreEstadoPublicacion estadoPublicacion) throws AlquilaCosasException {

        Publicacion publicacion = null;
        try {
            publicacion = entityManager.find(Publicacion.class, publicacionId);
        } catch (NoResultException e) {
            throw new AlquilaCosasException("No se encontro la Publicacion en la "
                    + "base de datos.");
        }
        publicacion.setTitulo(titulo);
        publicacion.setDescripcion(descripcion);
        publicacion.setDestacada(destacada);
        publicacion.setCantidad(cantidad);

        try {
            Categoria c = entityManager.find(Categoria.class, categoria);
            publicacion.setCategoriaFk(c);
        } catch (NoResultException e) {
            throw new AlquilaCosasException("No se encontro la Categoria en la "
                    + "base de datos." + e.getMessage());
        }

        PublicacionXEstado pxe = this.getPublicacionEstado(publicacion);

        if (pxe.getEstadoPublicacion().getNombre() != estadoPublicacion) {

            pxe.setFechaHasta(new Date());
            Query estadoQuery = entityManager.createNamedQuery("EstadoPublicacion.findByNombre");
            estadoQuery.setParameter("nombre", estadoPublicacion);
            EstadoPublicacion ep = null;
            try {
                ep = (EstadoPublicacion) estadoQuery.getSingleResult();
            } catch (NoResultException e) {
                throw new AlquilaCosasException("No se encontro el Estado en la "
                        + "base de datos." + e.getMessage());
            }

            PublicacionXEstado pxeNuevo = new PublicacionXEstado(publicacion, ep);
            publicacion.agregarPublicacionXEstado(pxeNuevo);
        }

        double precioDiario = precios.get(1).getPrecio();

        for (PrecioDTO precioFacade : precios) {

            if (precioFacade.getPrecioId() != 0) {

                if (precioFacade.getPrecio() == 0.0) {
                    if (precioFacade.getPeriodoNombre() == NombrePeriodo.DIA) {
                        precioFacade.setPrecio(precioDiario / 10.0);
                    } else if (precioFacade.getPeriodoNombre() == NombrePeriodo.SEMANA) {
                        precioFacade.setPrecio(precioDiario * 7.0);
                    } else if (precioFacade.getPeriodoNombre() == NombrePeriodo.MES) {
                        precioFacade.setPrecio(precioDiario * 30.0);
                    }
                }
                publicacion.actualizarPrecio(precioFacade.getPrecioId(), precioFacade.getPrecio());

            } else {
                
                Precio precioNuevo = new Precio();
                if (precioFacade.getPrecio() == 0) {
                    if (precioFacade.getPeriodoNombre() == NombrePeriodo.DIA) {
                        precioFacade.setPrecio(precioDiario / 10.0);
                    } else if (precioFacade.getPeriodoNombre() == NombrePeriodo.SEMANA) {
                        precioFacade.setPrecio(precioDiario * 7.0);
                    } else if (precioFacade.getPeriodoNombre() == NombrePeriodo.MES) {
                        precioFacade.setPrecio(precioDiario * 30.0);
                    }
                    precioNuevo.setPrecio(precioFacade.getPrecio());
                    Periodo period = periodoBean.getPeriodo(precioFacade.getPeriodoNombre());
                    precioNuevo.setPeriodoFk(period);
                    publicacion.agregarPrecio(precioNuevo);
                }
            }
        }

        if (imagenesABorrar != null) {
            for (Integer i : imagenesABorrar) {
                ImagenPublicacion ip = entityManager.find(ImagenPublicacion.class, i);
                publicacion.removerImagen(ip);
            }
        }

        for (byte[] imagen : imagenesAgregar) {
            ImagenPublicacion ip = new ImagenPublicacion();
            ip.setImagen(imagen);
            publicacion.agregarImagen(ip);
        }
        entityManager.merge(publicacion);
    }

    private PublicacionXEstado getPublicacionEstado(Publicacion p) {

        Query query = entityManager.createQuery(
                "SELECT pxe FROM PublicacionXEstado pxe "
                + "WHERE pxe.publicacion = :publicacion "
                + "AND pxe.fechaHasta IS NULL");
        query.setParameter("publicacion", p);

        PublicacionXEstado pxe = null;
        try {
            pxe = (PublicacionXEstado) query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("PublicacionXEstado no encontrada");
        }

        return pxe;
    }

    @Override
    @PermitAll
    public PublicacionDTO getPublicacion(int id) {
        Publicacion publicacion = entityManager.find(Publicacion.class, id);
        PublicacionDTO resultado = null;
        if (publicacion != null) {
            resultado = new PublicacionDTO(publicacion.getPublicacionId(), publicacion.getTitulo(),
                    publicacion.getDescripcion(), publicacion.getFechaDesde(), publicacion.getFechaHasta(), publicacion.getDestacada(),
                    publicacion.getCantidad());

            Domicilio domicilio = publicacion.getUsuarioFk().getDomicilioList().get(0);
            resultado.setPais(domicilio.getProvinciaFk().getPaisFk().getNombre());
            resultado.setCiudad(domicilio.getProvinciaFk().getNombre());

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
}
