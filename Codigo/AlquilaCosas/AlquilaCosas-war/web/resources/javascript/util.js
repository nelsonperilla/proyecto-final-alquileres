/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function handleAgregarDomicilio(xhr, status, args) {
    if(args.validationFailed) {
        jQuery('#dialogo').effect("shake", {
            times:3
        }, 100);
    } else {
        dialogoDom.hide();
    }
}

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
        nuevaPregunta.show();
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
