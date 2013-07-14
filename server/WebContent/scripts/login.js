$(document).ready(function() {
  $('#errorMsg').hide();
  $('#submitButton').on('click', function() {
    var user = $('#username').val();
    var pass = $('#password').val();
    var server = $('#server').val() ? $('#server').val() : 'localhost';
    var webservice = $('#webservice').val() ? $('#webservice').val() : 'localhost';
    tuxknifeWebService = webservice;

    $.ajax({
      type: "POST",
      url: 'http://' + webservice + ':8080/api/servers/' + server,
      data: { "username": user, "password": pass },
      success: function(data) { renderResponse(data) },
      error: function(data) { POSTFailure(data) },
      dataType: "json"
    }).done(function(data) {
        renderView(data);
      });
  });

});
