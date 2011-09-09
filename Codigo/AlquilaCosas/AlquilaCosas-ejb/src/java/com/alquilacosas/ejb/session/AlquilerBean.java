/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.common.NotificacionEmail;
import com.alquilacosas.common.Util;
import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.ejb.entity.Alquiler;
import com.alquilacosas.ejb.entity.AlquilerXEstado;
import com.alquilacosas.ejb.entity.EstadoAlquiler;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.facade.AlquilerFacade;
import com.alquilacosas.facade.AlquilerXEstadoFacade;
import com.alquilacosas.facade.EstadoAlquilerFacade;
import com.alquilacosas.facade.PublicacionFacade;
import com.alquilacosas.facade.UsuarioFacade;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
/**
 *
 * @author ignaciogiagante
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@DeclareRoles({"USER", "ADMIN"})
public class AlquilerBean implements AlquilerBeanLocal {

    @Resource(name = "emailConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(name = "jms/notificacionEmailQueue")
    private Destination destination;
    @Resource
    private SessionContext context;
    
    @EJB
    private AlquilerFacade alquilerFacade;
    @EJB
    private AlquilerXEstadoFacade alquilerXEstadoFacade;
    @EJB
    private EstadoAlquilerFacade estadoAlquilerFacade;        
    @EJB
    private PublicacionFacade publicacionFacade;
    @EJB
    private UsuarioFacade usuariofacade;
   
    @Override
    @RolesAllowed({"USUARIO", "ADMIN"})
    public void confirmarPedidoDeAlquiler( int alquilerId ) throws AlquilaCosasException{
        
        try {
            Alquiler alquiler = alquilerFacade.find(alquilerId);
        
            this.crearNuevoEstadoDeAlquiler(alquiler, EstadoAlquiler.NombreEstadoAlquiler.CONFIRMADO);
            this.enviarMail(alquiler, EstadoAlquiler.NombreEstadoAlquiler.CONFIRMADO.name());
            this.revisarPedidos(alquiler);
        } catch (Exception e) {
            context.getRollbackOnly();
            System.out.println("La confirmación del alquiler no pudo realizarse" + e.getStackTrace());
        }
        
    }
    
    /*
     * En el siguiente método el duenio del producto rechaza el pedido de alquiler por el 
     * alquilador
     */
    
    @Override
    public void rechazarPedidoDeAlquiler(Integer alquilerId) throws AlquilaCosasException{
        
        try {
            Alquiler alquiler = alquilerFacade.find(alquilerId);
            this.rechazarAlquiler(alquiler);
        } catch (Exception e) {
            context.getRollbackOnly();
            System.out.println("El rechazo del alquiler no pudo realizarse" + e.getStackTrace());
        }
    }
    
    /*
     * En el siguiente método el alquilador cancela el pedido de alquiler realizado en 
     * algún momento
     */
    
    @Override
    public void cancelarPedidoDeAlquiler(Integer alquilerId) throws AlquilaCosasException{
        
        try {
            Alquiler alquiler = alquilerFacade.find(alquilerId);
            this.cancelarAlquiler(alquiler);
        } catch (Exception e) {
            context.getRollbackOnly();
            System.out.println("La cancelacion del alquiler no pudo realizarse" + e.getStackTrace());
        }
    }
    
    @Override
    public List<AlquilerDTO> getPedidosRecibidos( int usuarioDuenioId ){
        
        Usuario usuarioDuenio = usuariofacade.find(usuarioDuenioId);
        List<Publicacion> publicaciones = usuarioDuenio.getPublicacionList();
        List<AlquilerDTO> pedidos = new ArrayList<AlquilerDTO>();
        
        for( Publicacion p : publicaciones ){
            
            Integer imagenId = this.getIdImagenPrincipal( p );
            List<Alquiler> listaAlquiler = new ArrayList<Alquiler>();
            listaAlquiler = alquilerFacade.getAlquileresPorPublicacion( p,
                    EstadoAlquiler.NombreEstadoAlquiler.PEDIDO);
            
            for( Alquiler a : listaAlquiler ){
                
                EstadoAlquiler ea = estadoAlquilerFacade.getEstadoAlquiler(a.getAlquilerId());
                AlquilerDTO alquilerDto = new AlquilerDTO( p.getPublicacionId(), p.getTitulo(),
                            usuarioDuenioId, a.getAlquilerId(), a.getFechaInicio(), 
                            a.getFechaFin(), a.getCantidad(), ea, imagenId);
                pedidos.add(alquilerDto);
            }         
        }
        
        return pedidos;
    }
    
    @Override
    public List<AlquilerDTO> getPedidosRealizados(Integer usuarioId) {
        
        Usuario usuario = usuariofacade.find(usuarioId);
        List<Alquiler> alquileres = alquilerFacade.getAlquileresPorUsuario(usuario);
        List<AlquilerDTO> pedidos = new ArrayList<AlquilerDTO>();
        
        for( Alquiler a : alquileres ){
            
            Publicacion p = a.getPublicacionFk();
            Integer imagenId = this.getIdImagenPrincipal( p );
            EstadoAlquiler ea = estadoAlquilerFacade.getEstadoAlquiler(a.getAlquilerId());
            
            AlquilerDTO alquilerDto = new AlquilerDTO( p.getPublicacionId(), p.getTitulo(),
                            usuarioId, a.getAlquilerId(), a.getFechaInicio(), 
                            a.getFechaFin(), a.getCantidad(), ea, imagenId);
            pedidos.add(alquilerDto);
        }
        return pedidos;
    }
    
    //mover este método a donde corresponde
    private Integer getIdImagenPrincipal( Publicacion publicacion ) {
        List<Integer> imagenes = new ArrayList<Integer>();
        for (ImagenPublicacion imagen : publicacion.getImagenPublicacionList()) {
            imagenes.add(imagen.getImagenPublicacionId());
        }
        if (imagenes.isEmpty()) {
            imagenes.add(new Integer(-1));
        }
        return imagenes.get(0);
    }
    
    private void revisarPedidos( Alquiler alquiler ) throws AlquilaCosasException{
        
        List<Date> fechas = this.getIntervaloFechas(alquiler.getFechaInicio(), alquiler.getFechaFin());
        List<Alquiler> alquileresPedidos = alquilerFacade.getAlquilerPorPeriodo(alquiler.getFechaInicio(),
                alquiler.getFechaFin(), alquiler.getAlquilerId());
        
        List<Alquiler> alquileresConfirmadosActivos = alquilerFacade.getAlquileresConfirmadosActivos();
       
        if( alquileresPedidos.isEmpty() || alquileresConfirmadosActivos.isEmpty())
            throw new AlquilaCosasException("Los alquileres del periodo no pudieron ser traidos de la BD");
        
        int max = 0;
        int cant = 0;
        int resto = 0;
        for( Date fecha : fechas ){
            for( Alquiler a : alquileresConfirmadosActivos ){
                if( this.contiene(fecha, a) )
                    cant += a.getCantidad();
            }
            if( cant > max ){
                max = cant;  
                cant = 0;
            }
        }
        
        Publicacion p = publicacionFacade.find(alquiler.getPublicacionFk().getPublicacionId());
        
        resto = p.getCantidad() - max;
        
        List<Alquiler> alquileresARechazar = new ArrayList<Alquiler>();
        
        for(Alquiler a: alquileresPedidos) {
            if(a.getCantidad() > resto) {
                alquileresARechazar.add(a);
            }
        }
        for(Alquiler a : alquileresARechazar) {
            this.rechazarAlquiler(a);
        }
     
    }
    
    private List<Date> getIntervaloFechas( Date fechaInicio, Date fechaFin ) {
        
        List<Date> dates = new ArrayList<Date>();
        
        long interval = 24*1000 * 60 * 60; // 1 hour in millis
        long endTime = fechaFin.getTime() ; 
        long curTime = fechaInicio.getTime();
        
        while (curTime <= endTime) {
          dates.add(new Date(curTime));
          curTime += interval;
        }
        
        return dates;
    }
    
    private boolean contiene( Date fecha, Alquiler a ){
        
        if( a.getFechaInicio().after(fecha) || a.getFechaInicio().equals(fecha) 
                || a.getFechaFin().before(fecha) || a.getFechaFin().equals(fecha))
            return true;
        else
            return false;
    }
    
    private void rechazarAlquiler( Alquiler alquiler) throws AlquilaCosasException{
        
        this.crearNuevoEstadoDeAlquiler(alquiler, EstadoAlquiler.NombreEstadoAlquiler.PEDIDO_RECHAZADO);
        String estado = Util.convertNombreEstadoAlquilerToString(EstadoAlquiler.NombreEstadoAlquiler.PEDIDO_RECHAZADO);
        this.enviarMail(alquiler, estado);
    }
    
    private void cancelarAlquiler( Alquiler alquiler) throws AlquilaCosasException{
        
        this.crearNuevoEstadoDeAlquiler(alquiler, EstadoAlquiler.NombreEstadoAlquiler.CANCELADO_ALQUILADOR);
        String estado = Util.convertNombreEstadoAlquilerToString(EstadoAlquiler.NombreEstadoAlquiler.CANCELADO_ALQUILADOR);
        this.enviarMail(alquiler, estado);
    }
    
    private void crearNuevoEstadoDeAlquiler( Alquiler alquiler, EstadoAlquiler.NombreEstadoAlquiler estado){
        
        AlquilerXEstado axeViejo = alquilerXEstadoFacade.findByAlquiler(alquiler.getAlquilerId());
        axeViejo.setFechaHasta( new Date() );
        
        EstadoAlquiler ea = estadoAlquilerFacade.findByNombre(estado);
        
        AlquilerXEstado axe = new AlquilerXEstado( alquiler, ea, new Date() );
        alquiler.agregarAlquilerXEstado(axe);
        alquilerFacade.edit(alquiler);
    }
    
    private void enviarMail( Alquiler alquiler, String estadoAlquiler ) throws AlquilaCosasException{
        
        Publicacion publicacion = publicacionFacade.find(alquiler.getPublicacionFk().getPublicacionId());
        Usuario usuario = usuariofacade.find(alquiler.getUsuarioFk().getUsuarioId());
        Usuario usuarioDuenio = usuariofacade.find(publicacion.getUsuarioFk().getUsuarioId());
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String fechaIncio = formatter.format(alquiler.getFechaInicio());
        String fechaFin = formatter.format(alquiler.getFechaFin());
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(true,
                    Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination);
            ObjectMessage message = session.createObjectMessage();

            String asunto = "Tu alquiler por el/la " + publicacion.getTitulo() + " ha sido " + estadoAlquiler +" ";
            String texto = "<html>Hola " + usuario.getNombre() + ", <br/><br/>"
                    + "El usuario <b>" + usuarioDuenio.getNombre() + "</b> ha <b>"+ estadoAlquiler + "</b> el pedido de alquiler de "
                    +  alquiler.getCantidad() + " articulos solicitados para la fecha " + fechaIncio + " " 
                    + "hasta la fecha " + fechaFin + ". <br/><br/>"
                    + "Atentamente, <br/> <b>AlquilaCosas </b>";
            NotificacionEmail notificacion = new NotificacionEmail(usuario.getEmail(), asunto, texto);
            message.setObject(notificacion);
            producer.send(message);
            session.close();
            connection.close();

        } catch (Exception e) {
            throw new AlquilaCosasException("Excepcion al enviar la notificacion" + e.getMessage());
        }
    }
    
    
 
}
