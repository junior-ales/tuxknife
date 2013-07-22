var renderView = function(dataToRender) {
  var data = $.parseJSON(dataToRender.responseData);
  var directive = { 'header.commandPageHeader h3':'hostname' };
  $('body').render(data, directive);

  $('#signOutButton').on('click', function() {
    $.ajax('http://' + tuxknifeWebService + '/api/signout', { 
      success: function(data) { renderResponse(data) },
      beforeSend: function() { preAjaxCall() },
      complete: function() { afterAjaxCall() }
    });
  });
};
