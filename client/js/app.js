var renderResponse = function(data) {
  var response = $('<p>' + JSON.stringify(data) + '</p>');
  $('#response').prepend(response);
}

$('document').ready(function() {
  $('#submitButton').on('click', function() {
    var user = $('#username').val();
    var pass = $('#password').val();
    var server = $('#server').val();
    var webservice = 'localhost';

    $.ajax({
      type: "POST",
      url: 'http://' + webservice + ':8083/servers/' + server,
      data: { "username": user, "password": pass },
      success: function(data) { renderResponse(data) },
      error: function(data) { alert("FAIL! Open a JS console to see the response"); console.log(data) },
      dataType: "json"
    });
  });

  $('#clearLog').on('click', function() {
    $('#response').empty();
  });
});
