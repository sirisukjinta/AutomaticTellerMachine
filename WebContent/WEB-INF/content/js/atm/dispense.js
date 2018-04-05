var $overlay;

$(document).ready(function () {
	$overlay = $('<div class="ui-overlay"><div class="ui-widget-overlay"></div></div>').hide().appendTo('body');
});

function dispenseAction(){
    if(checkForm()){
        if (confirm("Do you want to save this record ? ")){
        	$("[id$=_error]").text("");
			$overlay.show();
			$.ajax({
				type: "POST",
				url: window.pageContext + "/dispense",
				data: $("form").serialize(),
				success: function(response){
					processAjaxResultSuccess(response, "/dispense");
				},
				error: processAjaxResultError,
				complete: function(){
					$overlay.hide();
				},
				dataType: "json"
			});
        }
    }
}

function checkForm(){
    
    if (document.dispenseEntry.amount.value == '') {
        alert("Please input Amount");
        document.dispenseEntry.amount.focus();
        return false;
    }
    
    return true;
 }