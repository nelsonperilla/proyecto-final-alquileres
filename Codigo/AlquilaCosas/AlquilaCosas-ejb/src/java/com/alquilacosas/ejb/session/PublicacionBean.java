/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.common.PrecioFacade;
import com.alquilacosas.common.PublicacionFacade;
import com.alquilacosas.ejb.entity.Categoria;
import com.alquilacosas.ejb.entity.EstadoPublicacion;
import com.alquilacosas.ejb.entity.EstadoPublicacion.PublicacionEstado;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Periodo;
import com.alquilacosas.ejb.entity.Precio;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.PublicacionXEstado;
import com.alquilacosas.ejb.entity.PublicacionXEstadoPK;
import com.alquilacosas.ejb.entity.Usuario;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
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
public class PublicacionBean implements PublicacionBeanLocal {
    
    @PersistenceContext(unitName="AlquilaCosas-ejbPU") 
    private EntityManager entityManager;
    
    @Resource
    private SessionContext context;
    
    private Publicacion publicacion;
    private Usuario usuario;
    @Override
    public void registrarPublicacion( String titulo, String descripcion, 
            Date fecha_desde, Date fecha_hasta, boolean destacada, int cantidad,
            int usuarioId, int categoria, List<PrecioFacade> precios, 
            List<ImagenPublicacion> imagenes ) throws AlquilaCosasException {
        
        publicacion = new Publicacion();
        publicacion.setTitulo(titulo);
        publicacion.setDescripcion(descripcion);
        publicacion.setFechaDesde(fecha_desde);
        publicacion.setFechaHasta(fecha_hasta);
        publicacion.setDestacada(destacada);
        publicacion.setCantidad(cantidad);
        
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
   
        PublicacionXEstado pxe = new PublicacionXEstado();
        PublicacionXEstadoPK pxepk = new PublicacionXEstadoPK(
                publicacion.getPublicacionId(), 
                estadoPublicacion.getEstadoPublicacionId());

        pxe.setPublicacion(publicacion);
        pxe.setEstadoPublicacion(estadoPublicacion);
        pxe.setFechaDesde(new Date());
        pxe.setPublicacionXEstadoPK(pxepk);
        
        entityManager.persist(pxe);

        Precio precio = null;
        Periodo periodo = null;
        
        for( PrecioFacade p : precios ){

            periodo = this.getPeriodo(p.getPeriodoNombre());
            precio = new Precio();
            precio.setPeriodoFk(periodo);
            precio.setPrecio(p.getPrecio());
            precio.setPublicacionFk(publicacion);

            entityManager.persist(precio);
        }

        try {
           
            if( imagenes.size() < 1 )
                throw new AlquilaCosasException( "La publicacion debe contener "
                        + "por lo menos una imagen" );
            
            for( ImagenPublicacion ip : imagenes ){
                ip.setPublicacionFk(publicacion);
                entityManager.persist(ip);
            }   
        } catch (NoResultException e) {
            context.setRollbackOnly();
            throw new AlquilaCosasException("La publicacion debe contener "
                    + "por lo menos una imagen"
                    + e.getMessage());
        }
    
    }
    
    @Override
    public PublicacionFacade getDatosPublicacion(int publicacionId) {
        
        Publicacion p = entityManager.find(Publicacion.class, publicacionId);
        PublicacionXEstado pxe = this.getPublicacionEstado(publicacionId);
        
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
    public void actualizarPublicacion( int publicacionId, String titulo, String descripcion, 
            Date fecha_desde, Date fecha_hasta, boolean destacada, int cantidad,
            int usuarioId, int categoria, List<PrecioFacade> precios, 
            List<ImagenPublicacion> imagenes, int estadoPublicacion ) throws AlquilaCosasException {
        
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
        PublicacionXEstado pxe = this.getPublicacionEstado(publicacionId);
        
        if( !pxe.getEstadoPublicacion().getEstadoPublicacionId().equals(ep.getEstadoPublicacionId()) ){

            pxe.setFechaHasta(new Date());
            entityManager.merge(pxe);
            
            PublicacionXEstado pxeNuevo = new PublicacionXEstado();
            PublicacionXEstadoPK pxepk = new PublicacionXEstadoPK();
            pxeNuevo.setEstadoPublicacion(ep);
            pxeNuevo.setPublicacion(publicacion);
            pxeNuevo.setFechaDesde(new Date());
            pxeNuevo.setPublicacionXEstadoPK(pxepk);
            entityManager.persist(pxeNuevo);
        }
        
        entityManager.merge(publicacion);

            
        for( PrecioFacade precioFacade : precios ){
           
          if( precioFacade.getPrecioId() != 0 ){
                
              Precio precioViejo = entityManager.find(Precio.class, precioFacade.getPrecioId());

                if( precioFacade != null ){
                    precioViejo.setPrecio(precioFacade.getPrecio());
                    precioViejo.setPeriodoFk(this.getPeriodo(precioFacade.getPeriodoNombre()));
                    precioViejo.setPublicacionFk(publicacion);
                    publicacion.getPrecioList().set( precioFacade.getPeriodoId() - 1,                            
                                precioViejo);
                    entityManager.merge(precioViejo);
                }  
           }else{
                Precio precioNuevo = new Precio();
                
                if( precioFacade != null ){
                    precioNuevo.setPrecio(precioFacade.getPrecio());
                    precioNuevo.setPeriodoFk(this.getPeriodo(precioFacade.getPeriodoNombre()));
                    precioNuevo.setPublicacionFk(publicacion);
                    publicacion.getPrecioList().set( precioFacade.getPeriodoId() - 1,                            
                                precioNuevo);
                    entityManager.persist(precioNuevo);
                }  
          }
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
    public PublicacionXEstado getPublicacionEstado( int publicacionId ) {
        
        Query query = entityManager.createQuery(
                "SELECT pxe FROM PublicacionXEstado pxe, "
                + "Publicacion p, EstadoPublicacion ep "
                + "WHERE pxe.publicacionXEstadoPK.publicacionFk = :p "
                + "AND pxe.publicacionXEstadoPK.publicacionFk = p.publicacionId "
                + "AND pxe.publicacionXEstadoPK.estadoFk = ep.estadoPublicacionId "
                + "AND pxe.fechaHasta IS NULL "
                 );
        query.setParameter("p", publicacionId);      
        Object object = (Object) query.getSingleResult();    
        PublicacionXEstado pxe = (PublicacionXEstado) object;

        return pxe;
    }
   
}


