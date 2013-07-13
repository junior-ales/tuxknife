var renderError = function(data) {
  data.visibility = data.responseError ? 'display:block' : 'display:none'; 
  var directive = { '.@style':'visibility', 'label':'responseError' }
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
  data.responseError = data.statusText;
  renderResponse(data);
}

var loadResource = function(resourceName) {
  if (currentResource == resourceName) return;
  $('#content').load('/' + resourceName + '.html', function() {
    currentResource = resourceName;
    console.log(resourceName);
  });
}

var currentResource;
var tuxknifeWebService;

$('document').ready(function() {
  loadResource('login');
});
