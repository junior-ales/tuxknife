$('document').ready(function() {
  $('#backButton').on('click', function() {
    $.get('http://' + tuxknifeWebService + ':8080/').done(function(data) {
      renderResponse(data);
    });
  });
  $('#signOutButton').on('click', function() {
    $.get('http://' + tuxknifeWebService + ':8080/signout').done(function(data) {
      renderResponse(data);
    });
  });
})
