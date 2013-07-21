var renderView = function(dataToRender) {
  var data = $.parseJSON(dataToRender.responseData);
  var directive = { 'header.commandPageHeader h3':'hostname' };
  $('body').render(data, directive);

  $('#signOutButton').on('click', function() {
    preAjaxCall();
    $.get('http://' + tuxknifeWebService + '/api/signout').done(function(data) {
      renderResponse(data);
      afterAjaxCall();
    });
  });
};
