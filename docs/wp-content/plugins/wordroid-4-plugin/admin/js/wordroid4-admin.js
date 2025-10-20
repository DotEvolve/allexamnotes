console.log("Logging from js");

$(document).ready(function(){

  $("[id^=bottom_nav_group_]").change(function() {
	  alert( "Handler for .change() called." );
  });

});