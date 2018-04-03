function IsValidDate(myDate) {
    var filter = /^([012]?\d|3[01]) ([Jj][Aa][Nn]|[Ff][Ee][bB]|[Mm][Aa][Rr]|[Aa][Pp][Rr]|[Mm][Aa][Yy]|[Jj][Uu][Nn]|[Jj][Uu][Ll]|[aA][Uu][gG]|[Ss][eE][pP]|[oO][Cc][Tt]|[Nn][oO][Vv]|[Dd][Ee][Cc]) (19|20)\d\d$/
    return filter.test(myDate);
}
function checkDate(myDate){
     var monthArray = new Array("", "JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"); 
     var dateArray = myDate.split(" ");
     var month = 0;
     for (var i=0; i<monthArray.length; i++) { 
        if (monthArray[i] == dateArray[1].toUpperCase()) { 
            month = i;
        }   
    } 
     if (month < 1 || month > 12) {
        return false;
    }
    // Checks the day range
    if (dateArray[0] < 1 || dateArray[0] > 31){
        return false;
    }
    // Checks the month having 30 days
    if ((month == 4 || month == 6 || month == 9 || month == 11) && dateArray[0] == 31) {
        return false
    }
    // Checks for february 29th
    if (month == 2){
        var isleap = (dateArray[2] % 4 == 0 && (dateArray[2] % 100 != 0 || dateArray[2] % 400 == 0));
        if (dateArray[0] > 29 || (dateArray[0] == 29 && !isleap)) {
            return false;
        }
    }
    return true;  
    
}

function gotoUrl(href, target_win) {
	try {
		if (target_win) {
			target_win.location = href;
		}
		else {
			window.location = href;
		}
	}
	catch (e) {
		return false;
	}
}

function reloadWindow(target_win) {
	try {
		if (target_win) {
			target_win.location.reload();
		}
		else {
			window.location.reload();
		}
	} catch (e) {
		return false;
	}
}

function setColor(_form, _item, _color) {
	try {
		for (i = 0; i < _form.length; i ++) {
			_form.elements[i].style.backgroundColor = "";
		}
		if (_color != '') {
			_item.style.backgroundColor = _color;
		}
	}
	catch (e) {
		return false;
	}
}

var sarrChanged = false;
function isSARRChanged() {
	return sarrChanged;
}
function setSARRChanged(hasChanged) {
	sarrChanged = hasChanged;
}
function isTextAreaChange(){
	setSARRChanged(true); 
}

function confirmQuit(){
    var ok = true;
    if (isSARRChanged()) {
        //ok = confirm("Warning: There are unsaved changes, click OK if you are sure you want to quit without saving");
        ok = confirm("Are you sure you want to exit without saving changes?");
    }
    return ok;
}

function processAjaxResultSuccess(response, redirect){
	var i, 
		$error, 
		errOrphans = [];
	if(response.status == "SUCCESS"){
		window.location.href = window.pageContext + redirect;
	}else if(response.status == "FAIL"){
		for(i = 0; i < response.result.length; i++){
			$error = $("#" + response.result[i].field + "_error");
			if($error.length<1 || response.result[i].field == "globalError"){
				errOrphans.push(response.result[i].defaultMessage);
			}else{
				$error.html(response.result[i].defaultMessage);
			}
		}
		if(errOrphans.length){
			$("#globalError_error").html(errOrphans.join("<br/>"));							
		}
	}else if(response.status == "DUPLICATE"){
		$("#globalError_error").html(response.result);
	}
}

function processAjaxResultError(jqXHR, textStatus, errorThrown){
	var msg = "Unable to process your request due to the following: ",
		r;
	if (jqXHR.status == "901") {
		alert("Session timeout! Page must be reloaded.");
		window.location.reload();
	} else if (jqXHR.responseText == null || jqXHR.responseText == "") {
		msg += "Unable to determine the cause of this error (" + jqXHR.status + ").";
	} else {
		try {
			r = $.parseJSON(jqXHR.responseText);
			msg += (r.result || "Unable to determine the cause of this error.");
		} catch (err) {
			msg += "Unable to determine the cause of this error.";
		}
	}
	$("#globalError_error").html(msg);
}

function NumOnly() {
	if (event.keyCode>='0'.charCodeAt()&&event.keyCode <= '9'.charCodeAt()){
		event.returnValue = true;
	}else{
		event.returnValue = false;
    }
}

function reportWindow(mypage, myname, w, h, scroll) {
	var winl = (screen.width - w) / 2;
	var wint = (screen.height - h) / 2;
	winprops = 'height='+h+',width='+w+',top='+wint+',left='+winl+',scrollbars='+scroll+',resizable';
	win = window.open(mypage, myname, winprops);
	
	if (parseInt(navigator.appVersion) >= 4) { win.window.focus(); }
}

function IsCheckReport() {
	var atLeastOneIsChecked = $('input[name="selectITSecRiskIdPdf"]:checked').length > 0;
	if(atLeastOneIsChecked){
		var selectITSecRiskIdPdfs = $('input[name="selectITSecRiskIdPdf"]:checked').map(function() {
		   return this.value;
		}).get();
		reportWindow(window.pageContext + "/sarr/report/viewITSecRiskMultipleReport/" + selectITSecRiskIdPdfs,'report','600','600','yes');
    }
}

function IsCheckSARRReport() {
	var atLeastOneIsChecked = $('input[name="selectSarrIdPdf"]:checked').length > 0;
	if(atLeastOneIsChecked){
		var selectSarrIdPdfs = $('input[name="selectSarrIdPdf"]:checked').map(function() {
		   return this.value;
		}).get();
		reportWindow(window.pageContext + "/sarr/report/viewSarrMultipleReport/" + selectSarrIdPdfs,'report','600','600','yes');
    }
}