/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.common.DomicilioFacade;
import com.alquilacosas.common.NotificacionEmail;
import com.alquilacosas.common.UsuarioFacade;
import com.alquilacosas.ejb.entity.Domicilio;
import com.alquilacosas.ejb.entity.EstadoUsuario;
import com.alquilacosas.ejb.entity.EstadoUsuario.NombreEstado;
import com.alquilacosas.ejb.entity.Login;
import com.alquilacosas.ejb.entity.Pais;
import com.alquilacosas.ejb.entity.Provincia;
import com.alquilacosas.ejb.entity.Rol;
import com.alquilacosas.ejb.entity.Rol.NombreRol;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.ejb.entity.UsuarioXEstado;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author damiancardozo
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UsuarioBean implements UsuarioBeanLocal {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager entityManager;
    
    @Resource(name = "emailConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(name = "jms/notificacionEmailQueue")
    private Destination destination;
    
    @Resource
    private SessionContext context;

    
    @Override
    public UsuarioFacade getDatosUsuario(Integer id) {
        Usuario u = entityManager.find(Usuario.class, id);
        UsuarioFacade userFacade = new UsuarioFacade(u.getUsuarioId(), u.getNombre(), 
                u.getApellido(), u.getEmail(), u.getTelefono(), u.getDni(), u.getFechaNac());
        Domicilio d = u.getDomicilioList().get(0);
        
        DomicilioFacade dom = new DomicilioFacade(d.getCalle(), d.getNumero(),
                d.getPiso(), d.getDepto(), d.getBarrio(), d.getCiudad());
        Provincia prov = d.getProvinciaFk();
        dom.setProvinciaId(prov.getProvinciaId());
        dom.setProvincia(prov.getNombre());
        dom.setPaisId(prov.getPaisFk().getPaisId());
        dom.setPais(prov.getPaisFk().getNombre());
        
        userFacade.setDomicilio(dom);
        return userFacade;
    }
    
    @Override
    public void registrarUsuario(String username, String password, String nombre,
            String apellido, DomicilioFacade dom, int prov,
            Date fechaNacimiento, String dni, String telefono, String email) 
            throws AlquilaCosasException {

        Usuario u = null;
        try {
            Query q = entityManager.createNamedQuery("Usuario.findByEmail");
            q.setParameter("email", email);
            u = (Usuario) q.getSingleResult();
        } catch(NoResultException e) {
            // email valido
        }
        
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

        entityManager.persist(usuario);

        Provincia provincia = entityManager.find(Provincia.class, prov);
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
        domicilio.setUsuarioFk(usuario);
        entityManager.persist(domicilio);

        Login login = new Login();
        login.setFechaCreacion(new Date());
        login.setUsername(username);
        login.setPassword(password);
        login.setUsuarioFk(usuario);
        SecureRandom random = new SecureRandom();
        String activacion = new BigInteger(130, random).toString(32);
        login.setCodigoActivacion(activacion);

        Rol rol = null;
        try {
            Query getRolQuery = entityManager.createNamedQuery("Rol.findByNombre");
            getRolQuery.setParameter("nombre", NombreRol.USUARIO);
            rol = (Rol) getRolQuery.getSingleResult();
        } catch(NoResultException e) {
            context.setRollbackOnly();
            throw new AlquilaCosasException("No se encontro el Rol 'Usuario' en la base de datos.");
        }
        List<Rol> roles = new ArrayList<Rol>();
        roles.add(rol);

        login.setRolList(roles);
        entityManager.persist(login);

        EstadoUsuario estado = null;
        try {
            Query getEstadoQuery = entityManager.createNamedQuery("EstadoUsuario.findByNombre");
            getEstadoQuery.setParameter("nombre", NombreEstado.REGISTRADO);
            estado = (EstadoUsuario) getEstadoQuery.getSingleResult();
        } catch(NoResultException e) {
            context.setRollbackOnly();
            throw new AlquilaCosasException("No se encontro el estado de usuario 'Registrado' en la base de datos'");
        }

        UsuarioXEstado uxe = new UsuarioXEstado(usuario, estado);
        uxe.setFechaDesde(new Date());
        entityManager.persist(uxe);

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
     public UsuarioFacade actualizarUsuario(int idUsuario, String telefono, Date fechaNacimiento, 
             DomicilioFacade dom) throws AlquilaCosasException {
        Usuario usuario = entityManager.find(Usuario.class, idUsuario);
        usuario.setTelefono(telefono);
        usuario.setFechaNac(fechaNacimiento);
        
        List<Domicilio> domicilios = usuario.getDomicilioList();
        Domicilio domicilio = domicilios.get(0);
        
        if(!domicilio.getProvinciaFk().getProvinciaId().equals(dom.getProvinciaId())) {
            Provincia prov = entityManager.find(Provincia.class, dom.getProvinciaId());
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
        
        entityManager.merge(domicilio);
        entityManager.merge(usuario);
        
        UsuarioFacade uFacade = new UsuarioFacade(usuario.getUsuarioId(), usuario.getNombre(),
                usuario.getApellido(), usuario.getEmail(), usuario.getTelefono(), usuario.getDni(), 
                usuario.getFechaNac());
        uFacade.setDomicilio(dom);
        return uFacade;
    }
    
    @Override
    public boolean usernameExistente(String username) {
        Login login = null;
        try {
            Query query = entityManager.createNamedQuery("Login.findByUsername");
            query.setParameter("username", username);
            login = (Login) query.getSingleResult();
        } catch(NoResultException e) {
            return false;
        }
        if(login == null)
            return false;
        else
            return true;
    }

    @Override
    public Integer loginUsuario(String username) throws AlquilaCosasException {
        
        Login login = null;
        Query buscarLogin = entityManager.createNamedQuery("Login.findByUsername");
        buscarLogin.setParameter("username", username);
        try {
            login = (Login) buscarLogin.getSingleResult();
        } catch (Exception e) {
            throw new AlquilaCosasException("Username no valido");
        }
        
        Usuario usuario = login.getUsuarioFk();
        if(usuario == null)
            throw new AlquilaCosasException("Usuario no asociado a la cuenta.");
        
        return usuario.getUsuarioId();
    }
    
    @Override
    public List<Provincia> getProvincias(int pais) {
        Pais p = entityManager.find(Pais.class, pais);
        Query query = entityManager.createNamedQuery("Provincia.findByPais");
        query.setParameter("pais", p);
        List<Provincia> provincias = query.getResultList();
        return provincias;
    }

    @Override
    public List<Pais> getPaises() {
        Query query = entityManager.createNamedQuery("Pais.findAll");
        List<Pais> paises = query.getResultList();
        return paises;
    }

}
