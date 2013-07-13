$('document').ready(function() {
  $('#backButton').on('click', function() {
    $.get('http://' + tuxknifeWebService + ':8080/').done(function(data) {
      renderResponse(data);
    });
  });
})
