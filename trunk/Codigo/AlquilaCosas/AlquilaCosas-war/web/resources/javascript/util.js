/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function handleModificarCategoria(xhr, status, args) {
    if(args.validationFailed) {
        jQuery('#dialogo').effect("shake", {
            times:3
        }, 100);
    } else {
        dialogoModifCat.hide();
    }
}

function handleNuevoPeriodo(xhr, status, args) {
    if(args.validationFailed) {
        jQuery('#dialogo').effect("shake", {
            times:3
        }, 100);
    } else {
        dialogoNuevo.hide();
    }
}

function agregarPregunta(xhr, status, args) {
    if(args.logueado) {
        if(!args.ownerLogged)
            nuevaPregunta.show();
    }
    else {
        login.show();
    }
}

function revisarLogueo(xhr, status, args) {
    if(args.validationFailed || !args.logueado) {
        jQuery('#loginDialog').parent().effect("shake", {
            times:2
        }, 100);
    } else {
        loginDlg.hide();
        if(args.preguntar){
            loginDlg.hide();
            nuevaPregunta.show();
        }
        else if(args.alquilar){
            loginDlg.hide();
            confirmRent.show();
        }
    }
}

function revisarLogueo2(xhr, status, args) {
    if(args.logueado) {
       var url = window.location;
       window.location = url;
    }
}

function borrarImagen(img) {
    document.getElementById(':formPublicacion:' + img).style.display='none';
}

function highlightCalendar(specialDays, date, cssClass) {
    var d = date.getDate();
    var m = date.getMonth() + 1;
    var y = date.getFullYear();
    var result = [ true, null, '' ]; 

    if (specialDays[y] && specialDays[y][m]
        && specialDays[y][m][d]) {
        var s = specialDays[y][m][d];
        result = [ true, cssClass, '' ];
    }
    return result; // no change
}

function revisarDisponibilidad(xhr, status, args){
    if(args.logueado){
        if(!args.ownerLogged){
            if(args.hayDisponibilidad)
                confirmRent.show();
        }
    }
    else
        login.show();

}


function revisarPeriodoSeleccionado(xhr, status, args){
    if(args.mostrarHoraInicio)
        jQuery('#horaInicio').disabled = false;
    else
        jQuery('#horaInicio').disabled = true;
}

function handleModificarAlquiler(xhr, status, args) {
    if(!args.validationFailed && args.modificado) {
        modificarDlg.hide();
    }
}

function handlePedirCambioAlquiler(xhr, status, args) {
    if(!args.validationFailed && args.enviado) {
        pedirCambioDlg.hide();
    }
}
    
function getUrlVars() {
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++) {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}

var currentMarker = null;
var currentMarkerBis = null;

function loadMarker() {  
        if (currentMarker == null)
        {
            var address = document.getElementById("registro:numero").value + ' ';
            address = address + document.getElementById("registro:calle").value + ',';
            address = address + document.getElementById("registro:ciudad").value + ',';

            var selectedIndex = document.getElementById("registro:provincia_input").selectedIndex;
            address = address + document.getElementById("registro:provincia_input").options[selectedIndex].text + ',';
            selectedIndex = document.getElementById("registro:pais_input").selectedIndex;
            address = address + document.getElementById("registro:pais_input").options[selectedIndex].text;
            var geocoder = new google.maps.Geocoder();
            geocoder.geocode( { 'address': address}, function(results, status) {
              if (status == google.maps.GeocoderStatus.OK) {
                var gmap = myMap.getMap(); 
                gmap.setCenter(results[0].geometry.location);
                currentMarker = new google.maps.Marker({
                    position: results[0].geometry.location,
                    draggable: true
                });
                myMap.addOverlay(currentMarker);

                document.getElementById('lat').value = results[0].geometry.location.lat();  
                document.getElementById('lng').value = results[0].geometry.location.lng();         
                }
            });
            google.maps.event.addListener(
                currentMarker,
                'drag',
                function(event) {
                    document.getElementById('lat').value = currentMarker.position.lat();
                    document.getElementById('lng').value = currentMarker.position.lng();
                }
            );
        }
    }



/*
function handlePointClick(event) {  
        
    if(currentMarker == null) {  
        document.getElementById('lat').value = event.latLng.lat();  
        document.getElementById('lng').value = event.latLng.lng();  
  
        currentMarker = new google.maps.Marker({  
            position:new google.maps.LatLng(event.latLng.lat(), event.latLng.lng())  
        }); 
                              
        myMap.addOverlay(currentMarker);  
        dlg.show();
    }
      
}  
  
function markerAddComplete() {  
    var title = "alquilacosas";//document.getElementById('title');  
    currentMarker.setTitle(title.value);  
    var geocoder = new google.maps.Geocoder();
    var lat = parseFloat(document.getElementById('lat').value);
    var lng = parseFloat(document.getElementById('lng').value);
    var latlng = new google.maps.LatLng(lat, lng);
    geocoder.geocode({'latLng': latlng}, function(results, status) {
      if (status == google.maps.GeocoderStatus.OK) {
          /*document.getElementById('registro:barrio').value = results[0].formatted_address;
          var direction = results[0].address_components;
          for(var i = 0; i< direction.length; ++i)
            document.getElementById('registro:' + direction[i].types[0]).value = direction[i].long_name;
            document.getElementById('registro:' + direction[0].types[0]).value = direction[0].long_name;/   
      } else {
        document.getElementById('direccion').value = "No ok!!";
      }
    });
    getMarkerFromAdress();
    dlg.hide();   
}*/  

  function getLocationFromAdress() {
      if (currentMarkerBis == null)
      {
        var address = document.getElementById("tab:numero").value + ' ';
        address = address + document.getElementById("tab:calle").value + ',';
        address = address + document.getElementById("tab:ciudad").value + ',';
        var selectedIndex = document.getElementById("tab:provincia_input").selectedIndex;
        address = address + document.getElementById("tab:provincia_input").options[selectedIndex].text + ',';
        selectedIndex = document.getElementById("tab:pais_input").selectedIndex;
        address = address + document.getElementById("tab:pais_input").options[selectedIndex].text;
        //document.getElementById("tab:piso").value = address;
        var geocoder = new google.maps.Geocoder();
        geocoder.geocode( { 'address': address}, geoListener);
            google.maps.event.addListener(
                currentMarkerBis,
                'drag',
                function(event) {
                    document.getElementById('tab:lat').value = currentMarkerBis.position.lat();
                    document.getElementById('tab:lng').value = currentMarkerBis.position.lng();
                }
            );        
      }
  }

    function pedirPosicion(pos) {
        var gmap = myMap.getMap();
        var centro = new google.maps.LatLng(pos.coords.latitude,pos.coords.longitude);
        gmap.setCenter(centro); //pedimos que centre el mapa..
        gmap.setZoom(15);
     }

    function geolocalizame(){
        navigator.geolocation.getCurrentPosition(pedirPosicion);
    }

function geoListener(results, status) {
        if (status == google.maps.GeocoderStatus.OK) {
            var gmap = myMap.getMap(); 
            gmap.setCenter(results[0].geometry.location);
            currentMarkerBis = new google.maps.Marker({
                position: results[0].geometry.location,
                draggable: true
            });
            myMap.addOverlay(currentMarkerBis);

        document.getElementById('tab:lat').value = results[0].geometry.location.lat();  
        document.getElementById('tab:lng').value = results[0].geometry.location.lng(); 
      }
    }

function cancel() {  
    dlg.hide();  
    currentMarker.setMap(null);  
    currentMarker = null;  
  
    return false;  
}  

function onMarkerDrag(event){
    currentMarker = event.getMarker();
    document.getElementById('lat').value = event.latLng.lat();  
    document.getElementById('lng').value = event.latLng.lng();          
}
    
function updateCoordinates(){
    document.getElementById('lng').value = currentMarker.position.lng();
    document.getElementById('lat').value = currentMarker.position.lat();
    /*document.getElementById('lat').value = event.latLng.lat();  
    document.getElementById('lng').value = event.latLng.lng();          */
}

function updateCoordinatesEditUser(){
    document.getElementById('tab:lng').value = currentMarkerBis.position.lng();
    document.getElementById('tab:lat').value = currentMarkerBis.position.lat();
}




function manejarPopup(comp, popupClass) {
    var detalle = comp[0];
    if(detalle.style.display == 'none') {
        $('.' + popupClass).css('display', 'none');
        detalle.style.display = 'block';
        
        $('.' + popupClass).bind("mouseleave",function() {
            $('.' + popupClass).hide();
        });
        
    } else {
        $('.' + popupClass).css('display', 'none');
    }
}

/* Traduccion al español para los calendarios de primefaces */
PrimeFaces.locales['es'] = {
    closeText: 'Cerrar',
    prevText: 'Anterior',
    nextText: 'Siguiente',
    monthNames: ['Enero','Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
    monthNamesShort: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun','Jul','Ago','Sep','Oct','Nov','Dic'],
    dayNames: ['Domingo','Lunes','Martes','Miércoles','Jueves','Viernes','Sábado'],
    dayNamesShort: ['Dom','Lun', 'Mar', 'Mie', 'Jue', 'Vie', 'Sab'],
    dayNamesMin: ['D','L','M','X','J','V','S'],
    weekHeader: 'Semana',
    firstDay: 1,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix: '',
    timeOnlyTitle: 'Sólo hora',
    timeText: 'Tiempo',
    hourText: 'Hora',
    minuteText: 'Minuto',
    secondText: 'Segundo',
    currentText: 'Fecha actual',
    ampm: false,
    month: 'Mes',
    week: 'Semana',
    day: 'Día',
    allDayText : 'Todo el día'
};