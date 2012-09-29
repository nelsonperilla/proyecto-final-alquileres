/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.common.NotificacionEmail;
import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.ejb.entity.EstadoPedidoCambio.NombreEstadoPedidoCambio;
import com.alquilacosas.ejb.entity.Periodo.NombrePeriodo;
import com.alquilacosas.ejb.entity.*;
import com.alquilacosas.facade.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.*;
import javax.jms.*;
import org.apache.log4j.Logger;
/**
 *
 * @author ignaciogiagante
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PedidosBean implements PedidosBeanLocal {

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
    @EJB
    private PedidoCambioFacade pedidoFacade;
    @EJB
    private PedidoCambioXEstadoFacade pcxeFacade;
    @EJB
    private EstadoPedidoCambioFacade estadoPedidoFacade;
    
   
    @Override
    public void confirmarPedidoDeAlquiler( int alquilerId ) throws AlquilaCosasException{
        System.out.println("confirmando alquiler..");
        try {
            Alquiler alquiler = alquilerFacade.find(alquilerId);
        
            this.crearNuevoEstadoDeAlquiler(alquiler, EstadoAlquiler.NombreEstadoAlquiler.CONFIRMADO);
            
            Publicacion publicacion = publicacionFacade.find(alquiler.getPublicacionFk().getPublicacionId());
            Usuario usuario = usuariofacade.find(alquiler.getUsuarioFk().getUsuarioId());
            Usuario usuarioDuenio = usuariofacade.find(publicacion.getUsuarioFk().getUsuarioId());
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String fechaIncio = formatter.format(alquiler.getFechaInicio());
            String fechaFin = formatter.format(alquiler.getFechaFin());
            
            Domicilio domicilio = usuarioDuenio.getDomicilioList().get(0);
            
            // Se envía un mail a la persona que alquiló el producto con los datos del dueño del producto
            System.out.println("enviando emails");
            String asunto = "Tu alquiler por el producto " + publicacion.getTitulo() + " ha sido CONFIRMADO ";
            Integer p = domicilio.getPiso();
            String piso = p != null ? Integer.toString(p) : "";
            String depto = domicilio.getDepto() != null ? domicilio.getDepto() : "";
            String mensaje = "<html><body>Hola " + usuario.getNombre() + ", <br/><br/>"
                    + "El usuario <b>" + usuarioDuenio.getNombre() + "</b> ha <b> CONFIRMADO </b> el pedido de alquiler de "
                    +  alquiler.getCantidad() + " articulo/s solicitado/s para la fecha " + fechaIncio + " " 
                    + "hasta la fecha " + fechaFin + ". <br/>"
                    + "Los datos del usuario " + usuarioDuenio.getLoginList().get(0).getUsername() + " son: <br/>"
                    + "<b>Nombre Completo: </b>" + usuarioDuenio.getApellido() + ", " + usuarioDuenio.getNombre() + "<br/>"
                    + "<b>Dirección: </b>" + domicilio.getCalle() + " " + domicilio.getNumero() + " " + piso + " " + depto + ", Barrio " + domicilio.getBarrio() + "<br/>"
                    + "<b>Ciudad: </b>" + domicilio.getCiudad()+ ", " + domicilio.getProvinciaFk().getNombre() + "<br/>"
                    + "<b>Telefono: </b>" + usuarioDuenio.getTelefono() + "<br/><br/>"
                    + "Atentamente, <br/> <b>AlquilaCosas </b></body></html>";
            NotificacionEmail email = new NotificacionEmail(usuario.getEmail(), asunto, mensaje);
            enviarEmail(email);
            System.out.println("email1: " + mensaje);
            
            // Se envía un mail al duenio del producto con los datos de la persona que alquilo producto.
            
            String mensajeDomicilio = "";
            if(!usuario.getDomicilioList().isEmpty()) {
                domicilio = usuario.getDomicilioList().get(0);
                p = domicilio.getPiso();
                piso = p != null ? Integer.toString(p) : "";
                depto = domicilio.getDepto() != null ? domicilio.getDepto() : "";
                mensajeDomicilio = "<b>Dirección:</b> " + domicilio.getCalle() + " " + domicilio.getNumero() + " " + piso + " " + depto + ", Barrio " + domicilio.getBarrio() + "<br/>"
                    + "<b>Ciudad: </b>" + domicilio.getCiudad()+ ", " + domicilio.getProvinciaFk().getNombre() + "<br/>";
            }
            
            asunto = "Usted ha CONFIRMADO el alquiler de " + publicacion.getTitulo();
            String texto = "<html><body>Hola " + usuarioDuenio.getNombre() + ", <br/><br/>"
                    + "Usted ha <b> CONFIRMADO </b> el pedido de alquiler de "
                    +  alquiler.getCantidad() + " articulos solicitados para la fecha " + fechaIncio + " " 
                    + "hasta la fecha " + fechaFin + ". <br/>"
                    + "Los datos del usuario <b>" + usuario.getLoginList().get(0).getUsername() + "</b> son: <br/>"
                    + "<b>Nombre Completo:</b> " + usuario.getApellido() + ", " + usuario.getNombre() + "<br/>"
                    + mensajeDomicilio
                    + "<b>Telefono: </b>" + usuario.getTelefono() + "<br/><br/>"
                    + "Atentamente, <br/> <b>AlquilaCosas </b></body></html>";
            email = new NotificacionEmail(usuarioDuenio.getEmail(), asunto, texto);
            enviarEmail(email);
            System.out.println("email2: " + texto);
            
            this.revisarPedidos(alquiler); 
        } catch (Exception e) {
            context.getRollbackOnly();
            Logger.getLogger(PedidosBean.class).error("confirmarPedidoDeAlquiler(). "
                    + "Error al confirmar alquiler: " + e + ": " + e.getMessage());
        }
        
    }
    
    /**
     * En este método el dueño del producto rechaza el pedido de alquiler por el 
     * alquilador
     * @param alquilerId
     * @throws AlquilaCosasException 
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
    
    /**
     * En este método el alquilador cancela el pedido de alquiler realizado en 
     * algún momento
     * @param alquilerId
     * @throws AlquilaCosasException 
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
    
    /**
     * Este método devuelve los pedidos de alquiler realizados sobre un producto
     * @param usuarioDuenioId
     * @return 
     */
    @Override
    @PermitAll
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
                
                EstadoAlquiler ea = estadoAlquilerFacade.getEstadoAlquiler(a);
                AlquilerDTO alquilerDto = new AlquilerDTO( p.getPublicacionId(), a.getUsuarioFk().getUsuarioId(),
                            a.getAlquilerId(), imagenId, a.getFechaInicio(), 
                            a.getFechaFin(), ea.getNombre(), p.getTitulo(), a.getUsuarioFk().getLoginList().get(0).getUsername(),
                            a.getCantidad(), a.getMonto(), false);
                pedidos.add(alquilerDto);
               
            }         
        }
        
        return pedidos;
    }
    
    /**
     * Este método devuelve los pedidos de alquiler realizados por el usuario que está logeado
     * @param usuarioId
     * @return 
     */
    @Override
    public List<AlquilerDTO> getPedidosRealizados(Integer usuarioId) {
        
        Usuario usuario = usuariofacade.find(usuarioId);
        List<Alquiler> alquileres = alquilerFacade.getPedidosPorUsuario(usuario);
        List<AlquilerDTO> pedidos = new ArrayList<AlquilerDTO>();
        
        for( Alquiler a : alquileres ){
            
            Publicacion p = a.getPublicacionFk();
            Integer imagenId = this.getIdImagenPrincipal( p );
            EstadoAlquiler ea = estadoAlquilerFacade.getEstadoAlquiler(a);
            
            AlquilerDTO alquilerDto = new AlquilerDTO( p.getPublicacionId(), usuarioId,
                            a.getAlquilerId(), imagenId, a.getFechaInicio(), 
                            a.getFechaFin(), ea.getNombre(), p.getTitulo(), p.getUsuarioFk().getLoginList().get(0).getUsername(),
                            a.getCantidad(), a.getMonto(), false);
            pedidos.add(alquilerDto);
        }
        return pedidos;
    }
    
    
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
    
    /**
     * Aquí se revisan todos los pedidos realizados sobre un producto y se verifica que exista stock
     * @param alquiler
     * @throws AlquilaCosasException 
     */
    private void revisarPedidos( Alquiler alquiler ) throws AlquilaCosasException{
        
        Integer publicacionId = alquiler.getPublicacionFk().getPublicacionId();
        List<Date> fechas = this.getIntervaloFechas(alquiler.getFechaInicio(), alquiler.getFechaFin());
        List<Alquiler> alquileresPedidos = alquilerFacade.getAlquilerPorPeriodo(alquiler.getFechaInicio(),
                alquiler.getFechaFin(), alquiler.getAlquilerId(), publicacionId);
        
        List<Alquiler> alquileresConfirmadosActivos = alquilerFacade.getAlquileresConfirmadosActivos(publicacionId);
        List<PedidoCambio> pedidosDeCambio = alquilerFacade.getCambiosDeAlquileresRecibidos(alquiler.getPublicacionFk());
        
        if( alquileresPedidos.isEmpty() && alquileresConfirmadosActivos.isEmpty() && pedidosDeCambio.isEmpty())
            return;
        
        int max = 0;
        int cant = 0;
        int resto = 0;
        List<PedidoCambio> pedidosDeCambioAfectados = new ArrayList<PedidoCambio>();
        
        for( Date fecha : fechas ){
            for( Alquiler a : alquileresConfirmadosActivos ){
                if( this.contiene(fecha, a.getFechaInicio(), a.getFechaFin()) )
                    cant += a.getCantidad();
            }
            if( cant > max ){
                max = cant;  
                cant = 0;
            }
            for( PedidoCambio pc : pedidosDeCambio ){
                
                Alquiler a = pc.getAlquilerFk();
                Date fechaFinNueva = this.getFechaFinNueva(pc);
                
                if( this.contiene(fecha, a.getFechaFin(), fechaFinNueva) );
                    pedidosDeCambioAfectados.add(pc);        
            } 
        } 
        
        Publicacion p = publicacionFacade.find(alquiler.getPublicacionFk().getPublicacionId());
        
        resto = p.getCantidad() - max;
        
        List<Alquiler> alquileresARechazar = new ArrayList<Alquiler>();
        List<PedidoCambio> pedidosDeCambioARechazar = new ArrayList<PedidoCambio>();
        
        for(Alquiler a: alquileresPedidos) {
            if(a.getCantidad() > resto) {
                alquileresARechazar.add(a);
            }
        }
        for( PedidoCambio pc : pedidosDeCambioAfectados) {
            if(pc.getAlquilerFk().getCantidad() > resto) {
                pedidosDeCambioARechazar.add(pc);
            }
        }
        for(Alquiler a : alquileresARechazar) {
            this.rechazarAlquiler(a);
        } 
        for( PedidoCambio pc : pedidosDeCambioARechazar) {
            this.rechazarPedidoDeCambio(pc.getPedidoCambioId());
        }
        
    }
    
    private Date getFechaFinNueva( PedidoCambio pc ){
        
        Alquiler a = pc.getAlquilerFk();
        Calendar cal = Calendar.getInstance();
        Date fechaInicio = a.getFechaInicio();
        cal.setTime(fechaInicio);
        if ( pc.getPeriodoFk().getNombre() == NombrePeriodo.HORA ) {
            cal.add(Calendar.HOUR_OF_DAY, pc.getDuracion());
        } else {
            cal.add(Calendar.DAY_OF_YEAR, pc.getDuracion());
        }
        Date fechaFinNueva = cal.getTime();
        return fechaFinNueva;
    }
    
    private List<Date> getIntervaloFechas( Date fechaInicio, Date fechaFin ) {
        
        List<Date> dates = new ArrayList<Date>();
        
        long interval = 24*1000 * 60 * 60; // 1 hour in millis
        long endTime = fechaFin.getTime() ; 
        long curTime = fechaInicio.getTime();
        
        while (curTime < endTime) {
          dates.add(new Date(curTime));
          curTime += interval;
        }
        
        return dates;
    }
    /**
     * Se verifica que un objeto fecha este dentro de un rango de fechas
     * @param fecha
     * @param FechaInicio
     * @param fechaFin
     * @return 
     */
    private boolean contiene( Date fecha, Date FechaInicio, Date fechaFin ){
        
        if( (FechaInicio.before(fecha) || FechaInicio.equals(fecha)) && (fechaFin.after(fecha) || fechaFin.equals(fecha)))
            return true;
        else
            return false;
    }
    
    private void rechazarAlquiler(Alquiler alquiler) throws AlquilaCosasException{
        
        this.crearNuevoEstadoDeAlquiler(alquiler, EstadoAlquiler.NombreEstadoAlquiler.PEDIDO_RECHAZADO);
        
        Publicacion publicacion = publicacionFacade.find(alquiler.getPublicacionFk().getPublicacionId());
        Usuario usuario = usuariofacade.find(alquiler.getUsuarioFk().getUsuarioId());
        Usuario usuarioDuenio = usuariofacade.find(publicacion.getUsuarioFk().getUsuarioId());
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String fechaIncio = formatter.format(alquiler.getFechaInicio());
        String fechaFin = formatter.format(alquiler.getFechaFin());
        
        String asunto = "Tu alquiler por el producto " + publicacion.getTitulo() + " ha sido RECHAZADO ";
        String mensaje = "<html>Hola " + usuario.getNombre() + ", <br/><br/>"
                + "El usuario <b>" + usuarioDuenio.getNombre() + "</b> ha <b> RECHAZADO </b> el pedido de alquiler de "
                +  alquiler.getCantidad() + " articulo/s solicitado/s para la fecha " + fechaIncio + " " 
                + "hasta la fecha " + fechaFin + ". <br/><br/>"
                + "Atentamente, <br/> <b>AlquilaCosas </b>";
        NotificacionEmail email = new NotificacionEmail(usuario.getEmail(), asunto, mensaje);
        enviarEmail(email);
    }
    
    private void rechazarPedidoDeCambio(int pedidoCambioId){
          
          PedidoCambio pedido = pedidoFacade.find(pedidoCambioId);

          PedidoCambioXEstado pcxe = pcxeFacade.getPedidoCambioXEstadoActual(pedido);
          pcxe.setFechaHasta(new Date());

          PedidoCambioXEstado pcxeNuevo = new PedidoCambioXEstado();
          pcxeNuevo.setPedidoCambioFk(pedido);

          EstadoPedidoCambio estado = null;
          
          estado = estadoPedidoFacade.findByNombre(NombreEstadoPedidoCambio.RECHAZADO);
          
          pcxeNuevo.setEstadoPedidoCambioFk(estado);
          pcxeNuevo.setFechaDesde(new Date());
          pcxeFacade.edit(pcxe);
          pcxeFacade.create(pcxeNuevo);
    }
    
    private void cancelarAlquiler( Alquiler alquiler) throws AlquilaCosasException{
        
        this.crearNuevoEstadoDeAlquiler(alquiler, EstadoAlquiler.NombreEstadoAlquiler.CANCELADO_ALQUILADOR);
        
        Publicacion publicacion = publicacionFacade.find(alquiler.getPublicacionFk().getPublicacionId());
        Usuario usuario = usuariofacade.find(alquiler.getUsuarioFk().getUsuarioId());
        Usuario usuarioDuenio = usuariofacade.find(publicacion.getUsuarioFk().getUsuarioId());
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String fechaIncio = formatter.format(alquiler.getFechaInicio());
        String fechaFin = formatter.format(alquiler.getFechaFin());
        
        String asunto = "Tu alquiler por el producto " + publicacion.getTitulo() + " ha sido CANCELADO POR EL ALQUILADOR ";
        String mensaje = "<html>Hola " + usuario.getNombre() + ", <br/><br/>"
                + "El usuario <b>" + usuarioDuenio.getNombre() + "</b> ha <b> CANCELADO </b> el pedido de alquiler de "
                +  alquiler.getCantidad() + " articulo/s solicitado/s para la fecha " + fechaIncio + " " 
                + "hasta la fecha " + fechaFin + ". <br/><br/>"
                + "Atentamente, <br/> <b>AlquilaCosas </b>";
       NotificacionEmail email = new NotificacionEmail(usuario.getEmail(), asunto, mensaje);
       enviarEmail(email);
    }
    
    private void crearNuevoEstadoDeAlquiler( Alquiler alquiler, EstadoAlquiler.NombreEstadoAlquiler estado){
        
        AlquilerXEstado axeViejo = alquilerXEstadoFacade.findByAlquiler(alquiler.getAlquilerId());
        axeViejo.setFechaHasta( new Date() );
        
        EstadoAlquiler ea = estadoAlquilerFacade.findByNombre(estado);
        
        AlquilerXEstado axe = new AlquilerXEstado( alquiler, ea, new Date() );
        alquiler.agregarAlquilerXEstado(axe);
        alquilerFacade.edit(alquiler);
    }
    
    private void enviarEmail(NotificacionEmail email) {
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(true,
                    Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination);
            ObjectMessage message = session.createObjectMessage();
            message.setObject(email);
           // producer.send(message);
            session.close();
            connection.close();
        } catch (Exception e) {
            Logger.getLogger(AlquileresTomadosBean.class).error("enviarEmail(). "
                    + "Excepcion al enviar email: " + e + ": " + e.getMessage());
        }
    }
}
