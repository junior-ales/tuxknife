var renderView = function(dataToRender) {
  console.log('dataToRender stringified: ' + JSON.stringify(dataToRender));
  console.log('dataToRender: ' + dataToRender);
  var data = $.parseJSON(dataToRender.responseData);
  console.log('responseData: ' + JSON.stringify(data));
  var directive = { 'h3':'hostname' };
  $('header.commandPageHeader').render(data, directive);
};
