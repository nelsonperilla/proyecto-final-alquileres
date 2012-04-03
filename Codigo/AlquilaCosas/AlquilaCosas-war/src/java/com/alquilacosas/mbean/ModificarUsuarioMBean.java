/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.DomicilioDTO;
import com.alquilacosas.dto.UsuarioDTO;
import com.alquilacosas.ejb.entity.ImagenUsuario;
import com.alquilacosas.ejb.entity.Pais;
import com.alquilacosas.ejb.entity.Provincia;
import com.alquilacosas.ejb.session.UsuarioBeanLocal;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "modUsuario")
@ViewScoped
public class ModificarUsuarioMBean implements Serializable {

    @EJB
    private UsuarioBeanLocal usuarioBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean usuarioMBean;
    private UsuarioDTO usuario;
    private Date fechaNacimiento;
    private String nombre, apellido, dni, telefono;
    private String calle, depto, barrio, ciudad;
    private Integer numero, piso;
    private Date today;
    private List<SelectItem> provincias;
    private int provinciaSeleccionada;
    private List<SelectItem> paises;
    private int paisSeleccionado;
    private DomicilioDTO domicilio;
    private boolean editando, editandoDir, sinDomicilio;
    private Integer pictureSelected;
    private Integer imagenUsuarioId;
    private byte[] imagenPerfil;
    private boolean buttonPressed = false;
    private boolean renderImage = false;
    private boolean fotoSubida = false;
    private MapModel gMap;
    private MapModel editableGMap;
    private double lat;
    private double lng;
    private LatLng marker;
    /**
     * Creates a new instance of ModificarUsuarioMBean
     */
    public ModificarUsuarioMBean() {
    }

    @PostConstruct
    public void init() {
        Logger.getLogger(ModificarUsuarioMBean.class).debug("ModificarUsuarioMBean: postconstruct.");
        Integer id = usuarioMBean.getUsuarioId();
        usuario = usuarioBean.getDatosUsuario(id);

        nombre = usuario.getNombre();
        apellido = usuario.getApellido();
        dni = usuario.getDni();
        telefono = usuario.getTelefono();
        fechaNacimiento = usuario.getFechaNacimiento();
        domicilio = usuario.getDomicilio();

        if (domicilio != null) {
            paisSeleccionado = domicilio.getPaisId();
            provinciaSeleccionada = domicilio.getProvinciaId();
            ciudad = domicilio.getCiudad();
            barrio = domicilio.getBarrio();
            calle = domicilio.getCalle();
            numero = domicilio.getNumero();
            piso = domicilio.getPiso();
            depto = domicilio.getDepto();
            lat = domicilio.getLatitud();
            lng = domicilio.getLongitud();
        } else {
            sinDomicilio = true;
        }
        paises = new ArrayList<SelectItem>();
        provincias = new ArrayList<SelectItem>();
        if (paisSeleccionado != 0) {
            actualizarProvincias();
        }

        List<Pais> listaPais = usuarioBean.getPaises();
        if (!listaPais.isEmpty()) {
            for (Pais p : listaPais) {
                paises.add(new SelectItem(p.getPaisId(), p.getNombre()));
            }
        }
        today = new Date();

        ImagenUsuario iu = this.usuarioMBean.getUsuario().getImagen();
        if (iu != null) {
            this.imagenUsuarioId = iu.getImagenUsuarioId();
            this.fotoSubida = true;
            this.pictureSelected = 2; //quitar este hardcode una vez que se implemente
            //la funcionalidad que inicializa el radiobutton teniendo en cuenta si el usuario
            //se logea con facebook o a traves de una cuenta de AlquilaCosas
        }
        gMap = new DefaultMapModel(); 
        editableGMap = new DefaultMapModel();
        marker = new LatLng(lat, lng);  
        gMap.addOverlay(new Marker(marker, "AlquilaCosas"));         
    }

    public void updateCoordinates(MarkerDragEvent event)
    {
        setLat(event.getMarker().getLatlng().getLat());
        setLng(event.getMarker().getLatlng().getLng());
    }
    
    
    public void crearDomicilio() {
        domicilio = new DomicilioDTO();
        domicilio.setCalle(calle);
        domicilio.setNumero(numero);
        if (piso != null) {
            domicilio.setPiso(piso);
        }
        if (depto != null && !depto.equals("")) {
            domicilio.setDepto(depto);
        }
        domicilio.setBarrio(barrio);
        domicilio.setCiudad(ciudad);
        domicilio.setProvinciaId(provinciaSeleccionada);
        domicilio.setPaisId(paisSeleccionado);
        domicilio.setLatitud(lat);
        domicilio.setLongitud(lng);
        for (SelectItem si : paises) {
            if (si.getValue().equals(new Integer(paisSeleccionado))) {
                domicilio.setPais(si.getLabel());
                break; 
            }
        }
        for (SelectItem si : provincias) {
            if (si.getValue().equals(new Integer(provinciaSeleccionada))) {
                domicilio.setProvincia(si.getLabel());
                break;
            }
        }
        gMap.getMarkers().remove(0);
        //editableGMap.getMarkers().remove(0);
        
        marker = new LatLng(lat, lng);  
        gMap.addOverlay(new Marker(marker, "AlquilaCosas"));
    }

    public void actualizarInfo() {
        usuarioBean.actualizarInfoBasica(usuario.getId(), nombre, apellido, dni, telefono, fechaNacimiento);
        editando = false;
    }

    public void actualizarDomicilio() {
        crearDomicilio();
        if (sinDomicilio) {
            usuarioBean.agregarDomicilio(usuario.getId(), domicilio);
            sinDomicilio = false;
            usuario.setDomicilio(domicilio);
        } else {
            usuarioBean.actualizarDomicilio(usuario.getId(), domicilio);
        }
        editandoDir = false;
    }

    public void actualizarUsuario() {
        try {
            usuario = usuarioBean.actualizarUsuario(usuario.getId(), telefono,
                    fechaNacimiento, domicilio);
            editando = false;
        } catch (AlquilaCosasException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al actualizar usuario", e.getMessage()));
        }
    }

    public void actualizarProvincias() {
        provincias.clear();
        List<Provincia> provList = usuarioBean.getProvincias(paisSeleccionado);
        if (!provList.isEmpty()) {
            for (Provincia p : provList) {
                provincias.add(new SelectItem(p.getProvinciaId(), p.getNombre()));
            }
        }

    }

    //crear método que devuelva el ID de una imagenUsuario
    public void handleFileUpload(FileUploadEvent event) {
        imagenPerfil = event.getFile().getContents();
        ImagenUsuario iu = usuarioBean.actualizarImagen(usuario.getId(), imagenPerfil);
        if (iu != null) {
            usuarioMBean.setImagenUsuarioId(iu.getImagenUsuarioId());
            imagenUsuarioId = iu.getImagenUsuarioId();
            this.fotoSubida = true;
        }
        this.buttonPressed = true;
    }
    
    public void subirImagen(){
        this.renderImage = true;  
    }

    public void actualizarFoto() {
        if (this.pictureSelected.equals("1")) {
            //se selecciona la imagen de Facebook para mostrar en la imagen de perfil
            usuarioBean.seleccionarImagenPerfil(usuario.getId(), false);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Imagen Facebook seleccionada", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            // se selecciona la imagen que es subida por el usuario
            usuarioBean.seleccionarImagenPerfil(usuario.getId(), true);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Imagen Subida por el usuario seleccionada", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void editar() {
        editando = true;
    }

    public void cancelarEdicion() {
        editando = false;
    }

    public void editarDir() {
        editandoDir = true;
    }

    public void cancelarEdicionDir() {
        editandoDir = false;
    }

    /*
     * Getters & Setters
     */
    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDepto() {
        return depto;
    }

    public void setDepto(String depto) {
        this.depto = depto;
    }

    public DomicilioDTO getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(DomicilioDTO domicilio) {
        this.domicilio = domicilio;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public int getPaisSeleccionado() {
        return paisSeleccionado;
    }

    public void setPaisSeleccionado(int paisSeleccionado) {
        this.paisSeleccionado = paisSeleccionado;
    }

    public List<SelectItem> getPaises() {
        return paises;
    }

    public void setPaises(List<SelectItem> paises) {
        this.paises = paises;
    }

    public Integer getPiso() {
        return piso;
    }

    public void setPiso(Integer piso) {
        this.piso = piso;
    }

    public int getProvinciaSeleccionada() {
        return provinciaSeleccionada;
    }

    public void setProvinciaSeleccionada(int provinciaSeleccionada) {
        this.provinciaSeleccionada = provinciaSeleccionada;
    }

    public List<SelectItem> getProvincias() {
        return provincias;
    }

    public void setProvincias(List<SelectItem> provincias) {
        this.provincias = provincias;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public ManejadorUsuarioMBean getUsuarioMBean() {
        return usuarioMBean;
    }

    public void setUsuarioMBean(ManejadorUsuarioMBean usuarioMBean) {
        this.usuarioMBean = usuarioMBean;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public boolean isEditando() {
        return editando;
    }

    public void setEditando(boolean editando) {
        this.editando = editando;
    }

    public boolean isEditandoDir() {
        return editandoDir;
    }

    public void setEditandoDir(boolean editandoDir) {
        this.editandoDir = editandoDir;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPictureSelected() {
        return pictureSelected;
    }

    public void setPictureSelected(Integer pictureSelected) {
        this.pictureSelected = pictureSelected;
    }

    public Integer getImagenUsuarioId() {
        return imagenUsuarioId;
    }

    public void setImagenUsuarioId(Integer imagenUsuarioId) {
        this.imagenUsuarioId = imagenUsuarioId;
    }

    public boolean isButtonPressed() {
        return buttonPressed;
    }

    public void setButtonPressed(boolean buttonPressed) {
        this.buttonPressed = buttonPressed;
    }

    public byte[] getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(byte[] imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public boolean isRenderImage() {
        return renderImage;
    }

    public void setRenderImage(boolean renderImage) {
        this.renderImage = renderImage;
    }

    public boolean isFotoSubida() {
        return fotoSubida;
    }

    public void setFotoSubida(boolean fotoSubida) {
        this.fotoSubida = fotoSubida;
    }

    /**
     * @return the gMap
     */
    public MapModel getgMap() {
        return gMap;
    }

    /**
     * @param gMap the gMap to set
     */
    public void setgMap(MapModel gMap) {
        this.gMap = gMap;
    }

    /**
     * @return the lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * @param lat the lat to set
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * @return the lng
     */
    public double getLng() {
        return lng;
    }

    /**
     * @param lng the lng to set
     */
    public void setLng(double lng) {
        this.lng = lng;
    }
    /**
     * @return the editableGMap
     */
    public MapModel getEditableGMap() {
        return editableGMap;
    }

    /**
     * @param editableGMap the editableGMap to set
     */
    public void setEditableGMap(MapModel editableGMap) {
        this.editableGMap = editableGMap;
    }
}
