var renderError = function(data) {
  data.visibility = data.responseError ? 'display:block' : 'display:none'; 
  var directive = { '.@style':'visibility', 'label':'responseError' }
  $('#errorMsg').render(data, directive);
}

var renderRouting = function(data) {
  data.resource = data.resource ? data.resource : 'login';
  loadResource(data);
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

var loadResource = function(data) {
  if (currentResource == data.resource) return;
  $('#content').load('/' + data.resource + '.html', function() {
    loadScript(data);
  });
}

var loadScript = function(data) {
  $.getScript('/scripts/' + data.resource + '.js').done(function() {
    currentResource = data.resource;
    console.log(data);
    renderView(data);
  });
}

var currentResource;
var tuxknifeWebService;

$(document).ready(function() {
  loadResource({ "resource": "login" });
});
