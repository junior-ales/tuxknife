var renderView = function(dataToRender) {
  if (!dataToRender.responseData) return;
  var data = $.parseJSON(dataToRender.responseData);
  data.visibility = 'display:block';
  var directive = { '.@style': 'visibility', 'label': 'userMessage' };
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
  $('#login-form').on('submit', function(event) {
    event.preventDefault();
    $('#errorMsg').hide();

    var port = $('#port').val() || '22';
    var serverToMonitor = $('#server').val() || 'localhost';

    $.ajax('http://'+window.location.host + '/api/servers/' +
           normalizeServerURL(serverToMonitor) + '/'+port , {
      type: 'POST',
      data: { "username": $('#username').val(), "password": $('#password').val() },
      beforeSend: function() { preAjaxCall() },
      success: function(data) { renderResponse(data) },
      error: function(data) { requestFailure(data) },
      complete: function(data) { afterAjaxCall() },
      dataType: 'json',
      timeout: 8000
    });
  });
});
