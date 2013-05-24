$('document').ready(function() {
  $('#submitButton').on('click', function() {
    var user = $('#username').val();
    var pass = $('#password').val();
    var server = $('#server').val();

    $.ajax({
      type: "GET",
      url: 'http://10.71.2.89:8083/servers/' + server,
      data: { "username": user, "password": pass },
      success: function(data) { console.log(data) },
      error: function(data) { alert("faio"); console.log(data) },
      dataType: "json"
    });
  });
});
