var renderResponse = function(data) {
  $('#response').append(JSON.stringify(data));
}

$('document').ready(function() {
  $('#submitButton').on('click', function() {
    var user = $('#username').val();
    var pass = $('#password').val();
    var server = $('#server').val();
    var webservice = '10.71.2.89';

    $.ajax({
      type: "GET",
      url: 'http://' + webservice + ':8083/servers/' + server,
      data: { "username": user, "password": pass },
      success: function(data) { renderResponse(data) },
      error: function(data) { alert("faio"); console.log(data) },
      dataType: "json"
    });
  });
});
