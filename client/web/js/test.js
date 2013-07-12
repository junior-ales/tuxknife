$('document').ready(function() {
  $('#backButton').on('click', function() {
    var data = {"resource": "login","error": ""};
    renderResponse(data);
  });
});
