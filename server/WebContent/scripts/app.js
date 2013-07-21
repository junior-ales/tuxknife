var renderError = function (data) {
    if (!data.responseError) return;
    data.visibility = 'display:block';
    var directive = { '.@style': 'visibility', 'label': 'responseError' };
    $('#errorMsg').render(data, directive);
};

var renderRouting = function (data) {
    data.resource = data.resource ? data.resource : 'login';
    loadResource(data);
};

var renderResponse = function (data) {
    renderError(data);
    renderRouting(data);
};

var requestFailure = function (data) {
    console.log(data);
    data.responseError = data.statusText;
    renderResponse(data);
};

var loadResource = function (data) {
    if (currentResource == data.resource) return;
    $('#content').load('/' + data.resource + '.html', function () {
        loadScript(data);
    });
};

var loadScript = function (data) {
    $.getScript('/scripts/' + data.resource + '.js').done(function () {
        currentResource = data.resource;
        renderView(data);
    });
};

var currentResource;
var tuxknifeWebService;

$(document).ready(function() {
  loadResource({ "resource": "login" });
});
