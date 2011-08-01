/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.common.DomicilioFacade;
import com.alquilacosas.common.NotificacionEmail;
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
import com.alquilacosas.ejb.entity.UsuarioXEstadoPK;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
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
public class UsuarioBean implements UsuarioBeanLocal {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager entityManager;
    
    @Resource(name = "emailConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(name = "jms/notificacionEmailQueue")
    private Destination destination;

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
    public void registrarUsuario(String username, String password, String nombre,
            String apellido, List<DomicilioFacade> domicilios, int prov,
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
        for (DomicilioFacade d : domicilios) {
            Domicilio domicilio = new Domicilio();
            domicilio.setCalle(d.getCalle());
            domicilio.setNumero(d.getNumero());
            if(d.getDepto() != null && !d.getDepto().equals(""))
                domicilio.setDepto(d.getDepto());
            if(d.getPiso() != null && d.getPiso() != 0)
                domicilio.setPiso(d.getPiso());
            domicilio.setBarrio(d.getBarrio());
            domicilio.setCiudad(d.getCiudad());
            domicilio.setProvinciaFk(provincia);
            domicilio.setUsuarioFk(usuario);
            entityManager.persist(domicilio);
        }

        Login login = new Login();
        login.setFechaCreacion(new Date());
        login.setUsername(username);
        login.setPassword(password);
        login.setUsuarioFk(usuario);
        SecureRandom random = new SecureRandom();
        String activacion = new BigInteger(130, random).toString(32);
        login.setCodigoActivacion(activacion);

        Query getRolQuery = entityManager.createNamedQuery("Rol.findByNombre");
        getRolQuery.setParameter("nombre", NombreRol.USUARIO);
        Rol rol = (Rol) getRolQuery.getSingleResult();
        List<Rol> roles = new ArrayList<Rol>();
        roles.add(rol);

        login.setRolList(roles);
        entityManager.persist(login);

        Query getEstadoQuery = entityManager.createNamedQuery("EstadoUsuario.findByNombre");
        getEstadoQuery.setParameter("nombre", NombreEstado.REGISTRADO);
        EstadoUsuario estado = (EstadoUsuario) getEstadoQuery.getSingleResult();

        UsuarioXEstado uxe = new UsuarioXEstado();
        uxe.setEstadoUsuario(estado);
        uxe.setUsuario(usuario);
        uxe.setFechaDesde(new Date());
        UsuarioXEstadoPK uxePk = new UsuarioXEstadoPK(usuario.getUsuarioId(), estado.getEstadoUsuarioId());
        uxe.setUsuarioXEstadoPK(uxePk);
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
            
        } catch (JMSException e) {
            System.out.println("excepcion al enviar mensaje al mdb!!!! " + e);
        }
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
