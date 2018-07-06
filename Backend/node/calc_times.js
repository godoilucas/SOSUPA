// Recuperando a lista de UPAS
var mysql = require('mysql');

var con = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "root",
  database: "SOSUPA"
});

var upas = [];
var destination = [];
// Retonar as unidade existentes
con.connect(function(err) {
  if (err) throw err;
  console.log("Connected!");
  var sql_upas =  "Select nome_unidade, endereco from upa_tempo_estimado JOIN upas where upas.nome = upa_tempo_estimado.nome_unidade GROUP by nome, endereco;";
  con.query(sql_upas, function (err, result) {
    if (err) {
       console.log("Error: Connection with Database!");
       throw err;
    }
    // Fazer o parse das unidades
    for (var upa in result){
      var upa_o = new Object();
      upa_o["nome"]=result[upa]["nome_unidade"];
      upa_o["endereco"]=result[upa]["endereco"];
      upas.push(upa_o);
      destination.push(result[upa]["endereco"]);
    }

    //console.log(upas);
    });
});

setInterval(function () {
    con.query('SELECT 1');
}, 5000);


// Criando o client da APU
const googleMapsClient = require('@google/maps').createClient({
  key: 'AIzaSyDAcQbCAOI5-ZYqjqrcL_2OaN47-G8vFMo',
  Promise: Promise
});


// Modos de transporte
var transport_modes = ['driving','walking','transit','bicycling']

//var destination = ['UPA CIC','UPA CAMPO COMPRIDO','UPA BOA VISTA'];


module.exports.calc_time_travel = function (origin, callback) { 

	// Montando o json de resposta
	var json_response = {};
	json_response["UPAS"] = [];
	for (var i=0; i<upas.length;i++){
		(json_response["UPAS"]).push({'name': upas[i]['nome'],'distance':'', 'driving':'','walking':'','transit':'','bicycling':'','tempo_atendimento':''});
	}

	//var json_response = JSON.parse(JSON.stringify(json_response_model));

	//var json_response = json_response_model;

	var promises_transport = [];

	// Enviando a consulta de cada tempo de transporte
	for(var i=0; i<transport_modes.length; i++){
		promises_transport.push(googleMapsClient.distanceMatrix(
		{
		      origins: origin,
		      destinations: destination,
		      mode: transport_modes[i],
		}).asPromise())	
	}

	Promise.all(promises_transport).then((response) => {
		//console.log(response[0].json.rows[0].elements[1]);
		for (var i=0; i<response.length; i++){
			if (("status" in response[i].json)==false) {
				console.log('Error: Field status not found');
				console.log(response[i].json);
				break;
			}
			// Primeiro checamos se a resposta foi OK
	        if (response[i].json["status"] == "OK"){
	           // Para cada destino
	           for (var j=0; j<destination.length; j++){
	           		// Verificar se a reposta do elemento foi OK
	           		if(response[i].json.rows[0].elements[j].status == "OK"){
	           			var time = response[i].json.rows[0].elements[j].duration.value;
						var sec_num = parseInt(time, 10); // don't forget the second param
    					var hours   = Math.floor(sec_num / 3600);
    					var minutes = Math.floor((sec_num - (hours * 3600)) / 60);
						if (hours   < 10) {hours   = "0"+hours;}
    					if (minutes < 10) {minutes = "0"+minutes;}
	           			json_response["UPAS"][j][response[i].query.mode] = hours+'h'+minutes;
						// A distancia é calculada pelo distancia andando
						
						if(response[i].query.mode == "walking"){
							json_response["UPAS"][j]['distance'] = response[0].json.rows[0].elements[j].distance.text; 
						}
		       		}
	           }
	        }
		}
		 sql_time =  "select upa_atendimentos.nome_unidade, (CASE WHEN (Hour(NOW() - INTERVAL 3 HOUR)) > 0 and (Hour(NOW() - INTERVAL 3 HOUR)) < 9 then ROUND(3600*estimado_periodo1*(numero_atendimentos/atendimentos_periodo1),2) WHEN (Hour(NOW() - INTERVAL 3 HOUR)) > 9 and (Hour(NOW() - INTERVAL 3 HOUR)) < 18 then ROUND(3600*estimado_periodo2*(numero_atendimentos/atendimentos_periodo2),2) WHEN (Hour(NOW() - INTERVAL 3 HOUR)) > 18 and (Hour(NOW() - INTERVAL 3 HOUR)) < 24 then ROUND(3600*estimado_periodo3*(numero_atendimentos/atendimentos_periodo3),2) END) as tempo_atendimento from upa_atendimentos JOIN (select nome_unidade, estimado_periodo1, estimado_periodo2, estimado_periodo3, atendimentos_periodo1, atendimentos_periodo2, atendimentos_periodo3 from upa_tempo_estimado where ano=YEAR(now())-1 and mes=Month(now()) and dia_semana=Weekday(now())) AS T2 where upa_atendimentos.nome_unidade=T2.nome_unidade and ano=YEAR(now())-1 and mes=Month(now()) and semana=Week(now()) and hora=Hour(NOW() - INTERVAL 3 HOUR) and dia_semana=Weekday(now());";
                con.query(sql_time, function (err, result) {
                if (err) {
                        console.log("Error: Connection with Database!");
                        throw err;
                }
                console.log(result[0][0]);
		for (var j=0; j<result.length; j++){
			console.log(result[j]);
			//json_response["UPAS"][j]['tempo_atendimento']=result[j]["tempo_atendimento"];
		
 			var sec_num = parseInt(result[j]["tempo_atendimento"], 10); // don't forget the second param
                	var hours   = Math.floor(sec_num / 3600);
                	var minutes = Math.floor((sec_num - (hours * 3600)) / 60);
                	if (hours   < 10) {hours   = "0"+hours;}
                	if (minutes < 10) {minutes = "0"+minutes;}
			json_response["UPAS"][j]['tempo_atendimento']=hours+'h'+minutes;
		}
		callback(JSON.stringify(json_response, null, 2));
                });

 //		callback(JSON.stringify(json_response, null, 2));
//		console.log("Response sent");	
	});
}
