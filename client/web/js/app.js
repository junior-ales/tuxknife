var renderError = function(data) {
  data.visibility = data.error ? 'display:block' : 'display:none'; 
  var directive = { '.@style':'visibility', 'label':'error' }
  $('#errorMsg').render(data, directive);
}

var renderRouting = function(data) {
  data.resource = data.resource ? data.resource : 'login';
  var response = $('<p>' + JSON.stringify(data) + '</p>');
  $('#response').prepend(response);
  loadResource(data.resource);
}

var renderResponse = function(data) {
  renderError(data);
  renderRouting(data);
}

var POSTFailure = function(data) {
  console.log(data);
  data.error = data.statusText;
  renderResponse(data);
}

var loadResource = function(name) {
  if (currentResource == name) return;
  $('#content').load(name + '.html', function() {
    $.getScript('js/' + name + '.js')
    .done(function() {
      currentResource = name;
      console.log(name);
    });
  });
}

var currentResource;

$('document').ready(function() {
  loadResource('login');
});
