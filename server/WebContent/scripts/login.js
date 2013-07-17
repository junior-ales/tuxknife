var renderView = function(dataToRender) {
  if (!dataToRender.responseData) return;
  var data = $.parseJSON(dataToRender.responseData);
  data.visibility = 'display:block';
  var directive = { '.@style':'visibility', 'label':'userMessage' }
  $('#errorMsg').render(data, directive);
};

var normalizeServerURL = function(serverURL) {
  return serverURL.replace(/\./g, dotEncoderCode);
};

// this same code will be decoded server side,
// if your server url have a actual '__dot__' in the name
// I have bad news for you =(
var dotEncoderCode = '__dot__';

$(document).ready(function() {
  $('#submitButton').on('click', function() {
    $('#errorMsg').hide();
    var user = $('#username').val();
    var pass = $('#password').val();
    var port = $('#port').val() ? $('#port').val() : '22';
    var serverToMonitor = $('#server').val() ? $('#server').val() : 'localhost';
    tuxknifeWebService = window.location.host;

    $.ajax({
      type: "POST",
      url: 'http://' + tuxknifeWebService + '/api/servers/' + normalizeServerURL(serverToMonitor) + '/' + port,
      data: { "username": user, "password": pass },
      success: function(data) { renderResponse(data) },
      error: function(data) { requestFailure(data) },
      dataType: "json",
      timeout: 8000
    });
  });
});
