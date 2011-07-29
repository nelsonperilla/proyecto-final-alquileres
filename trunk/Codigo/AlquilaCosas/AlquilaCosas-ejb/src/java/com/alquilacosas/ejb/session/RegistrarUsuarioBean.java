/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class RegistrarUsuarioBean implements RegistrarUsuarioBeanLocal {

    @PersistenceContext(unitName="AlquilaCosas-ejbPU") 
    private EntityManager entityManager;
    
    @Override
    public void registrarUsuario(String username, String password, String nombre,
            String apellido, List<Domicilio> domicilios, int prov,
            Date fechaNacimiento, String dni, String telefono, String email) {
        
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        //usuario.setDomicilioList(domicilios);
        usuario.setFechaNac(fechaNacimiento);
        usuario.setDni(dni);
        usuario.setTelefono(telefono);
        usuario.setEmail(email);
        
        Provincia provincia = entityManager.find(Provincia.class, prov);
        for(Domicilio dom: domicilios) {
            dom.setProvinciaFk(provincia);
            dom.setUsuarioFk(usuario);
        }                
        
        Query getEstadoQuery = entityManager.createNamedQuery("EstadoUsuario.findByNombre");
        getEstadoQuery.setParameter("nombre", NombreEstado.REGISTRADO);
        EstadoUsuario estado = (EstadoUsuario) getEstadoQuery.getSingleResult();
        
        UsuarioXEstado uxe = new UsuarioXEstado();
        uxe.setEstadoUsuario(estado);
        uxe.setUsuario(usuario);
        uxe.setFechaDesde(new Date());
        
        Login login = new Login();
        login.setUsername(username);
        login.setPassword(password);
        login.setUsuarioFk(usuario);
        login.setCodigoActivacion("sarlanga");   
        
        Query getRolQuery = entityManager.createNamedQuery("Rol.findByNombre");
        getRolQuery.setParameter("nombre", NombreRol.USUARIO);
        Rol rol = (Rol) getRolQuery.getSingleResult();
        List<Rol> roles = new ArrayList<Rol>();
        roles.add(rol);
        login.setRolList(roles);
        
        entityManager.persist(usuario);        
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
