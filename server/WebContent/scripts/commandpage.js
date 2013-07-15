var renderView = function(dataToRender) {
  var data = $.parseJSON(dataToRender.responseData);
  var directive = { 'header.commandPageHeader h3':'hostname' };
  $('body').render(data, directive);

  $('#signOutButton').on('click', function() {
    $.get('http://' + tuxknifeWebService + ':8080/api/signout').done(function(data) {
      renderResponse(data);
    });
  });
};
