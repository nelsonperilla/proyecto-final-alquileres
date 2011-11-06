/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.ComentarioDTO;
import com.alquilacosas.dto.DenunciaDTO;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.entity.Comentario;
import com.alquilacosas.ejb.entity.Denuncia;
import com.alquilacosas.ejb.entity.EstadoDenuncia;
import com.alquilacosas.ejb.entity.MotivoDenuncia;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.facade.ComentarioFacade;
import com.alquilacosas.facade.DenunciaFacade;
import com.alquilacosas.facade.DenunciaXEstadoFacade;
import com.alquilacosas.facade.MotivoDenunciaFacade;
import com.alquilacosas.facade.PublicacionFacade;
import com.alquilacosas.facade.UsuarioFacade;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 *
 * @author jorge
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@DeclareRoles({"USUARIO", "ADMIN"})
public class DenunciaPreguntaBean implements DenunciaPreguntaBeanLocal
{

    @EJB
    private ComentarioFacade comentarioFacade;
    @EJB
    private PublicacionFacade publicacionFacade;    
    @EJB
    private MotivoDenunciaFacade motivoDenunciaFacade;    
    @EJB
    private UsuarioFacade usuarioFacade;    
    @EJB
    private DenunciaFacade denunciaFacade;    
    @EJB
    private DenunciaXEstadoFacade denunciaXEstadoFacade;    
    

    @Override
    public ComentarioDTO getComentario(int comentarioId) {
        Comentario temp = comentarioFacade.find(comentarioId);
        ComentarioDTO result = new ComentarioDTO(temp.getComentarioId(),
            temp.getComentario(),temp.getFecha(),temp.getUsuarioFk().getUsuarioId(),
            temp.getUsuarioFk().getNombre(), temp.getPublicacionFk().getPublicacionId(),
            null);
        return result;
    }

    @Override
    public String getPublication(int publicacionId) {
        Publicacion publicacion = publicacionFacade.find(publicacionId);
        return publicacion.getTitulo();
    }

    @Override
    public List<MotivoDenuncia> getMotivosDenuncia() {
        List<MotivoDenuncia> result = motivoDenunciaFacade.getAllMotivosDenuncia();
        return result;
    }

    @Override
    public List<MotivoDenuncia> getMotivosDenunciaPublicacion() {
        List<MotivoDenuncia> result = motivoDenunciaFacade.getMotivosDenunciaPublicacion();
        return result;
    }

    @Override
    public List<MotivoDenuncia> getMotivosDenunciaComentario() {
        List<MotivoDenuncia> result = motivoDenunciaFacade.getMotivosDenunciaComentario();
        return result;
    }
    
    
    @Override
    public void saveDenuncia(DenunciaDTO nuevaDenuncia, int motivoSeleccionado) {
        nuevaDenuncia.setMotivo(motivoDenunciaFacade.find(motivoSeleccionado));
        Denuncia denuncia = new Denuncia();
        
        denuncia.setExplicacion(nuevaDenuncia.getExplicacion());
        denuncia.setMotivoFk(nuevaDenuncia.getMotivo());
        if(nuevaDenuncia.getComentarioId() != -1)
            denuncia.setComentarioFk(comentarioFacade.find(nuevaDenuncia.getComentarioId()));
        else
            denuncia.setComentarioFk(null);
        denuncia.setFecha(nuevaDenuncia.getFecha());
        denuncia.setPulicacionFk(publicacionFacade.find(nuevaDenuncia.getPublicacionId()));
        denuncia.setUsuarioFk(usuarioFacade.find(nuevaDenuncia.getUsuarioId()));
        
        denunciaFacade.create(denuncia);
        denunciaXEstadoFacade.saveState(denuncia,EstadoDenuncia.NombreEstadoDenuncia.PENDIENTE);
        
        

    }

    /**
     * @return the denunciaXEstadoFacade
     */
    public DenunciaXEstadoFacade getDenunciaXEstadoFacade() {
        return denunciaXEstadoFacade;
    }

    /**
     * @param denunciaXEstadoFacade the denunciaXEstadoFacade to set
     */
    public void setDenunciaXEstadoFacade(DenunciaXEstadoFacade denunciaXEstadoFacade) {
        this.denunciaXEstadoFacade = denunciaXEstadoFacade;
    }
    
}
