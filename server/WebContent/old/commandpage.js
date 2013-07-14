$('document').ready(function() {
  $('#signOutButton').on('click', function() {
    $.get('http://' + tuxknifeWebService + ':8080/api/signout').done(function(data) {
      renderResponse(data);
    });
  });
})
