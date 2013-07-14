$('document').ready(function() {
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
        //var dataToRender = $.parseJSON(data.responseText);
        renderView(data);
      });
  });

  var renderView = function(dataToRender) {
    console.log('dataToRender stringified: ' + JSON.stringify(dataToRender));
    console.log('dataToRender: ' + dataToRender);
    var data = $.parseJSON(dataToRender.responseData);
    console.log('responseData: ' + JSON.stringify(data));
    var directive = { 'h3':'hostname' };
    $('header.commandPageHeader').render(data, directive);
  };
});
