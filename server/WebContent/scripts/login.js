var renderView = function(dataToRender) {
  if (!dataToRender.responseData) return;
  var data = $.parseJSON(dataToRender.responseData);
  data.visibility = 'display:block';
  var directive = { '.@style':'visibility', 'label':'userMessage' }
  $('#errorMsg').render(data, directive);
};

$(document).ready(function() {
  $('#submitButton').on('click', function() {
    $('#errorMsg').hide();
    var user = $('#username').val();
    var pass = $('#password').val();
    var port = $('#port').val() ? $('#port').val() : '22';
    var server = $('#server').val() ? $('#server').val() : 'localhost';
    var webservice = $('#webservice').val() ? $('#webservice').val() : 'localhost';
    tuxknifeWebService = webservice;

    $.ajax({
      type: "POST",
      url: 'http://' + webservice + '/api/servers/' + server + '/' + port,
      data: { "username": user, "password": pass },
      success: function(data) { renderResponse(data) },
      error: function(data) { requestFailure(data) },
      dataType: "json",
      timeout: 5000
    });
  });
});
