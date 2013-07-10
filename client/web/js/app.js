var addSettings = function(data) {
  data.visibility = data.error ? 'display:block' : 'display:none'; 
}

var renderResponse = function(data) {
  addSettings(data);
  var directive = { '.@style':'visibility', 'label':'error' }
  $('#errorMsg').render(data, directive);

  var response = $('<p>' + JSON.stringify(data) + '</p>');
  $('#response').prepend(response);
}

var POSTFailure = function(data) {
  console.log(data);
  data.error = data.statusText;
  renderResponse(data);
}

$('document').ready(function() {
  $('#errorMsg').hide();
  $('#submitButton').on('click', function() {
    var user = $('#username').val();
    var pass = $('#password').val();
    var server = $('#server').val() ? $('#server').val() : 'localhost';
    var webservice = $('#webservice').val() ? $('#webservice').val() : 'localhost';

    $.ajax({
      type: "POST",
      url: 'http://' + webservice + ':8080/servers/' + server,
      data: { "username": user, "password": pass },
      success: function(data) { renderResponse(data) },
      error: function(data) { POSTFailure(data) },
      dataType: "json"
    });
  });

  $('#clearLog').on('click', function() {
    $('#response').empty();
  });
});
