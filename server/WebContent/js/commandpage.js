$('document').ready(function() {
  $('#backButton').on('click', function() {
    $.get('http://' + tuxknifeWebService + ':8080/api/').done(function(data) {
      renderResponse(data);
    });
  });
  $('#signOutButton').on('click', function() {
    $.get('http://' + tuxknifeWebService + ':8080/api/signout').done(function(data) {
      renderResponse(data);
    });
  });
})
