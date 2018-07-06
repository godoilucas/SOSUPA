// Conexao com o BD
var mysql = require('mysql');

var con = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "root",
  database: "SOSUPA"
});

//var nome_upa = 'UPA BOA VISTA';


con.connect(function(err) {
  if (err) throw err;
  console.log("Connected!"); 
});

setInterval(function () {
    con.query('SELECT 1');
}, 5000);


module.exports.get_statistics = function (nome_upa, callback) {

var diagnosticos = [];
var json_response = {};

var sql_bi = "Select diagnostico from estatisticas_diagnosticos where nome_unidade="+nome_upa+" Group by nome_unidade, diagnostico, ano order by SUM(numero_casos) desc limit 10";
con.query(sql_bi, function (err, result) {
	if (err) {
	   console.log("Error: Connection with Database!");
	   callback(JSON.stringify(json_response, null, 2));
	   return;
	}
	if(JSON.stringify(result) === '[]'){
	   callback(JSON.stringify(json_response, null, 2));
	   return;	
	}
	for (var diagnostico in result){
		diagnosticos.push(result[diagnostico]["diagnostico"]);
	}
	console.log(diagnosticos);

	var sql_bi2 = "Select ROUND(100*SUM(pacientes_homens)/SUM(total),2) as porcentagem_homens, ROUND(100*SUM(pacientes_mulheres)/SUM(total),2) as porcentagem_mulheres, ROUND(100*SUM(pacientes_12)/SUM(total),2) as porcentagem_12,ROUND(100*SUM(pacientes_12_25)/SUM(total),2) as porcentagem_12_25, ROUND(100*SUM(pacientes_25_60)/SUM(total),2) as porcentagem_25_60, ROUND(100*SUM(pacientes_60)/SUM(total),2) as porcentagem_60, SUM(total) as total from estatisticas_pacientes_upa where nome_unidade="+nome_upa+" group by nome_unidade";
	con.query(sql_bi2, function (err, result) {
	if (err) {
	   console.log("Error: Connection with Database!");
	   callback(JSON.stringify(json_response, null, 2));
	   return;
	}
	if(JSON.stringify(result) === '[]'){
	   callback(JSON.stringify(json_response, null, 2));
	   return;	
	}
	json_response["nome_upa"] = nome_upa;
	json_response["info"] = "10/2017 - 11/2017 - 12/2017";
	json_response["porcentagem_homens"] = result[0]["porcentagem_homens"]; 
	json_response["porcentagem_mulheres"] = result[0]["porcentagem_mulheres"]; 
	json_response["porcentagem_12"] = result[0]["porcentagem_12"]; 
	json_response["porcentagem_12_25"] = result[0]["porcentagem_12_25"]; 
	json_response["porcentagem_25_60"] = result[0]["porcentagem_25_60"]; 
	json_response["porcentagem_60"] = result[0]["porcentagem_60"]; 
	json_response["total"] = result[0]["total"]; 
	json_response["top10_diagnosticos"] = diagnosticos;
	console.log(json_response);
	var sql_bi3 = "Select telefone, endereco from upas where nome="+nome_upa+"";
	con.query(sql_bi3, function (err, result) {
	if (err) {
	   console.log("Error: Connection with Database!");
	   callback(JSON.stringify(json_response, null, 2));
	   return;
	}
	if(JSON.stringify(result) === '[]'){
	   callback(JSON.stringify(json_response, null, 2));
	   return;	
	}

	json_response["telefone"] = result[0]["telefone"];
	json_response["endereco"] = result[0]["endereco"]; 
	console.log(result);
	callback(JSON.stringify(json_response, null, 2));		
	
	});
	
	});
});

}
