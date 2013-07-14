var renderError = function(data) {
  data.visibility = data.responseError ? 'display:block' : 'display:none'; 
  var directive = { '.@style':'visibility', 'label':'responseError' }
  $('#errorMsg').render(data, directive);
}

var renderRouting = function(data) {
  data.resource = data.resource ? data.resource : 'login';
  loadResource(data.resource);
}

var renderResponse = function(data) {
  renderError(data);
  renderRouting(data);
}

var POSTFailure = function(data) {
  console.log(data);
  data.responseError = data.statusText;
  renderResponse(data);
}

var loadResource = function(resourceName) {
  if (currentResource == resourceName) return;
  $('#content').load('/' + resourceName + '.html', function() {
    loadScript(resourceName);
  });
}

var loadScript = function(scriptName) {
  $.getScript('/scripts/' + scriptName + '.js').done(function() {
    currentResource = scriptName;
    console.log(scriptName);
  });
}

var currentResource;
var tuxknifeWebService;

$('document').ready(function() {
  loadResource('login');
});
