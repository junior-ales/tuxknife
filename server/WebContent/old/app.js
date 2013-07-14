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
    console.log(resourceName);
  });
}

var loadScript = function(scriptName) {
  //if (scriptsLoaded.indexOf(scriptName) >= 0) return;
  $.getScript('/js/' + scriptName + '.js').done(function() {
    currentResource = scriptName;
    scriptsLoaded.push(scriptName);
    console.log(scriptName);
  });
}

var currentResource;
var tuxknifeWebService;

$('document').ready(function() {
  loadResource('login');
});

