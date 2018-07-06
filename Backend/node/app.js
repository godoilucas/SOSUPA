var express = require('express');
var travel_time = require('./calc_times.js');
var bi = require('./bi.js');
var statistics = require('./statistics.js');

app = express();

var bodyParser = require('body-parser')
app.use( bodyParser.json() );      
app.use(bodyParser.urlencoded({     // to support URL-encoded bodies
  extended: true
})); 


app.get('/upa', function(req, res){

	console.log('UPA - Received Request!');
	
	statistics.get_statistics(req.query['nome'], function(response){
		console.log('UPA - Sent Response!');
		res.send(response);	
	});

});

app.post('/times', function(req, res){

	console.log('Times - Received Request!');

	// Checando se existe o campo origins
	if (!req.body.origins) return res.sendStatus(400);				
	
	// Salvando latlong para BI
	bi.save_latlong(req.body.origins);

	//calculando o tempo de deslocamento ate o destino
	travel_time.calc_time_travel(req.body.origins, function(response){
		console.log('Times - Sent Response!');
		res.send(response);	
	});

});

var server = app.listen(3000);
console.log('Servidor iniciado na porta %s', server.address().port);

