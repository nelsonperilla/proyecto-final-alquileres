/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;


import com.alquilacosas.common.ComentarioFacade;
import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.common.CategoriaFacade;
import com.alquilacosas.common.NotificacionEmail;
import com.alquilacosas.common.PrecioFacade;
import com.alquilacosas.common.PublicacionFacade;
import com.alquilacosas.ejb.entity.Categoria;
import com.alquilacosas.ejb.entity.Comentario;
import com.alquilacosas.ejb.entity.Domicilio;
import com.alquilacosas.ejb.entity.EstadoPublicacion;
import com.alquilacosas.ejb.entity.EstadoPublicacion.PublicacionEstado;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Periodo;
import com.alquilacosas.ejb.entity.Precio;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.PublicacionXEstado;
import com.alquilacosas.ejb.entity.Usuario;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
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
@DeclareRoles ({"USER", "ADMIN"})
public class PublicacionBean implements PublicacionBeanLocal {
    
    @PersistenceContext(unitName="AlquilaCosas-ejbPU") 
    private EntityManager entityManager;
    
    @Resource(name = "emailConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(name = "jms/notificacionEmailQueue")
    private Destination destination;
    
    @Resource
    private SessionContext context;
    
    //private Publicacion publicacion;
    //private Usuario usuario;
    
    @Override
    public void registrarPublicacion( String titulo, String descripcion, 
            Date fecha_desde, Date fecha_hasta, boolean destacada, int cantidad,
            int usuarioId, int categoria, List<PrecioFacade> precios, 
            List<ImagenPublicacion> imagenes ) throws AlquilaCosasException {
        
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
            publicacion.setUsuarioFk(usuario);
        } catch (NoResultException  e) {
            context.setRollbackOnly();
            throw new AlquilaCosasException("No se encontro el Usuario en la "
                    + "base de datos." + e.getMessage());
        }
        
        try {
            Categoria c = entityManager.find(Categoria.class, categoria);
            publicacion.setCategoriaFk(c); 
        } catch (NoResultException  e) {
            context.setRollbackOnly();
            throw new AlquilaCosasException("No se encontro la Categoria en la "
                    + "base de datos." + e.getMessage());
        }

        entityManager.persist(publicacion);
        EstadoPublicacion estadoPublicacion = null;
        
        try {
            Query query = entityManager.createNamedQuery("EstadoPublicacion.findByNombre");
            query.setParameter("nombre", PublicacionEstado.ACTIVA);
            estadoPublicacion = (EstadoPublicacion) query.getSingleResult();
        } catch (Exception e) {
            context.setRollbackOnly();
            throw new AlquilaCosasException("No se encontro el estado de la publicacion"
                    + " en la base de datos." + e.getMessage());
        }
   
        PublicacionXEstado pxe = new PublicacionXEstado(publicacion, estadoPublicacion);
        entityManager.persist(pxe);

        Precio precio = null;
        Periodo periodo = null;
        double pre = precios.get(1).getPrecio();
        
        
        for( PrecioFacade p : precios ){
            
            periodo = this.getPeriodo(p.getPeriodoNombre());
            precio = new Precio();
            
            if( p.getPrecio() == 0 ){
                if( p.getPeriodoNombre().equalsIgnoreCase("hora") ){
                    p.setPrecio(pre / 10.0);
                }else if( p.getPeriodoNombre().equalsIgnoreCase("semana") ){
                    p.setPrecio(pre * 7.0);
                }else if( p.getPeriodoNombre().equalsIgnoreCase("mes") ){
                    p.setPrecio(pre * 30.0);
                } 
            }else{
                precio.setPrecio(p.getPrecio());
            }
            
            
            precio.setPeriodoFk(periodo);
            precio.setPublicacionFk(publicacion);
            
            entityManager.persist(precio);
        }

            
         for( ImagenPublicacion ip : imagenes ){
                ip.setPublicacionFk(publicacion);
                entityManager.persist(ip);
         }   
    
    }
        
    @Override
    public PublicacionFacade getDatosPublicacion(int publicacionId) {
        
        Publicacion p = entityManager.find(Publicacion.class, publicacionId);
        PublicacionXEstado pxe = this.getPublicacionEstado(p);
        
        PublicacionFacade pf = new PublicacionFacade( 
                p.getPublicacionId(), p.getTitulo(), p.getDescripcion(), 
                p.getFechaDesde(), p.getFechaHasta(), p.getDestacada(), 
                p.getCantidad(), p.getCategoriaFk(), p.getImagenPublicacionList(),
                pxe.getEstadoPublicacion());
       
        pf.getPrecios().add( new PrecioFacade( 0, 1, 0.0, "HORA" ) );
        pf.getPrecios().add( new PrecioFacade( 0, 2, 0.0, "DIA" ) );
        pf.getPrecios().add( new PrecioFacade( 0, 3, 0.0, "SEMANA" ) );
        pf.getPrecios().add( new PrecioFacade( 0, 4, 0.0, "MES" ) );


        for( Precio precio : p.getPrecioList() ){          
                if( precio != null ){
                    pf.getPrecios().set( 
                            precio.getPeriodoFk().getPeriodoId() - 1, 
                            new PrecioFacade( precio.getPrecioId(),
                            precio.getPeriodoFk().getPeriodoId(),
                            precio.getPrecio(),
                            precio.getPeriodoFk().getNombre()) );
                }
          }
            
        return pf;    
    }
    
    
    @Override
    @RolesAllowed ({"USER", "ADMIN"})
    public void actualizarPublicacion( int publicacionId, String titulo, String descripcion, 
            Date fecha_desde, Date fecha_hasta, boolean destacada, int cantidad,
            int usuarioId, int categoria, List<PrecioFacade> precios, 
            List<byte[]> imagenesAgregar, List<Integer> imagenesABorrar, int estadoPublicacion ) throws AlquilaCosasException {
        
        Publicacion publicacion = null;
        
        try {
            publicacion = entityManager.find(Publicacion.class, publicacionId);
        } catch (NoResultException  e) {
            context.setRollbackOnly();
            throw new AlquilaCosasException("No se encontro la Publicacion en la "
                    + "base de datos." + e.getMessage());
        }
        
        publicacion.setTitulo(titulo);
        publicacion.setDescripcion(descripcion);
        publicacion.setFechaDesde(fecha_desde);
        publicacion.setFechaHasta(fecha_hasta);
        publicacion.setDestacada(destacada);
        publicacion.setCantidad(cantidad);
        
        Usuario usuario = null;
        try {
            usuario = entityManager.find(Usuario.class, usuarioId);
            publicacion.setUsuarioFk(usuario);
        } catch (NoResultException  e) {
            context.setRollbackOnly();
            throw new AlquilaCosasException("No se encontro el Usuario en la "
                    + "base de datos." + e.getMessage());
        }      
        
        try {
            Categoria c = entityManager.find(Categoria.class, categoria);
            publicacion.setCategoriaFk(c); 
        } catch (NoResultException  e) {
            context.setRollbackOnly();
            throw new AlquilaCosasException("No se encontro la Categoria en la "
                    + "base de datos." + e.getMessage());
        }
        
        EstadoPublicacion ep = entityManager.find(EstadoPublicacion.class, estadoPublicacion);
        PublicacionXEstado pxe = this.getPublicacionEstado(publicacion);
        
        if( !pxe.getEstadoPublicacion().getEstadoPublicacionId().equals(ep.getEstadoPublicacionId()) ){

            pxe.setFechaHasta(new Date());
            entityManager.merge(pxe);
            
            PublicacionXEstado pxeNuevo = new PublicacionXEstado(publicacion, ep);
            pxeNuevo.setFechaDesde(new Date());
            entityManager.persist(pxeNuevo);
        }
        
        entityManager.merge(publicacion);
        
        Periodo periodo = null;
        double pre = precios.get(1).getPrecio();
            
        for( PrecioFacade precioFacade : precios ){
           
          if( precioFacade.getPrecioId() != 0 ){
              
              periodo = this.getPeriodo(precioFacade.getPeriodoNombre());
                
              Precio precioViejo = entityManager.find(Precio.class, precioFacade.getPrecioId());

                if( precioFacade != null ){
                    
                  if( precioFacade.getPrecio() == 0 ){
                    if( precioFacade.getPeriodoNombre().equalsIgnoreCase("hora") ){
                        precioFacade.setPrecio(pre / 10.0);
                    }else if( precioFacade.getPeriodoNombre().equalsIgnoreCase("semana") ){
                        precioFacade.setPrecio(pre * 7.0);
                    }else if( precioFacade.getPeriodoNombre().equalsIgnoreCase("mes") ){
                        precioFacade.setPrecio(pre * 30.0);
                    } 
                  }else{
                   precioViejo.setPrecio(precioFacade.getPrecio());
                  }
                  precioViejo.setPeriodoFk(this.getPeriodo(precioFacade.getPeriodoNombre()));
                  precioViejo.setPublicacionFk(publicacion);
                  entityManager.merge(precioViejo);
                }  
           }else{
                Precio precioNuevo = new Precio();
                
                if( precioFacade != null ){
                    if( precioFacade.getPrecio() == 0 ){
                        if( precioFacade.getPeriodoNombre().equalsIgnoreCase("hora") ){
                            precioFacade.setPrecio(pre / 10.0);
                        }else if( precioFacade.getPeriodoNombre().equalsIgnoreCase("semana") ){
                            precioFacade.setPrecio(pre * 7.0);
                        }else if( precioFacade.getPeriodoNombre().equalsIgnoreCase("mes") ){
                            precioFacade.setPrecio(pre * 30.0);
                        } 
                    }else{
                        precioNuevo.setPrecio(precioFacade.getPrecio());
                    }
                    precioNuevo.setPrecio(precioFacade.getPrecio());
                    precioNuevo.setPeriodoFk(this.getPeriodo(precioFacade.getPeriodoNombre()));
                    precioNuevo.setPublicacionFk(publicacion);

                    entityManager.persist(precioNuevo);

                }  
           } 
        }
        
          if( imagenesABorrar != null ){
                for( Integer i : imagenesABorrar ){
                    ImagenPublicacion ip = new ImagenPublicacion(i);
                    entityManager.remove(entityManager.merge(ip));
                }
                
            }
            
          for( byte[] imagen : imagenesAgregar ){
                ImagenPublicacion ip = new ImagenPublicacion();
                ip.setImagen(imagen);        
                ip.setPublicacionFk(publicacion);
                entityManager.persist(ip);
          }
   }             
    

    
    @Override
    public List<Periodo> getPeriodos(){
        Query query = entityManager.createNamedQuery("Periodo.findAll");
        return query.getResultList();
    }
    
    @Override
    public Periodo getPeriodo( String nombrePeriodo ){
        Query query = entityManager.createNamedQuery("Periodo.findByNombre");
        query.setParameter("nombre", nombrePeriodo);
        Periodo periodo = (Periodo)query.getSingleResult();
        return periodo;
    }

    @Override
    public PublicacionXEstado getPublicacionEstado( Publicacion p ) {
       
//        Query query = entityManager.createNamedQuery("PublicacionXEstado.findByPublicacionFk");
//        query.setParameter("publicacion", p);
            
        Query query = entityManager.createQuery(
                "SELECT pxe FROM PublicacionXEstado pxe "
                + "WHERE pxe.publicacion = :publicacion "
                + "AND pxe.fechaHasta IS NULL"
                );
        query.setParameter("publicacion", p);
//        
//        Query query = entityManager.createQuery(
//                "SELECT pxe FROM PublicacionXEstado pxe, "
//                + "Publicacion p, EstadoPublicacion ep "
//                + "WHERE pxe.publicacionXEstadoPK.publicacionFk = :p "
//                + "AND pxe.publicacionXEstadoPK.publicacionFk = p.publicacionId "
//                + "AND pxe.publicacionXEstadoPK.estadoFk = ep.estadoPublicacionId "
//                + "AND pxe.fechaDesde = ( SELECT MAX(pxe1.fechaDesde) FROM  PublicacionXEstado pxe1 "
//                + "WHERE pxe.publicacionXEstadoPK.publicacionFk = pxe1.publicacionXEstadoPK.publicacionFk )"
//                 );
      
        PublicacionXEstado pxe = (PublicacionXEstado) query.getSingleResult();

        return pxe;
    }

    
    @Override
    public PublicacionFacade getPublicacion(int id){
        Publicacion publicacion=entityManager.find(Publicacion.class,id);
        PublicacionFacade resultado = null;
        if(publicacion!=null){
            resultado = new PublicacionFacade(publicacion.getPublicacionId(), publicacion.getTitulo(),
                    publicacion.getDescripcion(), publicacion.getFechaDesde(), publicacion.getFechaHasta(), publicacion.getDestacada(),
                    publicacion.getCantidad());

            Domicilio domicilio = publicacion.getUsuarioFk().getDomicilioList().get(0);
            resultado.setPais(domicilio.getProvinciaFk().getPaisFk().getNombre());
            resultado.setCiudad(domicilio.getProvinciaFk().getNombre());            
            
            resultado.setImagenIds(getImagesIds(publicacion));            
            resultado.setPrecios(getPrecios(publicacion));
            resultado.setCategoriaF(new CategoriaFacade(publicacion.getCategoriaFk().getCategoriaId(),
                    publicacion.getCategoriaFk().getNombre()));
        }
        return resultado;
    }

    private List<Integer> getImagesIds(Publicacion publicacion){
        List<Integer> imagenes = new ArrayList<Integer>();
        for(ImagenPublicacion imagen: publicacion.getImagenPublicacionList()) {
            imagenes.add(imagen.getImagenPublicacionId());
        }
        if(imagenes.isEmpty())
            imagenes.add(new Integer(-1));
        return imagenes;
    }

    private List<PrecioFacade> getPrecios(Publicacion filter){
        List<PrecioFacade> resultado = new ArrayList<PrecioFacade>();
        List<Precio> precios;
        Query query = entityManager.createNamedQuery("Precio.findByPublicacion");
        query.setParameter("publicacion", filter);
        precios = query.getResultList();   
        
        for(Precio precio: precios)
            resultado.add(new PrecioFacade(precio.getPrecio(), precio.getPeriodoFk().getNombre()));
        
        return resultado;

    }
    
    @Override
    public List<ComentarioFacade> getPreguntas(int publicationId) {
        List<Comentario> comentarios;
        List<ComentarioFacade> resultado = new ArrayList<ComentarioFacade>();
        Publicacion filter=entityManager.find(Publicacion.class,publicationId);
        Query query = entityManager.createNamedQuery("Comentario.findPreguntasByPublicacion");
        query.setParameter("publicacion", filter);
        comentarios = query.getResultList();   
        Comentario respuestaTemp;
        ComentarioFacade respuesta;
        for(Comentario comentario : comentarios){
            respuesta = null;
            respuestaTemp = comentario.getRespuesta();
            if(respuestaTemp != null)
                respuesta = new ComentarioFacade(respuestaTemp.getComentarioId(),
                    respuestaTemp.getComentario(), respuestaTemp.getFecha(), 
                    respuestaTemp.getUsuarioFk().getUsuarioId(), 
                    respuestaTemp.getUsuarioFk().getNombre(), null);
            
            resultado.add(new ComentarioFacade(comentario.getComentarioId(),
                    comentario.getComentario(), comentario.getFecha(), 
                    comentario.getUsuarioFk().getUsuarioId(), 
                    comentario.getUsuarioFk().getNombre(), respuesta ));
        }
        return resultado;
    }

    @Override
    public void setPregunta(int publicacionId, ComentarioFacade nuevaPregunta)
            throws AlquilaCosasException {
        Comentario pregunta = new Comentario();
        Publicacion publicacion = entityManager.find(Publicacion.class, publicacionId);
        pregunta.setPublicacionFk(publicacion);
        pregunta.setComentario(nuevaPregunta.getComentario());
        pregunta.setFecha(nuevaPregunta.getFecha());
        pregunta.setPregunta(Boolean.TRUE);
        Usuario usuario = entityManager.find(Usuario.class, nuevaPregunta.getUsuarioId());
        pregunta.setUsuarioFk(usuario);
        
        entityManager.persist(pregunta);
        
        Usuario usuarioDueno = publicacion.getUsuarioFk();
        
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(true,
                    Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination);
            ObjectMessage message = session.createObjectMessage();
            
            String asunto = "Has recibido una pregunta por tu articulo " + publicacion.getTitulo();
            String texto = "<html>Hola " + usuarioDueno.getNombre() + ", <br/><br/>" + 
                    "Has recibido una pregunta por tu articulo <b>" + publicacion.getTitulo() + "</b>: <br/><br/>" +
                    "'" + nuevaPregunta.getComentario() + "' <br/><br/>" +
                    "Para responder esta pregunta ingresa a tu panel de usuario. <br/><br/>" +
                    "Atentamente, <br/> <b>AlquilaCosas </b>";
            NotificacionEmail notificacion = new NotificacionEmail(usuarioDueno.getEmail(), asunto, texto);
            message.setObject(notificacion);
            producer.send(message);
            session.close();
            connection.close();
            
        } catch (Exception e) {
            context.setRollbackOnly();
            throw new AlquilaCosasException(e.getMessage());
        }
    }

    @Override
    public List<ComentarioFacade> getPreguntasSinResponder(int usuarioId) {
        List<Comentario> comentarios;
        List<ComentarioFacade> resultado = new ArrayList<ComentarioFacade>();
        Usuario filter=entityManager.find(Usuario.class,usuarioId);
        Query query = entityManager.createNamedQuery("Comentario.findPreguntasSinResponderByUsuario");
        query.setParameter("usuario", filter);
        comentarios = query.getResultList();   
        Comentario respuesta;
        for(Comentario comentario : comentarios)
                resultado.add(new ComentarioFacade(comentario.getComentarioId(),
                    comentario.getComentario(), comentario.getFecha(), 
                    comentario.getUsuarioFk().getUsuarioId(), 
                    comentario.getUsuarioFk().getNombre(),comentario.getPublicacionFk().getPublicacionId(), null ));
        
        return resultado;
    }    

    @Override 
    public void setRespuesta(ComentarioFacade preguntaConRespuesta) 
            throws AlquilaCosasException {
        Comentario respuesta = new Comentario();
        Comentario pregunta = entityManager.find(Comentario.class , preguntaConRespuesta.getId());
        Publicacion publicacion = pregunta.getPublicacionFk();
        respuesta.setPublicacionFk(publicacion);
        respuesta.setComentario(preguntaConRespuesta.getRespuesta().getComentario());
        respuesta.setFecha(preguntaConRespuesta.getRespuesta().getFecha());
        respuesta.setPregunta(Boolean.FALSE);
        Usuario usuarioResponde = entityManager.find(Usuario.class, preguntaConRespuesta.getRespuesta().getUsuarioId());
        respuesta.setUsuarioFk(usuarioResponde);
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
            String texto = "<html>Hola " + usuarioPregunto.getNombre() + ", <br/><br/>" + 
                    "Han respondido tu pregunta por el articulo <b>" + publicacion.getTitulo() + "</b>: <br/><br/>" +
                    "'" + respuesta.getComentario() + "' <br/>" +
                    "<br/><br/>" +
                    "Atentamente, <br/> <b>AlquilaCosas </b>";
            NotificacionEmail notificacion = new NotificacionEmail(usuarioPregunto.getEmail(), asunto, texto);
            message.setObject(notificacion);
            producer.send(message);
            session.close();
            connection.close();
            
        } catch (Exception e) {
            context.setRollbackOnly();
            throw new AlquilaCosasException(e.getMessage());
        }

    }    

}


