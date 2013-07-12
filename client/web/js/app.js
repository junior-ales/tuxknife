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

var loadResource = function(resourceName) {
  if (currentResource == resourceName) return;
  $('#content').load(resourceName + '.html', function() {
    loadScript(resourceName);
  });
}

var loadScript = function(scriptName) {
  $.getScript('js/' + scriptName + '.js').done(function() {
    currentResource = scriptName;
    console.log(scriptName);
  });
}

var currentResource;

$('document').ready(function() {
  loadResource('login');
});
