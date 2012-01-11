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
        nuevaPregunta.show();
    }
    else {
        loginDlg.show();
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
    if(!args.ownerLogged){
        if(args.hayDisponibilidad) {
            if(args.logueado)
                confirmRent.show();
            else
                loginDlg.show();
        }
    }
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

    
function getUrlVars()
{
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++)
    {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}

    var currentMarker = null;
    function handlePointClick(event) {  
        
        if(currentMarker == null) {  
            document.getElementById('lat').value = event.latLng.lat();  
            document.getElementById('lng').value = event.latLng.lng();  
  
            currentMarker = new google.maps.Marker({  
                position:new google.maps.LatLng(event.latLng.lat(), event.latLng.lng())  
            });  
                              
            myMap.addOverlay(currentMarker);  
        }
            dlg.show();  
             
    }  
  
    function markerAddComplete() {  
        var title = document.getElementById('title');  
        currentMarker.setTitle(title.value);  
        dlg.hide();  
    }  
  
    function cancel() {  
        dlg.hide();  
        currentMarker.setMap(null);  
        currentMarker = null;  
  
        return false;  
    }  
