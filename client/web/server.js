var express = require('express');
var app = express();

app.use(express.static(__dirname + '/bower_components'));
app.use(express.static(__dirname + '/app/public'));

app.set('views', __dirname + '/app/views');
app.engine('html', require('ejs').renderFile);

app.get('/', function(req, res){
  res.render('index.html');
});

app.listen(3000);
