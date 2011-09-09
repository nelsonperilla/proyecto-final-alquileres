/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.DomicilioDTO;
import com.alquilacosas.dto.UsuarioDTO;
import com.alquilacosas.ejb.entity.Domicilio;
import com.alquilacosas.ejb.entity.Pais;
import com.alquilacosas.ejb.entity.Provincia;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.facade.PaisFacade;
import com.alquilacosas.facade.ProvinciaFacade;
import com.alquilacosas.facade.UsuarioFacade;
import java.util.Date;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class ModificarUsuarioBean implements ModificarUsuarioBeanLocal {

    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private ProvinciaFacade provinciaFacade;
    @EJB
    private PaisFacade paisFacade;
    
    @Override
    @RolesAllowed({"USUARIO", "ADMIN"})
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
        Domicilio d = u.getDomicilioList().get(0);
        
        DomicilioDTO dom = new DomicilioDTO(d.getCalle(), d.getNumero(),
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
    public List<Pais> getPaises() {
        return paisFacade.findAll();
    }
    
    @Override
    public List<Provincia> getProvincias(int paisId) {
        return provinciaFacade.findByPais(paisId);
    }
    
}
