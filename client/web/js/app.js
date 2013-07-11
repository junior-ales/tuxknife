var addSettings = function(data) {
  data.visibility = data.error ? 'display:block' : 'display:none'; 
}

var renderResponse = function(data) {
  addSettings(data);
  var directive = { '.@style':'visibility', 'label':'error' }
  $('#errorMsg').render(data, directive);

  var response = $('<p>' + JSON.stringify(data) + '</p>');
  $('#response').prepend(response);
}

var POSTFailure = function(data) {
  console.log(data);
  data.error = data.statusText;
  renderResponse(data);
}

$('document').ready(function() {
  $('#content').load('login.html', function() {
    $.getScript('js/login.js');
  });
});
