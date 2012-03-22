/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.common.NotificacionEmail;
import com.alquilacosas.dto.DomicilioDTO;
import com.alquilacosas.dto.UsuarioDTO;
import com.alquilacosas.ejb.entity.*;
import com.alquilacosas.ejb.entity.EstadoUsuario.NombreEstadoUsuario;
import com.alquilacosas.ejb.entity.Rol.NombreRol;
import com.alquilacosas.facade.EstadoUsuarioFacade;
import com.alquilacosas.facade.LoginFacade;
import com.alquilacosas.facade.PaisFacade;
import com.alquilacosas.facade.ProvinciaFacade;
import com.alquilacosas.facade.RolFacade;
import com.alquilacosas.facade.UsuarioFacade;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class UsuarioBean implements UsuarioBeanLocal {

    @Resource(name = "emailConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(name = "jms/notificacionEmailQueue")
    private Destination destination;
    
    @Resource
    private SessionContext context;
    
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private ProvinciaFacade provinciaFacade;
    @EJB
    private PaisFacade paisFacade;
    @EJB
    private RolFacade rolFacade;
    @EJB
    private LoginFacade loginFacade;
    @EJB 
    private EstadoUsuarioFacade estadoUsuarioFacade;
    
    @Override
    public void registrarUsuario(String username, String password, String nombre,
            String apellido, DomicilioDTO dom, int prov,
            Date fechaNacimiento, String dni, String telefono, String email) 
            throws AlquilaCosasException {

        Usuario u = usuarioFacade.findByEmail(email);
        if(u != null) {
            throw new AlquilaCosasException("El email ya esta registrado para otro usuario");
        }
        
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setFechaNac(fechaNacimiento);
        usuario.setDni(dni);
        usuario.setTelefono(telefono);
        usuario.setEmail(email);

        Provincia provincia = provinciaFacade.find(prov);
        Domicilio domicilio = new Domicilio();
        domicilio.setCalle(dom.getCalle());
        domicilio.setNumero(dom.getNumero());
        if(dom.getDepto() != null && !dom.getDepto().equals(""))
            domicilio.setDepto(dom.getDepto());
        if(dom.getPiso() != null && dom.getPiso() != 0)
            domicilio.setPiso(dom.getPiso());
        domicilio.setBarrio(dom.getBarrio());
        domicilio.setCiudad(dom.getCiudad());
        domicilio.setProvinciaFk(provincia);
        
        usuario.agregarDomicilio(domicilio);

        Login login = new Login();
        login.setFechaCreacion(new Date());
        login.setUsername(username);
        login.setPassword(password);
        SecureRandom random = new SecureRandom();
        String activacion = new BigInteger(130, random).toString(32);
        login.setCodigoActivacion(activacion);
        
        Rol rol = rolFacade.findByNombre(NombreRol.USUARIO);
        if(rol == null) {
            throw new AlquilaCosasException("No se encontro el Rol 'Usuario' en la base de datos.");
        }
        
        login.agregarRol(rol);
        usuario.agregarLogin(login);

        EstadoUsuario estado = estadoUsuarioFacade.findByNombre(NombreEstadoUsuario.REGISTRADO);
        if(estado == null) {
            throw new AlquilaCosasException("No se encontro el estado de usuario 'Registrado' en la base de datos'");
        }

        UsuarioXEstado uxe = new UsuarioXEstado(usuario, estado);
        usuario.agregarUsuarioXEstado(uxe);
        usuarioFacade.create(usuario);

        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(true,
                    Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination);
            ObjectMessage message = session.createObjectMessage();
            
            String asunto = "Bienvenido a AlquilaCosas";
            String texto = "<html>" + usuario.getNombre() + ", <br/><br/>" + 
                    "Gracias por usar AlquilaCosas. Para confirmar su direccion de correo electronico " +
                    "y activar su cuenta, dirijase al siguiente link: <br/>" +
                    "http://localhost:8080/AlquilaCosas-war/faces/vistas/activarCuenta.xhtml?user=" +
                    login.getUsername() + "&codigo=" + login.getCodigoActivacion() + "<br/><br/>" +
                    "Atentamente, <br/> <b>AlquilaCosas </b>";
            NotificacionEmail notificacion = new NotificacionEmail(usuario.getEmail(), asunto, texto);
            message.setObject(notificacion);
            producer.send(message);
            session.close();
            connection.close();
            
        } catch (Exception e) {
            context.setRollbackOnly();
            throw new AlquilaCosasException("Error al enviar email de notificacion: " + e);
        }
    }
    
    @Override
    public void registrarUsuarioConFacebook(String email, String nombre, String apellido) throws AlquilaCosasException {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        
        Login login = new Login();
        login.setFechaCreacion(new Date());
        
        Rol rol = rolFacade.findByNombre(NombreRol.USUARIO);
        if(rol == null) {
            throw new AlquilaCosasException("No se encontro el Rol 'Usuario' en la base de datos.");
        }
        
        login.agregarRol(rol);
        usuario.agregarLogin(login);

        EstadoUsuario estado = estadoUsuarioFacade.findByNombre(NombreEstadoUsuario.ACTIVO);
        if(estado == null) {
            throw new AlquilaCosasException("No se encontro el estado de usuario 'Activo' en la base de datos'");
        }

        UsuarioXEstado uxe = new UsuarioXEstado(usuario, estado);
        usuario.agregarUsuarioXEstado(uxe);
        usuarioFacade.create(usuario);
    }
    
    @Override
    public boolean usernameExistente(String username) {
        Login login = loginFacade.findByUsername(username);
        if(login == null)
            return false;
        else
            return true;
    }
    
    @Override
    public UsuarioDTO actualizarUsuario(int idUsuario, String telefono, Date fechaNacimiento, 
             DomicilioDTO dom) throws AlquilaCosasException {
        Usuario usuario = usuarioFacade.find(idUsuario);
        usuario.setTelefono(telefono);
        usuario.setFechaNac(fechaNacimiento);
        
        List<Domicilio> domicilios = usuario.getDomicilioList();
        Domicilio domicilio = domicilios.get(0);
        
        if(!domicilio.getProvinciaFk().getProvinciaId().equals(dom.getProvinciaId())) {
            Provincia prov = provinciaFacade.find(dom.getProvinciaId());
            domicilio.setProvinciaFk(prov);
            dom.setProvincia(prov.getNombre());
            dom.setPais(prov.getPaisFk().getNombre());
        }
        domicilio.setCiudad(dom.getCiudad());
        domicilio.setBarrio(dom.getBarrio());
        domicilio.setCalle(dom.getCalle());
        domicilio.setNumero(dom.getNumero());
        domicilio.setPiso(dom.getPiso());
        domicilio.setDepto(dom.getDepto());
        
        usuarioFacade.edit(usuario);
        
        UsuarioDTO uFacade = new UsuarioDTO(usuario.getUsuarioId(), usuario.getNombre(),
                usuario.getApellido(), usuario.getEmail(), usuario.getTelefono(), usuario.getDni(), 
                usuario.getFechaNac());
        uFacade.setDomicilio(dom);
        return uFacade;
    }
    
    @Override
    public UsuarioDTO getDatosUsuario(Integer id) {
        Usuario u = usuarioFacade.find(id);
        UsuarioDTO userFacade = new UsuarioDTO(u.getUsuarioId(), u.getNombre(), 
                u.getApellido(), u.getEmail(), u.getTelefono(), u.getDni(), u.getFechaNac());
        Domicilio d = null;
        if(!u.getDomicilioList().isEmpty()) {
            d = u.getDomicilioList().get(0);
            DomicilioDTO dom = new DomicilioDTO(d.getCalle(), d.getNumero(),
                d.getPiso(), d.getDepto(), d.getBarrio(), d.getCiudad());
            Provincia prov = d.getProvinciaFk();
            dom.setProvinciaId(prov.getProvinciaId());
            dom.setProvincia(prov.getNombre());
            dom.setPaisId(prov.getPaisFk().getPaisId());
            dom.setPais(prov.getPaisFk().getNombre());

            userFacade.setDomicilio(dom);
        }
        return userFacade;
    }
    
    @Override
    public void actualizarInfoBasica(Integer usuarioId, String nombre, String apellido, 
            String dni, String telefono, Date fechaNacimiento) {
        Usuario usuario = usuarioFacade.find(usuarioId);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDni(dni);
        usuario.setTelefono(telefono);
        usuario.setFechaNac(fechaNacimiento);
        usuarioFacade.edit(usuario);
    }
    
    @Override
    public void agregarDomicilio(Integer usuarioId, DomicilioDTO dom) {
        Usuario usuario = usuarioFacade.find(usuarioId);
        Provincia provincia = provinciaFacade.find(dom.getProvinciaId());
        Domicilio domicilio = new Domicilio(dom.getCalle(), dom.getNumero(), 
                dom.getPiso(), dom.getDepto(), dom.getBarrio(), dom.getCiudad(), provincia);
        usuario.agregarDomicilio(domicilio);
        usuarioFacade.edit(usuario);
    }
    
    @Override
    public void actualizarDomicilio(Integer usuarioId, DomicilioDTO dom) {
        Usuario usuario = usuarioFacade.find(usuarioId);
        Domicilio domicilio = usuario.getDomicilioList().get(0);
        if(domicilio.getProvinciaFk().getProvinciaId() != dom.getProvinciaId()) {
            Provincia provincia = provinciaFacade.find(dom.getProvinciaId());
            domicilio.setProvinciaFk(provincia);
        }
        domicilio.setCiudad(dom.getCiudad());
        domicilio.setBarrio(dom.getBarrio());
        domicilio.setCalle(dom.getCalle());
        domicilio.setNumero(dom.getNumero());
        domicilio.setPiso(dom.getPiso());
        domicilio.setDepto(dom.getDepto());
        usuarioFacade.edit(usuario);
    }
    
    @Override
    public List<Pais> getPaises() {
        return paisFacade.findAll();
    }
    
    @Override
    public List<Provincia> getProvincias(int paisId) {
        return provinciaFacade.findByPais(paisId);
    }

    @Override
    public void actualizarImagen(int usuarioId, byte[] imagen) {
        Usuario usuario = usuarioFacade.find(usuarioId);
        ImagenUsuario iu = usuario.getImagenUsuarioList().get(0);
        iu.setImagen(imagen);
        usuarioFacade.edit(usuario);
    }

    @Override
    public void seleccionarImagenPerfil(Integer usuarioId, boolean usar) {
        Usuario usuario = usuarioFacade.find(usuarioId);
        ImagenUsuario iu = usuario.getImagenUsuarioList().get(0);
        iu.setUsar(usar);
        usuarioFacade.edit(usuario);
    }
    
    
}
